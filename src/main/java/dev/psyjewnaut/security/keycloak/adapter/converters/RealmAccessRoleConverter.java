package dev.psyjewnaut.security.keycloak.adapter.converters;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Keycloak Realm roles for Spring Security converter. Converting "realm_access" claim to GrantedAuthority set.
 *
 * @author psyjewnaut
 */
public class RealmAccessRoleConverter implements KeycloakClaimConverter {

    @Override
    @SuppressWarnings("unchecked")
    public Set<SimpleGrantedAuthority> convert(Jwt jwt) {
        Object realmAccessObj = jwt.getClaim("realm_access");
        if (!(realmAccessObj instanceof Map)) {
            return Collections.emptySet();
        }
        Map<String, Object> realmAccess = (Map<String, Object>) realmAccessObj;
        Object rolesObj = realmAccess.get("roles");
        if (!(rolesObj instanceof List)) {
            return Collections.emptySet();
        }
        List<String> roles = (List<String>) rolesObj;
        return roles.stream()
                .map(roleName -> new SimpleGrantedAuthority("ROLE_" + roleName))
                .collect(Collectors.toSet());
    }
}