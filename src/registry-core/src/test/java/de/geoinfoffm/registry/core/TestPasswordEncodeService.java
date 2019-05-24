package de.geoinfoffm.registry.core;

import org.junit.Assert;
import org.junit.Test;

public class TestPasswordEncodeService {

    @Test
    public void shouldMatchEncodedPassword() {
        String password = "a";
        String hash = "$2a$12$w8AoYGP2UxTsnrajt0UJ6eExjm1AD/IG2RD87wx5qoXkKXiCEgYT6";

        PasswordEncodingServiceImpl encoder = new PasswordEncodingServiceImpl();
        Assert.assertTrue(encoder.matches(password, hash));
    }
}
