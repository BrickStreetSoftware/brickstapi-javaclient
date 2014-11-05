package testapi;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.http.ParseException;
import org.apache.http.auth.AuthenticationException;
import org.testng.Assert;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import brickst.connectapi.ConnectAPI;
import brickst.connectapi.Conversation;

public class TestConversation
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

	@Test
	public void testConversations() throws AuthenticationException, ParseException, IOException, URISyntaxException 
	{
		Conversation[] convArr = api.getConversations();
		Assert.assertNotNull(convArr);
		if (convArr.length > 0) {
			for (int i = 0; i < convArr.length; i++) {
				
				// stop after 5
				if (i > 5) {
					break;
				}
				
				Conversation conv = convArr[i];
				Assert.assertNotNull(conv);			
				Assert.assertNotNull(conv.id);			
				Assert.assertNotNull(conv.name);			
				
				Conversation conv2 = api.getConversation(conv.id.longValue());
				Assert.assertNotNull(conv2);			
				Assert.assertEquals(conv2.id, conv.id);			
				Assert.assertEquals(conv2.name, conv.name);			
				
				conv2 = api.getConversationByName(conv.name);
				Assert.assertNotNull(conv2);			
				Assert.assertEquals(conv2.id, conv.id);			
				Assert.assertEquals(conv2.name, conv.name);			
			}
		}
	}
}
