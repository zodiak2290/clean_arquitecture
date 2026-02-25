package com.alberto.enterprise.application.usecase;

import java.util.Set;

import com.alberto.enterprise.application.exception.BadRequestException;
import com.alberto.enterprise.application.exception.NotFoundException;
import com.alberto.enterprise.application.exception.UnsupportedMediaTypeException;
import com.alberto.enterprise.application.port.out.CacheService;
import com.alberto.enterprise.application.port.out.EventPublisher;
import com.alberto.enterprise.application.port.out.FileStorage;
import com.alberto.enterprise.domain.repository.UserRepository;

public class UploadUserAvatarUseCase {

    private final UserRepository userRepository;
    private final FileStorage fileStorage;
    private final EventPublisher eventPublisher;
    private final CacheService cacheService;
    
    private static final long MAX_BYTES = 2L * 1024 * 1024; // 2MB
    private static final Set<String> ALLOWED_TYPES = Set.of("image/png", "image/jpeg");
    
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
        	.orElseThrow(() -> new NotFoundException("User not found: " + userId));


        // 2) validar archivo
        if (bytes == null || bytes.length == 0) {
            throw new BadRequestException("File is empty");
        }
        if (bytes.length > MAX_BYTES) {
            throw new BadRequestException("File too large. Max allowed is 2MB");
        }
        if (contentType == null || !ALLOWED_TYPES.contains(contentType)) {
            throw new UnsupportedMediaTypeException("Only PNG or JPEG allowed");
        }
        if (originalFileName == null || originalFileName.isBlank()) {
            throw new BadRequestException("File name is required");
        }        
        
        // 3) Subir archivo
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
