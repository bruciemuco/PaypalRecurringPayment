package com.paypal.api.agreement;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;

import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.paypal.api.Subscription.Subscription;
import com.paypal.api.payments.Agreement;
import com.paypal.base.rest.PayPalRESTException;

public class PayerAgreement extends Base<Agreement> {
	
	public PayerAgreement() throws PayPalRESTException,
			JsonSyntaxException, JsonIOException, FileNotFoundException {
		super(new Agreement());
		
	}
	public Agreement create() throws MalformedURLException, UnsupportedEncodingException, PayPalRESTException {
			
			// populate Invoice object that we are going to play with
					super.instance= super.load("billingagreement_create.json");
					super.instance = super.instance.create(accessToken);
					return super.instance;
		}
	public Agreement create(Agreement agreement) throws MalformedURLException, UnsupportedEncodingException, PayPalRESTException {
		
		// populate Invoice object that we are going to play with
				super.instance= agreement;
				super.instance = super.instance.create(accessToken);
				return super.instance;
	}
	public Agreement execute() {
		String json = "{}";
		//Patch patch = JSONFormatter.fromJSON(json, Patch.class);
		try {
			super.instance.execute(super.accessToken);
		} catch (PayPalRESTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return super.instance;
		
	}
	public static void main (String[] args) throws MalformedURLException, UnsupportedEncodingException{
		try {
			PayerAgreement payerAgreement = new PayerAgreement();
			
			@SuppressWarnings("unused")
			Agreement agreement = payerAgreement.create();
			System.out.println("create response:\n" + agreement.getLastResponse());
			agreement = payerAgreement.execute();
			
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
		} catch (JsonIOException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (PayPalRESTException e) {
			e.printStackTrace();
		}
		
	}


	
	

	
}
