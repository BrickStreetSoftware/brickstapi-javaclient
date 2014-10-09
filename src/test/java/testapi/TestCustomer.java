package testapi;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Random;

import org.apache.http.auth.AuthenticationException;
import org.apache.http.client.ClientProtocolException;
import org.testng.Assert;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import brickst.connectapi.ConnectAPI;
import brickst.connectapi.Customer;

public class TestCustomer 
{
	public ConnectAPI api;
	
	@Parameters({"apiUrl", "apiUser", "apiPass"})
	@BeforeSuite
	public void makeApiClient(String apiUrl, String apiUser, String apiPass) throws MalformedURLException
	{
		api = new ConnectAPI();
		api.apiBaseURL = new URL(apiUrl);
		api.apiUser = apiUser;
		api.apiPass = apiPass;		
	}


	@Parameters({"custId", "custEmail", "custAltId"})
	@Test
	public void testFetchCustomer(String sCustId, String email, String altCustId) 
			throws AuthenticationException, ClientProtocolException, URISyntaxException, IOException
	{
		Long custid = Long.parseLong(sCustId);
		
		Customer customer = api.getCustomer(custid.longValue());
		Assert.assertNotNull(customer, "Customer is null");
		Assert.assertEquals(customer.id, custid);
		Assert.assertEquals(customer.emailAddress, email);
		Assert.assertEquals(customer.altCustomerID, altCustId);
	}
	
	@Parameters({"custId", "custEmail", "custAltId"})
	@Test
	public void testFetchCustomerEmail(String sCustId, String email, String altCustId) 
			throws AuthenticationException, ClientProtocolException, URISyntaxException, IOException
	{
		Long custid = Long.parseLong(sCustId);
		
		Customer customer = api.getCustomerByEmail(email);
		Assert.assertNotNull(customer, "Customer is null");
		Assert.assertEquals(customer.id, custid);
		Assert.assertEquals(customer.emailAddress, email);
		Assert.assertEquals(customer.altCustomerID, altCustId);
	}

	@Parameters({"custId", "custEmail", "custAltId"})
	@Test
	public void testFetchCustomerAltId(String sCustId, String email, String altCustId) 
			throws AuthenticationException, ClientProtocolException, URISyntaxException, IOException
	{
		Long custid = Long.parseLong(sCustId);
		
		Customer customer = api.getCustomerByAltId(altCustId);
		Assert.assertNotNull(customer, "Customer is null");
		Assert.assertEquals(customer.id, custid);
		Assert.assertEquals(customer.emailAddress, email);
		Assert.assertEquals(customer.altCustomerID, altCustId);
	}

	@Parameters({"custId", "custEmail", "custAltId"})
	@Test
	public void testUpdateCustomer(String sCustId, String email, String altCustId) 
			throws AuthenticationException, ClientProtocolException, URISyntaxException, IOException
	{
		Long custid = Long.parseLong(sCustId);
		
		Customer customer = api.getCustomer(custid.longValue());
		Assert.assertNotNull(customer, "Customer is null");
		Assert.assertEquals(customer.id, custid);
		Assert.assertEquals(customer.emailAddress, email);
		Assert.assertEquals(customer.altCustomerID, altCustId);
		
		// set city, state, zip
		String origCity = customer.city;
		String origState = customer.state;
		String origPostal = customer.postalCode;
		String origCountry = customer.country;
		
		String newCity = "Toronto";
		String newState = "ON";		// NB: limited to 2 chars ???
		String newPostal = "M6E1B2";
		String newCountry = "CANADA";
		
		// check data
		Assert.assertNotEquals(origCity, newCity);
		Assert.assertNotEquals(origState, newState);
		Assert.assertNotEquals(origPostal, newPostal);
		Assert.assertNotEquals(origCountry, newCountry);
		
		// update customer record
		customer.city = newCity;
		customer.state = newState;
		customer.postalCode = newPostal;
		customer.country = newCountry;
		Customer c2 = api.updateCustomer(customer);
		Assert.assertNotNull(c2, "Customer 2 is null");
		
		// check saved data
		Assert.assertEquals(c2.city, newCity);
		Assert.assertEquals(c2.state, newState);
		Assert.assertEquals(c2.postalCode, newPostal);
		Assert.assertEquals(c2.country, newCountry);
		
		// restore data
		c2.city = origCity;
		c2.state = origState;
		c2.postalCode = origPostal;
		c2.country = origCountry;
		Customer c3 = api.updateCustomer(c2);
		Assert.assertNotNull(c3, "Customer 3 is null");

		// check saved data
		Assert.assertEquals(c3.city, origCity);
		Assert.assertEquals(c3.state, origState);
		Assert.assertEquals(c3.postalCode, origPostal);
		Assert.assertEquals(c3.country, origCountry);		
	}

	@Test
	public void testAddCustomer() 
			throws AuthenticationException, ClientProtocolException, URISyntaxException, IOException
	{
		Random rand = new Random();
		long val = rand.nextLong();
		if (val < 0) {	// should be positive
			val *= -1;
		}
		String custVal = "user+" + val + "@example.com";
		
		Customer c = new Customer();
		c.emailAddress = custVal;
		c.altCustomerID = custVal;
		c.addressLine1 = "215 S Broadway 241";
		c.city = "Salem";
		c.state = "NH";
		c.country = "USA";
		
		Customer c2 = api.addCustomer(c);
		Assert.assertNotNull(c2, "Customer is null");
		Assert.assertNotNull(c2.id, "Customer ID is null");
		Assert.assertEquals(c2.emailAddress, custVal);
		Assert.assertEquals(c2.altCustomerID, custVal);
		
		// set city, state, zip
		String origCity = c2.city;
		String origState = c2.state;
		String origPostal = c2.postalCode;
		String origCountry = c2.country;
		
		String newCity = "Toronto";
		String newState = "ON";		// NB: limited to 2 chars ???
		String newPostal = "M6E1B2";
		String newCountry = "CANADA";
		
		// check data
		Assert.assertNotEquals(origCity, newCity);
		Assert.assertNotEquals(origState, newState);
		Assert.assertNotEquals(origPostal, newPostal);
		Assert.assertNotEquals(origCountry, newCountry);
		
		// update customer record
		c2.city = newCity;
		c2.state = newState;
		c2.postalCode = newPostal;
		c2.country = newCountry;
		Customer c3 = api.updateCustomer(c2);
		Assert.assertNotNull(c3, "Customer 3 is null");
		
		// check saved data
		Assert.assertEquals(c3.city, newCity);
		Assert.assertEquals(c3.state, newState);
		Assert.assertEquals(c3.postalCode, newPostal);
		Assert.assertEquals(c3.country, newCountry);
		
		// restore data
		c3.city = origCity;
		c3.state = origState;
		c3.postalCode = origPostal;
		c3.country = origCountry;
		Customer c4 = api.updateCustomer(c3);
		Assert.assertNotNull(c4, "Customer 4 is null");

		// check saved data
		Assert.assertEquals(c4.city, origCity);
		Assert.assertEquals(c4.state, origState);
		Assert.assertEquals(c4.postalCode, origPostal);
		Assert.assertEquals(c4.country, origCountry);		
	}


	// NOT USED
	@DataProvider(name = "customer1")
	public Object[][] dataCustomer1()
	{
		Object[][] data = new Object[][] {
				new Object[] { new Long(38506), "user@example.com", "user@example.com" }
		};
		
		return data;
	}

}
