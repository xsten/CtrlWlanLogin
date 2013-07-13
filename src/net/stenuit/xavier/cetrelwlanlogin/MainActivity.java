package net.stenuit.xavier.cetrelwlanlogin;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	public static final String SettingsFile="settings.dat";
	Intent settingsIntent=null;
	public static Intent intent=null;
	private String login;
	private String password;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		
		TextView v=(TextView)findViewById(R.id.textView1);
		
		// reads version name from manifest file
		try {
			String versionName = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
			v.append("Cetrel WLAN login - v"+versionName+"\n");
		} catch (NameNotFoundException e) {
			Log.e(getClass().getName(),"Exception thrown",e);
		}
		
		
		
		// reads the wifi access point name
		try
		{
			WifiManager wifiManager=(WifiManager)getSystemService(Context.WIFI_SERVICE);
			WifiInfo wifiInfo=wifiManager.getConnectionInfo();
			String ssid=wifiInfo.getSSID();
			v.append("Wifi interface is connected to "+ssid+"\n");
		}
		catch(Exception e)
		{
			Log.e(getClass().getName(),"Exception thrown",e);
			v.append("Problem with WiFi - are you connected to Cetrel WLAN ?\n");
		}
		
		// Creates another intent for settings screen
		settingsIntent=new Intent(this,SettingsActivity.class);
		intent=this.getIntent();
		
		// Reads login and password from file
		readLoginFromFile();
	}

	private void readLoginFromFile() {
		try
		{
			// TODO : find a way to share the filename between SettingsActivity and MainActivity
/*			BufferedReader reader=new BufferedReader(new FileReader(SettingsActivity.SettingsFile));
			setLogin(reader.readLine());
			setPassword(reader.readLine());*/
		}
		catch(Exception e)
		{
			Log.e(getClass().getName(),"Exception thrown",e);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}



	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		TextView v=(TextView)findViewById(R.id.textView1);
		v.append("Menu Item "+item.getTitleCondensed()+" has been selected \n");
		
		if("setLogin".equals(item.getTitleCondensed()))
		{
			startActivity(settingsIntent);
			Log.i(getClass().getName(),"settingsIntent finished"); // seems never reached
		}
		
		return false; // TODO : what if we return true ???
	}

	public void ConnectToWlanClicked(View v)
	{

		
		getTextView(v).append("Connecting to WLAN\n");
		readLoginFromFile(); // in case the login has been changed from the settings menu
		
		// HttpResponse resp=(HttpResponse)new PostHttpTask().execute(getResources(),getTextView(v));
		PostHttpTask tsk=new PostHttpTask();
		tsk.execute(getResources(),getTextView(v),getLogin(),getPassword());
	}
	
	public void ExitClicked(View v)
	{
		finish();
	}
	
	private TextView getTextView(View v)
	{
		View parent=(View) v.getParent();
		return (TextView)parent.findViewById(R.id.textView1);
	}
	
	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}


}
