package ru.rakhmanov.myshop.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RequestHeaderUtil {

    private static final Long DEFAULT_CLIENT_ID = 999_999L;

    public static Long getClientId() {
        return DEFAULT_CLIENT_ID;
    }
}
