package com.example.store.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.os.Handler;

public class Network_util2 extends Thread {

	public static final int HTTP_ID_GET_NOE_PHOTO = 11;

	private int http_id;
	private String url;
	private String http_methed = "GET";
	private String parameter = "";
	private int connection_out = 5000;
	private int read_out = 5000;

	private Handler handler;

	private String resultString;

	public Network_util2(Handler handler, int http_id, String url,
			String http_methed, String parameter, int connection_out,
			int read_out) {
		this.handler = handler;
		this.http_id = http_id;
		this.url = url;
		this.http_methed = http_methed.toUpperCase();
		this.parameter = parameter;
		this.connection_out = connection_out;
		this.read_out = read_out;
		resultString = null;
	}

	public Network_util2(int http_id, String url, String http_methed,
			String parameter) {
		this.http_id = http_id;
		this.url = url;
		this.http_methed = http_methed;
		this.parameter = parameter;
	}

	public Network_util2(int http_id, String url) {
		this.http_id = http_id;
		this.url = url;
	}

	@Override
	public void run() 
	{
		// TODO Auto-generated method stub
		try {
			HttpURLConnection connection = (HttpURLConnection) new URL(url)
					.openConnection();
			connection.setConnectTimeout(connection_out);
			connection.setReadTimeout(read_out);
			connection.setRequestMethod(http_methed);
			if ("POSET".equals(http_methed)) {
				OutputStream outputStream = connection.getOutputStream();
				outputStream.write(parameter.getBytes());
			}
			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(connection.getInputStream()));
			StringBuffer sb = new StringBuffer();
			String line = null;
			while ((line = bufferedReader.readLine()) != null) {
				sb.append(line);
			}
			resultString = sb.toString();
			handler.sendMessage(handler.obtainMessage(http_id, resultString));
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}