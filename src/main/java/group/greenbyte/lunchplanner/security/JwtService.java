package group.greenbyte.lunchplanner.security;

import io.jsonwebtoken.*;
import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.Random;

@Component
public class JwtService {

    static final Logger LOGGER = LoggerFactory.getLogger(JwtService.class);

    private final int secretLength = 10;
    private static String secret = "adsfanaekhnKJdnaj";

    public JwtService() {
//        if(secret == null) {
//            StringBuilder temp = new StringBuilder();
//            Random r = new Random();
//            for (int i = 0; i < secretLength; i++) {
//                temp.append((char) (r.nextInt(26) + 'a'));
//            }
//            secret = temp.toString();
//        }
    }

    public String createToken(String username, Date expireAt) {
       if(StringUtils.hasText(username) && StringUtils.hasText(secret) && expireAt != null && expireAt.after(new Date()) ) {
           String secret2 = new String(Base64.encodeBase64(secret.getBytes()));
           String compactJws = Jwts.builder()
                    .setSubject(username)
                    .signWith(SignatureAlgorithm.HS512, secret2)
                    //.setExpiration(expireAt)
                    .compact();
            return compactJws;
        }
       return null;
    }

    public boolean isValid(String token) {
        if(StringUtils.hasText(token) && StringUtils.hasText(secret)) {
            try {
                String secret2 = new String(Base64.encodeBase64(secret.getBytes()));
                Jwts.parser().setSigningKey(secret2).parseClaimsJws(token);
                return true;
            } catch (JwtException e) {
                LOGGER.error(e.getMessage());
            }
        }
        return false;
    }

    public String getUsername(String token) {
        if(StringUtils.hasText(token) && StringUtils.hasText(secret)) {
            try {
                String secret2 = new String(Base64.encodeBase64(secret.getBytes()));
                JwtParser parser = Jwts.parser().setSigningKey(secret2);
                Jws jws = parser.parseClaimsJws(token);
                //jws.getBody().getSubject();
                return Jwts.parser().setSigningKey(secret2).parseClaimsJws(token).getBody().getSubject();
            }  catch (JwtException e) {
                LOGGER.error(e.getMessage());
            }
        }
        return null;
    }


}
