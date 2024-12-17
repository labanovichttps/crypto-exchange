package com.labanovich.crypto.exchange.service;

import com.labanovich.crypto.exchange.dto.UserCreateDto;
import com.labanovich.crypto.exchange.dto.UserInfoDetails;
import com.labanovich.crypto.exchange.entity.User;
import com.labanovich.crypto.exchange.exception.EntityNotFoundException;
import com.labanovich.crypto.exchange.mapper.UserMapper;
import com.labanovich.crypto.exchange.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
            .map(UserInfoDetails::new)
            .orElseThrow(() -> new UsernameNotFoundException(String.format("User wasn't found with username = %s", username)));
    }

    @Transactional
    public void createUser(UserCreateDto userCreateDto) {
        var encodedPassword = passwordEncoder.encode(userCreateDto.getPassword());
        var user = userMapper.map(userCreateDto, encodedPassword);
        userRepository.save(user);
    }

    public User get(String username) {
        return userRepository.findByUsername(username)
            .orElseThrow(() -> new EntityNotFoundException("username", username));
    }
}
