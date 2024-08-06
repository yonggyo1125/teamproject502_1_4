# 분산 서버 환경 세션 공유 처리
## redis 설치

> redis는 docker를 이용해서 설치 (윈도우즈에서는 설치가 불가하므로 필수)

```
docker pull redis

docker run -d -p 6379:6379 --name redis redis
```

## 프론트 웹, 관리자 웹 공통 

- 의존성 추가

> build.gradle

```groovy
dependencies {
    ...
    
	implementation 'org.springframework.boot:spring-boot-starter-data-redis'
	implementation 'org.springframework.session:spring-session-data-redis'
    
    ...
}
```

## 컨피그 서버

> front-service.yml
```yaml
server:
  port: 3001
  servlet:
    cookie:
      name: SESSIONID

spring:
  session:
    store-type: redis
    redis:
      namespace: spring:session
  data:
    redis:
      host: ${redis.host}
      port: ${redis.port}

```

> admin-service.yml
```yaml
server:
  port: 3002
  servlet:
    cookie:
      name: SESSIONID

spring:
  session:
    store-type: redis
    redis:
      namespace: spring:session
  data:
    redis:
      host: ${redis.host}
      port: ${redis.port}

  ....
```

- 각 프론트 서버, 어드민 서버 실행 시 다음 환경 변수 추가 
  - redis.host
  - redis.port
  - 예) redis.host=localhost;redis.port=6379