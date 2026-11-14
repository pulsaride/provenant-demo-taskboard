package com.github.hantsy.ee8sample.security;

import com.github.hantsy.ee8sample.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Users come from the retained UserRepository.findByUsername, the lookup the legacy identity
 * store performs; the user's roles are its authorities, written as stored.
 */
@Service
public class LegacyUserDetailsService implements UserDetailsService {

    private final ObjectProvider<UserRepository> repositories;

    public LegacyUserDetailsService(ObjectProvider<UserRepository> repositories) {
        this.repositories = repositories;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = repositories.getObject().findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("unknown user"));
        if (user.getPassword() == null || user.getUsername() == null) {
            throw new UsernameNotFoundException("user without credentials");
        }
        List<GrantedAuthority> authorities = new ArrayList<>();
        if (user.getRoles() != null) {
            for (String role : user.getRoles()) {
                authorities.add(new SimpleGrantedAuthority(role));
            }
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
    }
}
