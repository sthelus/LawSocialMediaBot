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
                try {
                    WebElement content = post.findElement(By.className("content"));
                    for(WebElement imageLink: content.findElements(By.className("postimage"))){
                        postsAsImageLinks.add(imageLink.getAttribute("src"));
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        return postsAsImageLinks;
    }
}
