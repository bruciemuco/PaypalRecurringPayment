package com.paypal.api.payments.util;

import com.paypal.base.ConfigManager;
import com.paypal.base.Constants;
import com.paypal.base.rest.OAuthTokenCredential;
import com.paypal.base.rest.PayPalRESTException;

public class GenerateAccessToken { 

	public static String getAccessToken() throws PayPalRESTException {

		// ###AccessToken
		// Retrieve the access token from
		// OAuthTokenCredential by passing in
		// ClientID and ClientSecret
		//@SuppressWarnings("deprecation")
		String clientID = "AWTJpBmR4PCIKdP563pkVWJTaUpHcoCqANk6fSjVXtLKXIfoJrAOFZFTfMCEqt4Q5wK5D2Ev8bQjeRC3";
		//@SuppressWarnings("deprecation")
		String clientSecret = "EOAFR2CjBPV84sK_Yd9liT2wBN_bnmCC9KeMYZRoARzns-JHOPk5EsYx5zqw33UmRT7TbRaS0ARtXAIa";

		return new OAuthTokenCredential(clientID, clientSecret)
				.getAccessToken();
	}
}
