# Learnings

## Unique constrains on a column of a table

```java
@Table(
name = "users",
uniqueConstraints = @UniqueConstraint(columnNames = "username")
)
```
In the users table, the username column must be unique — no two users can have the same username.

```java
@Table(
        name = "medications",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id","name"})
)
```
This enforces “a user can’t have two meds with the same name”. This is called a **composite unique constraint**

## DB automatically generates the ID of an entry

```java
 @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
```
Lets the database automatically generate the ID, usually by auto-increment.

How it works?
* JPA sends an INSERT statement without the ID, and the database assigns the next available ID.
* After insertion, JPA retrieves the generated ID and sets it in the entity.

## What does @Transactional mean?
All the database operations inside the method annotated with `@Transactional` should run in a single transaction — if any of them fail, rollback everything.

## What does @NotBlank check?
* The value is not null.
* After trimming whitespace, it’s not empty.

| Annotation  | Fails on `null`? | Fails on empty `""`? | Fails on `"   "`? |
|-------------|------------------|----------------------|-------------------|
| `@NotNull`  | ✅                | ❌                    | ❌                 |
| `@NotEmpty` | ✅                | ✅                    | ❌                 |
| `@NotBlank` | ✅                | ✅                    | ✅                 |

## When to use Records and when to use classes?
| Type       | Why used here                                                                                                                                                               |
| ---------- | --------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| **record** | Immutable, concise response type (`ApiMessage`) — you just need to send a simple structured value back; no setters, no validation.                                          |
| **class**  | Mutable + supports `@Valid` annotations (`RegisterRequest`, `LoginRequest`) — Spring needs to **deserialize JSON into them**, then **validate** and possibly modify fields. |

So:
* For input, use classes (with @Valid fields).
* For output, use records (clean, read-only, no boilerplate).

## Why should login() be a POST instead of a GET?
If you used GET:
* Credentials would appear in the URL (e.g., /login?user=alice&pass=123)
* They’d be logged in server logs, proxies, browser history — not safe

REST conventionally uses:
* POST → actions that change server state or handle sensitive input, even if they only return a token.
* GET → read-only, idempotent, safe for caching (not true for login).

## Database schema design

### Relationships and foreign keys
To design a database schema:
1. Think in sentences first, not in diagrams. E.g.: One user can have many medications. And each medication belongs to one user. => One user - many medications = 1 - N relationship.
2. The table that *owns* the "many" side gets the foreign key. Adding a foreign key is important for **data integrity**:
    * it prevents inserting a medication whose `user_id` doesn't exist
    * deletes or updates cascade properly

### Normalization
When data repeats (like user names or medication names stored in multiple places), it leads to:
* Update anomalies → if a name changes, you must fix it everywhere
* Insert anomalies → you can’t insert a medication until you have a fake user row
* Delete anomalies → deleting one thing might accidentally remove needed info
  So normalization is about splitting data into separate tables to avoid that.

#### Normalization forms
1. 1NF (First Normal Form) → Each column holds atomic values, not lists.
* ❌ medications: "aspirin, paracetamol"
* ✅ Separate rows: one per medication
2. 2NF (Second Normal Form) → Every non-key column depends on the whole key, not just part of it.
* Applies mostly to tables with composite keys.
3. 3NF (Third Normal Form) → No transitive dependencies (no derived attributes).
* ❌ intake_logs having medication_name (because that’s already in medications)
* ✅ keep only medication_id there; join when needed

## What is the difference between @PathVariable and @RequestParam?

“@PathVariable is for identifying a specific resource — it’s part of the URL path.
@RequestParam is for filtering or tuning the request — it’s part of the query string.”

* **@PathVariable** → identifies which resource
  * Used for things like:
    * /users/1/medications
    * /orders/42/items/7
  * Think of it as: “which specific entity or group am I addressing?”
* **@RequestParam** → filters, options, or extra info
  * Used for filters, pagination, or optional settings:
    * ?page=2&limit=20
    * ?sort=name

## What does this do? @SpringBootTest(classes = HealthtrackerApplication.class)
Start my full Spring Boot application context for tests.

**Interview response**: “@SpringBootTest loads a real application context so I can test my components end-to-end.
Specifying classes = … ensures it starts from my main configuration, even if the test package isn’t under the same root.”

## What does this do? @AutoConfigureMockMvc
Inject a real MockMvc instance so I can simulate HTTP calls — without running a real server.

**Interview response**: “@AutoConfigureMockMvc gives me a mock HTTP layer on top of the full Spring context.
It’s ideal for controller integration tests because it exercises request mapping, validation, and error handling end-to-end.”

## How to narrow the context for faster tests? @SpringBootTest vs @WebMvcTest
**@SpringBootTest** — “Full integration”

This starts the entire application context:
* All controllers, services, repositories, and configurations.
* It connects to the real database (or H2, depending on your config).
* It runs your app end-to-end, just without an external server.

Best when you want to test:
* Real DB interactions (repositories, Flyway, etc.)
* How multiple layers work together
* Application startup (e.g., migrations, security filters)

Downside:
* Slower to start (seconds vs milliseconds)
* Harder to isolate bugs because everything loads

**@WebMvcTest** — “Controller-only slice”

This starts only the web layer:
* Loads controllers, validation, exception handlers, Jackson, etc.
* Does not load service beans or repositories — you mock them instead.
* No database, no migrations — just HTTP request handling logic.

Best when you want to:
* Test just HTTP → controller → response JSON
* Verify validation, routing, error handling
* Run fast, hundreds per second

Downside:
* Doesn’t verify DB or service logic
* You must mock everything else

**Interview response**: I use @SpringBootTest for full-stack integration tests — verifying controllers, services, and repositories together.
I use @WebMvcTest when I only want to test controller behavior in isolation, mocking the service layer.
It’s a trade-off between confidence and speed. When using @WebMvcTest, I mock any dependencies that live outside the web layer using @MockBean, because Spring only scans controllers and their direct configuration classes.”

# What does `mvn verify -B` means?

mvn verify -B = “Run the full build and tests automatically, no user input, CI-friendly.”

# What does @Mock and @InjectMocks do?
* @Mock — to create mock objects
* @InjectMocks — to automatically inject those mocks into your class under test

# Difference between `assert` and `verify`?
* `assert` checks the result or state of your method under test; used to confirm what your method returned
* `verify` checks interation with dependencies; used to confirm how it behaved toward other classes (like mocks)

