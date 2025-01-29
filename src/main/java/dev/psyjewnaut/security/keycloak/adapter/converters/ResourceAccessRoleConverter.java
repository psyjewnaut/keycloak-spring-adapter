package dev.psyjewnaut.security.keycloak.adapter.converters;

import lombok.AllArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Keycloak Resource roles for Spring Security converter. Converting "resource_roles" claim to GrantedAuthority set.
 *
 * @author psyjewnaut
 */
@AllArgsConstructor
public class ResourceAccessRoleConverter implements KeycloakClaimConverter {

    private final Set<String> clientIds;

    @Override
    @SuppressWarnings("unchecked")
    public Set<SimpleGrantedAuthority> convert(Jwt jwt) {
        Object resourceAccessObj = jwt.getClaim("resource_access");
        if (!(resourceAccessObj instanceof Map)) {
            return Collections.emptySet();
        }
        Map<String, Object> resourceAccess = (Map<String, Object>) resourceAccessObj;
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();

        for (Map.Entry<String, Object> entry : resourceAccess.entrySet()) {
            String clientId = entry.getKey();
            if (!clientIds.isEmpty() && !clientIds.contains(clientId)) {
                continue;
            }
            Object val = entry.getValue();
            if (!(val instanceof Map)) {
                continue;
            }
            Map<String, Object> rolesMap = (Map<String, Object>) val;
            Object rolesObj = rolesMap.get("roles");
            if (!(rolesObj instanceof List)) {
                continue;
            }
            List<String> roles = (List<String>) rolesObj;
            Set<SimpleGrantedAuthority> clientAuthorities = roles.stream()
                    .map(r -> new SimpleGrantedAuthority("ROLE_" + r))
                    .collect(Collectors.toSet());
            authorities.addAll(clientAuthorities);
        }

        return authorities;
    }
}