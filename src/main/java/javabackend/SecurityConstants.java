/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javabackend;

/**
 *
 * @author katri
 */
public class SecurityConstants {
       
    public static final String SECRET = "${spring.profiles.active.equals(\"production\")?System.getenv(\"JWT_SECRET\"):${SECRET}}";
    public static final long EXPIRATION_TIME = 864_000_000; // 10 days
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String SIGN_UP_URL = "/sign-up";
}
