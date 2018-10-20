package com.belyabl9.client.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validator {
	private static final String PATTERN = "^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";

	public static boolean validateIP(final String ip){          
	  Pattern pattern = Pattern.compile(PATTERN);
	  Matcher matcher = pattern.matcher(ip);
	  return matcher.matches();             
	}
	
	public static boolean validatePort(int port) {
		return port > 0 && port <= 65535;
	}
	
}
