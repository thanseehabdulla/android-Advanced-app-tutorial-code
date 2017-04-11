package com.example.infinit.acount;

import android.content.Context;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MyServerAuthenticator implements IServerAuthenticator {

	/**
	 * A dummy authentication store containing known user names and passwords.
	 * TODO: remove after connecting to a real authentication system.
	 */
	Context context;
	private static Map<String, String> mCredentialsRepo;
	
	static {
		Map<String, String> credentials = new HashMap<String, String>();

		credentials.put("username", "demo");
		mCredentialsRepo = Collections.unmodifiableMap(credentials);
	}



	@Override
	public String signUp(String email, String username, String password) {
		// TODO: register new user on the server and return its auth token
		return null;
	}

	@Override
	public String signIn(String email, String password) {
		String authToken = null;
		final DateFormat df = new SimpleDateFormat("yyyyMMdd-HHmmss");
		
//		if (mCredentialsRepo.containsKey(email)) {
//			if (password.equals(mCredentialsRepo.get(email))) {
				authToken = email + "-" + df.format(new Date());


		
		return authToken;
	}

}
