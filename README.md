# Health Tracker

## Run the app

```shell
mvn spring-boot:run
```

## Test the app

Register (should be 200 OK)

```shell
curl -X POST http://localhost:8080/users/register \
-H "Content-Type: application/json" \
-d '{"username":"alice","password":"pass"}'
```

Duplicate register (should return 400 with our IllegalArgumentException)

```shell
curl -X POST http://localhost:8080/users/register \
-H "Content-Type: application/json" \
-d '{"username":"alice","password":"pass"}'
```

Login (should be 200 OK after first register)

```shell
curl -X POST http://localhost:8080/users/login \
-H "Content-Type: application/json" \
-d '{"username":"alice","password":"pass"}'
```
