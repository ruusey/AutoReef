package com.reef;

import java.awt.AWTException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class Chrome {
	static WebDriver driver = null;
	static VirtualKeyBoard kb = null;
	static String email = "";
	static String password = "";
	static String preOut = "[AutoReef] ";
	public Chrome() {
		// TODO Auto-generated constructor stub
	}
	public static void main(String[] args) {
		String exePath = "D:\\downloads\\chromedriver_win32x\\chromedriver.exe";
		System.setProperty("webdriver.chrome.driver", exePath);
		driver = new ChromeDriver();
		try {
			kb=new VirtualKeyBoard();
		} catch (AWTException e) {
			e.printStackTrace();
		}
		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
		Properties p = new Properties();
		try {
			InputStream is = classloader.getResourceAsStream("settings.properties");
			p.load(is);
			is.close();
			email = p.getProperty("email");
			password = p.getProperty("password");
			System.out.println(preOut+"Loaded account from file ("+email+")");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		driver.get("https://app.reef-education.com");
		while(driver.getTitle()==null) {
			delayAction(1000);
		}
		System.out.println(preOut+"Succesfully connected to: "+driver.getCurrentUrl());
		delayAction(1000);
		clickEmail();
		clickPassword();
		clickSignIn();
		delayAction(1000);
		clickFirstCourse();
		boolean displayJoinAttmpts = true;
		while(!joinAvailable()) {
			if(displayJoinAttmpts) {
				System.out.println(preOut+"No Sessions Available, Idling...");
				displayJoinAttmpts=false;
			}
			delayAction(5000);	
		}
	}
	public static void clickEmail() {
		driver.findElement(By.id("userEmail")).click();
		driver.findElement(By.id("userEmail")).sendKeys(email);
		delayAction(1000);
	}
	public static void clickPassword() {
		driver.findElement(By.id("userPassword")).click();
		driver.findElement(By.id("userPassword")).sendKeys(password);
		delayAction(1000);
	}
	public static void clickSignIn() {
		driver.findElement(By.id("sign-in-button")).click();
		delayAction(1000);
	}
	public static void clickFirstCourse() {
		driver.findElement(By.tagName("a")).click();
		delayAction(1000);
		
	}
	public static boolean joinAvailable() {
		boolean canJoin = driver.findElement(By.id("join-inner-container")).isDisplayed();
		if(canJoin) {
			driver.findElement(By.className("join-button")).click();
			return true;
		}else {
			return false;
		}
	}
	public static void delayAction(long millis) {
		try {
			Thread.sleep(millis);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}
