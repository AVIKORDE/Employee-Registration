package com.example.demo.REpo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.User;

@Repository
public interface userRepo extends JpaRepository<User, Long> {

	User findByUsername(String username);

	User findByEmail(String email);
}
