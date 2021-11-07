package guru.qa;

import com.codeborne.selenide.Configuration;
import guru.qa.data.MenuItem;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Selenide.$;

public class GithubSearchTest {

    static Stream<Arguments> contributorAndTabsShouldBeCorrect() {
        ArrayList<MenuItem> tabs = new ArrayList<>();
        tabs.add(MenuItem.ISSUES);
        tabs.add(MenuItem.PULL_REQUESTS);
        return Stream.of(
                Arguments.of(
                        "selenide", "Andrei Solntsev", tabs
                ),
                Arguments.of(
                        "allure2", "Dmitry Baev", tabs
                )
        );
    }

    @BeforeAll
    static void setUp()
    {
        Configuration.baseUrl = "https://github.com";
    }

    @ValueSource(strings = {
            "selenide",
            "allure2",
            "junit5",
            "testng"

    })
    @ParameterizedTest(name = "Check the title of the project for input string: {0}")
    public void shouldFindRepositoryPage(String searchQuery) {
        open("/");
        $("[data-test-selector=\"nav-search-input\"]").setValue(searchQuery).pressEnter();
        $$(".repo-list li").first().$("a").click();
        $("h1").shouldHave(text(searchQuery));
    }

    @CsvSource(
            {"selenide, Andrei Solntsev",
             "allure2, Dmitry Baev",
             "junit5, Sam Brannen",
             "testng, Cedric Beust"}
    )
    @ParameterizedTest(name = "Check main contributor of the project: {0}, {1}")
    public void shouldFindCorrectMainContributor(String searchQuery, String name){
        open("/");
        $("[data-test-selector=\"nav-search-input\"]").setValue(searchQuery).pressEnter();
        $$(".repo-list li").first().$("a").click();
        $("h1").shouldHave(text(searchQuery));
        $(".BorderGrid").$(byText("Contributors"))
                .closest("div").$("li").hover();
        $$(".Popover-message").findBy(visible)
                .shouldHave(text(name));
    }

    @EnumSource(value = MenuItem.class)
    @ParameterizedTest(name = "Check the titles for the tabs: {0}")
    public void tabShouldHaveCorrectTitle(MenuItem menuItem){
        open("/selenide/selenide");
        $(String.format("span[data-content=\"%s\"]", menuItem.getContent()))
                .shouldHave(text(menuItem.getContent()));

    }

    @MethodSource("contributorAndTabsShouldBeCorrect")
    @ParameterizedTest(name = "Check the contributor and tabs content of the project")
    public void contributorAndTabsShouldBeCorrect(String searchQuery, String name, List<MenuItem> tabs){
        open("/");
        $("[data-test-selector=\"nav-search-input\"]").setValue(searchQuery).pressEnter();
        $$(".repo-list li").first().$("a").click();
        $("h1").shouldHave(text(searchQuery));
        $(".BorderGrid").$(byText("Contributors"))
                .closest("div").$("li").hover();
        $$(".Popover-message").findBy(visible)
                .shouldHave(text(name));
        for (MenuItem i:tabs) {
            $(String.format("span[data-content=\"%s\"]", i.getContent()))
                .shouldHave(text(i.getContent()));
        }
    }
}
