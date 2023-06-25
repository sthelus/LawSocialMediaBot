package com.lawbot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import javax.security.auth.login.LoginException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
public class Reposter {

    static void sendMessage(TextChannel ch, String message)
    {
        ch.sendMessage(message).queue();
    }

    public List<String> postLinks;
    public List<TextChannel> channels;

    public void getTwitterPosts(WebDriver webDriver, PostFetcher postFetcher, LoginToLAW loginToLAW){
        webDriver.get("http://www.law-rp.com");
        WebDriverWait wait = new WebDriverWait(webDriver, 10);
        boolean success = false;
        while (!success) {
            try {
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Quick links")));
                success = true;
            } catch (TimeoutException tme) {
                tme.printStackTrace();
                webDriver.get(webDriver.getCurrentUrl());
            }
        }
        if(!webDriver.findElements(By.linkText("Login")).isEmpty()){
            loginToLAW.login(webDriver);
        }

        postLinks = postFetcher.getUnreadPosts(webDriver);

        if(postLinks!= null){
            sendToChannel(channels, postLinks);
        }

        System.out.println("FINISHED ONE RUN");
    }

    public void sendToChannel(List<TextChannel> textChannels, List<String> postLinks){
        for(TextChannel ch : textChannels) {
            if(postLinks == null){
                assert true;
            }
            else {
                for (String post : postLinks) {
                    sendMessage(ch, post);
                }
            }
        }
    }

    public static void main(String[] args) throws LoginException, InterruptedException {
        ChromeOptions options = new ChromeOptions();
        System.setProperty("webdriver.chrome.driver", "src\\main\\resources\\chromedriver_win32\\chromedriver.exe");
        options.setHeadless(false);
        WebDriver driver = new ChromeDriver(options);

        PostFetcher postFetcher = new PostFetcher();
        LoginToLAW loginToLAW = new LoginToLAW();
        Reposter reposter = new Reposter();

        JDABuilder jdaBuilder = JDABuilder.createDefault("");//TODO: You must paste the sercurity token here
        // TODO: The token should not be publicly exposed. I've shared it with you in our discord server. Please do not share the security token with others
        JDA jda;

        Timer myTimer = new Timer ();
        TimerTask myTask = new TimerTask () {
            @Override
            public void run () {
                System.out.println("SEEKING POSTS!!!!");
                reposter.getTwitterPosts(driver, postFetcher, loginToLAW);
            }
        };

        try{
            jda = jdaBuilder.build();
            jda.awaitReady();
            System.out.println("Bot is up and running! Online!");
            reposter.channels = jda.getTextChannelsByName("law-social-media", true);
            myTimer.scheduleAtFixedRate(myTask , 0L, 2 * (60*1000));//every 2 mins
        }
        catch (Exception e){
            e.printStackTrace();
            jda = jdaBuilder.build();
            jda.awaitReady();
            System.out.println("Bot is up and running! Online!");
            reposter.channels = jda.getTextChannelsByName("law-social-media", true);
            myTimer.scheduleAtFixedRate(myTask , 0L, 2 * (60*1000));//every 2 mins
        }

    }
}
