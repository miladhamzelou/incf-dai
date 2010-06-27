package org.incf.atlas.aba.util;

import java.io.IOException;
import java.net.URI;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

public class HTTPClient {
	
	public HTTPClient() {
		
	}
	
	public String executeGet() {
		
		
		return null;
	}

	private HttpResponse getResponse(URI uri) 
			throws ClientProtocolException, IOException {
		HttpClient httpclient = new DefaultHttpClient();
		HttpGet httpget = new HttpGet(uri);
		return httpclient.execute(httpget);
	}

}
