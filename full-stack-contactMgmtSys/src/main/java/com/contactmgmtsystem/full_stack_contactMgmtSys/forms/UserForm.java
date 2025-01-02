package com.contactmgmtsystem.full_stack_contactMgmtSys.forms;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
// This userForm will only receive the data entered in registrationform
public class UserForm {
 
    // Its better to take the same entity that are in User-Component
    @NotBlank(message = "Username is required")
    @Size(min = 3, message = "Minimum 3 characters is required")
    private String name;

    @Email(message = "Invalid Email Address")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Atleast 8 characters is required" +
            "minimum 1 capital letter" +
            "minimum 1 number" +
            "minimum 1 small letter" +
            "minimum 1 special character")
    private String password;
    @NotBlank(message = "Write about yourself")
    private String about;
    @NotEmpty(message = "Phone Number is required for verification")
    @Size(min = 7, max = 14, message = "Invalid phone number")
    private String phoneNumber;



}
