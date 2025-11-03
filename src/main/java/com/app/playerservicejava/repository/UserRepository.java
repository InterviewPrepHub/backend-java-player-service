package com.app.playerservicejava.repository;

import com.app.playerservicejava.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
}
