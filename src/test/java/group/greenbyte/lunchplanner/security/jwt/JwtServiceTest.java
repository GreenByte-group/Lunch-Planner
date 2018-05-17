package group.greenbyte.lunchplanner.security.jwt;

import group.greenbyte.lunchplanner.security.JwtService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.tomcat.util.codec.binary.Base64;
import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.*;

public class JwtServiceTest {

    public static final String USERNAME = "username";
    JwtService jwtService;

    Date generateValidDate() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_WEEK, 1);
        return c.getTime();
    }

    Date generateInValidDate() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_WEEK, -1);
        return c.getTime();
    }

    @Before
    public void before() {
        jwtService = new JwtService();
    }

    @Test
    public void testCreateTokenEmptyUsername() {
        assertNull(jwtService.createToken(null, generateValidDate()));
        assertNull(jwtService.createToken("", generateValidDate()));
        assertNull(jwtService.createToken("  ", generateValidDate()));
    }

    @Test
    public void testCreateTokenInvalidDate() {
        assertNull(jwtService.createToken(USERNAME, null));
        assertNull(jwtService.createToken(USERNAME, generateInValidDate()));
    }

    @Test
    public void testCreateTokenSuccess() {
        Date d = generateValidDate();
        String s = jwtService.createToken(USERNAME, d);
        String secret2 = new String(Base64.encodeBase64(JwtService.secret.getBytes()));
        String compactJws = Jwts.builder()
                .setSubject(USERNAME)
                .signWith(SignatureAlgorithm.HS512, secret2)
                //.setExpiration(d)
                .compact();
        assertEquals(compactJws, s);
    }

    @Test
    public void testIsValidTokenEmptyToken() {
        assertFalse(jwtService.isValid(null));
        assertFalse(jwtService.isValid(" "));
    }

    @Test
    public void testIsValidTokenExpiredDate() {
        Date d = generateInValidDate();
        String secret2 = new String(Base64.encodeBase64(JwtService.secret.getBytes()));
        String compactJws = Jwts.builder()
                .setSubject(USERNAME)
                .signWith(SignatureAlgorithm.HS512, secret2)
                .setExpiration(d)
                .compact();
        assertFalse(jwtService.isValid(compactJws));
    }

    @Test
    public void testIsValidTokenWrongSecret() {
        String wrongSecret = "wrongSecret";

        Date d = generateValidDate();
        String secret2 = new String(Base64.encodeBase64(wrongSecret.getBytes()));
        String compactJws = Jwts.builder()
                .setSubject(USERNAME)
                .signWith(SignatureAlgorithm.HS512, secret2)
                .setExpiration(d)
                .compact();
        assertFalse(jwtService.isValid(compactJws));
        assertFalse(jwtService.isValid(compactJws));
    }

    @Test
    public void testIsValidTokenSuccess() {
        Date d = generateValidDate();
        String secret2 = new String(Base64.encodeBase64(JwtService.secret.getBytes()));
        String compactJws = Jwts.builder()
                .setSubject(USERNAME)
                .signWith(SignatureAlgorithm.HS512, secret2)
                .setExpiration(d)
                .compact();
        assertTrue(jwtService.isValid(compactJws));
    }

    @Test
    public void testGetUsernameEmptyToken() {
        assertNull(jwtService.getUsername(null));
        assertNull(jwtService.getUsername(" "));
    }

    @Test
    public void testGetUsernameExpired() {
        Date d = generateInValidDate();
        String secret2 = new String(Base64.encodeBase64(JwtService.secret.getBytes()));
        String compactJws = Jwts.builder()
                .setSubject(USERNAME)
                .signWith(SignatureAlgorithm.HS512, secret2)
                .setExpiration(d)
                .compact();

        assertNull(jwtService.getUsername(compactJws));
    }

    @Test
    public void testGetUsernameNoSubject() {
        Date d = generateValidDate();
        String secret2 = new String(Base64.encodeBase64(JwtService.secret.getBytes()));
        String compactJws = Jwts.builder()
                .signWith(SignatureAlgorithm.HS512, secret2)
                .setExpiration(d)
                .compact();

        assertNull(jwtService.getUsername(compactJws));
    }

    @Test
    public void testGetUsernameSuccess() {
        Date d = generateValidDate();
        String secret2 = new String(Base64.encodeBase64(JwtService.secret.getBytes()));
        String compactJws = Jwts.builder()
                .setSubject(USERNAME)
                .signWith(SignatureAlgorithm.HS512, secret2)
                .setExpiration(d)
                .compact();
        assertEquals(USERNAME, jwtService.getUsername(compactJws));
    }

}
