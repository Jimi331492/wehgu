package com.wehgu.admin.utils;

import com.wehgu.admin.config.Constants;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.util.encoders.Hex;

import java.security.MessageDigest;

/**
 * <p> 加密工具 </p>
 *
 */
@Slf4j
public class PasswordUtil {

    //初始密码
    public static final String DEFINES_PASSWORD = "7c4a8d09ca3762af61e59520943dc26494f8941b";
    private char cr;

    /**
     * 校验密码是否一致
     *
     * @param password:                    前端传过来的密码
     * @param hashedPassword：数据库中储存加密过后的密码
     * @return
     */
    public static boolean isValidPassword(String password, String hashedPassword) {
        String s = encodePassword(password);
        return hashedPassword.equalsIgnoreCase(encodePassword(password));
    }

    /**
     * 通过SHA1对密码进行编码
     *
     * @param password：密码
     * @return
     */
    public static String encodePassword(String password) {
        String encodedPassword;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            byte[] hashed = digest.digest(password.getBytes());
            int iterations = Constants.HASH_ITERATIONS-1;
            for (int i = 0; i < iterations; ++i) {
                digest.reset();
                hashed = digest.digest(hashed);
            }
            encodedPassword = new String(Hex.encode(hashed));
        } catch (Exception e) {
            log.error("验证密码异常:", e);
            return null;
        }
        return encodedPassword;
    }

    public static boolean isLetterDigit(String str) {
        boolean isDigit = false;//定义一个boolean值，用来表示是否包含数字
        boolean isLetter = false;//定义一个boolean值，用来表示是否包含字母
        for (int i = 0; i < str.length(); i++) {
            if (Character.isDigit(str.charAt(i))) {   //用char包装类中的判断数字的方法判断每一个字符
                isDigit = true;
            } else if (Character.isLetter(str.charAt(i))) {  //用char包装类中的判断字母的方法判断每一个字符
                isLetter = true;
            }
        }
        String regex = "^[a-zA-Z0-9]{8,20}$";
        return isDigit && isLetter && str.matches(regex);
    }
    public static boolean isPhone(String str) {
        boolean isDigit = false;//定义一个boolean值，用来表示是否包含数字
        boolean isLetter = false;//定义一个boolean值，用来表示是否包含字母
        boolean isIntBey = false;//定义一个boolean值，用来表示是否包含字母
        for (int i = 0; i < str.length(); i++) {
            if (Character.isDigit(str.charAt(i)))
            {   //用char包装类中的判断数字的方法判断每一个字符
                if (i==1)
                {
                    char cr=str.charAt(i);
                    if (cr !=1)
                    {
                        return false;
                    }
                }
                isDigit = true;
            } else if (Character.isLetter(str.charAt(i))) {  //用char包装类中的判断字母的方法判断每一个字符
                isLetter = true;
            }
        }
        String regex = "^[a-zA-Z0-9]{8,20}$";
        return isDigit && isLetter && str.matches(regex);
    }



}