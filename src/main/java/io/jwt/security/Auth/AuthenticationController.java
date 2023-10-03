package io.jwt.security.Auth;

import io.jwt.security.Repository.UserRepository;
import io.jwt.security.User.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;

    private final UserRepository repository;

    public static final String TOKEN_PREFIX = "Bearer";

    public static final String HEADER_STRING = "Authorization";


    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest request
    ) {
        return ResponseEntity.ok(service.register(request));
    }
    @PostMapping("/authenticate")
    public ResponseEntity<NewAuthResponse> authenticate(
            @RequestBody AuthenticationRequest request,
            HttpServletResponse response
    ) throws IOException {


//        if(user.isPresent()){
//            response.getWriter().write( new JSONObject()
//                    .put("userId", user.get().getId())
//                    .put("role",user.get().getRole())
//                    .toString());
//        }
//
//        response.setHeader("Access-Control-Expose-Headers", "Authorization");
//        response.setHeader("Access-Control-Allow-Headers", "Authorization, X-Pingother, Origin, X-Requested-With, Content-Type,Accept,X-Custom-Header");
//        AuthenticationResponse authRes = service.authenticate(request);
//        response.setHeader(HEADER_STRING,TOKEN_PREFIX + authRes.getAccessToken());


        AuthenticationResponse authResponse = service.authenticate(request);

        Optional<User> user = repository.findByEmail(request.getEmail());

        NewAuthResponse newAuth = null;
        if (user.isPresent()) {

            new UserDto();
            UserDto userDto = UserDto.builder()
                    .userId(user.get().getId())
                    .name(user.get().getUsername())
                    .role(String.valueOf(user.get().getRole()))
                    .build();


            new NewAuthResponse();
            newAuth = NewAuthResponse.builder()
                    .userDto(userDto)
                    .accessToken(authResponse.getAccessToken())
                    .refreshToken(authResponse.getRefreshToken())
                    .build();

        }


        return ResponseEntity.ok(newAuth);
    }

    @PostMapping("/refresh-token")
    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        service.refreshToken(request, response);
    }


}
