package com.projectlms.projectlms.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.projectlms.projectlms.domain.dao.User;
import com.projectlms.projectlms.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("user " + username + " not found"));

        log.info("User '{}' found", username);
        return CustomUserDetails.build(user);
    }
}





//         Optional<User> user = userRepository.findByUsername(username);

//         if (user.isEmpty()) {
//             throw new UsernameNotFoundException("User " + username + " not found");
//         }
// //                .orElseThrow(() -> new UsernameNotFoundException("User Not Found for the username: " + username));

//         return CustomUserDetails.build(user.get());

        // User user = userRepository.findByUsername(username);

        // if(user != null) {
        //     log.info("User '{}' found", username);
        //     return user;
        // } else {
        //     log.error("User '{}' not found", username);
        //     throw new UsernameNotFoundException("Username not found");
        // }
    
