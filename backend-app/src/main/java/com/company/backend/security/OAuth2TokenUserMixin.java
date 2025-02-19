package com.company.backend.security;

import com.company.backend.entity.Department;
import com.company.backend.entity.UserStep;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.jmix.authserver.service.mapper.DefaultOAuth2TokenUserMixin;
import io.jmix.core.FileRef;

import java.util.List;

public class OAuth2TokenUserMixin extends DefaultOAuth2TokenUserMixin {

    @JsonIgnore
    private Department department;

    @JsonIgnore
    private List<UserStep> steps;

    @JsonIgnore
    private FileRef picture;
}
