package dev.psyjewnaut.security.keycloak.adapter.configs;

import dev.psyjewnaut.security.keycloak.adapter.KeycloakJwtConverter;
import dev.psyjewnaut.security.keycloak.adapter.converters.KeycloakClaimConverter;
import dev.psyjewnaut.security.keycloak.adapter.converters.GroupsConverter;
import dev.psyjewnaut.security.keycloak.adapter.converters.RealmAccessRoleConverter;
import dev.psyjewnaut.security.keycloak.adapter.converters.ResourceAccessRoleConverter;
import dev.psyjewnaut.security.keycloak.adapter.converters.ScopeConverter;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Spring boot converters autoconfiguration
 *
 * @author psyjewnaut
 */
@AutoConfiguration
@EnableConfigurationProperties(KeycloakConvertersProperties.class)
public class KeycloakConvertersAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(KeycloakJwtConverter.class)
    public KeycloakJwtConverter keycloakJwtConverter(KeycloakConvertersProperties props) {
        List<KeycloakClaimConverter> converters = new ArrayList<>();
        if (props.isRealmAccess()) {
            converters.add(new RealmAccessRoleConverter());
        }
        if (props.isScope()) {
            converters.add(new ScopeConverter());
        }
        if (props.isGroups()) {
            converters.add(new GroupsConverter());
        }
        if (props.isResourceAccess()) {
            converters.add(new ResourceAccessRoleConverter(new HashSet<>(props.getResourceClientIds())));
        }
        return new KeycloakJwtConverter(converters);
    }
}
