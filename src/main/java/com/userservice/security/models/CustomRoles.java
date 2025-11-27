package com.userservice.security.models;

import com.userservice.models.Role;
import org.springframework.security.core.GrantedAuthority;

public class CustomRoles implements GrantedAuthority {
    private Role role;

    public CustomRoles(Role role) {
        this.role = role;
    }
    @Override
    public String getAuthority() {
        return role.getRoleName();
    }
}
