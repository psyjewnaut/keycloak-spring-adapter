package dev.psyjewnaut.security.keycloak.adapter.converters;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Keycloak Groups roles for Spring Security converter. Converting "groups" claim to GrantedAuthority set.
 *
 * @author psyjewnaut
 */
public class GroupsConverter implements KeycloakClaimConverter {

    @Override
    @SuppressWarnings("unchecked")
    public Set<SimpleGrantedAuthority> convert(Jwt jwt) {
        Object groupsObj = jwt.getClaims().get("groups");
        if (!(groupsObj instanceof List)) {
            return Collections.emptySet();
        }
        List<String> groups = (List<String>) groupsObj;
        return groups.stream()
                .map(g -> g.replace("/", ""))
                .map(g2 -> new SimpleGrantedAuthority("ROLE_GROUP_" + g2))
                .collect(Collectors.toSet());
    }

}