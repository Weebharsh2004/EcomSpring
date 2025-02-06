package com.Ecommerce.Ecommerce.dataTransfer.auth;

import lombok.Data;

@Data
public class AuthRequestDTO {
    private String email;
    private String password;
}
