package miny.remote;
/*
Miny - AndroidRemote

Copyright (c) 2011 LIFL - Université Lille 1

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA

If you have any questions or comments, please email:

Le Pallec Xavier
xavier.le-pallec@univ-lille1.fr
http://www.lifl.fr/~lepallex

*/
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class Home extends Activity {
	
	private static final int REQUEST_SCAN = 0;
	
	private static final int VOICE_RECOGNITION_REQUEST_CODE = 1234;
	
	UserMenu userMenu;
	
	@Override
	protected void onResume() {
		super.onStart();
		HomePositionning.positionning(94,90,this);
        userMenu=new UserMenu(this);
	}

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alternative_home);
    }

	//
	// RESPONSES TO CLICK
	//
	
	public void textMessageClicked (View view) {
		if (notConnected_ShowMessage())
			return;
		startTextMessageActivity();
	}

	public void compassClicked (View view) {
		if (notConnected_ShowMessage())
			return;
		startCompassActivity();
	}

	public void qrConfigClicked (View view) {
		startConfigActivity();
	}	

	public void gpsClicked (View view) {
		if (notConnected_ShowMessage())
			return;
		
	}

	public void vocalRecognitionClicked (View view) {
		if (notConnected_ShowMessage())
			return;
		startVocalRecognition();
	}

	

	public void qrCodeClicked (View view) {
		if (notConnected_ShowMessage())
			return;
		startQRCodeScan();
	}

	

	public void accelerometerClicked (View view) {
		if (notConnected_ShowMessage())
			return;
		startAccelerometerActivity();
	}

	public void lightClicked (View view) {
		if (notConnected_ShowMessage())
			return;
		
	}

	//
	// ACTUAL FUNCTIONS
	//

	void connectToWSE() {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
		Communicator.urlServer = preferences.getString("serverUrl", "");
		Communicator.sessionName = preferences.getString("sessionName", "");
		if (Communicator.urlServer.equals("")) {
			Toast.makeText(this, "Url of server is not indicated. Please set it in preferences",
					Toast.LENGTH_LONG).show();
			return;
		}
		if (Communicator.sessionName.equals("")) {
			Toast.makeText(this, "Session name is not indicated. Please set it in preferences",
					Toast.LENGTH_LONG).show();
			return;
		}	
		Communicator.location = preferences.getString("location", "");
		Communicator.locationParams = preferences.getString("locationParameters", "");
		Toast.makeText(this, "Connected with value "+Communicator.urlServer+" , "+Communicator.sessionName+" , "+Communicator.location+" , "+Communicator.locationParams,
				Toast.LENGTH_LONG).show();
		
		Vibrator vibrator = (Vibrator) getSystemService(android.content.Context.VIBRATOR_SERVICE);
		vibrator.vibrate(300);
		
		Communicator.initMinyDriver(vibrator);
		Communicator.minyDriver.start();
		
	}

	
	
	public void onActivityResult(int reqCode, int resCode, Intent intent) {
	    if (REQUEST_SCAN == reqCode) {
	        qrCodeToWSE(resCode, intent);
	    }
	    if (reqCode == VOICE_RECOGNITION_REQUEST_CODE && resCode == RESULT_OK) {
            vocalRecognisedTextToWSE(intent);
        }
	}



	private void vocalRecognisedTextToWSE(Intent intent) {
		// Fill the list view with the strings the recognizer thought it could have heard
		ArrayList<String> matches = intent.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
		String allSentences="";
		for (String sentence : matches) {
			allSentences+=sentence;
		}
		Communicator.minyDriver.textRecognised(allSentences);
	}



	private void qrCodeToWSE(int resCode, Intent intent) {
		if (RESULT_OK == resCode) {
		    String contents = intent.getStringExtra("SCAN_RESULT");
		    Communicator.minyDriver.qrCode(contents);
		} else if (RESULT_CANCELED == resCode) {
			
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu _menu) {
		return userMenu.onCreateOptionsMenu(_menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return userMenu.onOptionsItemSelected(item);
	}



	private void startQRCodeScan() {
		Intent intent = new Intent("com.google.zxing.client.android.SCAN");
		intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
		try {
		  startActivityForResult(intent, REQUEST_SCAN);
      } catch (ActivityNotFoundException e) {
		 
      }
	}
	
	private void startVocalRecognition() {
		PackageManager pm = getPackageManager();
        List<ResolveInfo> activities = pm.queryIntentActivities(
                new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
        if (activities.size() == 0) {
        	Toast.makeText(this, "Speech recognition is not installed",
					Toast.LENGTH_LONG).show();
        }
        
        try {
        	Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                    RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say what you want");
            startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE);
        	
       } catch (ActivityNotFoundException e) {
         
       }
	}

	private void startCompassActivity() {
		Intent myIntent = new Intent(Home.this, CompassLoop.class);
		Home.this.startActivity(myIntent);
	}
	
	private void startAccelerometerActivity() {
		Intent myIntent = new Intent(Home.this, AccelerometerLoop.class);
		Home.this.startActivity(myIntent);
	}
	
	private void startTextMessageActivity() {
		Intent myIntent = new Intent(Home.this, SendMessage.class);
		Home.this.startActivity(myIntent);
	}
	
	private void startConfigActivity() {
		Intent myIntent = new Intent(Home.this, Configuration.class);
		Home.this.startActivity(myIntent);
	}
	
	private boolean notConnected_ShowMessage() {
		boolean connected = Communicator.minyDriver!=null;
		if (!connected)
			Toast.makeText(this, "You are not connected to a WSE session. See Menu button",
					Toast.LENGTH_LONG).show();
		return !connected;
	}


}