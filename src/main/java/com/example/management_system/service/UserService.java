package com.example.management_system.service;

import com.example.management_system.controller.errors.InvalidUserException;
import com.example.management_system.controller.errors.UserNotFoundException;
import com.example.management_system.domain.dto.Pagination;
import com.example.management_system.domain.dto.user.*;
import com.example.management_system.domain.entity.User;
import com.example.management_system.domain.entity.UserRole;
import com.example.management_system.repository.UserRepository;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.mindrot.jbcrypt.BCrypt;

import java.util.*;
import java.util.stream.Collectors;

@Stateless
public class UserService {
    @Inject
    private UserRepository userRepository;

    @Inject
    private UserRoleService userRoleService;

    @Transactional
    public SimpleUserDTO create(RegisterUserValidation validation) {

        Optional<User> byUsername = userRepository.findByUsername(validation.getUsername());
        Optional<User> byEmail = userRepository.findByEmail(validation.getUsername());

        if (byUsername.isPresent() || byEmail.isPresent()) {
            throw new InvalidUserException("User already exists!");
        }

        String encodedPassword = encodePassword(validation.getPassword());
        UserRole role;
        if (validation.getRole() == null || validation.getRole().isEmpty()) {
            role = userRoleService.findByName("USER");
        } else {
            role = userRoleService.findByName(validation.getRole());
        }

        User user = new User(validation.getUsername(),
                validation.getFirstName(),
                validation.getLastName(),
                validation.getEmail(),
                encodedPassword,
                role);

        User saved = userRepository.save(user);
        return mapToSimpleUserDTO(saved);
    }

    public boolean deleteById(Long id) {
        return userRepository.deleteById(id);
    }

    public void update(UpdateUserValidation validation) {
        User user = findById(validation.getId());
        UserRole role = userRoleService.findByName(validation.getRole());

        user.setFirstName(validation.getFirstName());
        user.setLastName(validation.getLastName());
        user.setRole(role);
        userRepository.save(user);
    }

    public List<PrivateSimpleUserDTO> getByRoles(String roles) {
        List<UserRole> userRoles = Arrays.stream(roles.split(","))
                .map(role -> userRoleService.findByName(role))
                .collect(Collectors.toList());

        return userRepository.findAllByRoleIds(userRoles.stream().map(UserRole::getId).collect(Collectors.toList()))
                .stream()
                .map(this::mapToPrivateSimpleUserDTO)
                .collect(Collectors.toList());
    }

    public Set<User> findAllByIds(List<Long> userIds) {
        return new HashSet<>(userRepository.findAllByIds(userIds));
    }

    public User findById(Long id) {
        return userRepository
                .findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with id : " + id + " not found!"));
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    private String encodePassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }


    public boolean checkPassword(String password, String hashed) {
        return BCrypt.checkpw(password, hashed);
    }

    public SimpleUserDTO mapToSimpleUserDTO(User user) {
        return new SimpleUserDTO(user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole().getRole().name());
    }

    public PrivateSimpleUserDTO mapToPrivateSimpleUserDTO(User user) {
        return new PrivateSimpleUserDTO(user.getId(),
                user.getUsername(),
                user.getFirstName() + " " + user.getLastName(),
                user.getRole().getRole().name());
    }

    public Pagination<DetailedUserDTO> getAll(int page, int size, String sort, String order) {
        long totalRecords = userRepository.getTotalCount();
        List<DetailedUserDTO> users = userRepository
                .findAll(page, size, sort, order)
                .stream()
                .map(this::maptoDetailedUserDTO)
                .collect(Collectors.toList());

        return new Pagination<>(users, totalRecords);
    }

    public DetailedUserDTO maptoDetailedUserDTO(User user) {
        return new DetailedUserDTO(user.getId(),
                user.getUsername(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getRole().getRole().name());
    }

}
