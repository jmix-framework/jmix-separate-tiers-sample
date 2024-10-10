package com.company.backend.security;

import com.company.backend.entity.Step;
import com.company.backend.entity.User;
import com.company.backend.entity.UserStep;
import io.jmix.rest.security.role.RestMinimalRole;
import io.jmix.security.model.EntityAttributePolicyAction;
import io.jmix.security.model.EntityPolicyAction;
import io.jmix.security.model.SecurityScope;
import io.jmix.security.role.annotation.EntityAttributePolicy;
import io.jmix.security.role.annotation.EntityPolicy;
import io.jmix.security.role.annotation.ResourceRole;

// tag::employee-role[]
@ResourceRole(name = "Employee", code = "employee", scope = SecurityScope.API)
public interface EmployeeRole extends RestMinimalRole {

    @EntityAttributePolicy(entityClass = User.class,
            attributes = "*",
            action = EntityAttributePolicyAction.VIEW)
    @EntityPolicy(entityClass = User.class,
            actions = {EntityPolicyAction.READ, EntityPolicyAction.UPDATE})
    void user();

    @EntityAttributePolicy(entityClass = UserStep.class, attributes = "completedDate", action = EntityAttributePolicyAction.MODIFY)
    @EntityAttributePolicy(entityClass = UserStep.class, attributes = {"id", "version", "user", "step", "dueDate", "sortValue"}, action = EntityAttributePolicyAction.VIEW)
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