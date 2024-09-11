package com.company.frontend.entity;

import io.jmix.core.FileRef;
import io.jmix.core.HasTimeZone;
import io.jmix.core.annotation.Secret;
import io.jmix.core.entity.annotation.JmixGeneratedValue;
import io.jmix.core.entity.annotation.JmixId;
import io.jmix.core.entity.annotation.SystemLevel;
import io.jmix.core.metamodel.annotation.*;
import io.jmix.security.authentication.JmixUserDetails;
import org.springframework.security.core.GrantedAuthority;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Store(name = "backend")
@JmixEntity(annotatedPropertiesOnly = true)
public class User implements JmixUserDetails, HasTimeZone {

    @JmixProperty
    @JmixId
    @JmixGeneratedValue
    private UUID id;

    @JmixProperty
    @Version
    private Integer version;

    @JmixProperty
    protected String username;

    @JmixProperty
    @Secret
    @SystemLevel
    protected String password;

    @JmixProperty
    protected String firstName;

    @JmixProperty
    protected String lastName;

    @JmixProperty
    @Email
    protected String email;

    @JmixProperty
    protected Boolean active = true;

    @JmixProperty
    protected String timeZoneId;

    @JmixProperty
    private Integer onboardingStatus;

    @JmixProperty
    private Department department;

    @JmixProperty
    @Composition
    private List<UserStep> steps;

    @JmixProperty
    private LocalDate joiningDate;

    @JmixProperty
    private FileRef picture;

    @Transient
    protected Collection<? extends GrantedAuthority> authorities;

    public UUID getId() {
        return id;
    }

    public void setId(final UUID id) {
        this.id = id;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(final Integer version) {
        this.version = version;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(final Boolean active) {
        this.active = active;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities != null ? authorities : Collections.emptyList();
    }

    @Override
    public void setAuthorities(final Collection<? extends GrantedAuthority> authorities) {
        this.authorities = authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return Boolean.TRUE.equals(active);
    }

    @InstanceName
    @DependsOnProperties({"firstName", "lastName", "username"})
    public String getDisplayName() {
        return String.format("%s %s [%s]", (firstName != null ? firstName : ""),
                (lastName != null ? lastName : ""), username).trim();
    }

    @Override
    public String getTimeZoneId() {
        return timeZoneId;
    }

    public void setTimeZoneId(final String timeZoneId) {
        this.timeZoneId = timeZoneId;
    }

    public OnboardingStatus getOnboardingStatus() {
        return onboardingStatus == null ? null : OnboardingStatus.fromId(onboardingStatus);
    }

    public void setOnboardingStatus(OnboardingStatus onboardingStatus) {
        this.onboardingStatus = onboardingStatus == null ? null : onboardingStatus.getId();
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public List<UserStep> getSteps() {
        return steps;
    }

    public void setSteps(List<UserStep> steps) {
        this.steps = steps;
    }

    public LocalDate getJoiningDate() {
        return joiningDate;
    }

    public void setJoiningDate(LocalDate joiningDate) {
        this.joiningDate = joiningDate;
    }

    public FileRef getPicture() {
        return picture;
    }

    public void setPicture(FileRef picture) {
        this.picture = picture;
    }
}