package com.example.airbnb.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.airbnb.dto.response.FilterResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JWTVerifyFilter extends OncePerRequestFilter {
    private final ObjectMapper objectMapper;

    public JWTVerifyFilter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest req) throws ServletException {
        String uri = req.getRequestURI();
        return uri.startsWith("/account") || uri.startsWith("/validate") || req.getMethod().equals("OPTIONS");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse resp, FilterChain filterChain) throws ServletException, IOException {
        //PrintWriter out = resp.getWriter(); 이렇게 바깥에서 선언하면, 당연히 프런트로 아웃 보내는 줄 알고 filterChain.doFilter(req, resp); 할때 오류터짐
        String token = req.getHeader("Token");
        FilterResponse fr = new FilterResponse(false);

        if (token == null || token.isEmpty()) {
            fr.setMessage("Token is empty");
            resp.getWriter().println(objectMapper.writeValueAsString(fr));
            return;
        }
        DecodedJWT jwt;
        try {
            jwt = JWT.require(Algorithm.HMAC256("f891c04f488d6c66cd4a5e4d9c8c0615"))
                    .withIssuer("airbnb").build().verify(token);
        } catch (Exception e) {
            e.printStackTrace();
            fr.setMessage("Invalid Token");
            resp.getWriter().println(objectMapper.writeValueAsString(fr));
            return;
        }
        String sub = jwt.getSubject();
        req.setAttribute("tokenId", Integer.parseInt(sub));
        filterChain.doFilter(req, resp);
    }
}
