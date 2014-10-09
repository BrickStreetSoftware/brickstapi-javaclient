package brickst.connectapi;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ConnectAPI
{
	public URL apiBaseURL;
	public String apiUser;
	public String apiPass;
	
	private HttpGet getGet(String apiPath, HttpContext http) throws URISyntaxException, AuthenticationException
	{
		// construct URI
		URI uri = new URIBuilder()
			.setScheme(apiBaseURL.getProtocol())
			.setHost(apiBaseURL.getHost())
			.setPort(apiBaseURL.getPort())
			.setPath(apiPath)
			.build();		
	
		// build http request
		UsernamePasswordCredentials upCreds = new UsernamePasswordCredentials(apiUser, apiPass);
		HttpGet httpGet = new HttpGet(uri);
		httpGet.addHeader(new BasicScheme().authenticate(upCreds, httpGet, http));

		return httpGet;		
	}
	
	private HttpPut getPut(String apiPath, HttpContext http) throws URISyntaxException, AuthenticationException
	{
		// construct URI
		URI uri = new URIBuilder()
			.setScheme(apiBaseURL.getProtocol())
			.setHost(apiBaseURL.getHost())
			.setPort(apiBaseURL.getPort())
			.setPath(apiPath)
			.build();		
	
		// build http request
		UsernamePasswordCredentials upCreds = new UsernamePasswordCredentials(apiUser, apiPass);
		HttpPut httpPut = new HttpPut(uri);
		httpPut.addHeader(new BasicScheme().authenticate(upCreds, httpPut, http));

		return httpPut;		
	}

	private HttpPost getPost(String apiPath, HttpContext http) throws URISyntaxException, AuthenticationException
	{
		// construct URI
		URI uri = new URIBuilder()
			.setScheme(apiBaseURL.getProtocol())
			.setHost(apiBaseURL.getHost())
			.setPort(apiBaseURL.getPort())
			.setPath(apiPath)
			.build();		
	
		// build http request
		UsernamePasswordCredentials upCreds = new UsernamePasswordCredentials(apiUser, apiPass);
		HttpPost httpPost = new HttpPost(uri);
		httpPost.addHeader(new BasicScheme().authenticate(upCreds, httpPost, http));

		return httpPost;		
	}

	
	public String getVersion() throws URISyntaxException, AuthenticationException, ClientProtocolException, IOException
	{
		// construct api path for entry point
		String apiPath = apiBaseURL.getPath();
		if (! apiPath.endsWith("/")) {
			apiPath += "/";
		}
		apiPath += "metadata/version";

		BasicHttpContext http = new BasicHttpContext();
		HttpGet httpGet = getGet(apiPath, http);
		
		CloseableHttpClient httpClient = HttpClients.createDefault();	
		CloseableHttpResponse httpResp = null;
		
		String version = null;
		
		try 
		{
			httpResp = httpClient.execute(httpGet);
			HttpEntity entity = httpResp.getEntity();
			version = EntityUtils.toString(entity);
			return version;			
		} 
		finally {
			if (httpResp != null) {
				try { httpResp.close(); } catch (Exception x) {}
			}
			if (httpClient != null) {
				try { httpClient.close(); } catch (Exception x) {}
			}
		}		
	}

	private Customer _getCustomer(String apiPath) 
			throws AuthenticationException, URISyntaxException, ClientProtocolException, IOException
	{
		BasicHttpContext http = new BasicHttpContext();
		HttpGet httpGet = getGet(apiPath, http);
		
		CloseableHttpClient httpClient = HttpClients.createDefault();	
		CloseableHttpResponse httpResp = null;
		
		String json = null;
		
		try 
		{
			httpResp = httpClient.execute(httpGet);
			HttpEntity entity = httpResp.getEntity();
			json = EntityUtils.toString(entity);
			
			// map json to pojo using jackson databinding
			ObjectMapper mapper = new ObjectMapper();
			Customer customer = mapper.readValue(json, Customer.class);
			return customer;
		} 
		finally {
			if (httpResp != null) {
				try { httpResp.close(); } catch (Exception x) {}
			}
			if (httpClient != null) {
				try { httpClient.close(); } catch (Exception x) {}
			}
		}		
		
	}

	public Customer getCustomer(long id) 
			throws AuthenticationException, URISyntaxException, ClientProtocolException, IOException
	{
		// construct api path for entry point
		String apiPath = apiBaseURL.getPath();
		if (! apiPath.endsWith("/")) {
			apiPath += "/";
		}
		apiPath += "data/customer/id/" + id;

		Customer cust = _getCustomer(apiPath);
		return cust;
	}

	public Customer getCustomerByEmail(String email) 
			throws AuthenticationException, URISyntaxException, ClientProtocolException, IOException
	{
		// construct api path for entry point
		String apiPath = apiBaseURL.getPath();
		if (! apiPath.endsWith("/")) {
			apiPath += "/";
		}
		apiPath += "data/customer/email/" + email;

		Customer cust = _getCustomer(apiPath);
		return cust;
	}

	public Customer getCustomerByAltId(String altId) 
			throws AuthenticationException, URISyntaxException, ClientProtocolException, IOException
	{
		// construct api path for entry point
		String apiPath = apiBaseURL.getPath();
		if (! apiPath.endsWith("/")) {
			apiPath += "/";
		}
		apiPath += "data/customer/altid/" + altId;

		Customer cust = _getCustomer(apiPath);
		return cust;
	}
	
	public Customer updateCustomer(Customer cust)
			throws AuthenticationException, URISyntaxException, ClientProtocolException, IOException
	{
		// construct api path for entry point
		String apiPath = apiBaseURL.getPath();
		if (! apiPath.endsWith("/")) {
			apiPath += "/";
		}
		apiPath += "data/customer/id/" + cust.id.longValue();
		
		BasicHttpContext http = new BasicHttpContext();
		HttpPut httpPut = getPut(apiPath, http);
				
		// map pojo to json using jackson databinding
		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(cust);

		// store json in http request
		StringEntity stringEntity = new StringEntity(json);
		stringEntity.setContentType("application/json");
		httpPut.setEntity(stringEntity);
		
		CloseableHttpClient httpClient = HttpClients.createDefault();	
		CloseableHttpResponse httpResp = null;

		try 
		{
			httpResp = httpClient.execute(httpPut);
			HttpEntity entity = httpResp.getEntity();
			json = EntityUtils.toString(entity);

			// update call returns new customer object
			Customer cust1 = mapper.readValue(json, Customer.class);
			return cust1;
		} 
		finally {
			if (httpResp != null) {
				try { httpResp.close(); } catch (Exception x) {}
			}
			if (httpClient != null) {
				try { httpClient.close(); } catch (Exception x) {}
			}
		}		
		
	}
	
	public Customer addCustomer(Customer cust)
			throws AuthenticationException, URISyntaxException, ClientProtocolException, IOException
	{
		// construct api path for entry point
		String apiPath = apiBaseURL.getPath();
		if (! apiPath.endsWith("/")) {
			apiPath += "/";
		}
		apiPath += "data/customer";
		
		BasicHttpContext http = new BasicHttpContext();
		HttpPost httpPost = getPost(apiPath, http);
				
		// map pojo to json using jackson databinding
		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(cust);

		// store json in http request
		StringEntity stringEntity = new StringEntity(json);
		stringEntity.setContentType("application/json");
		httpPost.setEntity(stringEntity);
		
		CloseableHttpClient httpClient = HttpClients.createDefault();	
		CloseableHttpResponse httpResp = null;

		try 
		{
			httpResp = httpClient.execute(httpPost);
			HttpEntity entity = httpResp.getEntity();
			json = EntityUtils.toString(entity);

			// update call returns new customer object
			Customer cust1 = mapper.readValue(json, Customer.class);
			return cust1;
		} 
		finally {
			if (httpResp != null) {
				try { httpResp.close(); } catch (Exception x) {}
			}
			if (httpClient != null) {
				try { httpClient.close(); } catch (Exception x) {}
			}
		}		
		
	}


	public Conversation[] getConversations() throws ParseException, IOException, AuthenticationException, URISyntaxException
	{
		// construct api path for entry point
		String apiPath = apiBaseURL.getPath();
		if (! apiPath.endsWith("/")) {
			apiPath += "/";
		}
		apiPath += "data/conversation";

		BasicHttpContext http = new BasicHttpContext();
		HttpGet httpGet = getGet(apiPath, http);
		
		CloseableHttpClient httpClient = HttpClients.createDefault();	
		CloseableHttpResponse httpResp = null;
		
		String json = null;
		
		try 
		{
			httpResp = httpClient.execute(httpGet);
			HttpEntity entity = httpResp.getEntity();
			json = EntityUtils.toString(entity);
			
			// map json to pojo using jackson databinding
			ObjectMapper mapper = new ObjectMapper();
			Conversation[] convArr = mapper.readValue(json, Conversation[].class);
			return convArr;
		} 
		finally {
			if (httpResp != null) {
				try { httpResp.close(); } catch (Exception x) {}
			}
			if (httpClient != null) {
				try { httpClient.close(); } catch (Exception x) {}
			}
		}		
		
	}

	private Conversation _getConversation(String apiPath) throws ParseException, IOException, AuthenticationException, URISyntaxException
	{
		BasicHttpContext http = new BasicHttpContext();
		HttpGet httpGet = getGet(apiPath, http);
		
		CloseableHttpClient httpClient = HttpClients.createDefault();	
		CloseableHttpResponse httpResp = null;
		
		String json = null;
		
		try 
		{
			httpResp = httpClient.execute(httpGet);
			HttpEntity entity = httpResp.getEntity();
			json = EntityUtils.toString(entity);
			
			// map json to pojo using jackson databinding
			ObjectMapper mapper = new ObjectMapper();
			Conversation conv = mapper.readValue(json, Conversation.class);
			return conv;
		} 
		finally {
			if (httpResp != null) {
				try { httpResp.close(); } catch (Exception x) {}
			}
			if (httpClient != null) {
				try { httpClient.close(); } catch (Exception x) {}
			}
		}				
	}

	public Conversation getConversation(long id) throws ParseException, IOException, AuthenticationException, URISyntaxException
	{
		// construct api path for entry point
		String apiPath = apiBaseURL.getPath();
		if (! apiPath.endsWith("/")) {
			apiPath += "/";
		}
		apiPath += "data/conversation/id/" + id;

		Conversation conv = _getConversation(apiPath);
		return conv;
	}

	public Conversation getConversationByName(String name) throws ParseException, IOException, AuthenticationException, URISyntaxException
	{
		// construct api path for entry point
		String apiPath = apiBaseURL.getPath();
		if (! apiPath.endsWith("/")) {
			apiPath += "/";
		}
		apiPath += "data/conversation/name/" + name;

		Conversation conv = _getConversation(apiPath);
		return conv;
	}
	
	
	public String getEvent(long id) 
			throws AuthenticationException, URISyntaxException, ClientProtocolException, IOException
	{
		// construct api path for entry point
		String apiPath = apiBaseURL.getPath();
		if (! apiPath.endsWith("/")) {
			apiPath += "/";
		}
		apiPath += "data/event/id/" + id;

		BasicHttpContext http = new BasicHttpContext();
		HttpGet httpGet = getGet(apiPath, http);
		
		CloseableHttpClient httpClient = HttpClients.createDefault();	
		CloseableHttpResponse httpResp = null;
		
		String event = null;
		
		try 
		{
			httpResp = httpClient.execute(httpGet);
			HttpEntity entity = httpResp.getEntity();
			event = EntityUtils.toString(entity);
			return event;			
		} 
		finally {
			if (httpResp != null) {
				try { httpResp.close(); } catch (Exception x) {}
			}
			if (httpClient != null) {
				try { httpClient.close(); } catch (Exception x) {}
			}
		}		
	}
	
	public Event addEvent(Event event)
			throws AuthenticationException, URISyntaxException, ClientProtocolException, IOException
	{
		// construct api path for entry point
		String apiPath = apiBaseURL.getPath();
		if (! apiPath.endsWith("/")) {
			apiPath += "/";
		}
		apiPath += "data/event";
		
		BasicHttpContext http = new BasicHttpContext();
		HttpPost httpPost = getPost(apiPath, http);
				
		// map pojo to json using jackson databinding
		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(event);

		// store json in http request
		StringEntity stringEntity = new StringEntity(json);
		stringEntity.setContentType("application/json");
		httpPost.setEntity(stringEntity);
		
		CloseableHttpClient httpClient = HttpClients.createDefault();	
		CloseableHttpResponse httpResp = null;

		try 
		{
			httpResp = httpClient.execute(httpPost);
			HttpEntity entity = httpResp.getEntity();
			json = EntityUtils.toString(entity);

			// update call returns new customer object
			Event event1 = mapper.readValue(json, Event.class);
			return event1;
		} 
		finally {
			if (httpResp != null) {
				try { httpResp.close(); } catch (Exception x) {}
			}
			if (httpClient != null) {
				try { httpClient.close(); } catch (Exception x) {}
			}
		}				
	}

}
