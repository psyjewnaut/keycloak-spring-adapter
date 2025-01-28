package dev.psyjewnaut.security.keycloak.adapter.converters;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * JwtGrantedAuthoritiesConverter wrapper with "SCOPE_" prefix. Converting "scope" claim to GrantedAuthority set.
 *
 * @author psyjewnaut
 */
public class ScopeConverter implements KeycloakClaimConverter {

    private static final JwtGrantedAuthoritiesConverter JWT_GRANTED_AUTHORITIES_CONVERTER = new JwtGrantedAuthoritiesConverter();

    @Override
    public Set<SimpleGrantedAuthority> convert(Jwt jwt) {
        JWT_GRANTED_AUTHORITIES_CONVERTER.setAuthorityPrefix("SCOPE_");
        Collection<GrantedAuthority> converted = JWT_GRANTED_AUTHORITIES_CONVERTER.convert(jwt);
        return converted.stream()
                .map(a -> new SimpleGrantedAuthority(a.getAuthority()))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }
}