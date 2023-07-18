package com.ghtk.repository;

import com.ghtk.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findUserByUsername(String username);

    @Query(nativeQuery = true, value = "SELECT EXISTS(SELECT * FROM user WHERE username = ?1)")
    int existsByUsername(String username);

    @Query (
            nativeQuery = true,
            value = "SELECT id from user where username = ?1"
    )
    int findIdByUsername(String username);
}
