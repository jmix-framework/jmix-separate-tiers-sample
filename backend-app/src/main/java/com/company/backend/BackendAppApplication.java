package com.company.backend;

import com.company.backend.entity.User;
import com.company.backend.security.OAuth2TokenUserMixin;
import com.google.common.base.Strings;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import io.jmix.authserver.service.mapper.JdbcOAuth2AuthorizationServiceJsonMapperCustomizer;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.jdbc.autoconfigure.DataSourceProperties;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import tools.jackson.databind.json.JsonMapper;

import javax.sql.DataSource;

@Push
@Theme(value = "backend-app")
@PWA(name = "Backend App", shortName = "Backend App", offline = false)
@SpringBootApplication
@StyleSheet(Lumo.UTILITY_STYLESHEET)
public class BackendAppApplication implements AppShellConfigurator {

    @Autowired
    private Environment environment;

    public static void main(String[] args) {
        SpringApplication.run(BackendAppApplication.class, args);
    }

    @Bean
    @Primary
    @ConfigurationProperties("main.datasource")
    DataSourceProperties dataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @Primary
    @ConfigurationProperties("main.datasource.hikari")
    DataSource dataSource(final DataSourceProperties dataSourceProperties) {
        return dataSourceProperties.initializeDataSourceBuilder().build();
    }

    // tag::tokenObjectMapperCustomizer[]
    @Bean
    JdbcOAuth2AuthorizationServiceJsonMapperCustomizer tokenObjectMapperCustomizer() {
        return (JsonMapper.Builder builder) ->
                builder.addMixIn(User.class, OAuth2TokenUserMixin.class);
    }
    // end::tokenObjectMapperCustomizer[]

    @EventListener
    public void printApplicationUrl(final ApplicationStartedEvent event) {
        LoggerFactory.getLogger(BackendAppApplication.class).info("Application started at "
                + "http://localhost:"
                + environment.getProperty("local.server.port")
                + Strings.nullToEmpty(environment.getProperty("server.servlet.context-path")));
    }
}
