package io.jwt.security.Auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDtoWithoutRole {

    private Integer userId;
    private String name;
    private String email;
    private String address;

}
