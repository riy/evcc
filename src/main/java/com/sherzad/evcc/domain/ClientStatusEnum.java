package com.sherzad.evcc.domain;

import java.util.Arrays;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ClientStatusEnum {

    // For best practice purposes I'm adding an identifier to every Enum. This is in case an Enum needs to be identified in the
    // database, message properties or client-side code, without having to couple it to the Java enumeration name.

    ACTIVE("active"),
    INACTIVE("inactive");

    public String identifier;

    public static ClientStatusEnum byIdentifier(final String identifier) {
        return Arrays.stream(ClientStatusEnum.values())
                .filter(clientStatusEnum -> clientStatusEnum.getIdentifier().equals(identifier))
                .findFirst()
                .orElse(null);
    }
}
