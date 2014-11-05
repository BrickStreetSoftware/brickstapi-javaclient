package testapi;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.Random;

import org.apache.http.auth.AuthenticationException;
import org.apache.http.client.ClientProtocolException;
import org.testng.Assert;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import brickst.connectapi.Attribute;
import brickst.connectapi.ConnectAPI;
import brickst.connectapi.Customer;
import brickst.connectapi.CustomerAttribute;

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
		String custVal = "cmaeda+" + val + "@cmaeda.com";
		
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

	@Parameters({"attributeName", "attributeValue"})
	@Test
	public void testAddCustomerWithAttribute(String attributeName, String attributeValue) 
			throws AuthenticationException, ClientProtocolException, URISyntaxException, IOException
	{
		// lookup attribute
		Attribute attrDef = api.getAttribute(attributeName);
		Assert.assertNotNull(attrDef, "Attribute Not Found");
		Assert.assertEquals(attrDef.name, attributeName);
		
		// create new customer
		Random rand = new Random();
		long val = rand.nextLong();
		if (val < 0) {	// should be positive
			val *= -1;
		}
		String custVal = "cmaeda+" + val + "@cmaeda.com";
		
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

		// add an attribute
		CustomerAttribute attr = c2.getAttribute(attributeName);
		if (attr == null) {
			attr = new CustomerAttribute();
			attr.name = attrDef.name;
			attr.type = attrDef.type;
			attr.dataType = attrDef.dataType;
			attr.value = attributeValue;
			c2.attributes.add(attr);
		}
		else {
			attr.value = attributeValue;
		}
		
		Customer c3 = api.updateCustomer(c2);
		Assert.assertNotNull(c3, "Customer 3 is null");
		
		// check saved data
		CustomerAttribute c3attr = c3.getAttribute(attributeName);
		Assert.assertNotNull(c3attr);
		Assert.assertEquals(c3attr.value, attributeValue);
	}

	@Parameters({"preferenceName"})
	@Test
	public void testAddCustomerWithPreference(String preferenceName) 
			throws AuthenticationException, ClientProtocolException, URISyntaxException, IOException
	{
		// lookup attribute
		Attribute attrDef = api.getAttribute(preferenceName);
		Assert.assertNotNull(attrDef, "Attribute Not Found");
		Assert.assertEquals(attrDef.name, preferenceName);
		String attrType = attrDef.type;
		
		// create new customer
		Random rand = new Random();
		long val = rand.nextLong();
		if (val < 0) {	// should be positive
			val *= -1;
		}
		String custVal = "cmaeda+" + val + "@cmaeda.com";
		
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

		c = c2;
		
		//
		// add preference
		//
		
		CustomerAttribute attr = null;
		if ("preference".equals(attrType)) {
			// add a preference
			attr = c.getAttribute(preferenceName);
			if (attr == null) {
				attr = new CustomerAttribute();
				attr.name = attrDef.name;
				attr.type = attrDef.type;
				attr.dataType = attrDef.dataType;
				// start with 1 value
				attr.preferenceValues = new String[1];
				attr.preferenceValues[0] = "pval" + val;
				c.attributes.add(attr);
			}
			else {
				// add a value
				String[] vals = attr.preferenceValues;
				String[] newVals = new String[vals.length + 1];
				System.arraycopy(vals, 0, newVals, 0, vals.length);
				newVals[newVals.length - 1] = "pval" + val;
				attr.preferenceValues = newVals;
			}
		}
		else if ("multiaddress".equals(attrType)) {
			// add a channel address
			attr = c.getChannelAddress(preferenceName);
			if (attr == null) {
				attr = new CustomerAttribute();
				attr.name = attrDef.name;
				attr.type = attrDef.type;
				attr.dataType = attrDef.dataType;
				// start with 1 value
				attr.preferenceValues = new String[1];
				attr.preferenceValues[0] = "pval" + val;
				c.channelAddresses.add(attr);
			}
			else {
				String[] vals = attr.preferenceValues;
				String[] newVals = new String[vals.length + 1];
				System.arraycopy(vals, 0, newVals, 0, vals.length);
				newVals[newVals.length - 1] = "pval" + val;
				attr.preferenceValues = newVals;
			}
		}
		else {
			Assert.assertTrue(false, "Unknown pref type " + attrType);
		}
		
		// save orig value
		String[] prefVals = attr.preferenceValues;
		
		c2 = api.updateCustomer(c);
		Assert.assertNotNull(c2, "Customer 2 is null");
		
		// check saved data
		CustomerAttribute c2attr = null;
		if ("preference".equals(attrType)) {
			c2attr = c2.getAttribute(preferenceName);
		}
		else if ("multiaddress".equals(attrType)) {
			c2attr = c2.getChannelAddress(preferenceName);
		}
		Assert.assertNotNull(c2attr);
		Assert.assertTrue(Arrays.equals(c2attr.preferenceValues, prefVals));

		c = c2;
		
		//
		// add a second pref value
		//
		
		// new random value
		val = rand.nextLong();
		if (val < 0) {	// should be positive
			val *= -1;
		}

		if ("preference".equals(attrType)) {
			// add a preference
			attr = c.getAttribute(preferenceName);
			Assert.assertNotNull(attr);

			// add a value
			String[] vals = attr.preferenceValues;
			String[] newVals = new String[vals.length + 1];
			System.arraycopy(vals, 0, newVals, 0, vals.length);
			newVals[newVals.length - 1] = "pval" + val;
			attr.preferenceValues = newVals;
		}
		else if ("multiaddress".equals(attrType)) {
			// add a channel address
			attr = c.getChannelAddress(preferenceName);
			Assert.assertNotNull(attr);
			
			String[] vals = attr.preferenceValues;
			String[] newVals = new String[vals.length + 1];
			System.arraycopy(vals, 0, newVals, 0, vals.length);
			newVals[newVals.length - 1] = "pval" + val;
			attr.preferenceValues = newVals;
		}
		else {
			Assert.assertTrue(false, "Unknown pref type " + attrType);
		}

		prefVals = attr.preferenceValues;
		c2 = api.updateCustomer(c);
		Assert.assertNotNull(c2, "Customer 2 is null");
		
		// check saved data
		c2attr = null;
		if ("preference".equals(attrType)) {
			c2attr = c2.getAttribute(preferenceName);
		}
		else if ("multiaddress".equals(attrType)) {
			c2attr = c2.getChannelAddress(preferenceName);
		}
		Assert.assertNotNull(c2attr);
		Assert.assertTrue(Arrays.equals(c2attr.preferenceValues, prefVals));

		c = c2;
		
		//
		// remove a preference value
		//
		
		if ("preference".equals(attrType)) {
			// add a preference
			attr = c.getAttribute(preferenceName);
		}
		else if ("multiaddress".equals(attrType)) {
			attr = c.getChannelAddress(preferenceName);
		}		
		Assert.assertNotNull(attr);
		
		// remove first value
		String[] oldVals = attr.preferenceValues;
		String[] newVals = new String[oldVals.length - 1];
		System.arraycopy(oldVals, 1, newVals, 0, newVals.length);	
		attr.preferenceValues = newVals;
		
		prefVals = attr.preferenceValues;
		c2 = api.updateCustomer(c);
		Assert.assertNotNull(c2, "Customer 2 is null");
		
		// check saved data
		c2attr = null;
		if ("preference".equals(attrType)) {
			c2attr = c2.getAttribute(preferenceName);
		}
		else if ("multiaddress".equals(attrType)) {
			c2attr = c2.getChannelAddress(preferenceName);
		}
		Assert.assertNotNull(c2attr);
		Assert.assertTrue(Arrays.equals(c2attr.preferenceValues, prefVals));
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
