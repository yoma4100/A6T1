package ru.netology.data;

import lombok.Value;

public class DataHelper {

    @Value
    public static class AuthInfo {
        String login;
        String password;
    }

    public static AuthInfo getAuthInfo() {
        return new AuthInfo("vasya", "qwerty123");
    }

    @Value
    public static class VerificationCode {
        String verificationCode;
    }

    public static VerificationCode getVerificationCode() {
        return new VerificationCode("12345");
    }

    @Value
    public static class CardNumbers {
        String firstCardNumber;
        String secondCardNumber;
        String nonExistCardNumber;
    }

    public static CardNumbers getCardNumbers() {
        return new CardNumbers("5559 0000 0000 0001", "5559000000000002", "5559 0000 0000 0003");
    }

    public static int getCardBalance(String elementSelector) {
        String balanceStart = "баланс: ";
        String balanceFinish = " р.";
        String balanceValue = elementSelector;
        int start = balanceValue.indexOf(balanceStart);
        int finish = balanceValue.indexOf(balanceFinish);
        balanceValue = balanceValue.substring(start + balanceStart.length(), finish);
        return Integer.parseInt(balanceValue);
    }

    public static String checkToCard(String expectedCardNumber) {
        String digitsOnly = expectedCardNumber.replaceAll("\\D", "");
        String maskedPart = "*".repeat(digitsOnly.length() - 4).replaceAll("(.{4})", "$1 ");
        return String.format("%s %s", maskedPart, digitsOnly.substring(digitsOnly.length() - 4));
    }
}