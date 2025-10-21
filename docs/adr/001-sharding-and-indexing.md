# Sharding and Indexing

## Context

The HealthTracker app will eventually handle many users, each with their own medications, regimens and logs.
Most queries (create/list medications, compute adherence, symptom trends) are scoped to a single user.

## Decision

Use `user_id` as the sharding key if horizontal scaling becomes necessary.
* Each user’s data (medications, regimens, intakes, symptoms) will live in the same shard.
* The application logic always queries by user_id, which keeps reads and writes localized.
* Avoids cross-shard joins for common queries.

Add supporting indexes now to optimize single-user reads:
* medications(user_id, created_at DESC)
* intake_logs(user_id, taken_at DESC)
* symptom_entries(user_id, recorded_at DESC)

## Consequences

* **Pros**: simple key, natural data grouping, predictable query patterns.
* **Cons**: requires rebalancing if number of shards changes (can later use consistent hashing).
* **Future options**: move to user-based hash partitioning in PostgreSQL or use separate DB clusters.

## Status
Accepted.