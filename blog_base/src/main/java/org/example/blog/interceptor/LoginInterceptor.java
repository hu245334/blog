package org.example.blog.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.blog.utils.JwtUtil;
import org.example.blog.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Map;

@Component
public class LoginInterceptor implements HandlerInterceptor {

    private final StringRedisTemplate stringRedisTemplate;

    public LoginInterceptor(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 获取请求头中的token
        String token = request.getHeader("Authorization");
        // 验证token
        try {
            // 从redis中获取token
            ValueOperations<String, String> valueOperations = stringRedisTemplate.opsForValue();
            String redisToken = valueOperations.get(token);
            if (redisToken == null) {
                throw new RuntimeException("token已过期");
            }

            Map<String, Object> claims = JwtUtil.parseToken(token);

            // 将用户信息存储到ThreadLocal中
            ThreadLocalUtil.set(claims);

            return true;
        } catch (Exception e) {
            // 失败了Http响应码为401
            response.setStatus(401);
            return false;
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 请求结束后, 清空ThreadLocal中的数据
        ThreadLocalUtil.remove();
    }
}
