# Health Tracker

[![CI](https://github.com/cristina-sirbu/HealthTracker/actions/workflows/ci.yml/badge.svg)](https://github.com/cristina-sirbu/HealthTracker/actions/workflows/ci.yml)

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

Create medication.
```shell
curl -i -X POST "http://localhost:8080/users/1/medications" \
  -H "Content-Type: application/json" \
  -d '{"name":"Ibuprofen","form":"tablet","strength":"200mg"}'
```

List medications.
```shell
curl -i "http://localhost:8080/users/1/medications"
```