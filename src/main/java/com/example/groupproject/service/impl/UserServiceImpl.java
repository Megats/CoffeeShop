package com.example.groupproject.service.impl;


import com.example.groupproject.dto.UserDto;
import com.example.groupproject.entity.Role;
import com.example.groupproject.entity.User;
import com.example.groupproject.repository.RoleRepository;
import com.example.groupproject.repository.UserRepository;
import com.example.groupproject.service.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService{

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void saveUser(UserDto userDto){
        User user = new User();
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        //encrypt password using spring security
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setPhone_num(userDto.getPhone_num());
        user.setAddress(userDto.getAddress());

        //determine role based on email domain
        String emailDomain = userDto.getEmail().substring(userDto.getEmail().indexOf("@")+1);
        String roleName = emailDomain.equals("coffeeblends.com") ? "SELLER" : "CUSTOMER";

        //find the role by name, or create it if it doesn't exist
        Role role = roleRepository.findByName(roleName);
        if(role == null){
            role = new Role();
            role.setName(roleName);
            roleRepository.save(role);
        }
       /* Role role;
        if (domain.equals("coffee.com")){
            role = roleRepository.findByName("Seller");
            if(role == null){
                role = createRoleIfNotFound("Seller");
            }
        }else{
            role = roleRepository.findByName("Customer");
            if (role == null){
                role = createRoleIfNotFound("Customer");
            }
        }*/
        user.setRoles(Arrays.asList(role));
        userRepository.save(user);
    }


    @Override
    public User findUserByEmail(String email){
        return userRepository.findByEmail(email);
    }


    @Override
    public List<UserDto> findAllUsers(){
        List<User> users = userRepository.findAll();
            return users.stream()
                    .map((user) -> mapToUserDto(user))
                    .collect(Collectors.toList());


    }

    private UserDto mapToUserDto(User user){
        UserDto userDto = new UserDto();
        //String[] str = user.getName().split(" ");
        //userDto.setName(str[0]);
        //userDto.setName(str[1]);
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());
        userDto.setPhone_num(user.getPhone_num());
        userDto.setAddress(user.getAddress());
        //userDto.setName(user.getName());
        return userDto;
    }


}
