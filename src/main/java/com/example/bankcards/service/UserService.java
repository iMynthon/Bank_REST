package com.example.bankcards.service;
import com.example.bankcards.dto.request.UserRequest;
import com.example.bankcards.dto.response.AllUserResponse;
import com.example.bankcards.dto.response.UserResponse;
import com.example.bankcards.exception.EntityNotFoundException;
import com.example.bankcards.mapper.UserMapper;
import com.example.bankcards.model.Role;
import com.example.bankcards.model.User;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final ApplicationContext context;
    private final PasswordEncoder encoder;

    @Transactional(readOnly = true)
    public AllUserResponse findAll(Pageable pageable){
        return userMapper.listEntityToListResponse(userRepository.findAll(pageable));
    }

    public UserResponse findByUser(){
        return userMapper.entityToResponse(context.getBean(UserService.class).findById());
    }

    public UserResponse findByUser(UUID id){
        return userMapper.entityToResponse(context.getBean(UserService.class).findById(id));
    }

    @Transactional(readOnly = true)
    public User findById(){
        return userRepository.findById(SecurityUtils.userId())
                .orElseThrow(()-> new EntityNotFoundException("Такой пользователь не зарегистрирован в системe"));
    }

    @Transactional(readOnly = true)
    public User findById(UUID id){
        return userRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("Такой пользователь не зарегистрирован в системe"));
    }

    @Transactional(readOnly = true)
    public User findByPhoneNumber(String phoneNumber){
        return userRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(()-> new EntityNotFoundException("Такой пользователь не зарегистрирован в системe"));
    }

    @Transactional
    public UserResponse save(UserRequest request){
        User saveUser = userMapper.requestToEntity(request);
        Role role = Role.builder().roleType(request.role()).user(saveUser).build();
        saveUser.getRoles().add(role);
        saveUser.setPassword(encoder.encode(saveUser.getPassword()));
        return userMapper.entityToResponse(userRepository.save(saveUser));
    }

    @Transactional
    public UserResponse update(UserRequest request){
        User exists = context.getBean(UserService.class).findById();
        userMapper.updateEntity(exists,userMapper.requestToEntity(request));
        return userMapper.entityToResponse(userRepository.save(exists));
    }

    public String delete(UUID userId){
        userRepository.deleteById(userId);
        return "Пользователь удален из системы";
    }

}
