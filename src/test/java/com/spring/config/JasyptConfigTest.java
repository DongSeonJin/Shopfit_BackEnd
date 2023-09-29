package com.spring.config;

import org.assertj.core.api.Assertions;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JasyptConfigTest {

    @Test
    void jasypt(){
        String username = "암호화할 문자 입력";

        String encryptUsername = jasyptEncrypt(username);

        System.out.println("encryptUsername : " + encryptUsername);


    }

    private String jasyptEncrypt(String input) {
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setPassword("mySecretKey");
        return encryptor.encrypt(input);
    }

    private String jasyptDecryt(String input){
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setPassword("mySecretKey");
        return encryptor.decrypt(input);
    }

}