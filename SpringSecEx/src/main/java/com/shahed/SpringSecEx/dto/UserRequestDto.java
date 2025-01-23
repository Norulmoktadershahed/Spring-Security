package com.shahed.SpringSecEx.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestDto {
    @NotBlank(message = "username is required")
    @Size(max=50,message = "username can not exceed 50 characters")
    @Schema(example = "imam.hasan")
    private String username;

    @NotBlank(message = "password is required")
    @Size(min = 6,message = "password can not exceed 6 characters")
    @Schema(example = "123456")
    private String password;
}
