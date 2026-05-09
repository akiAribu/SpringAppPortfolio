package org.example.springappportfolio.controllers.api.user;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.example.springappportfolio.config.UserPrincipal;
import org.example.springappportfolio.dto.ChangePasswordRequest;
import org.example.springappportfolio.dto.UpdateUserRequest;
import org.example.springappportfolio.models.User;
import org.example.springappportfolio.repositories.UserRepository;
import org.example.springappportfolio.services.AuthService;
import org.example.springappportfolio.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserApiController {

    private final UserRepository userRepository;
    private final UserService userService;

    @PostMapping("/me/avatar")
    public ResponseEntity<String> uploadAvatar(
            @RequestParam("file")MultipartFile file,
            Authentication authentication
    ) {
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        userService.updateUserAvatar(principal.getId(), file);

        return ResponseEntity.ok("Avatar uploaded");
    }


    @GetMapping("/me/avatar")
    public ResponseEntity<byte[]> getMyAvatar(Authentication authentication) {

        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();

        User user = userRepository.findById(principal.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        return getAvatarResponse(user);
    }

    @GetMapping("/{userId}/avatar")
    public ResponseEntity<byte[]> getAvatar(@PathVariable Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return getAvatarResponse(user);
    }

    @PutMapping("/me")
    public ResponseEntity<String> updateUser(
        @RequestBody UpdateUserRequest request,
        Authentication authentication
    ) {
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        userService.updateUser(principal.getId(), request);

        return ResponseEntity.ok("Profile updated");
    }

    private ResponseEntity<byte[]> getAvatarResponse(User user) {
        byte[] image = user.getUserImage();
        String contentType = user.getImageType();

        if (image == null) {
            image = loadDefaultImage();
            contentType = "image/png";
        }

        return ResponseEntity.ok()
                .header("Content-Type", contentType)
                .body(image);
    }

    @PostMapping("/me/password")
    public ResponseEntity<String> changePassword(
            @RequestBody ChangePasswordRequest request,
            Authentication authentication
    ) {
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        userService.changePassword(principal.getId(), request);

        return ResponseEntity.ok("Password updated");
    }

    @DeleteMapping("/me")
    public ResponseEntity<String> deleteCurrentUser(Authentication authentication) {
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        userService.deleteUser(principal.getId());
        SecurityContextHolder.clearContext();

        return ResponseEntity.ok("User deleted");
    }

    private byte[] loadDefaultImage() {
        try (InputStream is = getClass()
                .getResourceAsStream("/static/images/default-avatar.png")) {
            return is.readAllBytes();
        } catch (Exception e) {
            throw new RuntimeException("Default image not found");
        }
    }

}
