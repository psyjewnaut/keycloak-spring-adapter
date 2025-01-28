# KeycloakSpringAdapter Library

**KeycloakSpringAdapter** — это библиотека автоконфигурации, которая автоматически создаёт и настраивает `JwtAuthenticationConverter` для **Spring Boot** приложений, позволяя корректно извлекать роли и другие поля (`realm_access`, `resource_access`, `groups`, `scope` и т.д.) из JWT-токена Keycloak.

---

## Возможности

1. **Автоконфигурация**  
   Благодаря Spring Boot Auto-Configuration, библиотека сама создаёт бин `KeycloakJwtConverter`, если он не объявлен вручную.

2. **Гибкая настройка**  
   Поддержка **realm roles** (через `RealmAccessRoleConverter`), **client roles** (`ResourceAccessRoleConverter`), **scope** (`ScopeConverter`), **groups** (`GroupsConverter`). Можно включать/отключать нужные поля через проперти.

3. **Лёгкая интеграция**  
   Просто добавьте зависимость — и всё готово: Spring Boot автоматически подхватит автоконфигурацию библиотеки.

4. **Совместимость**  
   Подходит для Spring Boot 3.x (и, при необходимости, может работать с Boot 2.x, если добавить запись в `META-INF/spring.factories`).

---

## Преимущества

- **Упрощённая конфигурация**: Не нужно каждый раз вручную писать `JwtAuthenticationConverter` - библиотека делает это за вас.
- **Гибкая настройка**: Через проперти (`KeycloakConvertersProperties`) вы регулируете, что именно извлекается из токена: realm roles, resource roles, groups и т. д.
- **Лёгкая кастомизация**: При желании вы можете переопределить поведение, объявив собственный бин `KeycloakJwtConverter`.

---

## Быстрый старт

### 1. Добавьте зависимость

В `pom.xml` вашего проекта (Maven):

```xml
<dependency>
    <groupId>dev.psyjewnaut.security</groupId>
    <artifactId>keycloak-spring-adapter</artifactId>
    <version>1.0.0</version>
</dependency>
```

## 2. Подключите бин в `SecurityFilterChain`

В вашем классе конфигурации Spring Security (например, `SecurityConfig`):

```java
@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
            KeycloakJwtConverter keycloakJwtConverter) throws Exception {

        http.oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwtConfigurer ->
                    jwtConfigurer.jwtAuthenticationConverter(keycloakJwtConverter)
                )
            )
            ...
        return http.build();
    }
}
```

Теперь KeycloakJwtConverter будет автоматически создан (если не переопределён вручную) и использован в качестве JwtAuthenticationConverter. 
В результате в Authentication попадут JWT claims, указанные в application.yml.

## (Опционально) Настройка KeycloakConvertersProperties
В application.yml можно включать/отключать отдельные конвертеры, а так же ограничить resource_access только определёнными клиентами:

```yaml
#defaults
psyjewnaut:
  keycloak-converters:
    realm-access: true # realm_access.roles → ROLE_
    resource-access: false # resource_access.<resource-client-ids>.roles → ROLE_
    resource-client-ids: # default all
       - test-client
    scope: false # scope/scp → SCOPE_
    groups: false # groups → ROLE_GROUP_
```

## Принцип работы

1. При запросе с Bearer-токеном Spring Security валидирует JWT.
2. Передаёт его в simpleJwtAuthentication.convert(jwt).
3. Библиотека последовательно вызывает все выбранные конвертеры (realm, resource, scope, groups).
4. Получившиеся GrantedAuthority (например, ROLE_user, SCOPE_offline_access) складываются в Authentication запроса.