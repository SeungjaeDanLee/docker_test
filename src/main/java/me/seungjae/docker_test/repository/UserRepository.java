package me.seungjae.docker_test.repository;

import me.seungjae.docker_test.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}