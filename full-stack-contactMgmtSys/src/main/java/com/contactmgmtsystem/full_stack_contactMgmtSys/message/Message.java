package com.contactmgmtsystem.full_stack_contactMgmtSys.message;


import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Message {

    private String content;
//    Default message
    @Builder.Default
    private MessageType type=MessageType.blue;
}
