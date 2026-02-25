package com.alberto.enterprise.application.usecase;

import com.alberto.enterprise.application.port.out.CacheService;
import com.alberto.enterprise.application.port.out.EventPublisher;
import com.alberto.enterprise.application.port.out.FileStorage;
import com.alberto.enterprise.domain.repository.UserRepository;

public class UploadUserAvatarUseCase {

    private final UserRepository userRepository;
    private final FileStorage fileStorage;
    private final EventPublisher eventPublisher;
    private final CacheService cacheService;

    public UploadUserAvatarUseCase(
            UserRepository userRepository,
            FileStorage fileStorage,
            EventPublisher eventPublisher,
            CacheService cacheService
    ) {
        this.userRepository = userRepository;
        this.fileStorage = fileStorage;
        this.eventPublisher = eventPublisher;
        this.cacheService = cacheService;
    }

    public AvatarResponse execute(Long userId, byte[] bytes, String originalFileName, String contentType) {
        // 1) Validar que exista
        userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found: " + userId));

        // 2) Subir archivo
        String safeName = "user-" + userId + "-" + System.currentTimeMillis() + "-" + originalFileName;
        String avatarUrl = fileStorage.upload(bytes, safeName, contentType);

        // 3) Guardar referencia en BD
        userRepository.updateAvatar(userId, avatarUrl);

        // 4) Publicar evento + cache
        eventPublisher.publish("user.avatar.updated", userId);
        cacheService.put("user:" + userId + ":avatar", avatarUrl, 300);

        return new AvatarResponse(userId, avatarUrl);
    }

    public record AvatarResponse(Long userId, String avatarUrl) {}
}
