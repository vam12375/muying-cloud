package com.muyingmall.gateway.filter;

import com.alibaba.fastjson2.JSON;
import com.muyingmall.common.dto.Result;
import com.muyingmall.common.security.jwt.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
@Component
public class AuthFilter implements GlobalFilter, Ordered {

    @Autowired
    private JwtUtils jwtUtils;

    private static final List<String> SKIP_URLS = List.of(
        "/api/user/register",
        "/api/user/login",
        "/api/products",
        "/api/categories",
        "/api/brands",
        "/api/search",
        "/api/payments/alipay/notify",
        "/api/payments/wechat/notify"
    );

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();
        
        log.debug("Gateway filter processing path: {}", path);
        
        // 跳过不需要认证的接口
        if (shouldSkipAuth(path)) {
            return chain.filter(exchange);
        }
        
        // 获取token
        String token = getToken(request);
        if (token == null) {
            return handleUnauthorized(exchange, "缺少认证令牌");
        }
        
        // 验证token
        try {
            // 使用新版JwtUtils的validateToken方法（单参数版本）
            if (!jwtUtils.validateToken(token)) {
                return handleUnauthorized(exchange, "认证令牌无效或已过期");
            }

            // 由于兼容类没有getClaimsFromToken方法，需要使用getUsernameFromToken等方法分别获取信息
            String username = jwtUtils.getUsernameFromToken(token);
            Integer userId = jwtUtils.getUserIdFromToken(token);
            String role = jwtUtils.getRoleFromToken(token);

            ServerHttpRequest newRequest = request.mutate()
                .header("X-User-Id", userId != null ? String.valueOf(userId) : "")
                .header("X-Username", username != null ? username : "")
                .header("X-User-Role", role != null ? role : "")
                .build();
            
            return chain.filter(exchange.mutate().request(newRequest).build());
            
        } catch (Exception e) {
            log.warn("Token validation failed: {}", e.getMessage());
            return handleUnauthorized(exchange, "认证令牌验证失败");
        }
    }

    private boolean shouldSkipAuth(String path) {
        return SKIP_URLS.stream().anyMatch(path::startsWith);
    }

    private String getToken(ServerHttpRequest request) {
        String authorization = request.getHeaders().getFirst("Authorization");
        if (authorization != null && authorization.startsWith("Bearer ")) {
            return authorization.substring(7);
        }
        return null;
    }

    private Mono<Void> handleUnauthorized(ServerWebExchange exchange, String message) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
        
        Result<Object> result = Result.error(401, message);
        String body = JSON.toJSONString(result);
        DataBuffer buffer = response.bufferFactory().wrap(body.getBytes(StandardCharsets.UTF_8));
        
        return response.writeWith(Mono.just(buffer));
    }

    @Override
    public int getOrder() {
        return -100;
    }
}