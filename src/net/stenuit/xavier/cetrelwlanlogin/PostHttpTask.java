package net.stenuit.xavier.cetrelwlanlogin;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;

import android.content.res.Resources;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

public class PostHttpTask extends AsyncTask {

	private TextView textView;
	@Override
	protected Object doInBackground(Object... arg0) {
		Resources res=(Resources)arg0[0];
		KeyStore localKeyStore;
		HttpResponse response=null;
		textView=(TextView)arg0[1];
		String login=(String)arg0[2];
		String password=(String)arg0[3];
		
		try
		{
			Log.d(getClass().getName(),"Sending credentials to server : "+login+" "+password);
			
			localKeyStore=KeyStore.getInstance("BKS");
			InputStream in=res.openRawResource(R.raw.mykeystore);
			localKeyStore.load(in,"password".toCharArray());
			
			SchemeRegistry schemeRegistry = new SchemeRegistry();
			schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
			org.apache.http.conn.ssl.SSLSocketFactory sslSocketFactory = new org.apache.http.conn.ssl.SSLSocketFactory(localKeyStore);
			schemeRegistry.register(new Scheme("https", sslSocketFactory, 443));
			HttpParams params = new BasicHttpParams();
			ClientConnectionManager cm = new ThreadSafeClientConnManager(params, schemeRegistry);

			HttpClient client = new DefaultHttpClient(cm, params);

			// textView.append("creating post object");
			HttpPost post=new HttpPost("https://1.1.1.1/login.html");
			// textView.append("creating the values");
			List<NameValuePair> pairs=new ArrayList<NameValuePair>();
			/*
			 * username:login
			 * redirect_url:http://www.lecho.be/
			 * password:password
			 * info_msg:
			 * info_flag:0
			 * err_msg:
			 * err_flag:0
			 * buttonClicked:4
			 */
			pairs.add(new BasicNameValuePair("username", login));
			pairs.add(new BasicNameValuePair("redirect_url", "http://m.lecho.be"));
			pairs.add(new BasicNameValuePair("password", password));
			pairs.add(new BasicNameValuePair("info_msg", ""));
			pairs.add(new BasicNameValuePair("info_flag", "0"));
			pairs.add(new BasicNameValuePair("err_msg", ""));
			pairs.add(new BasicNameValuePair("err_flag", "0"));
			pairs.add(new BasicNameValuePair("buttonClicked", "4"));

			// textView.append("Posting everyting");
			post.setEntity(new UrlEncodedFormEntity(pairs));

		
			response=client.execute(post);
			// textView.append("Status code : "+response.getStatusLine().getStatusCode());
		
		}
		catch(Exception e)
		{
			Log.e(getClass().getName(),"Exception thrown",e);
		}
		return response;
	}

	@Override
	protected void onPostExecute(Object result) {
		if(result==null)
			result="null";
		
		Log.d(getClass().getName(),"On Post execute, argument is a "+result.getClass().getName());
		
		if(result instanceof HttpResponse)
		{
			HttpResponse resp=(HttpResponse)result;
			textView.append("Status code : "+resp.getStatusLine().getStatusCode()+"\n");
		}
		else
			textView.append("Error\n");
	}

	
}
