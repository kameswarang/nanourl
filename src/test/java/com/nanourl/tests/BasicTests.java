package com.nanourl.tests;

import org.junit.Test;

import com.nanourl.persistence.User;
import com.nanourl.persistence.Url;

public class BasicTests {
	//@Test
	public void printUserPassword() {
		User u = new User();
		u.setPassword("testing".toCharArray());
		u.setHashFromPassword();

		User v = new User();
		v.setPassword("testingg".toCharArray());

		System.out.println(u.verifyPassword(v));
	}

	//@Test
	public void refelectionTest() {
		Character[] cArray = new Character[10];
		Character c = 'a';
		System.out.println("Char Array: " + cArray.getClass().getName());
		System.out.println("Char" + c.getClass().getName());
	}

	//@Test
	public void convertTest() {
		String s = "";
		if(s.equals("")) {
			char[] ob = {' '};
			System.out.println("Converter: " + ob);
		}
		System.out.println(s.equals(""));
	}
	
	//@Test
	public void prettyTest() {
		Url u = new Url();
		u.setOurl("https://www.google.com/search?q=sfasd&oq=sfasd&aqs=chrome..69i57j69i60l5.279j0j7&sourceid=chrome&ie=UTF-8");
		System.out.println(u.getOurl());
		System.out.println(u.getPrettyOurl());
	}
}