package dev.psyjewnaut.security.keycloak.adapter.configs;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * Properties from application.yml with psyjewnaut.keycloak.converters prefix
 *
 * @author psyjewnaut
 */
@ConfigurationProperties(prefix = "psyjewnaut.security.keycloak.converters")
@Getter
@Setter
public class KeycloakConvertersProperties {

    /**
     * Read realm_access.roles
     */
    private boolean realmAccess = true;

    /**
     * Read resource_access.clientId.roles
     */
    private boolean resourceAccess = true;

    /**
     * Include clientIds
     */
    private List<String> resourceClientIds = new ArrayList<>();

    /**
     * Read scope (JwtGrantedAuthoritiesConverter)
     */
    private boolean scope = true;

    /**
     * Read groups
     */
    private boolean groups = true;

}