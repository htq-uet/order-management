package com.ghtk.repository;

import com.ghtk.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    @Query(nativeQuery = true, value = "SELECT * FROM user WHERE username = ?1")
    User findUserByUsername(String username);

    @Query(nativeQuery = true, value = "SELECT EXISTS(SELECT * FROM user WHERE username = ?1)")
    int existsByUsername(String username);

    @Query (
            nativeQuery = true,
            value = "SELECT id from user where username = ?1"
    )
    int findIdByUsername(String username);

    @Query (
            nativeQuery = true,
            value = "SELECT password from user where username = ?1"
    )
    String findPasswordByUsername(String username);

    @Query (
            nativeQuery = true,
            value = "SELECT role from user where username = ?1"
    )
    String findRoleByUsername(String username);

    @Query(
            nativeQuery = true,
            value = "SELECT s.shop_id " +
                    "FROM staff s " +
                    "left join user u " +
                    "on s.user_id = u.id " +
                    "WHERE u.username = ?1 " +
                    "UNION " +
                    "SELECT sh.id " +
                    "FROM shop sh " +
                    "left join user u " +
                    "on sh.user_id = u.id " +
                    "WHERE u.username =?1"
    )
    Integer findShopIdByUsername(String username) ;
}
