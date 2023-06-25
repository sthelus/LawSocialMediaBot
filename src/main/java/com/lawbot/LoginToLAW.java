package com.lawbot;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import static org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable;

public class LoginToLAW {

    public void login(WebDriver webDriver) {
        WebDriverWait wait = new WebDriverWait(webDriver, 5);

        webDriver.get("http://www.law-rp.com");
        wait.until(elementToBeClickable(By.linkText("Login")));
        webDriver.findElement(By.linkText("Login")).click();
        webDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        Properties properties = new Properties();
        
        try (InputStream inputStream = new FileInputStream("src/main/resources/Properties.txt")) {

            properties.load(inputStream);
            WebElement username = webDriver.findElement(By.name("username"));
            WebElement password = webDriver.findElement(By.name("password"));
            username.click();
            username.sendKeys(properties.getProperty("lawuser"));
            password.click();
            password.sendKeys(properties.getProperty("lawpass"));
            webDriver.findElement(By.name("login")).click();
            webDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

            if (webDriver.getCurrentUrl().contains("login")) {
                login(webDriver);
            }
        } catch (
                Exception e) {
            e.printStackTrace();
        }
    }
}
