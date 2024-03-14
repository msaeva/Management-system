package com.example.management_system.service.mapper;

import com.example.management_system.domain.dto.RegisterUserValidation;
import com.example.management_system.domain.entity.User;
import com.example.management_system.repository.UserRoleRepository;
import jakarta.inject.Inject;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "jsr330")
public abstract class UserMapper {

    @Inject
    protected UserRoleRepository userRoleRepository;

    @Mapping(target = "username", source = "validation.username")
    @Mapping(target = "role", expression = "java( userRoleRepository.findByName(validation.getRole()).orElse(null))")
    public abstract User toUser(RegisterUserValidation validation);

}
