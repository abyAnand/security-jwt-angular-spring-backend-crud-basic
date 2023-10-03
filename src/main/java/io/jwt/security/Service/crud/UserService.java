package io.jwt.security.Service.crud;

import io.jwt.security.Auth.UserDto;
import io.jwt.security.Auth.UserDtoWithoutRole;
import io.jwt.security.Repository.UserRepository;
import io.jwt.security.User.Role;
import io.jwt.security.User.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    UserRepository userRepo;

    public UserDto updateUser(int userId, UserDtoWithoutRole userDto){
        Optional<User> optionalUser = userRepo.findById(userId);
        if(optionalUser.isPresent()){
            User user = optionalUser.get();
            BeanUtils.copyProperties(userDto, user);
            user.setRole(Role.USER);
            User updatedUser = userRepo.save(user);
            UserDto updateUserDto = new UserDto();
            updateUserDto.setUserId(user.getId());
            BeanUtils.copyProperties(updatedUser, updateUserDto);
            updateUserDto.setRole(Role.USER.name());
            return updateUserDto;
        }
        return null;
    }
}
