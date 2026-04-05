package edu.eci.dosw.tdd.security;

import edu.eci.dosw.tdd.persistence.relational.entity.UserEntity;
import edu.eci.dosw.tdd.persistence.relational.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity entity = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Usuario no encontrado: " + username));

        return new org.springframework.security.core.userdetails.User(
                entity.getUsername(),
                entity.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_" + entity.getRole()))
        );
    }
}