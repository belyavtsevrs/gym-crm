package com.epam.gymcore.util;

import com.epam.gymcore.domain.entity.User;

import java.util.List;
import java.util.Random;
import java.util.function.Function;

public class UserUtil {
    public static String createUsername(User user, Function<String, Boolean> isUsernameTaken) {
        String base = user.getFirstName().toLowerCase() + "." + user.getLastName().toLowerCase();
        String candidate = base;
        int suffix = 1;

        while (isUsernameTaken.apply(candidate)) {
            candidate = base + "#" + suffix;
            suffix++;
        }

        return candidate;
    }

    public static String generatePassword(){
        StringBuilder password = new StringBuilder();
        char[] data = "qwertyuiop".toCharArray();
        for(int i = 0; i < 10;i++){
            password.append(data[new Random().nextInt(data.length)]);
        }
        return new String(password);
    }
}
