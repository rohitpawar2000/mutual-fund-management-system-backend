package com.user.user.jwt;

import com.user.user.entity.User;
import com.user.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class CustomUserDetailsService  implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {


        System.out.println("This is  customyserdetails--------------------"+ username);
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> {
                    System.out.println("❌ USER NOT FOUND EXECUTED");
                    return new UsernameNotFoundException("User not found");
                });

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
//                .roles(user.getRole().name().replace("ROLE_", ""))
                .build();
    }


}
