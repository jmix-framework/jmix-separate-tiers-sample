package com.company.frontend.security;

import com.company.frontend.entity.Step;
import com.company.frontend.entity.User;
import com.company.frontend.entity.UserStep;
import io.jmix.security.model.EntityAttributePolicyAction;
import io.jmix.security.model.EntityPolicyAction;
import io.jmix.security.model.SecurityScope;
import io.jmix.security.role.annotation.EntityAttributePolicy;
import io.jmix.security.role.annotation.EntityPolicy;
import io.jmix.security.role.annotation.ResourceRole;
import io.jmix.securityflowui.role.annotation.MenuPolicy;
import io.jmix.securityflowui.role.annotation.ViewPolicy;

// tag::employee-role[]
@ResourceRole(name = "Employee", code = "employee", scope = SecurityScope.UI)
public interface EmployeeRole extends UiMinimalRole {

    @MenuPolicy(menuIds = "MyOnboardingView")
    @ViewPolicy(viewIds = "MyOnboardingView")
    void screens();

    @EntityAttributePolicy(entityClass = User.class,
            attributes = "*",
            action = EntityAttributePolicyAction.VIEW)
    @EntityPolicy(entityClass = User.class,
            actions = {EntityPolicyAction.READ, EntityPolicyAction.UPDATE})
    void user();

    @EntityAttributePolicy(entityClass = UserStep.class,
            attributes = "*",
            action = EntityAttributePolicyAction.VIEW)
    @EntityPolicy(entityClass = UserStep.class,
            actions = {EntityPolicyAction.READ, EntityPolicyAction.UPDATE})
    void userStep();

    @EntityAttributePolicy(entityClass = Step.class,
            attributes = "*",
            action = EntityAttributePolicyAction.VIEW)
    @EntityPolicy(entityClass = Step.class,
            actions = EntityPolicyAction.READ)
    void step();
}
// end::employee-role[]