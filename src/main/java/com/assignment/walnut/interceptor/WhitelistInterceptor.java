package com.assignment.walnut.interceptor;

import com.assignment.walnut.config.WhitelistProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;

@Component
public class WhitelistInterceptor implements HandlerInterceptor {

    private final List<String> whitelist;

    public WhitelistInterceptor(WhitelistProperties properties) {
        this.whitelist = properties.getWhitelist();
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String clientIp = request.getRemoteAddr();

        if ("0:0:0:0:0:0:0:1".equals(clientIp)) {
            clientIp = "127.0.0.1";
        }

        if (!whitelist.contains(clientIp)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("Access denied: IP not allowed.");
            return false;
        }

        return true;
    }
}
