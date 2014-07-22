package com.example.setalarm;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener{

	Button b1;
	Button b2;
	Button b3;
	Button b4;
	Button b5;
	Button b6;
	Button b7_issue53;
	Button b8_issue53;
	Button b9_issue53_continues;
	TextView t1;
	TextView t2;
	TextView t3_hotfix;
	TextView t4_hotfix;
	int period = 30;
	int count = 0;
	BroadcastReceiver receiver;
	AlarmManager manager;
	PendingIntent pintent;
	WifiManager wifiManager;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		b1 = (Button) findViewById(R.id.button1);
		b1.setOnClickListener(this);
		b2 = (Button) findViewById(R.id.button2);
		b2.setOnClickListener(this);
		t1 = (TextView)findViewById(R.id.textView1);
		t2 = (TextView)findViewById(R.id.textView2);
		wifiManager = (WifiManager) this.getSystemService(Context.WIFI_SERVICE); 
		final TelephonyManager telephonyManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
		receiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) 
			{
				
				count++;
				// TODO Auto-generated method stub
				
				
				/* 
				if(wifiManager.isWifiEnabled())
				{
					wifiManager.setWifiEnabled(false);
					t1.setText("turn off wifi..." + count);
				}else
				{
					wifiManager.setWifiEnabled(true);
					t1.setText("turn on wifi..." + count);
				}*/
				
				boolean isEnabled;
				if(telephonyManager.getDataState() == TelephonyManager.DATA_CONNECTED)
				{
			        isEnabled = true;
			    }else{
			        isEnabled = false;  
			    }  
				
				try 
				{
					Method dataConnSwitchmethod;
					Class telephonyManagerClass;
					Object ITelephonyStub;
					Class ITelephonyClass;
					Method getITelephonyMethod;
					telephonyManagerClass = Class.forName(telephonyManager.getClass().getName());
					getITelephonyMethod = telephonyManagerClass.getDeclaredMethod("getITelephony");
					getITelephonyMethod.setAccessible(true);
					ITelephonyStub = getITelephonyMethod.invoke(telephonyManager);
					ITelephonyClass = Class.forName(ITelephonyStub.getClass().getName());
					
					if (isEnabled) 
					{
				        dataConnSwitchmethod = ITelephonyClass.getDeclaredMethod("disableDataConnectivity");
				        dataConnSwitchmethod.setAccessible(true);
					    dataConnSwitchmethod.invoke(ITelephonyStub);
					    t1.setText("turn off data..." + count);
					    t1.setBackgroundColor( Color.RED );
				        dataConnSwitchmethod = ITelephonyClass.getDeclaredMethod("enableDataConnectivity"); 
				        t1.setText("turn on data..." + count);
				        t1.setBackgroundColor( Color.GREEN );
				        
				        
				        dataConnSwitchmethod.setAccessible(true);
					    dataConnSwitchmethod.invoke(ITelephonyStub);
				        
					}/* else {
				        dataConnSwitchmethod = ITelephonyClass.getDeclaredMethod("enableDataConnectivity"); 
				        t1.setText("turn on data..." + count);
				        t1.setBackgroundColor( Color.GREEN );
				    }*/
				    //dataConnSwitchmethod.setAccessible(true);
				    //dataConnSwitchmethod.invoke(ITelephonyStub);
					
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				

				
				

				manager.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + 1000*period, pintent );
			}
		 };
		 
		 this.registerReceiver( receiver, new IntentFilter("com.blah.blah.somemessage") );
		 pintent = PendingIntent.getBroadcast( this, 0, new Intent("com.blah.blah.somemessage"), 0 );
		 
		 manager = (AlarmManager)(this.getSystemService( Context.ALARM_SERVICE ));
		 
	}
	
	@Override
	protected void onDestroy() 
	{
		super.onDestroy();
		this.unregisterReceiver(receiver);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View arg0) 
	{
		switch(arg0.getId())
		{
			case R.id.button1:
			{
				 //t1.setText("Alarm starting....");
				// Put here YOUR code.
	             Toast.makeText(this, "Alarm starting", Toast.LENGTH_LONG).show(); // For example
	             
	             // set alarm to fire 5 sec (1000*5) from now (SystemClock.elapsedRealtime())
	             manager.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + 1000*period, pintent );
	             
	             t2.setText("Alarm scheduling....");
			}break;
			case R.id.button2:
			{
				//t1.setText("Alarm Stop....");
				Toast.makeText(this, "Alarm stop", Toast.LENGTH_LONG).show(); // For example
				
				manager.cancel(pintent);
				
				 t2.setText("Alarm stop....");
			}break;
			
		}
	}
}
