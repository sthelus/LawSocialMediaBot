package com.lawbot;

import org.apache.commons.validator.routines.UrlValidator;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class PostFetcher {

    public List<String> getUnreadPosts(WebDriver driver) {
        System.out.println("PUREEEEEESE");
        String[] schemes = {"http", "https"};

        List<String> notifications = new ArrayList<>();
        List<String> postsAsImageLinks = new ArrayList<>();
        List<WebElement> unreads = new ArrayList<>();
        WebDriverWait wait = new WebDriverWait(driver, 10);
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Quick links")));
        }
        catch (Exception e){
            driver.manage().timeouts().implicitlyWait(60,TimeUnit.SECONDS);
            getUnreadPosts(driver);
        }

        try {
            unreads = driver.findElements(By.cssSelector("a[class='mark_read icon-mark']"));
        }
        catch (Exception e){
            System.out.println("Hit exception with unreads");
            e.printStackTrace();
            getUnreadPosts(driver);
        }

        for (WebElement unread : unreads) {
            notifications.add(unread.getAttribute("href"));
        }

        if (unreads.isEmpty()) {
            return null;
        }

        List<String> forumSections = new ArrayList<>();

        for (String notification : notifications) {
            driver.get(notification);
            driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

            List<WebElement> forumSectionBreakdown = driver.findElements(By.cssSelector("span[itemprop*=name"));
            for (WebElement forumSection : forumSectionBreakdown) {
                forumSections.add(forumSection.getText());
            }

            if (forumSections.contains("LAW Twitter")) {

                List<WebElement> postsAsWebElements = driver.findElements(By.cssSelector("div[class*=unread]"));
                for (WebElement post : postsAsWebElements) {
                    try {
                        WebElement content = post.findElement(By.className("content"));
                        for (WebElement imageLink : content.findElements(By.className("postimage"))) {
                            postsAsImageLinks.add(imageLink.getAttribute("src"));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            if (forumSections.contains("LAW Magazine")) {
                postsAsImageLinks.add(driver.getCurrentUrl());
            }
        }

        UrlValidator urlValidator = new UrlValidator(schemes);

        postsAsImageLinks.removeIf(link -> !urlValidator.isValid(link));

        for (String link : postsAsImageLinks) {
            try {
                URL url = new URL(link);
                HttpURLConnection huc = (HttpURLConnection) url.openConnection();
                huc.setRequestMethod("HEAD");
                if (!Integer.toString(huc.getResponseCode()).startsWith("20")) {
                    postsAsImageLinks.remove(link);
                }
            } catch (Exception e) {
                postsAsImageLinks.remove(link);
            }
        }

        return postsAsImageLinks;
    }
}
