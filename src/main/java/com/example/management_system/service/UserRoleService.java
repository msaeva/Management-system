package com.example.management_system.service;

import com.example.management_system.domain.entity.UserRole;
import com.example.management_system.repository.UserRoleRepository;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

@Stateless
public class UserRoleService {

    @Inject
    private UserRoleRepository userRoleRepository;

    public UserRole findByName(String name) {
        return userRoleRepository.findByName(name).orElse(null);
    }
}
