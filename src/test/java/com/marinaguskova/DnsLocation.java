package com.marinaguskova;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverConditions.currentFrameUrl;

public class DnsLocation {

    @BeforeAll
    static void beforeAll() {
        Configuration.timeout = 8000;
    }

    @ValueSource(strings = {"Владивосток", "Новосибирск"})
    @ParameterizedTest(name = "Проверка изменения региона сайта на {0}")
    @Tag("CRITICAL")
    void changeLocation(String city) {
        open("https://www.dns-shop.ru");
        $x("//button[@class='base-ui-button-v2_medium base-ui-button-v2_grey base-ui-button-v2_ico-none base-ui-button-v2']").click();
        $x("//input[@class='base-ui-input-search__input_YOW']").click();
        $x("//input[@class='base-ui-input-search__input_YOW']").setValue(city).pressEnter();
        $x("//span[@class='city-select__text']").shouldHave(text(city));
        closeWindow();
    }
}
