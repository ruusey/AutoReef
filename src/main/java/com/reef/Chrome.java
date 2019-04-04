package com.reef;

// @Copyright Robert Usey 04/03/2019
// AutoReef is proprietary code developed not intended for redistribution
// By the aforementioned author.

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
public class Chrome {
	static long delayTime = 500;
	static long joinSleepTime = 5000;
	static WebDriver driver = null;
	static String email =null;
	static String password = null;
	static String preOut = "[AutoReef] ";
	static String driverPath = "D:\\downloads\\chromedriver_win32x\\chromedriver.exe";
	static String distDriverPathWin = "chromedriver.exe";
	static String distDriverPathMac = "chromedriverMac";
	static String distDriverPathLinux = "chromedriverLinux";
	static String reefUrl = "https://app.reef-education.com";
	public static void main(String[] args) throws Exception {
		try {
			if(OSValidator.isMac()) {
				System.setProperty("webdriver.chrome.driver", distDriverPathMac);
			}else if(OSValidator.isWindows()) {
				System.setProperty("webdriver.chrome.driver", distDriverPathWin);
			}else if(OSValidator.isUnix()) {
				System.setProperty("webdriver.chrome.driver", distDriverPathLinux);
			}
			driver = new ChromeDriver();
		}catch(Exception e) {
			throw new Exception(preOut+"Unable to create driver instance of: ("+driverPath+")");
		}
		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
		Properties p = new Properties();
		try {
			InputStream is = classloader.getResourceAsStream("settings.properties");
			p.load(is);
			is.close();
			email = p.getProperty("email");
			password = p.getProperty("password");
			if(email==null || password==null || password.length()<2 || email.length()<2) {
				throw new Exception(preOut+"Unable to load account credentials please ensure you have filled out the email and password feilds in `settings.properties`");
			}
			System.out.println(preOut+"Loaded account from file: ("+email+")");
			driver.get(reefUrl);
		} catch (FileNotFoundException e) {
			throw new Exception(preOut+"Unable to locate `settings.properties` please ensure the file is present.");
		} catch (IOException e) {
			throw new Exception(preOut+"Unable to parse credentials from `settings.properties` please ensure its structure is correct.");
		}catch (Exception e) {
			throw new Exception(preOut+e.getMessage());
		}
		while(driver.getTitle()==null) {
			delayAction(delayTime);
		}
		System.out.println(preOut+"Succesfully connected to: "+driver.getCurrentUrl());
		delayAction(delayTime);
		clickEmail();
		clickPassword();
		clickSignIn();
		System.out.println(preOut+"Succesfully connected to: "+driver.getCurrentUrl());
		clickFirstCourse();
		System.out.println(preOut+"Succesfully connected to: "+driver.getCurrentUrl());
		System.out.println(preOut+"Succesfully selected course: ("+driver.findElement(By.tagName("h1")).getText()+")");
		boolean displayJoinAttmpts = true;
		while(!joinAvailable()) {
			if(displayJoinAttmpts) {
				System.out.println(preOut+"No open course sessions available, idling...");
				displayJoinAttmpts=false;
			}
			delayAction(joinSleepTime);	
		}
	}
	public static void clickEmail() {
		System.out.println(preOut+"Selecting email feild, filling with `email`");
		driver.findElement(By.id("userEmail")).click();
		driver.findElement(By.id("userEmail")).sendKeys(email);
		delayAction(delayTime);
	}
	public static void clickPassword() {
		System.out.println(preOut+"Selecting password feild, filling with `password`");
		driver.findElement(By.id("userPassword")).click();
		driver.findElement(By.id("userPassword")).sendKeys(password);
		delayAction(delayTime);
	}
	public static void clickSignIn() {
		System.out.println(preOut+"Clicking sign-in.");
		driver.findElement(By.id("sign-in-button")).click();
		delayAction((long) (delayTime*1.2));
	}
	public static void clickFirstCourse() {
		System.out.println(preOut+"Selecting first course.");
		driver.findElement(By.tagName("a")).click();
		delayAction(delayTime);
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