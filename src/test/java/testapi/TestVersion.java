package testapi;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.http.auth.AuthenticationException;
import org.apache.http.client.ClientProtocolException;
import org.testng.Assert;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import brickst.connectapi.ConnectAPI;

public class TestVersion
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
	public void testApiVersion()
			throws AuthenticationException, ClientProtocolException, URISyntaxException, IOException 
	{
		String version = api.getVersion();
		Assert.assertNotNull(version, "Null api version string");
		Assert.assertTrue(version.contains("Brick Street Connect"), "Version string not from Brick Street Connect");
	}
}
