package dev.psyjewnaut.security.keycloak.adapter;

import dev.psyjewnaut.security.keycloak.adapter.converters.KeycloakClaimConverter;
import lombok.AllArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Composite converters to JwtAuthenticationToken with full GrantedAuthority set.
 *
 * @author psyjewnaut
 */
@AllArgsConstructor
public class KeycloakJwtConverter implements Converter<Jwt, JwtAuthenticationToken> {

    private final List<KeycloakClaimConverter> converters;

    @Override
    public JwtAuthenticationToken convert(Jwt jwt) {
        Set<SimpleGrantedAuthority> result = new HashSet<>();
        for (KeycloakClaimConverter converter : converters) {
            Set<SimpleGrantedAuthority> grantedAuthorities = converter.convert(jwt);
            if (grantedAuthorities != null) {
                result.addAll(grantedAuthorities);
            }
        }
        return new JwtAuthenticationToken(jwt, result);
    }
}
