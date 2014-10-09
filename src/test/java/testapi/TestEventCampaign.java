package testapi;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;

import org.apache.http.auth.AuthenticationException;
import org.apache.http.client.ClientProtocolException;

import brickst.connectapi.ConnectAPI;
import brickst.connectapi.Customer;
import brickst.connectapi.CustomerAttribute;
import brickst.connectapi.Event;
import brickst.connectapi.EventParameter;
import brickst.connectapi.EventParameterMaster;

/**
 * This test program posts an event to an event-triggered campaign.
 * It takes two arguments: alt-customer-id and event-name.
 * The event-name argument should correspond to an event-triggered campaign that already exists in Connect.
 * 
 * The code assumes the event-triggered campaign has three event parameters:
 * - Message (string)
 * - Badge (number)
 * - AlertID (string)
 * 
 * @author cmaeda
 *
 */
public class TestEventCampaign 
{
	public ConnectAPI api;
	
	public void makeApiClient(String apiUrl, String apiUser, String apiPass) throws MalformedURLException
	{
		api = new ConnectAPI();
		api.apiBaseURL = new URL(apiUrl);
		api.apiUser = apiUser;
		api.apiPass = apiPass;		
	}

	/**
	 * Submit an event for an event-triggered campaign.
	 * 
	 * @param altCustId - AltCustomerId field for the customer record.
	 * @param eventName - Event Name for the event-triggered campaign.
	 * 
	 * @throws AuthenticationException
	 * @throws ClientProtocolException
	 * @throws URISyntaxException
	 * @throws IOException
	 */
	public void testExistingEventCampaign(String altCustId, String eventName, String tokenName, String tokenValue) 
			throws AuthenticationException, ClientProtocolException, URISyntaxException, IOException
	{
		String timecode = Long.toString(System.currentTimeMillis());
				
		// find or create a customer record				`
		Customer customer = api.getCustomerByAltId(altCustId);
		if (customer == null) {
			// customer does exist; create a new one
			String randomEmail = "user" + timecode + "@example.com";
			customer = new Customer();
			customer.emailAddress = randomEmail;
			customer.altCustomerID = altCustId;
			
			Customer cust2 = api.addCustomer(customer);
			if (cust2 == null) {
				throw new RuntimeException("null customer received from addCustomer");
			}
			customer = cust2;
		}
		
		// we have the customer record; grab the id
		long custId = customer.id;
		
		if (tokenName != null && tokenValue != null) {
		
			// Now add the device token to the customer record
			ArrayList<CustomerAttribute> attrs = customer.channelAddresses;
			CustomerAttribute foundAttr = null;
			// search for attribute
			for (CustomerAttribute attr : attrs) {
				if (attr.name.equals(tokenName)) {
					foundAttr = attr;
					break;
				}
			}
		
			boolean doupdate = false;
			if (foundAttr == null) {
				foundAttr = new CustomerAttribute();
				foundAttr.name = tokenName;
				foundAttr.value = tokenValue;
				foundAttr.type = CustomerAttribute.TYPE_ATTRIBUTE;
				foundAttr.dataType = EventParameterMaster.TYPE_STRING;
				attrs.add(foundAttr);
				doupdate = true;
			}
			else {
				if (foundAttr.value == null || !foundAttr.value.equals(tokenValue)) {
					// update attr and save
					foundAttr.value = tokenValue;
					doupdate = true;
				}
			}
		
			// save updated customer record if necessary
			if (doupdate) {
				Customer custSave2 = api.updateCustomer(customer);
				if (custSave2 == null) {
					throw new RuntimeException("null customer received from updateCustomer");
				}
				customer = custSave2;
			}

		}
		
		//
		// create an event record
		//
		Event event = new Event();
		event.eventName = eventName;
		event.customerId = custId;
		event.subscribe = true;
		event.parameters = new ArrayList<EventParameter>();
		
		Date now = new Date();
		
		// event parameter: Message
		EventParameter ep1 = new EventParameter();
		ep1.parameterName = "Message";
		ep1.parameterValue = "Test at " + now.toString();
		ep1.encrypted = false;
		event.parameters.add(ep1);
		// event parameter: Badge
		EventParameter ep2 = new EventParameter();
		ep2.parameterName = "Badge";
		ep2.parameterValue = "42";
		ep2.encrypted = false;
		event.parameters.add(ep2);
		// event parameter: AlertID
		EventParameter ep3 = new EventParameter();
		ep3.parameterName = "AlertID";
		ep3.parameterValue = timecode;
		ep3.encrypted = false;
		event.parameters.add(ep3);
		
		// 
		// submit event to connect
		//
		Event posted = api.addEvent(event);
		if (posted == null) {
			throw new RuntimeException("addEvent returned null");
		}
		long eventQueueId = posted.id;
		long eventId = posted.eventId;
		System.out.println("Posted Event; ID=" + eventQueueId + " for Customer ID=" + custId + " and Event ID=" + eventId);
	}
	
	
	public static void main(String[] args) 
	{
		if (args.length < 4) {
			System.out.println("usage: customer-id event-name attr-name attr-value");
			System.exit(2);
		}
		String altCustId = args[0];
		String eventName = args[1];
		String tokenName = args[2];
		String tokenValue = args[3];
		
		TestEventCampaign test = new TestEventCampaign();
		try {
			test.makeApiClient("https://example.com/brickstapi", "username", "password");
		}
		catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(1);
		}
		
		try {
//			test.testExistingEventCampaign(altCustId, eventName, tokenName, tokenValue);
			test.testExistingEventCampaign(altCustId, eventName, null, null);
		}
		catch (Throwable e) {
			e.printStackTrace();
			System.exit(1);			
		}
		
	}

}
