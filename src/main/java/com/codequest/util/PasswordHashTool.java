package com.codequest.util;

import java.io.Console;
import java.security.GeneralSecurityException;
import java.util.Arrays;

public final class PasswordHashTool {
    private PasswordHashTool() {
    }

    public static void main(String[] args) throws GeneralSecurityException {
        Console console = System.console();
        if (console == null) {
            throw new IllegalStateException("Run this tool from a console so the password is not echoed.");
        }
        char[] password = console.readPassword("Admin password: ");
        if (password == null || password.length < 8) {
            throw new IllegalArgumentException("Use a password with at least 8 characters.");
        }
        try {
            console.printf("%s%n", PasswordUtil.hash(password));
        } finally {
            Arrays.fill(password, '\0');
        }
    }
}