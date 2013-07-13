package net.stenuit.xavier.cetrelwlanlogin;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class SettingsActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
	}

	public void OkButtonClicked(View v)
	{
		Log.i(getClass().getName(), "OKButtonClicked called");
		
		try
		{
			BufferedWriter writer=new BufferedWriter(new FileWriter(MainActivity.SettingsFile,false));
			
			String login=((TextView)findViewById(R.id.loginField)).getText().toString();
			writer.write(login);
			writer.write("\n");
			String pwd=((EditText)findViewById(R.id.editText2)).getText().toString();
			Log.i(getClass().getName(),"Writing to file : "+login+","+pwd);
			writer.write(pwd);
			writer.write("\n");
			writer.flush();
			writer.close();
			
			Log.d(getClass().getName(),"File saved");
		}
		catch(Exception e)
		{
			Log.e(getClass().getName(),"Exception thrown",e);
		}
		
		finish();
		
	}
	
	public void CancelButtonClicked(View v)
	{
		Log.i(getClass().getName(), "CancelButtonClicked called");
		finish(); 
	}
}
