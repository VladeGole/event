package com.example.event.core.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${secret}")
    private String myValues ;


    public String getUsername(String token){
        return extractClaims(token, Claims::getSubject);
    }

    public Date expired(String token){
        return extractClaims(token, Claims::getExpiration);
    }


    public String generateToken(UserDetails userDetails){
        return generateToken(new HashMap<>(),userDetails);
    }


    public String generateToken(Map<String,Object> extraClaims, UserDetails userDetails){
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date((System.currentTimeMillis()) + 1000 * 60 * 60))
                .signWith(getSignIn())
                .compact();
    }


    public <T> T extractClaims(String token, Function<Claims,T>resolver){
        Claims claims=extractAllClaims(token);
        return resolver.apply(claims);
    }

    public Claims extractAllClaims(String token){
        return Jwts.parserBuilder()
                .setSigningKey(getSignIn())
                .build()
                .parseClaimsJws(token)
                .getBody();

    }

    private Key getSignIn() {
        byte[] key= Decoders.BASE64.decode(myValues);
        return Keys.hmacShaKeyFor(key);
    }

    public boolean isExpired(String token){
        return expired(token).before(new Date(System.currentTimeMillis()));
    }

    public boolean isTokenValid(String token, UserDetails userDetails){
        String username=extractClaims(token,Claims::getSubject);
        return (!isExpired(token) && userDetails.getUsername().equals(username));
    }



}
