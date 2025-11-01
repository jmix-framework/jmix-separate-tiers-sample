package com.company.frontend;

import com.google.common.base.Strings;
import io.jmix.core.JmixOrder;
import io.jmix.core.MetadataMutationTools;
import io.jmix.core.MetadataPostProcessor;
import io.jmix.core.metamodel.model.Session;
import io.jmix.flowuirestds.genericfilter.FilterConfiguration;
import io.jmix.flowuirestds.settings.UserSettingsItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Temporary fix of https://github.com/jmix-framework/jmix/issues/4850
 */
@Component
@Order(JmixOrder.LOWEST_PRECEDENCE - 100)
public class FrontendRestDsEntityConfigurer implements MetadataPostProcessor {

    @Value("${jmix.restds.ui-config-store:}")
    private String uiConfigStore;

    @Autowired
    private MetadataMutationTools metadataMutationTools;

    @Override
    public void process(Session session) {
        if (Strings.isNullOrEmpty(uiConfigStore))
            return;

        metadataMutationTools.setStore(session.getClass(UserSettingsItem.class), uiConfigStore);
        metadataMutationTools.setStore(session.getClass(FilterConfiguration.class), uiConfigStore);
    }
}
