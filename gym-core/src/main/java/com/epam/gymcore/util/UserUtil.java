package com.epam.gymcore.util;

import com.epam.gymcore.domain.model.User;

import java.util.List;
import java.util.Random;

public class UserUtil {
    public static String createUsername(User user, List<? extends User> allUsers){
        String firstName = user.getFirstName();
        String lastName = user.getLastName();
        StringBuilder username = new StringBuilder();
        username.append(firstName);
        username.append(".");
        username.append(lastName);
        String baseUsername = user.getFirstName() + "." + user.getLastName();

        if (allUsers == null || allUsers.isEmpty()) {
            return baseUsername;
        }

        long count = allUsers.stream()
                .filter(u -> u.getUsername() != null)
                .filter(u -> u.getUsername().equalsIgnoreCase(baseUsername)
                        || u.getUsername().toLowerCase().startsWith(baseUsername.toLowerCase() + "#"))
                .count();

        if (count == 0) {
            return baseUsername;
        } else {
            return baseUsername + "#" + (count + 1);
        }
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
