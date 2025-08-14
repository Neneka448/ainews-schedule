package com.mortis.ainews.application.persistence.repository.interfaces;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mortis.ainews.application.persistence.po.users.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByAcAndDeleted(String ac, int deleted);

    Optional<User> findByIdAndDeleted(Long id, int deleted);
}
