package com.arranger.eurekaclient.exception;

import com.arranger.eurekaclient.entity.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResourcesExhaustedException extends IllegalStateException{
    private static final String RESOURCES_EXHAUSTED = "You have reached max process number 6";

    public ResourcesExhaustedException(String message) {
        super(message.isEmpty() ? RESOURCES_EXHAUSTED : message);
    }

    public ResourcesExhaustedException() {
        super(RESOURCES_EXHAUSTED);
    }
}
