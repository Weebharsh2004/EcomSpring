package com.Ecommerce.Ecommerce.dataTransfer.user;

import com.Ecommerce.Ecommerce.model.actors.Role;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    // Only present in responses and updates (not in create)
    @Null(groups = Create.class)       // ID must be null during creation
    @NotNull(groups = Update.class)    // ID must be provided during updates
    private Long id;

    @Email
    private String email;

    // Password is required only during registration (create)
    @NotBlank(groups = Create.class)
    @Null(groups = Update.class)       // Prevent password updates via this DTO (use a separate endpoint)
    private String password;

    private Role role;

    // Define validation groups
    public interface Create {}
    public interface Update {}
}