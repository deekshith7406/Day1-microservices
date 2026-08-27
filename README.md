
**Terminal 1 — User Service (port 8081)**
```bash
cd user-service
mvn spring-boot:run
```

**Terminal 2 — Order Service (port 8082)**
```bash
cd order-service
mvn spring-boot:run
```

## Exercise A — try User Service directly

```bash
curl http://localhost:8081/api/users/1
# 200 OK, JSON user

curl -i http://localhost:8081/api/users/999
# 404 Not Found, JSON error body
```

## Exercise B — try Order Service (which calls User Service internally)

```bash
curl http://localhost:8082/api/orders/101
# 200 OK — order JSON that includes an embedded "user" object,
# proving Order Service received it from User Service over HTTP.

curl -i http://localhost:8082/api/orders/103
# 404 Not Found — this order references userId 99, which doesn't
# exist in User Service. Order Service correctly propagates that.
```

## Exercise C — prove the network boundary

1. With both services running, call:
   ```bash
   curl http://localhost:8082/api/orders/101
   ```
   You should see the full response, including user details fetched
   from User Service.

2. Stop User Service (Ctrl+C in Terminal 1).

3. Call Order Service again:
   ```bash
   curl -i http://localhost:8082/api/orders/101
   ```
   You'll get **503 Service Unavailable** with a clear JSON error
   body (`USER_SERVICE_UNAVAILABLE`) instead of a crash or a hang —
   this is the "network call ≠ local method call" lesson from the
   handbook, made visible.

4. Restart User Service and confirm Order Service recovers
   automatically (no restart needed on the Order Service side).

## Design notes (why it's built this way)

- **No shared code/database between services.** Order Service has
  its own `UserSummaryDto` — it does not import User Service's
  classes or touch its data. Each service owns its own contract.
- **Configurable URL.** User Service's base URL lives in
  `order-service/src/main/resources/application.yml`
  (`user-service.base-url`), not hard-coded in the client class —
  override it with `--user-service.base-url=...` if needed.
- **Three distinct error cases** are handled deliberately differently:
  - Order doesn't exist → `404 ORDER_NOT_FOUND`
  - Order exists, but its user doesn't → `404 RELATED_USER_NOT_FOUND`
  - User Service is down/unreachable → `503 USER_SERVICE_UNAVAILABLE`
- **Explicit timeouts** on the `RestTemplate` (3s connect/read) so a
  hung User Service can't hang Order Service forever.

## Suggested Git workflow

Commit incrementally rather than all at once, e.g.:

```
git init
git add user-service/pom.xml user-service/src/main/java/.../UserServiceApplication.java
git commit -m "create user service skeleton"

git add user-service/...UserController.java user-service/...UserService.java
git commit -m "add user retrieval API"

git add user-service/...UserNotFoundException.java user-service/...GlobalExceptionHandler.java
git commit -m "add user-not-found handling"

git add order-service/pom.xml order-service/src/main/java/.../OrderServiceApplication.java
git commit -m "create order service skeleton"

git add order-service/...UserServiceClient.java order-service/...OrderService.java
git commit -m "integrate order service with user service"

git add order-service/...UserServiceUnavailableException.java order-service/...GlobalExceptionHandler.java
git commit -m "handle downstream user-service failure"

git add README.md
git commit -m "update documentation"
```
