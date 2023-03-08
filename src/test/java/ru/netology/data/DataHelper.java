package ru.netology.data;

import static com.codeborne.selenide.Selenide.$;

public class DataHelper {

    public static class AuthInfo {
        public String getLogin() {
            return login;
        }

        public String getPassword() {
            return password;
        }

        private String login;
        private String password;

        public AuthInfo(String login, String password) {
            this.login = login;
            this.password = password;
        }
    }

    public static AuthInfo getAuthInfo() {
        return new AuthInfo("vasya", "qwerty123");
    }

    public static class VerificationCode {
        public String getVerificationCode() {
            return verificationCode;
        }

        private String verificationCode;

        public VerificationCode(String verificationCode) {
            this.verificationCode = verificationCode;
        }
    }

    public static VerificationCode getVerificationCode() {
        return new VerificationCode("12345");
    }

    public static class CardNumbers {
        public String getFirstCardNumber() {
            return firstCardNumber;
        }

        public String getSecondCardNumber() {
            return secondCardNumber;
        }

        public String getNonExistCardNumber() {
            return nonExistCardNumber;
        }

        private String firstCardNumber;
        private String secondCardNumber;
        private String nonExistCardNumber;

        public CardNumbers(String firstCardNumber, String secondCardNumber, String nonExistCardNumber) {
            this.firstCardNumber = firstCardNumber;
            this.secondCardNumber = secondCardNumber;
            this.nonExistCardNumber = nonExistCardNumber;
        }
    }

    public static CardNumbers getCardNumbers() {
        return new CardNumbers("5559 0000 0000 0001", "5559000000000002", "5559 0000 0000 0003");
    }

    public static class Amount {
        public int getAmount() {
            return amount;
        }

        private int amount;

        public Amount(int amount) {
            this.amount = amount;
        }
    }

    public static Amount getAmount() {
        return new Amount(100);
    }

    public static int getTransferAmount() {
        return 100;
    }

    public static int getCardBalance(String elementSelector) {
        String balanceStart = "баланс: ";
        String balanceFinish = " р.";
        String balanceValue = $(elementSelector).getText();
        int start = balanceValue.indexOf(balanceStart);
        int finish = balanceValue.indexOf(balanceFinish);
        balanceValue = balanceValue.substring(start + balanceStart.length(), finish);
        return Integer.parseInt(balanceValue);
    }

    public static String checkToCard(String expectedCardNumber) {
        String digitsOnly = expectedCardNumber.replaceAll("\\D", "");
        StringBuilder sb = new StringBuilder(digitsOnly);
        for (int i = 4; i < sb.length(); i += 5) {
            sb.insert(i, ' ');
        }
        String maskedPart = "";
        for (int i = 0; i < digitsOnly.length() - 4; i++) {
            maskedPart += "*";
            if ((i + 1) % 4 == 0) {
                maskedPart += " ";
            }
        }
        return maskedPart + sb.substring(sb.length() - 4);
    }
}