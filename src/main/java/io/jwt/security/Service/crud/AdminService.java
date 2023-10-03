package io.jwt.security.Service.crud;

import io.jwt.security.Auth.NewAuthResponse;
import io.jwt.security.Auth.RegisterRequest;
import io.jwt.security.Auth.UserDto;
import io.jwt.security.Repository.UserRepository;
import io.jwt.security.Service.JwtService;
import io.jwt.security.User.Role;
import io.jwt.security.User.User;
import io.jwt.security.token.Token;
import io.jwt.security.token.TokenRepository;
import io.jwt.security.token.TokenType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepo;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    private final TokenRepository tokenRepository;



    public User postUser(User user){
        return userRepo.save(user);
    }

    public UserDto register(RegisterRequest request) {
        var user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .address(request.getAddress())
                .role("USER".equals(request.getRole()) ? Role.USER : "ADMIN".equals(request.getRole()) ? Role.ADMIN : null)
                .build();


        var savedUser = userRepo.save(user);
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        saveUserToken(savedUser, jwtToken);

        var userDto = UserDto.builder()
                .name(savedUser.getName())
                .userId(savedUser.getId())
                .address(savedUser.getAddress())
                .email(savedUser.getEmail())
                .role(savedUser.getRole().toString())
                .build();

        return userDto;


//        return NewAuthResponse.builder()
//                .accessToken(jwtToken)
//                .refreshToken(refreshToken)
//                .userDto(userDto)
//                .build();
    }

    private void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }


    public List<UserDto> getAllUsers(){
        return userRepo.findAllByRole(Role.USER)
                .stream()
                .filter(user -> !user.isDeleted())
                .map(User::getUserDto)
                .collect(Collectors.toList());
    }

    public void deleteUser(int id){

        User user = userRepo.findById(id).orElse(null);

        user.setDeleted(true);

        userRepo.save(user);
    }


    public UserDto findById(int id){

        User user = userRepo.findById(id).orElse(null);

        assert user != null;
        return UserDto.builder()
                .userId(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .address(user.getAddress())
                .role(user.getRole().toString())
                .build();
    }


//    public UserDto updateUser(int id, UserDto userDto) {
//        Optional<User> optionalUser = userRepo.findById(id);
//        if(optionalUser.isPresent()){
//            User user = optionalUser.get();
//            user.setName(userDto.getName());
//            user.setEmail(userDto.getEmail());
//            user.setRole("USER".equals(userDto.getRole()) ? Role.USER : "ADMIN".equals(userDto.getRole()) ? Role.ADMIN : null);
//            user.setAddress(user.getAddress());
//
//            User updatedUser = userRepo.save(user);
//
//            UserDto updateUserDto = new UserDto();
//            updateUserDto.setUserId(userDto.getUserId());
//            return updateUserDto;
//        }
//        return null;
//    }

    public UserDto updateUser(int userId, UserDto userDto){
        Optional<User> optionalUser = userRepo.findById(userId);
        if(optionalUser.isPresent()){
            User user = optionalUser.get();
            BeanUtils.copyProperties(userDto, user);
            user.setRole("USER".equals(userDto.getRole()) ? Role.USER : "ADMIN".equals(userDto.getRole()) ? Role.ADMIN : null);
            User updatedUser = userRepo.save(user);
            UserDto updateUserDto = new UserDto();
            updateUserDto.setUserId(user.getId());
            BeanUtils.copyProperties(updatedUser, updateUserDto);
            return updateUserDto;
        }
        return null;
    }
}
