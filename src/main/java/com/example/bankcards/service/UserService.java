package com.example.bankcards.service;
import com.example.bankcards.aop.LogService;
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

import static com.example.bankcards.util.StringUtilsMessage.USER_DELETE;
import static com.example.bankcards.util.StringUtilsMessage.USER_ENTITY_NOT_FOUND;

@Service
@RequiredArgsConstructor
@LogService
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final ApplicationContext context;
    private final PasswordEncoder encoder;


    @Transactional(readOnly = true)
    public AllUserResponse findAll(Pageable pageable){
        return userMapper.listEntityToListResponse(userRepository.findAll(pageable));
    }

    public UserResponse findByUserSecurityContext(){
        return userMapper.entityToResponse(context.getBean(UserService.class).findById());
    }

    @Transactional(readOnly = true)
    public UserResponse findByUserId(UUID id){
        return userMapper.entityToResponse(userRepository.findById(id)
                .orElseThrow());
    }

    @Transactional(readOnly = true)
    public User findById(){
        return userRepository.findById(SecurityUtils.userId())
                .orElseThrow(()-> new EntityNotFoundException(USER_ENTITY_NOT_FOUND));
    }

    @Transactional
    public UserResponse save(UserRequest request){
        return userMapper.entityToResponse(userRepository.save(createUser(request)));
    }

    @Transactional
    public UserResponse update(UserRequest request){
        User exists = context.getBean(UserService.class).findById();
        User root = userMapper.requestToEntity(request);
        userMapper.updateEntity(exists,root);
        return userMapper.entityToResponse(userRepository.save(exists));
    }

    public String delete(UUID userId){
        userRepository.deleteById(userId);
        return USER_DELETE;
    }

    private User createUser(UserRequest request){
        User saveUser = userMapper.requestToEntity(request);
        Role role = Role.builder().roleType(request.role()).user(saveUser).build();
        saveUser.getRoles().add(role);
        saveUser.setPassword(encoder.encode(saveUser.getPassword()));
        return saveUser;
    }

}
