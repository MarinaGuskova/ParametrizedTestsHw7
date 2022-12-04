package com.marinaguskova;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import java.util.List;
import java.util.stream.Stream;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Configuration.baseUrl;
import static com.codeborne.selenide.Selenide.*;


public class DnsSearch {


    @BeforeAll
    static void beforeAll() {

        Configuration.holdBrowserOpen = true;
        baseUrl = "https://www.dns-shop.ru";
    }

    @BeforeEach
    void dnsSetUp() {
        open(baseUrl);
    }

    @CsvFileSource(resources = "/searchByName.csv")
    @ParameterizedTest(name = "Проверка наличия и количества товара {0} в поисковой выдаче")
    @Tag("BLOCKER")
    void searchProducByNameTest(String searshQuery, int resultCount) {
        $x("//div[@class='header-menu-wrapper']//input").setValue(searshQuery).pressEnter();
        $(".products-list__content").shouldHave(text(searshQuery));
        $$x("//div[@data-id='product']").should(CollectionCondition.size(resultCount));
    }


    @ValueSource(strings = {"4751582", "1019622"})
    @ParameterizedTest(name = "Проверка перехода в карточку товара {0} из поисковой выдачи")
    @Tag("BLOCKER")
    void searchProductByNumberTest(String productNumber) {
        $x("//div[@class='header-menu-wrapper']//input").setValue(productNumber).pressEnter();
        $x("//div[@class='product-card-top__code']").shouldHave(text(productNumber));
    }

    static Stream<Arguments> catalogSearch() {
        return Stream.of(
                Arguments.of("Смартфоны и фототехника", List.of("Смартфоны и гаджеты", "Планшеты, электронные книги", "Фототехника")),
                Arguments.of("ТВ, консоли и аудио", List.of("Телевизоры и аксессуары", "Консоли и видеоигры", "Аудиотехника"))
        );
    }

    @Disabled  // Не поняла как сделать "содержит текст"(containExactTextsCaseSensitive не подходит)
    @MethodSource
    @ParameterizedTest(name = "Проверка отображения подкатегорий каталога при наведении на категорию {0}")
    @Tag("CRITICAL")
    void catalogSearch(String сategories, List<String> subcategories) {
        $$x("//a[@class='ui-link menu-desktop__root-title']").find(text(сategories)).hover();
        $$x("//div[@class='menu-desktop__submenu menu-desktop__submenu_top']").filter(visible)
                .shouldHave(CollectionCondition.containExactTextsCaseSensitive(subcategories));
    }


    @CsvSource({"На Android, Операционная система: Android",
            "На IOS, Операционная система: iOS"})
    @ParameterizedTest(name = "Проверка фильтрации товаров в листинге с помощью выбора тэга {0}")
    @Tag("CRITICAL")
    void filterProductsByTagsTest(String pushedTag, String choosedTag) {
        open("/catalog/17a8a01d16404e77/smartfony/");
        $$x("//a[@class='receipts__item ui-link']").find(text(pushedTag)).click();
        $x("//div[@class='picked-filter']").shouldHave(text(choosedTag));
    }

    @DisplayName("Проверка поиска товаров по артикулу и их наличия в поисковой выдаче")
    @Test
    @Tag("CRITICAL")
    void searchProducByNumberTest() {
        $x("//div[@class='header-menu-wrapper']//input").setValue("4751582 1019622").pressEnter();
        $x("//div[@data-code][1]").hover();
        $x("//div[@class='catalog-product__code']").shouldHave(text("4751582"));
        $x("//div[@data-code][2]").hover();
        $x("//div[@class='catalog-product__code']").shouldHave(text("1019622"));

    }
}
