package com.techm.mobicom.config;

import com.techm.mobicom.models.Admin;
import com.techm.mobicom.models.User;
import com.techm.mobicom.repository.AdminRepository;
import com.techm.mobicom.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;

@Service
@Primary
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return adminRepository.findByUsername(username)
                .map(admin -> new org.springframework.security.core.userdetails.User(
                        admin.getUsername(),
                        admin.getPassword(),
                        Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN"))
                ))
                .orElseThrow(() -> new UsernameNotFoundException("Username not found: " + username));
    }

    public UserDetails loadUserByMobileNumber(String mobileNumber) throws UsernameNotFoundException {
        return userRepository.findByMobileNumber(mobileNumber)
                .map(user -> new org.springframework.security.core.userdetails.User(
                        user.getMobileNumber(),
                        "",
                        Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
                ))
                .orElseThrow(() -> new UsernameNotFoundException("Mobile number not found: " + mobileNumber));
    }
}