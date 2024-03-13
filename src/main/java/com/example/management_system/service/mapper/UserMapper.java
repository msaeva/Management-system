package com.example.management_system.service.mapper;

import com.example.management_system.domain.dto.RegisterUserValidation;
import com.example.management_system.domain.entity.User;
import com.example.management_system.domain.entity.UserRole;
import com.example.management_system.repository.UserRoleRepository;
import jakarta.inject.Inject;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "jsr330")
public abstract class UserMapper {

    @Inject
    protected UserRoleRepository userRoleRepository;

    @Mapping(target = "role", expression = "java( userRoleRepository.findByName(validation.getRole()).orElse(null))")
    public abstract User toUser(RegisterUserValidation validation);

//    @Mapping(target = "role", expression = "java(findUserRole(validation.getRole()))")
//    public abstract User toUser(RegisterUserValidation validation);
//
//    protected UserRole findUserRole(String name) {
//        return userRoleRepository.findByName(name)
//                .orElseThrow(() -> new RuntimeException("UserRole not found for name: " + name));
//    }
}
