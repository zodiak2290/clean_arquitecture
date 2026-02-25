package com.alberto.enterprise.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import com.alberto.enterprise.infrastructure.entity.UserEntity;

public interface SpringDataUserRepository extends JpaRepository<UserEntity, Long> {

}
