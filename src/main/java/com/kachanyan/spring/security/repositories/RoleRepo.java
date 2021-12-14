package com.kachanyan.spring.security.repositories;

import com.kachanyan.spring.security.models.Role;

import java.util.List;

public interface RoleRepo {
    Role getRoleByName(String name);

    Role getRoleById(Long id);

    List<Role> allRoles();
}
