package com.example.BookRental.helper;

import com.example.BookRental.config.CustomUserDetail;
import com.example.BookRental.config.CustomUserDetailService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class JwtHelper {
    private CustomUserDetailService customUserDetailService;
//    SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    String secretKey = "[BA63f6bed1BA63f6bed1BA63f6bed1BA63f6bed1BA63f6bed1";

    public String generateToken(CustomUserDetail principal) {

        Map<String , Object> claims = new HashMap<>();

        this.setCustomClaims(claims , principal);

        return this.doGenerateToken(claims, principal.getUsername());
    }

    private void setCustomClaims(Map<String, Object> claims, CustomUserDetail principal) {
        List<String> authorities = new ArrayList<>();
        for(GrantedAuthority auth : principal.getAuthorities()) {
            authorities.add(auth.getAuthority());
        }
        claims.put("authorities", authorities);

    }
    private String doGenerateToken(Map<String, Object> claims, String subject) {

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+(5*60*60*1000)))
                .signWith(SignatureAlgorithm.HS256,secretKey)
                .compact();

    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(this.secretKey).parseClaimsJws(token).getBody();
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        Claims claims = this.getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);

    }

    public List<GrantedAuthority> getAuthoritiesClaimFromToken(String token){
        Claims claims = this.getAllClaimsFromToken(token);
        List<GrantedAuthority> returnValue = null;
        List<String>  authorities = (List)claims.get("authorities");

        if(authorities == null) return returnValue;

        returnValue = new ArrayList<>();

        return authorities.stream().map(SimpleGrantedAuthority :: new) .collect(Collectors.toList());

    }

    public String getUserNameFromToken(String token) {
        return this.getClaimFromToken(token, Claims::getSubject);
    }

    public Date getExpirationDateFromToken(String token) {
        return this.getClaimFromToken(token, Claims::getExpiration);
    }

    private Boolean isTokenExpired(String token) {

        Date expiration = this.getExpirationDateFromToken(token);

        return expiration.before(new Date());
    }

    public Boolean validatToken(String token, CustomUserDetail principal) {
        String userName = this.getUserNameFromToken(token);
        return StringUtils.hasText(userName) && userName.equals(principal.getUsername()) && !this.isTokenExpired(token);

    }
    public Boolean validateToken(String token, String principal) {
        String userName = this.getUserNameFromToken(token);
        return StringUtils.hasText(userName) && userName.equals(principal) && !this.isTokenExpired(token);

    }


    public Authentication getAthentication(String userName, List<GrantedAuthority> authorities, HttpServletRequest request) {

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userName, null, authorities);

        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        return authenticationToken;
    }

}


















