package com.paypal.api.servlet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.paypal.api.payments.Address;
import com.paypal.api.payments.Agreement;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payer;
import com.paypal.api.payments.Plan;
import com.paypal.base.rest.JSONFormatter;
import com.paypal.base.rest.OAuthTokenCredential;
import com.paypal.base.rest.PayPalRESTException;
import com.paypal.base.rest.PayPalResource;

/**
 * Servlet implementation class PaypalServlet
 */
public class PaypalServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private String accessToken;
	private Agreement instance;
	int paid = 1;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PaypalServlet() {
        super();
        // TODO Auto-generated constructor stub
    }
    public void init(ServletConfig servletConfig) throws ServletException {
		// ##Load Configuration
		// Load SDK configuration for
		// the resource. This intialization code can be
		// done as Init Servlet.
				
		try {
			
			OAuthTokenCredential tokenCredential = PayPalResource.initConfig(new File("C:/Users/mucob/Documents/WorkProjects/Paypal/src/resources/sdk_config.properties"));
			accessToken = tokenCredential.getAccessToken();
			
		} catch (PayPalRESTException e) {
			e.printStackTrace();
			//LOGGER.fatal(e.getMessage());
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		doPost(request, response);
		
		
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("This is a post");
		
		System.out.println(accessToken);
		if(request.getParameter("token")== null){
			try {
				this.instance = new Agreement();
				loadData();
				Agreement agreement = this.create(this.instance);
				// get the links return through response. 
				// change it to JSON then to Agreement class. (Kinda long way to convert from string to class)
				//System.out.println("create response:\n" + agreement.getLastResponse());
				String lastResponseJSON = agreement.getLastResponse();
				Agreement responseAgreement = JSONFormatter.fromJSON(lastResponseJSON, Agreement.class);
				List<Links> links = responseAgreement.getLinks();
				
				// redirect customer to the paypal page to pay.
				if(links.get(1).getRel().contentEquals("execute")){
					
					request.setAttribute("REDIRECT", links.get(0).getHref());
				    String location = links.get(0).getHref();
				    //response.setStatus(response.SC_MOVED_TEMPORARILY);
				   // response.setHeader("Location", location);
				    response.sendRedirect(location);
				}else{
					// redirect to failed page.
				}
							
							
							
				//System.out.println("create response:\n" + agreement.getLastResponse());
				
				
			} catch (JsonSyntaxException e) {
				e.printStackTrace();
			} catch (JsonIOException e) {
				e.printStackTrace();
			} catch (PayPalRESTException e) {
				e.printStackTrace();
			}
		}else{
			System.out.println(request.getParameter("token"));
			this.execute();
		}
			
			
	}
	/**
	 * populate billing agreement details to the instance object
	 */
	public void loadData(){
		
		// set billing plan id.
		Plan plan = new Plan();
		plan.setId("P-1DB69567JP098443FYRCUCMQ");
		
		// set payment method
		Payer payer = new Payer();
		payer.setPaymentMethod("paypal");
		
		// set payer address
		Address address = new Address();
		address.setLine1("111 First Street");
		address.setCity("Saratoga");
		address.setState("CA");
		address.setPostalCode("95070");
		address.setCountryCode("US");
		
		// get user agreement info.
		this.instance.setName("Shoe of the month club");
		this.instance.setDescription("This is a demo being created.");
		this.instance.setStartDate("2016-12-12T00:37:04Z");
		this.instance.setPlan(plan);
		this.instance.setPayer(payer);
		this.instance.setShippingAddress(address);
		
	}
	/**
	 * populate instance with agreement details
	 * @return agreement object that has been 
	 * Initialized with the details fetched from JSON file
	 * @throws MalformedURLException
	 * @throws UnsupportedEncodingException
	 * @throws PayPalRESTException
	 */
	public Agreement create() throws MalformedURLException, UnsupportedEncodingException, PayPalRESTException {
		
		// populate Invoice object that we are going to play with
		this.instance= this.load("billingagreement_create.json");
		this.instance = this.instance.create(accessToken);
		return this.instance;
	}
	/**
	 * load data from JSONFIle
	 * @param jsonFile- file to be loaded from
	 * @return object with loaded details
	 */
	
	protected <T> T load(String jsonFile) {
	    try {
		    BufferedReader br = new BufferedReader(new FileReader("Paypal/src/resources/" + jsonFile));
	        StringBuilder sb = new StringBuilder();
	        String line = br.readLine();

	        while (line != null) {
	            sb.append(line);
	            sb.append(System.getProperty("line.separator"));
	            line = br.readLine();
	        }
	        br.close();
	        return (T)JSONFormatter.fromJSON(sb.toString(), instance.getClass());
	        
	    } catch (IOException e) {
	    	e.printStackTrace();
	    	return (T)instance;
	    }
	}
	/**
	 * load data from JSONFIle
	 * @param jsonFile- file to be loaded from
	 * @return object with loaded details
	 */
	@SuppressWarnings({ "unchecked", "hiding" })
	protected <T> T loadJSON(String jsonFile) {
	    try {
		    BufferedReader br = new BufferedReader(new FileReader("src/resources/" + jsonFile));
	        StringBuilder sb = new StringBuilder();
	        String line = br.readLine();

	        while (line != null) {
	            sb.append(line);
	            sb.append(System.getProperty("line.separator"));
	            line = br.readLine();
	        }
	        br.close();
	        return (T) sb.toString();
	        
	    } catch (IOException e) {
	    	e.printStackTrace();
	    	return (T)instance;
	    }
	}
	/**
	 *  Send request via REST API to create the agreement
	 * @param agreement - the object whose agreement is being created for.
	 * @return the object with created agreement
	 * @throws MalformedURLException
	 * @throws UnsupportedEncodingException
	 * @throws PayPalRESTException
	 */
	public Agreement create(Agreement agreement) throws MalformedURLException, UnsupportedEncodingException, PayPalRESTException {
		
		// populate Invoice object that we are going to play with
				this.instance= agreement;
				this.instance = this.instance.create(accessToken);
				return this.instance;
	}
	/**
	 * confirm/approve the agreement after creation
	 * @return confirmed agreement
	 */
	public Agreement execute() {
		String json = "{}";
		//Patch patch = JSONFormatter.fromJSON(json, Patch.class);
		try {
			this.instance.execute(accessToken);
		} catch (PayPalRESTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return this.instance;
		
	}
	

}
