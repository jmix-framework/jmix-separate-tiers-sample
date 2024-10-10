package com.company.frontend.security;

import com.vaadin.flow.server.VaadinServletRequest;
import io.jmix.core.security.ClientDetails;
import io.jmix.restds.auth.RestAuthenticationToken;
import io.jmix.security.model.SecurityScope;
import io.jmix.securityflowui.authentication.LoginViewSupport;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.TimeZone;

// tag::auth-token[]
@Primary
@Component
public class RestLoginViewSupport extends LoginViewSupport {

    @Override
    protected Authentication createAuthenticationToken(String username, String password, Locale locale, TimeZone timeZone) {
        RestAuthenticationToken authenticationToken = new RestAuthenticationToken(username, password);
        // ...
        // end::auth-token[]
        VaadinServletRequest request = VaadinServletRequest.getCurrent();

        ClientDetails clientDetails = ClientDetails.builder()
                .locale(locale != null ? locale : getDefaultLocale())
                .scope(SecurityScope.UI)
                .sessionId(request.getSession().getId())
                .timeZone(timeZone == null ? getDeviceTimeZone() : timeZone)
                .build();

        authenticationToken.setDetails(clientDetails);

        // tag::auth-token[]
        return authenticationToken;
    }
}
// end::auth-token[]
