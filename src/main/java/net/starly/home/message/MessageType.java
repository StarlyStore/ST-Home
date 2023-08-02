package net.starly.home.message;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum MessageType {

    ERROR("errorMessages"),
    NORMAL("messages"),
    DATA("data");

    private final String key;
}