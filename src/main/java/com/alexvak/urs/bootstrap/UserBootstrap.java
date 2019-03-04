package com.alexvak.urs.bootstrap;

import com.alexvak.urs.domain.User;
import com.alexvak.urs.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@Profile("default")
public class UserBootstrap implements ApplicationListener<ContextRefreshedEvent> {

    private final UserRepository userRepository;

    public UserBootstrap(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        log.debug("Loading Bootstrap Data");
        userRepository.saveAll(getUsers());
    }

    private List<User> getUsers() {
        List<User> userList = new ArrayList<>();

        User user1 = new User();
        user1.setName("Test User1");
        user1.setEmail("user1@gmail.com");
        user1.setAddress("Test User Address");

        User user2 = new User();
        user2.setName("Test User2");
        user2.setEmail("user2@gmail.com");
        user2.setAddress("Test User Address");

        userList.add(user1);
        userList.add(user2);

        return userList;
    }

}
