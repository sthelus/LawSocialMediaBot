package com.lawbot;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class PostFetcher {

    public List<String> getUnreadPosts(WebDriver driver) {
        System.out.println("PUREEEEEESE");


        List<String> notifications = new ArrayList<>();
        List<String> postsAsImageLinks = new ArrayList<>();
        List<WebElement> unreads = driver.findElements(By.cssSelector("a[class='mark_read icon-mark']"));
        for(WebElement unread : unreads){
            notifications.add(unread.getAttribute("href"));
        }

        if(unreads.isEmpty()){
            return null;
        }

        for(String notification : notifications){
            driver.get(notification);
            driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
            List<WebElement> postsAsWebElements = driver.findElements(By.cssSelector("div[class*=unread]"));
            for(WebElement post : postsAsWebElements){
                    postsAsImageLinks.add(post.findElement(By.className("postimage")).getAttribute("src"));
            }
        }
        return postsAsImageLinks;
    }
}
