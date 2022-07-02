// package com.projectlms.projectlms.security;

// import org.springframework.security.core.Authentication;
// import org.springframework.stereotype.Component;
// import com.projectlms.projectlms.service.CustomUserDetails;
// import io.jsonwebtoken.Jwts;
// import io.jsonwebtoken.SignatureAlgorithm;
// import io.jsonwebtoken.security.Keys;
// import lombok.extern.slf4j.Slf4j;
// import java.security.Key;

// @Slf4j
// @Component
// public class JwtProvider {
//     private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);


//     public String generateToken(Authentication auth) {
//         //final User user = (User) authentication.getPrincipal();
//         CustomUserDetails userPrincipal = (CustomUserDetails) auth.getPrincipal();

//         //Date now = new Date(System.currentTimeMillis());

//         // Map<String, Object> claims = new HashMap<>();
//         // claims.put("id", userPrincipal.getId());
//         // claims.put("username", userPrincipal.getUsername());
//         //claims.put("email", userPrincipal.getUsername());

//         return Jwts.builder()
//             //.setId(userPrincipal.getId().toString())
//             .setSubject((userPrincipal.getUsername()))
//             //.setClaims(claims)
//             //.setIssuedAt(now)
//             .signWith(key)
//             .compact();
//     }

//     public boolean validateToken(String token) {
//         try {
//             Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
//             return true;
//         } catch (Exception e) {
//             log.error(e.getMessage(), e);
//             return false;
//         }
//         // } catch (SignatureException ex) {
//         //    log.error("Invalid Jwt Signature: {}", ex.getMessage());
//         // } catch (MalformedJwtException ex) {
//         //     log.error("Invalid Jwt Token: {}", ex.getMessage());
//         // } catch (ExpiredJwtException ex) {
//         //     log.error("Expired Jwt Token: {}", ex.getMessage());
//         // } catch (UnsupportedJwtException ex) {
//         //     log.error("Unsupported Jwt Token: {}", ex.getMessage());
//         // } catch (IllegalArgumentException ex) {
//         //     log.error("Jwt claim string is empty: {}", ex.getMessage());
//         //} 

//         //return false;
//     }

//     public String getUsername(String token) {
//         // Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
//         // return claims.get("username").toString();
//         return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getSubject();
//     }
// }
