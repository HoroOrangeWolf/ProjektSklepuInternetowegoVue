package com.computer.parts.shop.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM account u WHERE u.email LIKE ?1")
    Optional<User> findByEmail(String email);

    @Query("SELECT u FROM account u WHERE UPPER(u.email) LIKE CONCAT(UPPER(?1), '%')")
    List<User> findAllBySimilarEmail(String email);

}
