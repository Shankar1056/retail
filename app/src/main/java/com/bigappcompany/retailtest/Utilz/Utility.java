package com.bigappcompany.retailtest.Utilz;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.zip.GZIPInputStream;


public class Utility {
	
	private static HttpClient mHttpClient;
	private static final int HTTP_TIMEOUT = 60 * 1000; // milliseconds
	
	private static HttpClient getHttpClient() {
		if (mHttpClient == null) {
			mHttpClient = new DefaultHttpClient();
			final HttpParams params = mHttpClient.getParams();
			HttpConnectionParams.setConnectionTimeout(params, HTTP_TIMEOUT);
			HttpConnectionParams.setSoTimeout(params, HTTP_TIMEOUT);
			ConnManagerParams.setTimeout(params, HTTP_TIMEOUT);
		}
		return mHttpClient;
		
	}
	
	
	public static String executeHttpPost(String url,
                                         ArrayList<NameValuePair> postParameters) throws Exception {
		BufferedReader in = null;
		try {
			HttpClient client = getHttpClient();
			// HttpPost request = new HttpPost(url);
			
			HttpPost request = new HttpPost();
			request.setURI(new URI(url));
			
			UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(
			    postParameters);
			request.setEntity(formEntity);
			HttpResponse response = client.execute(request);
			in = new BufferedReader(new InputStreamReader(response.getEntity()
			    .getContent()));
			
			StringBuffer sb = new StringBuffer("");
			String line = "";
			String NL = System.getProperty("line.separator");
			while ((line = in.readLine()) != null) {
				sb.append(line).append(NL);
			}
			in.close();
			
			return sb.toString();
			//  return result;
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	public static String executeHttpGet(String url) throws Exception {
		InputStream in = null;
		try {
			HttpClient client = getHttpClient();
			HttpGet request = new HttpGet();
			request.addHeader("Accept-Encoding", "gzip");
			request.setURI(new URI(url));
			HttpResponse response = client.execute(request);
			/*in = new BufferedReader(new InputStreamReader(response.getEntity()
			    .getContent()));*/
			in= response.getEntity().getContent();
			Header contentEncoding = response.getFirstHeader("Content-Encoding");
			if (contentEncoding != null && contentEncoding.getValue().equalsIgnoreCase("gzip")) {
				try {
					in = new GZIPInputStream(in);
				}
				catch (Exception e){
					e.printStackTrace();
				}
			}

			BufferedReader rd = new BufferedReader(new InputStreamReader(in));

			StringBuffer sb = new StringBuffer("");
			String line = "";
			String NL = System.getProperty("line.separator");
			while ((line = rd.readLine()) != null) {
				sb.append(line).append(NL);
			}
			in.close();
			
			return sb.toString();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}




	public static Boolean isInternetAvailable(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		if (null != activeNetwork) {
			if(activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
				return true;

			if(activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
				return true;
		}
		return false;
	}



	private static ProgressDialog dialog;
	
	public static void showDailog(Context c, String msg) {
		dialog = new ProgressDialog(c);
		dialog.setCanceledOnTouchOutside(true);
		dialog.setMessage(msg);
		dialog.show();
	}

	public static void closeDialog() {
		if (dialog != null)
			dialog.cancel();
	}
	

	

	public static String getUniqueImageName(){
		//will generate a random num
		//between 15-10000
		Random r = new Random();
		int num = r.nextInt(10000 - 15) + 15;
		String fileName = "img_"+num+".png";
		return  fileName;
	}
	
	public static int round(Double total, int number) {
		if (number < 0) throw new IllegalArgumentException();
		
		long factor = (long) Math.pow(10, number);
		total = total * factor;
		long tmp = Math.round(total);
		return (int)(tmp / factor);
	}

}
