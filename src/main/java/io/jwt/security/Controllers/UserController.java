package io.jwt.security.Controllers;


import io.jwt.security.Auth.UserDto;
import io.jwt.security.Auth.UserDtoWithoutRole;
import io.jwt.security.Service.crud.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;


    @PutMapping("update/{studentId}")
    public ResponseEntity<?> updateStudent(@PathVariable int studentId, @RequestBody UserDtoWithoutRole userDto){
        UserDto theUserDto = userService.updateUser(studentId, userDto);
        if(theUserDto == null) return new ResponseEntity<>("Something went wrong", HttpStatus.BAD_REQUEST);
        return ResponseEntity.status(HttpStatus.CREATED).body(theUserDto);
    }
}
