package com.testsuite.e2e;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DashboardTest {

    static Playwright playwright;
    static Browser browser;
    BrowserContext context;
    Page page;

    @BeforeAll
    static void launchBrowser() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions()
                        .setHeadless(false)
                        .setSlowMo(1000));
    }

    @AfterAll
    static void closeBrowser() {
        playwright.close();
    }

    @BeforeEach
    void createPage() {
        context = browser.newContext();
        page = context.newPage();
    }

    @AfterEach
    void closePage() {
        context.close();
    }

    @Test
    @Order(1)
    @DisplayName("ダッシュボードが表示される")
    void dashboardLoads() {
        page.navigate("http://localhost:8080");
        assertNotNull(page.title());
        assertTrue(page.url().contains("localhost:8080"));
    }

    @Test
    @Order(2)
    @DisplayName("ニュースセクションが表示される")
    void newsSectionVisible() {
        page.navigate("http://localhost:8080");
        assertTrue(page.locator(".news-section").count() > 0);
    }

    @Test
    @Order(3)
    @DisplayName("メモページに遷移できる")
    void navigateToMemos() {
        page.navigate("http://localhost:8080");
        page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("メモ")).click();
        assertTrue(page.url().contains("/memos"));
    }

    @Test
    @Order(4)
    @DisplayName("単語一覧ページに遷移できる")
    void navigateToWords() {
        page.navigate("http://localhost:8080");
        page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("単語一覧")).click();
        assertTrue(page.url().contains("/words"));
    }

    @Test
    @Order(5)
    @DisplayName("タスクを追加できる")
    void addtask() {
        page.navigate("http://localhost:8080");
        page.getByPlaceholder("タスクを入力...").fill("テストタスク");
        page.locator(".add-form button[type='submit']").first().click();
        assertTrue(page.locator(".task-list").textContent().contains("テストタスク"));
    }

    @Test
    @Order(6)
    @DisplayName("タスクを削除できる")
    void deleteTask() {
        page.navigate("http://localhost:8080");
        int beforeCount = page.locator(".task-item").count();
        page.locator(".delete-btn").first().click();
        int afterCount = page.locator(".task-item").count();
        assertTrue(afterCount < beforeCount);
    }
}
