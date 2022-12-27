package test;

import com.codeborne.selenide.logevents.SelenideLogger;
import data.Data;
import data.SqlHelper;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import page.MainPage;
import page.PaymentCardForm;
import page.PaymentCreditForm;

import static com.codeborne.selenide.Selenide.open;

public class TestUIWithCreditPayment {

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @AfterAll
    static void tearDown() {
        SqlHelper.cleanDatabases();
    }


    @Test
    @DisplayName("TestCase #1 by credit")
    public void testCreditPaymentWithApprovedCard() {
        open("http://localhost:8080");

        MainPage mainPage = new MainPage();
        mainPage.selectPaymentByCredit();

        PaymentCreditForm paymentCreditForm = new PaymentCreditForm();
        paymentCreditForm.fillCardNumber(Data.getApprovedCardNumber());
        paymentCreditForm.fillMonth(Data.getValidMonth());
        paymentCreditForm.fillYear(Data.getValidYear());
        paymentCreditForm.fillCVC(Data.getValidCVV());
        paymentCreditForm.fillCardholder(Data.getValidCardholder());
        paymentCreditForm.clickButton();

        paymentCreditForm.shouldReceiveSuccessNotification();

        var status = SqlHelper.getCreditStatusById();
        Assertions.assertEquals("APPROVED", status);
    }

    @Test
    @DisplayName("TestCase #2 by credit")
    public void testCreditPaymentWithCurrentData() {
        open("http://localhost:8080");

        MainPage mainPage = new MainPage();
        mainPage.selectPaymentByCredit();

        PaymentCreditForm paymentCreditForm = new PaymentCreditForm();
        paymentCreditForm.fillCardNumber(Data.getApprovedCardNumber());
        paymentCreditForm.fillMonth(Data.getCurrentDate("MM"));
        paymentCreditForm.fillYear(Data.getCurrentDate("yy"));
        paymentCreditForm.fillCVC(Data.getValidCVV());
        paymentCreditForm.fillCardholder(Data.getValidCardholder());
        paymentCreditForm.clickButton();

        paymentCreditForm.shouldReceiveSuccessNotification();
    }

    @Test
    @DisplayName("TestCase #3 by credit")
    public void testCreditPaymentWithDeclinedCard() {
        open("http://localhost:8080");

        MainPage mainPage = new MainPage();
        mainPage.selectPaymentByCredit();

        PaymentCreditForm paymentCreditForm = new PaymentCreditForm();
        paymentCreditForm.fillCardNumber(Data.getDeclinedCardNumber());
        paymentCreditForm.fillMonth(Data.getValidMonth());
        paymentCreditForm.fillYear(Data.getValidYear());
        paymentCreditForm.fillCVC(Data.getValidCVV());
        paymentCreditForm.fillCardholder(Data.getValidCardholder());
        paymentCreditForm.clickButton();

        paymentCreditForm.shouldReceiveErrorNotification();

        var status = SqlHelper.getCreditStatusById();
        Assertions.assertEquals("DECLINED", status);
    }

    @Test
    @DisplayName("TestCase #4 by credit")
    public void testCreditPaymentWithOneDigitInCardField() {
        open("http://localhost:8080");

        MainPage mainPage = new MainPage();
        mainPage.selectPaymentByCredit();

        PaymentCardForm paymentCardForm = new PaymentCardForm();
        paymentCardForm.fillCardNumber(Data.getRandomDigit());
        paymentCardForm.fillMonth(Data.getValidMonth());
        paymentCardForm.fillYear(Data.getValidYear());
        paymentCardForm.fillCVC(Data.getValidCVV());
        paymentCardForm.fillCardholder(Data.getValidCardholder());
        paymentCardForm.clickButton();

        paymentCardForm.shouldHaveTooltipWrongFormatInCardField(); // поле карты должно иметь подсказку "Неверный формат"
    }

    @Test
    @DisplayName("TestCase #5 by credit")
    public void testCreditPaymentWithNotingInCardField() {
        open("http://localhost:8080");

        MainPage mainPage = new MainPage();
        mainPage.selectPaymentByCredit();

        PaymentCardForm paymentCardForm = new PaymentCardForm();
        paymentCardForm.fillMonth(Data.getValidMonth());
        paymentCardForm.fillYear(Data.getValidYear());
        paymentCardForm.fillCVC(Data.getValidCVV());
        paymentCardForm.fillCardholder(Data.getValidCardholder());
        paymentCardForm.clickButton();

        paymentCardForm.shouldHaveTooltipWrongFormatInCardField(); // поле карты должно иметь подсказку "Неверный формат"
    }

    @Test
    @DisplayName("TestCase #6 by credit")
    public void testCreditPaymentWithZerosInCardField() {
        String zeros = "0000_0000_0000_0000";

        open("http://localhost:8080");

        MainPage mainPage = new MainPage();
        mainPage.selectPaymentByCredit();

        PaymentCardForm paymentCardForm = new PaymentCardForm();

        paymentCardForm.fillCardNumber(zeros);
        paymentCardForm.fillMonth(Data.getValidMonth());
        paymentCardForm.fillYear(Data.getValidYear());
        paymentCardForm.fillCVC(Data.getValidCVV());
        paymentCardForm.fillCardholder(Data.getValidCardholder());
        paymentCardForm.clickButton();

        paymentCardForm.shouldHaveTooltipWrongFormatInCardField(); // поле карты должно иметь подсказку "Неверный формат"
    }

    @Test
    @DisplayName("TestCase #7 by credit")
    public void testCreditPaymentWithPastYear() {
        open("http://localhost:8080");

        MainPage mainPage = new MainPage();
        mainPage.selectPaymentByCredit();

        PaymentCardForm paymentCardForm = new PaymentCardForm();
        paymentCardForm.fillCardNumber(Data.getApprovedCardNumber());
        paymentCardForm.fillMonth(Data.getValidMonth());
        paymentCardForm.fillYear(Data.getPastYear());
        paymentCardForm.fillCVC(Data.getValidCVV());
        paymentCardForm.fillCardholder(Data.getValidCardholder());
        paymentCardForm.clickButton();

        paymentCardForm.shouldHaveTooltipOfCardExpiration(); // поле год должно иметь подсказку "Истек срок действия карты"
    }

    @Test
    @DisplayName("TestCase #8 by credit")
    public void testCreditPaymentWithPreviousMonth() {
        open("http://localhost:8080");

        MainPage mainPage = new MainPage();
        mainPage.selectPaymentByCredit();

        PaymentCardForm paymentCardForm = new PaymentCardForm();
        paymentCardForm.fillCardNumber(Data.getApprovedCardNumber());
        paymentCardForm.fillMonth(Data.getPreviousMonth());
        paymentCardForm.fillYear(Data.getCurrentDate("yy"));
        paymentCardForm.fillCVC(Data.getValidCVV());
        paymentCardForm.fillCardholder(Data.getValidCardholder());
        paymentCardForm.clickButton();

        paymentCardForm.shouldHaveTooltipWrongTermOfCardInMonthField();
        // поле месяц должно иметь подсказку "Неверно указан срок действия карты"
    }

    @Test
    @DisplayName("TestCase #9 by credit")
    public void testCreditPaymentWithThirteenthMonth() {
        String wrongMonth = "13";

        open("http://localhost:8080");

        MainPage mainPage = new MainPage();
        mainPage.selectPaymentByCredit();

        PaymentCardForm paymentCardForm = new PaymentCardForm();
        paymentCardForm.fillCardNumber(Data.getApprovedCardNumber());
        paymentCardForm.fillMonth(wrongMonth);
        paymentCardForm.fillYear(Data.getValidYear());
        paymentCardForm.fillCVC(Data.getValidCVV());
        paymentCardForm.fillCardholder(Data.getValidCardholder());
        paymentCardForm.clickButton();

        paymentCardForm.shouldHaveTooltipWrongTermOfCardInMonthField();
        // поле месяц должно иметь подсказку "Неверно указан срок действия карты"
    }

    @Test
    @DisplayName("TestCase #10 by credit")
    public void testCreditPaymentWithZeroMonth() {
        String zeroMonth = "00";

        open("http://localhost:8080");

        MainPage mainPage = new MainPage();
        mainPage.selectPaymentByCredit();

        PaymentCreditForm paymentCreditForm = new PaymentCreditForm();
        paymentCreditForm.fillCardNumber(Data.getApprovedCardNumber());
        paymentCreditForm.fillMonth(zeroMonth);
        paymentCreditForm.fillYear(Data.getValidYear());
        paymentCreditForm.fillCVC(Data.getValidCVV());
        paymentCreditForm.fillCardholder(Data.getValidCardholder());
        paymentCreditForm.clickButton();

        paymentCreditForm.shouldHaveTooltipWrongTermOfCardInMonthField();
        // поле месяц должно иметь подсказку "Неверно указан срок действия карты"
    }

    @Test
    @DisplayName("TestCase #11 by credit")
    public void testCreditPaymentWithZeroYear() {
        String zeroYear = "00";

        open("http://localhost:8080");

        MainPage mainPage = new MainPage();
        mainPage.selectPaymentByCredit();

        PaymentCreditForm paymentCreditForm = new PaymentCreditForm();
        paymentCreditForm.fillCardNumber(Data.getApprovedCardNumber());
        paymentCreditForm.fillMonth(Data.getValidMonth());
        paymentCreditForm.fillYear(zeroYear);
        paymentCreditForm.fillCVC(Data.getValidCVV());
        paymentCreditForm.fillCardholder(Data.getValidCardholder());
        paymentCreditForm.clickButton();

        paymentCreditForm.shouldHaveTooltipOfCardExpiration(); // поле год должно иметь подсказку "Истек срок действия карты"
    }

    @Test
    @DisplayName("TestCase #12 by credit")
    public void testCreditPaymentWithoutDate() {
        open("http://localhost:8080");

        MainPage mainPage = new MainPage();
        mainPage.selectPaymentByCredit();

        PaymentCreditForm paymentCreditForm = new PaymentCreditForm();
        paymentCreditForm.fillCardNumber(Data.getApprovedCardNumber());
        paymentCreditForm.fillCVC(Data.getValidCVV());
        paymentCreditForm.fillCardholder(Data.getValidCardholder());
        paymentCreditForm.clickButton();


        paymentCreditForm.shouldHaveTooltipWrongFormatInMonthField(); // поле месяц должно иметь подсказку "Неверный формат"
        paymentCreditForm.shouldHaveTooltipWrongFormatInYearField(); // поле год должно иметь подсказку "Неверный формат"
    }

    @Test
    @DisplayName("TestCase #13 by credit")
    public void testCreditPaymentWithoutMonth() {
        open("http://localhost:8080");

        MainPage mainPage = new MainPage();
        mainPage.selectPaymentByCredit();

        PaymentCreditForm paymentCreditForm = new PaymentCreditForm();
        paymentCreditForm.fillCardNumber(Data.getApprovedCardNumber());
        paymentCreditForm.fillYear(Data.getValidYear());
        paymentCreditForm.fillCVC(Data.getValidCVV());
        paymentCreditForm.fillCardholder(Data.getValidCardholder());
        paymentCreditForm.clickButton();

        paymentCreditForm.shouldHaveTooltipWrongFormatInMonthField(); // поле месяц должно иметь подсказку "Неверный формат"
    }

    @Test
    @DisplayName("TestCase #14 by credit")
    public void testCreditPaymentWithoutYear() {
        open("http://localhost:8080");

        MainPage mainPage = new MainPage();
        mainPage.selectPaymentByCredit();

        PaymentCreditForm paymentCreditForm = new PaymentCreditForm();
        paymentCreditForm.fillCardNumber(Data.getApprovedCardNumber());
        paymentCreditForm.fillMonth(Data.getValidMonth());
        paymentCreditForm.fillCVC(Data.getValidCVV());
        paymentCreditForm.fillCardholder(Data.getValidCardholder());
        paymentCreditForm.clickButton();

        paymentCreditForm.shouldHaveTooltipWrongFormatInYearField(); // поле год должно иметь подсказку "Неверный формат"
    }

    @Test
    @DisplayName("TestCase #15 by credit")
    public void testCreditPaymentWithYearPlusSixYears() {
        open("http://localhost:8080");

        MainPage mainPage = new MainPage();
        mainPage.selectPaymentByCredit();

        PaymentCreditForm paymentCreditForm = new PaymentCreditForm();
        paymentCreditForm.fillCardNumber(Data.getApprovedCardNumber());
        paymentCreditForm.fillMonth(Data.getValidMonth());
        paymentCreditForm.fillYear(Data.getYearPlusSixYears());
        paymentCreditForm.fillCVC(Data.getValidCVV());
        paymentCreditForm.fillCardholder(Data.getValidCardholder());
        paymentCreditForm.clickButton();

        paymentCreditForm.shouldHaveTooltipWrongTermOfCardInYearField();
        // поле год должно иметь подсказку "Неверно указан срок действия карты"
    }

    @Test
    @DisplayName("TestCase #16 by credit")
    public void testCreditPaymentWithSymbolsInDateField() {
        String symbols = "!.,/|**&?";

        open("http://localhost:8080");

        MainPage mainPage = new MainPage();
        mainPage.selectPaymentByCredit();

        PaymentCreditForm paymentCreditForm = new PaymentCreditForm();
        paymentCreditForm.fillCardNumber(Data.getApprovedCardNumber());
        paymentCreditForm.fillMonth(symbols);
        paymentCreditForm.fillYear(symbols);
        paymentCreditForm.fillCVC(Data.getValidCVV());
        paymentCreditForm.fillCardholder(Data.getValidCardholder());
        paymentCreditForm.clickButton();

        paymentCreditForm.shouldHaveTooltipWrongFormatInMonthField(); // поле месяц должно иметь подсказку "Неверный формат"
        paymentCreditForm.shouldHaveTooltipWrongFormatInYearField(); // поле год должно иметь подсказку "Неверный формат"

        paymentCreditForm.monthShouldBeEmpty(); //поле месяц должно быть пустым
        paymentCreditForm.yearShouldBeEmpty(); //поле год должно быть пустым
    }

    @Test
    @DisplayName("TestCase #17 by credit")
    public void testCreditPaymentWithOneDigitInDateField() {
        open("http://localhost:8080");

        MainPage mainPage = new MainPage();
        mainPage.selectPaymentByCredit();

        PaymentCreditForm paymentCreditForm = new PaymentCreditForm();
        paymentCreditForm.fillCardNumber(Data.getApprovedCardNumber());
        paymentCreditForm.fillMonth(Data.getRandomDigit());
        paymentCreditForm.fillYear(Data.getRandomDigit());
        paymentCreditForm.fillCVC(Data.getValidCVV());
        paymentCreditForm.fillCardholder(Data.getValidCardholder());
        paymentCreditForm.clickButton();

        paymentCreditForm.shouldHaveTooltipWrongFormatInMonthField(); //поле месяц должно иметь подсказку "Неверный формат"
        paymentCreditForm.shouldHaveTooltipWrongFormatInYearField(); //поле год должно иметь подсказку "Неверный формат"
    }

    @Test
    @DisplayName("TestCase #18 by credit")
    public void testCreditPaymentWithRussianTextInCardholderField() {
        open("http://localhost:8080");

        MainPage mainPage = new MainPage();
        mainPage.selectPaymentByCredit();

        PaymentCreditForm paymentCreditForm = new PaymentCreditForm();
        paymentCreditForm.fillCardNumber(Data.getApprovedCardNumber());
        paymentCreditForm.fillMonth(Data.getValidMonth());
        paymentCreditForm.fillYear(Data.getValidYear());
        paymentCreditForm.fillCVC(Data.getValidCVV());
        paymentCreditForm.fillCardholder(Data.getRussianName());
        paymentCreditForm.clickButton();

        paymentCreditForm.shouldHaveTooltipWrongFormatInCardholderField(); //поле Имя Владельца должно иметь подсказку "Неверный формат"
    }

    @Test
    @DisplayName("TestCase #19 by credit")
    public void testCreditPaymentWithDigitInCardholderField() {
        open("http://localhost:8080");

        MainPage mainPage = new MainPage();
        mainPage.selectPaymentByCredit();

        PaymentCreditForm paymentCreditForm = new PaymentCreditForm();
        paymentCreditForm.fillCardNumber(Data.getApprovedCardNumber());
        paymentCreditForm.fillMonth(Data.getValidMonth());
        paymentCreditForm.fillYear(Data.getValidYear());
        paymentCreditForm.fillCVC(Data.getValidCVV());
        paymentCreditForm.fillCardholder(Data.getRandomDigit());
        paymentCreditForm.clickButton();


        paymentCreditForm.shouldHaveTooltipWrongFormatInCardholderField(); //поле Имя Владельца должно иметь подсказку "Неверный формат"
    }

    @Test
    @DisplayName("TestCase #20 by credit")
    public void testCreditPaymentWithOneCharInCardholderField() {
        String characterOne = "a";

        open("http://localhost:8080");

        MainPage mainPage = new MainPage();
        mainPage.selectPaymentByCredit();

        PaymentCreditForm paymentCreditForm = new PaymentCreditForm();
        paymentCreditForm.fillCardNumber(Data.getApprovedCardNumber());
        paymentCreditForm.fillMonth(Data.getValidMonth());
        paymentCreditForm.fillYear(Data.getValidYear());
        paymentCreditForm.fillCVC(Data.getValidCVV());
        paymentCreditForm.fillCardholder(characterOne);
        paymentCreditForm.clickButton();

        paymentCreditForm.shouldHaveTooltipWrongFormatInCardholderField(); //поле Имя Владельца должно иметь подсказку "Неверный формат"
    }

    @Test
    @DisplayName("TestCase #21 by credit")
    public void testCreditPaymentWithSymbolsInCardholderField() {
        String symbols = "!.,/|**&?";

        open("http://localhost:8080");

        MainPage mainPage = new MainPage();
        mainPage.selectPaymentByCredit();

        PaymentCreditForm paymentCreditForm = new PaymentCreditForm();
        paymentCreditForm.fillCardNumber(Data.getApprovedCardNumber());
        paymentCreditForm.fillMonth(Data.getValidMonth());
        paymentCreditForm.fillYear(Data.getValidYear());
        paymentCreditForm.fillCVC(Data.getValidCVV());
        paymentCreditForm.fillCardholder(symbols);
        paymentCreditForm.clickButton();

        paymentCreditForm.shouldHaveTooltipWrongFormatInCardholderField(); //поле Имя Владельца должно иметь подсказку "Неверный формат"
    }

    @Test
    @DisplayName("TestCase #22 by credit")
    public void testCreditPaymentWithoutCardholderField() {
        open("http://localhost:8080");

        MainPage mainPage = new MainPage();
        mainPage.selectPaymentByCredit();

        PaymentCreditForm paymentCreditForm = new PaymentCreditForm();
        paymentCreditForm.fillCardNumber(Data.getApprovedCardNumber());
        paymentCreditForm.fillMonth(Data.getValidMonth());
        paymentCreditForm.fillYear(Data.getValidYear());
        paymentCreditForm.fillCVC(Data.getValidCVV());
        paymentCreditForm.clickButton();


        paymentCreditForm.cardholderShouldBeFilledIn(); //поле Имя Владельца должно иметь подсказку "Поле обязательно для заполнения"
    }

    @Test
    @DisplayName("TestCase #23 by credit")
    public void testCreditPaymentWithZerosInCVCField() {
        String zeros = "000";

        open("http://localhost:8080");

        MainPage mainPage = new MainPage();
        mainPage.selectPaymentByCredit();

        PaymentCreditForm paymentCreditForm = new PaymentCreditForm();
        paymentCreditForm.fillCardNumber(Data.getApprovedCardNumber());
        paymentCreditForm.fillMonth(Data.getValidMonth());
        paymentCreditForm.fillYear(Data.getValidYear());
        paymentCreditForm.fillCVC(zeros);
        paymentCreditForm.fillCardholder(Data.getValidCardholder());
        paymentCreditForm.clickButton();


        paymentCreditForm.shouldHaveTooltipWrongFormatInCVVField(); //поле cvv должно иметь подсказку "Неверный формат"
    }

    @Test
    @DisplayName("TestCase #24 by credit")
    public void testCreditPaymentWithOneDigitInCVCField() {
        open("http://localhost:8080");

        MainPage mainPage = new MainPage();
        mainPage.selectPaymentByCredit();

        PaymentCreditForm paymentCreditForm = new PaymentCreditForm();
        paymentCreditForm.fillCardNumber(Data.getApprovedCardNumber());
        paymentCreditForm.fillMonth(Data.getValidMonth());
        paymentCreditForm.fillYear(Data.getValidYear());
        paymentCreditForm.fillCVC(Data.getRandomDigit());
        paymentCreditForm.fillCardholder(Data.getValidCardholder());
        paymentCreditForm.clickButton();

        paymentCreditForm.shouldHaveTooltipWrongFormatInCVVField(); //поле cvv должно иметь подсказку "Неверный формат"
    }

    @Test
    @DisplayName("TestCase #25 by credit")
    public void testCreditPaymentWithoutCVCField() {
        open("http://localhost:8080");

        MainPage mainPage = new MainPage();
        mainPage.selectPaymentByCredit();

        PaymentCreditForm paymentCreditForm = new PaymentCreditForm();
        paymentCreditForm.fillCardNumber(Data.getApprovedCardNumber());
        paymentCreditForm.fillMonth(Data.getValidMonth());
        paymentCreditForm.fillYear(Data.getValidYear());
        paymentCreditForm.fillCardholder(Data.getValidCardholder());
        paymentCreditForm.clickButton();

        paymentCreditForm.shouldHaveTooltipWrongFormatInCVVField(); //поле cvv должно иметь подсказку "Неверный формат"
    }

    @Test
    @DisplayName("TestCase #26 by credit")
    public void testCreditPaymentWithCharactersInCVCField() {
        String text = "Text";

        open("http://localhost:8080");

        MainPage mainPage = new MainPage();
        mainPage.selectPaymentByCredit();

        PaymentCreditForm paymentCreditForm = new PaymentCreditForm();
        paymentCreditForm.fillCardNumber(Data.getApprovedCardNumber());
        paymentCreditForm.fillMonth(Data.getValidMonth());
        paymentCreditForm.fillYear(Data.getValidYear());
        paymentCreditForm.fillCVC(text);
        paymentCreditForm.fillCardholder(Data.getValidCardholder());
        paymentCreditForm.clickButton();


        paymentCreditForm.shouldHaveTooltipWrongFormatInCVVField(); //поле cvv должно иметь подсказку "Неверный формат"
        paymentCreditForm.cvvShouldBeEmpty(); //поле cvv должно быть пустым
    }

    @Test
    @DisplayName("TestCase #27 by credit")
    public void testCreditPaymentWithSymbolsInCVCField() {
        String symbols = "!.,/|**&?";

        open("http://localhost:8080");

        MainPage mainPage = new MainPage();
        mainPage.selectPaymentByCredit();

        PaymentCreditForm paymentCreditForm = new PaymentCreditForm();
        paymentCreditForm.fillCardNumber(Data.getApprovedCardNumber());
        paymentCreditForm.fillMonth(Data.getValidMonth());
        paymentCreditForm.fillYear(Data.getValidYear());
        paymentCreditForm.fillCVC(symbols);
        paymentCreditForm.fillCardholder(Data.getValidCardholder());
        paymentCreditForm.clickButton();

        paymentCreditForm.shouldHaveTooltipWrongFormatInCVVField(); //поле cvv должно иметь подсказку "Неверный формат"
        paymentCreditForm.cvvShouldBeEmpty(); //поле cvv должно быть пустым
    }

    @Test
    @DisplayName("TestCase #28 by credit")
    public void testCreditPaymentWithEmptyFields() {
        open("http://localhost:8080");

        MainPage mainPage = new MainPage();
        mainPage.selectPaymentByCredit();

        PaymentCreditForm paymentCreditForm = new PaymentCreditForm();
        paymentCreditForm.clickButton();

        paymentCreditForm.shouldHaveTooltipWrongFormatInCardField();
        paymentCreditForm.shouldHaveTooltipWrongFormatInMonthField();
        paymentCreditForm.shouldHaveTooltipWrongFormatInYearField();
        paymentCreditForm.shouldHaveTooltipWrongFormatInCVVField();
        paymentCreditForm.cardholderShouldBeFilledIn();
    }

    @Test
    @DisplayName("TestCase #29 by credit")
    public void testCreditPaymentWithRandomCreditNumber() {
        open("http://localhost:8080");

        MainPage mainPage = new MainPage();
        mainPage.selectPaymentByCredit();

        PaymentCreditForm paymentCreditForm = new PaymentCreditForm();
        paymentCreditForm.fillCardNumber(Data.getRandomCardNumber());
        paymentCreditForm.fillMonth(Data.getValidMonth());
        paymentCreditForm.fillYear(Data.getValidYear());
        paymentCreditForm.fillCVC(Data.getValidCVV());
        paymentCreditForm.fillCardholder(Data.getValidCardholder());
        paymentCreditForm.clickButton();


        paymentCreditForm.shouldReceiveErrorNotification();
    }


    @Test
    @DisplayName("TestCase #30 by credit")
    public void testCreditPaymentNotifications() {
        open("http://localhost:8080");

        MainPage mainPage = new MainPage();
        mainPage.selectPaymentByCredit();


        PaymentCreditForm paymentCreditForm = new PaymentCreditForm();
        paymentCreditForm.fillCardNumber(Data.getApprovedCardNumber());
        paymentCreditForm.fillMonth(Data.getValidMonth());
        paymentCreditForm.fillYear(Data.getValidYear());
        paymentCreditForm.fillCVC(Data.getValidCVV());
        paymentCreditForm.fillCardholder(Data.getValidCardholder());
        paymentCreditForm.clickButton();

        paymentCreditForm.shouldReceiveOnlySuccessNotification(); // должно быть видно только одно уведомление об успешной оплате
    }


}

