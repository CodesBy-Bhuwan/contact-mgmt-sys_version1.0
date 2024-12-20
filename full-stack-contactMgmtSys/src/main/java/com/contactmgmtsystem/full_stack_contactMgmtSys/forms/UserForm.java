package com.contactmgmtsystem.full_stack_contactMgmtSys.forms;

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
    
    private String name;
    private String email;
    private String password;
    private String about;
    private String phoneNumber;



}
