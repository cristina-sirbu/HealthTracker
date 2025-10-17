# Learnings

## Unique constrains on a column of a table

```java
@Table(
name = "users",
uniqueConstraints = @UniqueConstraint(columnNames = "username")
)
```
In the users table, the username column must be unique — no two users can have the same username.

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
