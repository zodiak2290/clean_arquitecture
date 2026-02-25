package com.alberto.enterprise.domain.repository;

import java.util.Optional;

import com.alberto.enterprise.domain.model.User;

public interface UserRepository {
	 User save(User user);
	 Optional<User> findById(Long id);
	 User updateAvatar(Long id, String avatarUrl);
}
