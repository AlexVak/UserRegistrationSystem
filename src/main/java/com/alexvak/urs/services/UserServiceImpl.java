package com.alexvak.urs.services;

import com.alexvak.urs.domain.User;
import com.alexvak.urs.exceptions.DuplicateUserFoundException;
import com.alexvak.urs.exceptions.UserNotFoundException;
import com.alexvak.urs.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User createUser(User user) {
        Optional<User> userDTOOptional = userRepository.findByName(user.getName());

        if (userDTOOptional.isPresent()) {
            throw new DuplicateUserFoundException(user.getName());

        }

        return userRepository.save(user);
    }

    @Override
    public User findById(Long id) {
        Optional<User> userDTOOptional = userRepository.findById(id);

        if (!userDTOOptional.isPresent()) {
            throw new UserNotFoundException(id);

        }
        return userDTOOptional.get();
    }

    @Override
    public User updateUser(Long id, User user) {
        User currentUser = findById(id);

        currentUser.setName(user.getName());
        currentUser.setAddress(user.getAddress());
        currentUser.setEmail(user.getEmail());

        return userRepository.saveAndFlush(currentUser);
    }

    @Override
    public void deleteUser(Long id) {
        Optional<User> userDTOOptional = userRepository.findById(id);

        if (!userDTOOptional.isPresent()) {
            throw new UserNotFoundException(id);

        }
        
        userRepository.deleteById(id);
    }
}
