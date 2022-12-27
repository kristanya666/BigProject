package page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;

public class PaymentCardForm {

    private final SelenideElement paymentForm = $("form.form");

    private final SelenideElement cardField = $("form div:nth-child(1) input.input__control");

    private final SelenideElement month = $x("//span[text()='Месяц']/following::input");
    private final SelenideElement year = $x("//span[text()='Год']/following::input");
    private final SelenideElement name = $x("//span[text()='Владелец']/following::input");
    private final SelenideElement cvc = $x("//span[text()='CVC/CVV']/following::input");
    private final SelenideElement buttonPay = $("form div:last-child button");
    private final SelenideElement tooltipOfCard = $("form div:nth-child(1) span.input__sub");
    private final SelenideElement tooltipOfMonth = $x("//span[text()='Месяц']/following::span[2]");
    private final SelenideElement tooltipOfYear = $x("//span[text()='Год']/following::span[2]");
    private final SelenideElement tooltipOfCardholder = $x("//span[text()='Владелец']/following::span[2]");
    private final SelenideElement tooltipOfCVC = $x("//span[text()='CVC/CVV']/following::span[2]");
    private final SelenideElement notificationSuccess = $(byText("Успешно"));
    private final SelenideElement notificationError = $("div.notification_status_error");
    private final SelenideElement notificationTitleError = notificationError.$("div.notification__title");
    private final String wrongFormat = "Неверный формат";


    public PaymentCardForm() {
        paymentForm.shouldBe(visible);
    }


    public void fillMonth(String monthValue) {
        month.setValue(monthValue);
    }

    public void fillYear(String yearValue) {
        year.setValue(yearValue);
    }

    public void fillCardNumber(String cardValue) {
        cardField.setValue(cardValue);
    }

    public void fillCardholder(String cardholderValue) {
        name.setValue(cardholderValue);
    }

    public void fillCVC(String cvcValue) {
        cvc.setValue(cvcValue);
    }

    public void clickButton() {
        buttonPay.click();
    }


    public void shouldHaveTooltipWrongTermOfCardInMonthField() {
            tooltipOfMonth.shouldBe(visible);
            tooltipOfMonth.shouldHave(exactText("Неверно указан срок действия карты"));
    }
    public void shouldHaveTooltipWrongTermOfCardInYearField() {
            tooltipOfYear.shouldBe(visible);
            tooltipOfYear.shouldHave(exactText("Неверно указан срок действия карты"));
    }

    public void shouldHaveTooltipOfCardExpiration() {
        tooltipOfYear.shouldBe(visible);
        tooltipOfYear.shouldHave(exactText("Истёк срок действия карты"));
    }

    public void shouldHaveTooltipWrongFormatInMonthField() {
        tooltipOfMonth.shouldBe(visible);
        tooltipOfMonth.shouldHave(exactText(wrongFormat));
    }

    public void shouldHaveTooltipWrongFormatInYearField() {
        tooltipOfYear.shouldBe(visible);
        tooltipOfYear.shouldHave(exactText(wrongFormat));
    }

    public void shouldHaveTooltipWrongFormatInCardField() {
        tooltipOfCard.shouldBe(visible);
        tooltipOfCard.shouldHave(exactText(wrongFormat));
    }

    public void shouldHaveTooltipWrongFormatInCardholderField() {
        tooltipOfCardholder.shouldBe(visible);
        tooltipOfCardholder.shouldHave(exactText(wrongFormat));
    }

    public void shouldHaveTooltipWrongFormatInCVVField() {
        tooltipOfCVC.shouldBe(visible);
        tooltipOfCVC.shouldHave(exactText(wrongFormat));
    }


    public void monthShouldBeEmpty() {
        month.shouldBe(empty);
    }

    public void yearShouldBeEmpty() {
        year.shouldBe(empty);
    }

    public void cvvShouldBeEmpty() {
        cvc.shouldBe(empty);
    }


    public void cardholderShouldBeFilledIn() {
        tooltipOfCardholder.shouldBe(visible);
        tooltipOfCardholder.shouldHave(exactText("Поле обязательно для заполнения"));
    }

    public void shouldReceiveErrorNotification() {
        notificationError.shouldBe(Condition.visible, Duration.ofSeconds(10));
        notificationTitleError.shouldHave(exactText("Ошибка"));

    }

    public void shouldReceiveSuccessNotification() {
        notificationSuccess.shouldBe(Condition.visible, Duration.ofSeconds(10));
        notificationSuccess.shouldHave(exactText("Успешно"));


    }

    public void shouldReceiveOnlySuccessNotification() {
        notificationSuccess.shouldBe(Condition.visible, Duration.ofSeconds(10));
        notificationSuccess.shouldHave(exactText("Успешно"));

        notificationTitleError.shouldBe(hidden);
    }


}
