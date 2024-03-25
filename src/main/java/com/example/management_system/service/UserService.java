package com.example.management_system.service;

import com.example.management_system.controller.errors.InvalidUserException;
import com.example.management_system.controller.errors.UserNotFoundException;
import com.example.management_system.domain.dto.user.DetailedUserDTO;
import com.example.management_system.domain.dto.user.RegisterUserValidation;
import com.example.management_system.domain.dto.user.SimpleUserDTO;
import com.example.management_system.domain.dto.user.UpdateUserValidation;
import com.example.management_system.domain.entity.User;
import com.example.management_system.domain.entity.UserRole;
import com.example.management_system.repository.UserRepository;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.mindrot.jbcrypt.BCrypt;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Stateless
public class UserService {
    @Inject
    private UserRepository userRepository;

    @Inject
    private UserRoleService userRoleService;

    private static final Logger LOGGER = Logger.getLogger(UserService.class.getName());

    @Transactional
    public SimpleUserDTO create(RegisterUserValidation validation) {

        Optional<User> byUsername = userRepository.findByUsername(validation.getUsername());
        Optional<User> byEmail = userRepository.findByEmail(validation.getUsername());

        if (byUsername.isPresent()) {
            throw new InvalidUserException("Username already exists!");
        }
        // TODO check by username and email
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

    public Set<User> findAllByIds(List<Long> userIds) {
        return new HashSet<>(userRepository.findAllByIds(userIds));
    }

    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User with id : " + id + " not found!"));
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

    public List<DetailedUserDTO> getALl() {
        return userRepository
                .findAll()
                .stream()
                .map(this::maptoDetailedUserDTO)
                .collect(Collectors.toList());
    }

    public DetailedUserDTO maptoDetailedUserDTO(User user) {
        return new DetailedUserDTO(user.getId(),
                user.getUsername(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getRole().getRole().name());
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
}
