package com.national.entry;

import java.util.Calendar;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import com.national.menu.LandingMenu;

public class AppEntry {

	public static String currUserName = "";
	public static String currPassWord = "";
	public static String currUserType = "";

	public static Logger log = Logger.getLogger(AppEntry.class);

	// Called by other menu classes.
	// It updates the user credential in AppEntry, like a "global state".
	// This helps track who is the current user and its type.
	public static void setCredential(String userName, String passWord, String userType) {
		AppEntry.currUserName = userName;
		AppEntry.currPassWord = passWord;
		AppEntry.currUserType = userType;
	}

	public static String getCurrentTime() {
		Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int date = c.get(Calendar.DAY_OF_MONTH);
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int minutes = c.get(Calendar.MINUTE);
		int seconds = c.get(Calendar.SECOND);
		String timeStr = hour + ":" + minutes + ":" + seconds + " (" + (month + 1) + "-" + date + "-" + year + ")";
		return timeStr;
	}

	public static void main(String[] args) {
		log.setLevel(Level.INFO);
		log.info("Java Nation Bank app starts at: " + getCurrentTime());
		System.out.println("Welcome To The Java National Bank Application (Alpha 1.0) -- Programmed By Guannan Lyu");
		LandingMenu.render();
		System.out.println("\nThank you for using Java National Bank Application.");
		log.info("Java Nation Bank app ends at: " + getCurrentTime());
	}

}
