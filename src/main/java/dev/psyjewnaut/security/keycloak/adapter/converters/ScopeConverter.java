package dev.psyjewnaut.security.keycloak.adapter.converters;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Keycloak Resource roles for Spring Security converter. Converting "scope" claim to GrantedAuthority set.
 *
 * @author psyjewnaut
 */
public class ScopeConverter implements KeycloakClaimConverter {

    private final JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter;

    public ScopeConverter(){
        this.jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("SCOPE_");
    }

    @Override
    public Set<SimpleGrantedAuthority> convert(Jwt jwt) {
        Collection<GrantedAuthority> converted = jwtGrantedAuthoritiesConverter.convert(jwt);
        if (converted.isEmpty()){
            return new HashSet<>();
        }
        return converted.stream()
                .map(a -> new SimpleGrantedAuthority(a.getAuthority()))
                .collect(Collectors.toSet());
    }
}