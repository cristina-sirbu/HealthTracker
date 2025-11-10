# Rate limiting

## Context

HealthTracker API occasionally receives bursty traffic (legit and accidental). 
Without throttling, we risk thread-pool starvation, DB contention and degraded UX while autoscaling catches up.
We also want predictable fairness across users.

## Decision

Implement a **per-user** rate limit using a **Token Bucket**: **100 requests / 60 seconds**.  
For each userId we keep `tokens` (capacity 100) and `lastRefillTime`. 
On each request we refill by elapsed time (≈1.67 tokens/sec), consume 1 token if available, otherwise respond **429 Too Many Requests**.

## Placement (v1 → v2)
- **v1 (now):** inside the HealthTracker service (lightweight filter/interceptor).
- **v2 (future):** move enforcement to the **API gateway** for cross-service protection; keep per-endpoint fine-tuning in service if needed.

## Implementation Notes (v1)
- Key = `userId` (extracted from auth).
- State per user: `double tokens`, `long lastRefillMs`.
- Headers on response:
    - `X-RateLimit-Limit: 100`
    - `X-RateLimit-Remaining: <current tokens floored>`
    - `Retry-After: <seconds until 1 token refills>` when 429.
- Failure mode: **fail-open** if the limiter state/store is unavailable (log + metric) to preserve UX and availability; revisit if abuse is observed.

## Alternatives Considered
- **Fixed window counter (simple, fast):** lower memory but boundary bursts at minute edges.
- **Sliding window log (smoothest):** precise but stores many timestamps; higher memory/ops cost.
- **Leaky bucket (queue-shaped):** smooths output rate but doesn’t directly model “credits” per user.

## Consequences

* **Pros**: protects service under load, fair per-user limits, predictable SLOs, clear 429 signaling for clients.
* **Cons**: in-memory v1 is instance-local; needs distributed store for cluster-wide accuracy.
* **Future options**: add metrics (`rate.limit.allowed`, `rate.limit.blocked`), dashboards and v2 migration to gateway/Redis with Lua for atomicity.

## Status
Accepted.