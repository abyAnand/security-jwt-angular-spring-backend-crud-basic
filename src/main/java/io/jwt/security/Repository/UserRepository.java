package io.jwt.security.Repository;

import io.jwt.security.Auth.UserDto;
import io.jwt.security.User.Role;
import io.jwt.security.User.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByEmail(String email);

    List<User> findAllByRole(Role role);
}
