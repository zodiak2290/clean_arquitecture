package com.alberto.enterprise.interfaces.rest;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.alberto.enterprise.application.usecase.CreateUserUseCase;
import com.alberto.enterprise.application.usecase.GetUserUseCase;
import com.alberto.enterprise.application.usecase.UploadUserAvatarUseCase;
import com.alberto.enterprise.domain.model.User;

@RestController
@RequestMapping("/users")
public class UserController {
    private final CreateUserUseCase createUserUseCase;
    private final UploadUserAvatarUseCase uploadUserAvatarUseCase;
    private final GetUserUseCase getUserUseCase;
    
    public UserController(CreateUserUseCase createUserUseCase,
                          UploadUserAvatarUseCase uploadUserAvatarUseCase,
                          GetUserUseCase getUserUseCase) {
        this.createUserUseCase = createUserUseCase;
        this.uploadUserAvatarUseCase = uploadUserAvatarUseCase;
        this.getUserUseCase = getUserUseCase;
    }

    @PostMapping
    public User create(@RequestBody CreateUserRequest request) {
        return createUserUseCase.execute(request.name(), request.email());
    }

    @GetMapping("/{id}")
    public User get(@PathVariable Long id) {
        return getUserUseCase.execute(id)
                .orElseThrow(() -> new com.alberto.enterprise.application.exception.NotFoundException("User not found: " + id));
    }
    
    @PostMapping(value = "/{id}/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public UploadUserAvatarUseCase.AvatarResponse uploadAvatar(
            @PathVariable Long id,
            @RequestPart("file") MultipartFile file
    ) throws Exception {
        return uploadUserAvatarUseCase.execute(
                id,
                file.getBytes(),
                file.getOriginalFilename(),
                file.getContentType()
        );
    }

    record CreateUserRequest(String name, String email) {}
}
