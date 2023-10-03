package io.jwt.security.Controllers;

import io.jwt.security.Auth.*;
import io.jwt.security.Service.crud.AdminService;
import io.jwt.security.User.User;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private AuthenticationService authService;


    @PostMapping("/user")
    public ResponseEntity<?> addStudent(@RequestBody
                                            RegisterRequest user){

        var authResponse = adminService.register(user);
        if(authResponse == null){
            return new ResponseEntity<>("Something went wrong", HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(authResponse);
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserDto>> getAllUsers(){
        var allUsers = adminService.getAllUsers();
        return ResponseEntity.ok(allUsers);
    }


    @DeleteMapping("/users/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable int userId){
        adminService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<UserDto> findById(@PathVariable int userId){
        UserDto userDto = adminService.findById(userId);
        if(userDto == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(userDto);
    }

    @PutMapping("/users/{studentId}")
    public ResponseEntity<?> updateStudent(@PathVariable int studentId,@RequestBody UserDto userDto){
        UserDto theUserDto = adminService.updateUser(studentId, userDto);
        if(theUserDto == null) return new ResponseEntity<>("Something went wrong", HttpStatus.BAD_REQUEST);
        return ResponseEntity.status(HttpStatus.CREATED).body(theUserDto);
    }


}
