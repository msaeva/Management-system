package com.example.management_system.service.mapper;

import com.example.management_system.repository.UserRoleRepository;
import jakarta.inject.Inject;
import org.mapstruct.Mapper;

@Mapper(componentModel = "jsr330")
public abstract class UserMapper {

    @Inject
    protected UserRoleRepository userRoleRepository;

//    @Mapping(target = "username", source = "validation.username")
//    @Mapping(target = "role", expression = "java( userRoleRepository.findByName(validation.getRole()).orElse(null))")
//    public abstract User toUser(RegisterUserValidation validation);

}
