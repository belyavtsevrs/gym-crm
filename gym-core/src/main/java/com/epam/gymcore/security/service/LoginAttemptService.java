package com.epam.gymcore.security.service;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
@Service
public class LoginAttemptService {
    private static final int MAX_ATTEMPTS = 3;
    private static final long BLOCK_DURATION_MS = 5_000;

    private final Map<String, LoginAttempt> attemptsMap = new HashMap<>();

    public void loginFailed(String username) {
        LoginAttempt attempt = attemptsMap.getOrDefault(username, new LoginAttempt());
        attempt.decrimentAttempts();
        attempt.setLastAttemptTime(System.currentTimeMillis());

        attemptsMap.put(username, attempt);
    }

    public void loginSucceeded(String username) {
        attemptsMap.remove(username);
    }

    public boolean isBlocked(String username) {
        LoginAttempt attempt = attemptsMap.get(username);
        if (attempt == null) {
            return false;
        }

        if (attempt.getAttempts() == 0) {
            long blockTime = attempt.getLastAttemptTime() + BLOCK_DURATION_MS;
            if (System.currentTimeMillis() < blockTime) {
                return true;
            } else {
                attemptsMap.remove(username);
                return false;
            }
        }

        return false;
    }

    @Getter
    @Setter
    public static class LoginAttempt {
        private int attempts = MAX_ATTEMPTS;
        private long lastAttemptTime = 0;

        public void decrimentAttempts() {
            this.attempts--;
        }
    }

    public Integer getAttempts(String username){
        return attemptsMap.get(username).attempts;
    }

}
