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

- 프론트 서버, 어드민 서버 공통
> global/configs/RedisConfig.java

```java
package org.g9project4.global.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Value("${spring.data.redis.host}")
    private String host;

    @Value("${spring.data.redis.port}")
    private int port;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(host, port);
    }

    @Bean
    public RedisTemplate<?, ?> redisTemplate() {
        RedisTemplate<?, ?> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        redisTemplate.setEnableTransactionSupport(true);

        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());

        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new StringRedisSerializer());
        return redisTemplate;
    }
}
```

## 회원 관련 커맨드 객체, 엔티티 모두 Serializable 인터페이스 추가

- global/entities/BaseEntity.java
- global/entities/BaseMemberEntity.java

- member/entities/Member.java
- member/entities/Authorities.java
- member/entities/AuthoritiesId.java

- member/controllers/RequestJoin.java
- member/controllers/RequestLogin.java
- member/MemberInfo.java

# 소셜 로그인

## 카카오 로그인

### REST API 키 설정 추가
> templates/config/api.html

```html
...
    <h2>카카오 API 앱키</h2>
    ...
            <tr>
                <th>REST API 키</th>
                <td>
                    <input type="text" name="kakaoRestApiKey" th:field="*{kakaoRestApiKey}">
                </td>
            </tr>
        </table>
```

> config/controllers/ApiConfig.java

```java
...

@Data
public class ApiConfig {
  ...

  private String kakaoJavascriptKey; // 카카오 API - 자바스크립트 앱 키
  private String kakaoRestApiKey; // 카카오 API - REST API 키
}
```