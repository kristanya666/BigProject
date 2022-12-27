package page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

public class MainPage {
    private final SelenideElement notCreditButton = $(byText("Купить"));
    private final SelenideElement creditButton = $(byText("Купить в кредит"));
    private final SelenideElement page = $("div.App_appContainer__3jRx1");

    public MainPage() {
        page.shouldBe(visible);
    }

    public PaymentCardForm selectPaymentByCard() {
        notCreditButton.click();
        return new PaymentCardForm();
    }

    public PaymentCreditForm selectPaymentByCredit() {
        creditButton.click();
        return new PaymentCreditForm();
    }
}
