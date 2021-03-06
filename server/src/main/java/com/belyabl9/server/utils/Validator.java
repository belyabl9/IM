package com.belyabl9.server.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validator {
	private static final String PATTERN = "^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";

	public static boolean validateIp(final String ip){          
	      Pattern pattern = Pattern.compile(PATTERN);
	      Matcher matcher = pattern.matcher(ip);
	      return matcher.matches();             
	}
	
}
