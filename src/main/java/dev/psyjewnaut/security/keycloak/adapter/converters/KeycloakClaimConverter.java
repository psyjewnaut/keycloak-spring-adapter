package dev.psyjewnaut.security.keycloak.adapter.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Set;

/**
 * Converters extract SimpleGrantedAuthority set from Jwt
 *
 * @author psyjewnaut
 */
public interface KeycloakClaimConverter extends Converter<Jwt, Set<SimpleGrantedAuthority>> {
}
