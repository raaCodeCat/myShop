package ru.rakhmanov.myshop.utils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import jakarta.servlet.http.HttpServletRequest;

public class RequestHeaderUtil {

    private static final Long DEFAULT_CLIENT_ID = 999_999L;

    public static Long getClientId() {
        HttpServletRequest request = getCurrentRequest();

        if (StringUtils.isBlank(request.getHeader("X-Client-ID"))) {
            return DEFAULT_CLIENT_ID;
        } else {
            return Long.parseLong(request.getHeader("X-Client-ID"));
        }
    }

    private static HttpServletRequest getCurrentRequest() {
        ServletRequestAttributes attributes = (ServletRequestAttributes)
                RequestContextHolder.getRequestAttributes();

        if (attributes == null) {
            throw new IllegalStateException("Request context is not available");
        }

        return attributes.getRequest();
    }
}
