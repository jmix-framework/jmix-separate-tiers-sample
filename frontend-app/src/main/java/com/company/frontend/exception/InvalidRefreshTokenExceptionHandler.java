package com.company.frontend.exception;

import io.jmix.flowui.exception.AbstractUiExceptionHandler;
import io.jmix.flowui.sys.LogoutSupport;
import io.jmix.restds.exception.InvalidRefreshTokenException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

// tag::exception-handler[]
@Component
public class InvalidRefreshTokenExceptionHandler extends AbstractUiExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(InvalidRefreshTokenExceptionHandler.class);

    @Autowired
    private LogoutSupport logoutSupport;

    public InvalidRefreshTokenExceptionHandler() {
        super(InvalidRefreshTokenException.class.getName());
    }

    @Override
    protected void doHandle(@NonNull String className, @NonNull String message, @Nullable Throwable throwable) {
        log.debug(message);

        logoutSupport.logout();
    }
}
// end::exception-handler[]
