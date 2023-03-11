package ru.netology.tests;

import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import ru.netology.data.DataHelper;
import ru.netology.pages.DashboardPage;
import ru.netology.pages.LoginPage;
import ru.netology.pages.TransferPage;
import ru.netology.pages.VerificationPage;

public class Tests {
    private DashboardPage dashboardPage;

    DataHelper.Cards cardNumbers = DataHelper.getCardNumbers();
    DataHelper.AuthInfo authInfo = DataHelper.getAuthInfo();

    DataHelper.VerificationCode verifyCode = DataHelper.getVerificationCode();

    String loginPageHeader = "Мы гарантируем безопасность ваших данных";
    String verificationPageHeader = "Необходимо подтверждение";
    String dashboardPageHeader = "Ваши карты";
    String transferPageHeader = "Пополнение карты";


    @BeforeEach
    void setUp() {
        open("http://localhost:9999");
        clearBrowserCookies();
        clearBrowserLocalStorage();

        LoginPage loginPage = new LoginPage(loginPageHeader);
        VerificationPage verificationPage = loginPage.validLogin(verificationPageHeader, authInfo.getLogin(), authInfo.getPassword());
        dashboardPage = verificationPage.validVerify(dashboardPageHeader, verifyCode.getVerificationCode());
    }

    @AfterEach
    void closeUp() {
        Configuration.holdBrowserOpen = false;
    }


    @Test
    void shouldTransferMoneyFromFirstToSecondCard() {
        int balance1stCardStart = dashboardPage.getCardBalance(cardNumbers.getFirstCardNumber());
        int balance2ndCardStart = dashboardPage.getCardBalance(cardNumbers.getSecondCardNumber());
        int amount = DataHelper.getAmount(balance1stCardStart);
        TransferPage transferPage = dashboardPage.depositToCard2();
        transferPage.checkHeading(transferPageHeader);
        transferPage.transferMoney(amount, cardNumbers.getFirstCardNumber(), cardNumbers.getSecondCardNumber());
        dashboardPage = new DashboardPage(dashboardPageHeader);
        int balance1stCardFact = dashboardPage.getCardBalance(cardNumbers.getFirstCardNumber());
        int balance2ndCardFact = dashboardPage.getCardBalance(cardNumbers.getSecondCardNumber());
        int balance1stCardExp = balance1stCardStart - amount;
        int balance2ndCardExp = balance2ndCardStart + amount;
        assertEquals(balance1stCardFact, balance1stCardExp);
        assertEquals(balance2ndCardFact, balance2ndCardExp);
    }

    @Test
    void shouldTransferMoneyFromSecondToFirstCard() {
        int balance1stCardStart = dashboardPage.getCardBalance(cardNumbers.getFirstCardNumber());
        int balance2ndCardStart = dashboardPage.getCardBalance(cardNumbers.getSecondCardNumber());
        int amount = DataHelper.getAmount(balance2ndCardStart);
        TransferPage transferPage = dashboardPage.depositToCard1();
        transferPage.checkHeading(transferPageHeader);
        transferPage.transferMoney(amount, cardNumbers.getSecondCardNumber(), cardNumbers.getFirstCardNumber());
        dashboardPage = new DashboardPage(dashboardPageHeader);
        int balance1stCardFact = dashboardPage.getCardBalance(cardNumbers.getFirstCardNumber());
        int balance2ndCardFact = dashboardPage.getCardBalance(cardNumbers.getSecondCardNumber());
        int balance1stCardExp = balance1stCardStart + amount;
        int balance2ndCardExp = balance2ndCardStart - amount;
        assertEquals(balance1stCardFact, balance1stCardExp);
        assertEquals(balance2ndCardFact, balance2ndCardExp);
    }

    @Test
    void shouldNotTransferMoneyFromFirstToFirstCard() {
        int balance1stCardStart = dashboardPage.getCardBalance(cardNumbers.getFirstCardNumber());
        int amount = DataHelper.getAmount(balance1stCardStart);
        TransferPage transferPage = dashboardPage.depositToCard1();
        transferPage.checkHeading(transferPageHeader);
        transferPage.transferMoney(amount, cardNumbers.getFirstCardNumber(), cardNumbers.getFirstCardNumber());
        transferPage.checkError();
    }

    @Test
    void shouldNotTransferMoneyFromSecondToSecondCard() {
        int balance2ndCardStart = dashboardPage.getCardBalance(cardNumbers.getSecondCardNumber());
        int amount = DataHelper.getAmount(balance2ndCardStart);
        TransferPage transferPage = dashboardPage.depositToCard2();
        transferPage.checkHeading(transferPageHeader);
        transferPage.transferMoney(amount, cardNumbers.getSecondCardNumber(), cardNumbers.getSecondCardNumber());
        transferPage.checkError();
    }

    @Test
    void shouldNotTransferMoneyFromNonExistToSecondCard() {
        int amount = DataHelper.getAmount();
        TransferPage transferPage = dashboardPage.depositToCard2();
        transferPage.checkHeading(transferPageHeader);
        transferPage.transferMoney(amount, cardNumbers.getNonExistCardNumber(), cardNumbers.getSecondCardNumber());
        transferPage.checkError();
    }

    @Test
    void shouldNotTransferMoneyFromNonExistToFirstCard() {
        int amount = DataHelper.getAmount();
        TransferPage transferPage = dashboardPage.depositToCard1();
        transferPage.checkHeading(transferPageHeader);
        transferPage.transferMoney(amount, cardNumbers.getNonExistCardNumber(), cardNumbers.getFirstCardNumber());
        transferPage.checkError();
    }

    @Test
    void shouldTransferAllMoneyFromFirstToSecondCard() {
        int balance1stCardStart = dashboardPage.getCardBalance(cardNumbers.getFirstCardNumber());
        int balance2ndCardStart = dashboardPage.getCardBalance(cardNumbers.getSecondCardNumber());
        TransferPage transferPage = dashboardPage.depositToCard2();
        transferPage.checkHeading(transferPageHeader);
        transferPage.transferMoney(balance1stCardStart, cardNumbers.getFirstCardNumber(), cardNumbers.getSecondCardNumber());
        dashboardPage = new DashboardPage(dashboardPageHeader);
        int balance1stCardFact = dashboardPage.getCardBalance(cardNumbers.getFirstCardNumber());
        int balance2ndCardFact = dashboardPage.getCardBalance(cardNumbers.getSecondCardNumber());
        int balance1stCardExp = 0;
        int balance2ndCardExp = balance2ndCardStart + balance1stCardStart;
        assertEquals(balance1stCardFact, balance1stCardExp);
        assertEquals(balance2ndCardFact, balance2ndCardExp);
    }

    @Test
    void shouldTransferAllMoneyFromSecondToFirstCard() {
        int balance1stCardStart = dashboardPage.getCardBalance(cardNumbers.getFirstCardNumber());
        int balance2ndCardStart = dashboardPage.getCardBalance(cardNumbers.getSecondCardNumber());
        TransferPage transferPage = dashboardPage.depositToCard1();
        transferPage.checkHeading(transferPageHeader);
        transferPage.transferMoney(balance2ndCardStart, cardNumbers.getSecondCardNumber(), cardNumbers.getFirstCardNumber());
        dashboardPage = new DashboardPage(dashboardPageHeader);
        int balance1stCardFact = dashboardPage.getCardBalance(cardNumbers.getFirstCardNumber());
        int balance2ndCardFact = dashboardPage.getCardBalance(cardNumbers.getSecondCardNumber());
        int balance1stCardExp = balance1stCardStart + balance2ndCardStart;
        int balance2ndCardExp = 0;
        assertEquals(balance1stCardFact, balance1stCardExp);
        assertEquals(balance2ndCardFact, balance2ndCardExp);
    }

    @Test
    void shouldNotTransferMoreThanAmountFromFirstToSecondCard() {
        int balance1stCardStart = dashboardPage.getCardBalance(cardNumbers.getFirstCardNumber());
        int amount = balance1stCardStart + 1;
        TransferPage transferPage = dashboardPage.depositToCard2();
        transferPage.checkHeading(transferPageHeader);
        transferPage.transferMoney(amount, cardNumbers.getFirstCardNumber(), cardNumbers.getSecondCardNumber());
        transferPage.checkError();
    }

    @Test
    void shouldNotTransferMoreThanAmountFromSecondToFirstCard() {
        int balance2ndCardStart = dashboardPage.getCardBalance(cardNumbers.getSecondCardNumber());
        int amount = balance2ndCardStart + 1;
        TransferPage transferPage = dashboardPage.depositToCard1();
        transferPage.checkHeading(transferPageHeader);
        transferPage.transferMoney(amount, cardNumbers.getSecondCardNumber(), cardNumbers.getFirstCardNumber());
        transferPage.checkError();
    }

    @Test
    void shouldChangeNegativeAmountFromFirstToSecondCard() {
        int balance1stCardStart = dashboardPage.getCardBalance(cardNumbers.getFirstCardNumber());
        int balance2ndCardStart = dashboardPage.getCardBalance(cardNumbers.getSecondCardNumber());
        int amount = -DataHelper.getAmount(balance1stCardStart);
        TransferPage transferPage = dashboardPage.depositToCard2();
        transferPage.checkHeading(transferPageHeader);
        transferPage.transferMoney(amount, cardNumbers.getFirstCardNumber(), cardNumbers.getSecondCardNumber());
        dashboardPage = new DashboardPage(dashboardPageHeader);
        int balance1stCardFact = dashboardPage.getCardBalance(cardNumbers.getFirstCardNumber());
        int balance2ndCardFact = dashboardPage.getCardBalance(cardNumbers.getSecondCardNumber());
        int balance1stCardExp = balance1stCardStart + amount;
        int balance2ndCardExp = balance2ndCardStart - amount;
        assertEquals(balance1stCardFact, balance1stCardExp);
        assertEquals(balance2ndCardFact, balance2ndCardExp);
    }

    @Test
    void shouldChangeNegativeAmountFromSecondToFirstCard() {
        int balance1stCardStart = dashboardPage.getCardBalance(cardNumbers.getFirstCardNumber());
        int balance2ndCardStart = dashboardPage.getCardBalance(cardNumbers.getSecondCardNumber());
        int amount = -DataHelper.getAmount(balance2ndCardStart);
        TransferPage transferPage = dashboardPage.depositToCard1();
        transferPage.checkHeading(transferPageHeader);
        transferPage.transferMoney(amount, cardNumbers.getSecondCardNumber(), cardNumbers.getFirstCardNumber());
        dashboardPage = new DashboardPage(dashboardPageHeader);
        int balance1stCardFact = dashboardPage.getCardBalance(cardNumbers.getFirstCardNumber());
        int balance2ndCardFact = dashboardPage.getCardBalance(cardNumbers.getSecondCardNumber());
        int balance1stCardExp = balance1stCardStart - amount;
        int balance2ndCardExp = balance2ndCardStart + amount;
        assertEquals(balance1stCardFact, balance1stCardExp);
        assertEquals(balance2ndCardFact, balance2ndCardExp);
    }

    @Test
    void shouldNotTransferZeroFromFirstToSecondCard() {
        int amount = 0;
        TransferPage transferPage = dashboardPage.depositToCard2();
        transferPage.checkHeading(transferPageHeader);
        transferPage.transferMoney(amount, cardNumbers.getFirstCardNumber(), cardNumbers.getSecondCardNumber());
        transferPage.checkError();
    }

    @Test
    void shouldNotTransferZeroFromSecondToFirstCard() {
        int amount = 0;
        TransferPage transferPage = dashboardPage.depositToCard1();
        transferPage.checkHeading(transferPageHeader);
        transferPage.transferMoney(amount, cardNumbers.getSecondCardNumber(), cardNumbers.getFirstCardNumber());
        transferPage.checkError();
    }

    @Test
    void shouldNotTransferEmptyAmountFromFirstToSecondCard() {
        TransferPage transferPage = dashboardPage.depositToCard2();
        transferPage.checkHeading(transferPageHeader);
        transferPage.transferMoney(cardNumbers.getFirstCardNumber(), cardNumbers.getSecondCardNumber());
        transferPage.checkError();
    }

    @Test
    void shouldNotTransferEmptyAmountFromSecondToFirstCard() {
        TransferPage transferPage = dashboardPage.depositToCard1();
        transferPage.checkHeading(transferPageHeader);
        transferPage.transferMoney(cardNumbers.getSecondCardNumber(), cardNumbers.getFirstCardNumber());
        transferPage.checkError();
    }

    @Test
    void shouldCancelTransferFromFirstToSecondCard() {
        int balance1stCardStart = dashboardPage.getCardBalance(cardNumbers.getFirstCardNumber());
        int balance2ndCardStart = dashboardPage.getCardBalance(cardNumbers.getSecondCardNumber());
        int amount = DataHelper.getAmount(balance1stCardStart);
        TransferPage transferPage = dashboardPage.depositToCard2();
        transferPage.checkHeading(transferPageHeader);
        transferPage.cancelTransfer(amount, cardNumbers.getFirstCardNumber(), cardNumbers.getSecondCardNumber());
        dashboardPage = new DashboardPage(dashboardPageHeader);
        int balance1stCardFact = dashboardPage.getCardBalance(cardNumbers.getFirstCardNumber());
        int balance2ndCardFact = dashboardPage.getCardBalance(cardNumbers.getSecondCardNumber());
        int balance1stCardExp = balance1stCardStart;
        int balance2ndCardExp = balance2ndCardStart;
        assertEquals(balance1stCardFact, balance1stCardExp);
        assertEquals(balance2ndCardFact, balance2ndCardExp);
    }

    @Test
    void shouldCancelTransferFromSecondToFirstCard() {
        int balance1stCardStart = dashboardPage.getCardBalance(cardNumbers.getFirstCardNumber());
        int balance2ndCardStart = dashboardPage.getCardBalance(cardNumbers.getSecondCardNumber());
        int amount = DataHelper.getAmount(balance2ndCardStart);
        TransferPage transferPage = dashboardPage.depositToCard1();
        transferPage.checkHeading(transferPageHeader);
        transferPage.cancelTransfer(amount, cardNumbers.getSecondCardNumber(), cardNumbers.getFirstCardNumber());
        dashboardPage = new DashboardPage(dashboardPageHeader);
        int balance1stCardFact = dashboardPage.getCardBalance(cardNumbers.getFirstCardNumber());
        int balance2ndCardFact = dashboardPage.getCardBalance(cardNumbers.getSecondCardNumber());
        int balance1stCardExp = balance1stCardStart;
        int balance2ndCardExp = balance2ndCardStart;
        assertEquals(balance1stCardFact, balance1stCardExp);
        assertEquals(balance2ndCardFact, balance2ndCardExp);
    }
}