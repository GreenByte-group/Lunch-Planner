package group.greenbyte.lunchplanner.user;

import io.jsonwebtoken.lang.Assert;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.validation.constraints.AssertTrue;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import static org.junit.Assert.*;


public class SecurityHelperTest {

    @Test
    public void hashPassword() throws Exception {
        String password = "1234";
        String hash = SecurityHelper.hashPassword(password);
        Assert.notNull(hash);
    }

    @Test
    public void validatePassword() throws Exception {
        String password = "1234";
        String hash = SecurityHelper.hashPassword(password);

        Assert.isTrue(SecurityHelper.validatePassword(password, hash));
        Assert.isTrue(!SecurityHelper.validatePassword(null, hash));
        Assert.isTrue(!SecurityHelper.validatePassword(password, null));
        Assert.isTrue(!SecurityHelper.validatePassword(password + "a", hash ));
    }
}