package com.kachanyan.spring.security.services;

import com.kachanyan.spring.security.models.Role;

import java.util.List;

public interface RoleService {
    Role getRoleByName(String name);

    Role getRoleById(Long id);

    List<Role> allRoles();
}
