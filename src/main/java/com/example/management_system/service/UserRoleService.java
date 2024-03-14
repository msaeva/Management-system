package com.example.management_system.service;

import com.example.management_system.domain.entity.UserRole;
import com.example.management_system.repository.UserRoleRepository;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Stateless
public class UserRoleService {

    @Inject
    private UserRoleRepository userRoleRepository;

    public UserRole findByName(String name) {
        return userRoleRepository.findByName(name).orElse(null);
    }

    public Set<String> findRoleByUsername(String username) {
        List<UserRole> userRoles = userRoleRepository.findRoleByUserUsername(username);
        return userRoles
                .stream()
                .map(u -> u.getRole().name())
                .collect(Collectors.toSet());
    }
}
