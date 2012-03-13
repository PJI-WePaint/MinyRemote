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
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Configuration extends Activity {

	private static final int REQUEST_SCAN = 0;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.configuration);
        ((EditText)findViewById(R.id.ConfigurationServerUrl)).setText(Communicator.urlServer);
		((EditText)findViewById(R.id.ConfigurationSessionName)).setText(Communicator.sessionName);
		((EditText)findViewById(R.id.ConfigurationLocation)).setText(Communicator.location);
		((EditText)findViewById(R.id.ConfigurationLocationParams)).setText(Communicator.locationParams);
        behavioriseApplyButton();
        behavioriseScanButton();
	}
	
	protected void onStop() {
		super.onStop();
	}
	
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
	
	public void apply () {
		Communicator.urlServer=((EditText)findViewById(R.id.ConfigurationServerUrl)).getText()+"";
		Communicator.sessionName=((EditText)findViewById(R.id.ConfigurationSessionName)).getText()+"";
		Communicator.location=((EditText)findViewById(R.id.ConfigurationLocation)).getText()+"";
		Communicator.locationParams=((EditText)findViewById(R.id.ConfigurationLocationParams)).getText()+"";
		Communicator.initMinyDriver(null);
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
		Editor editor = preferences.edit();
		editor.putString("serverUrl", ((EditText)findViewById(R.id.ConfigurationServerUrl)).getText()+"");
		editor.putString("sessionName", ((EditText)findViewById(R.id.ConfigurationSessionName)).getText()+"");
		editor.putString("location", ((EditText)findViewById(R.id.ConfigurationLocation)).getText()+"");
		editor.putString("locationParameters", ((EditText)findViewById(R.id.ConfigurationLocationParams)).getText()+"");
		editor.commit();
	}
	
	private void behavioriseApplyButton() {
		Button apply=(Button) findViewById(R.id.ConfigurationApply);
		apply.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				apply();
				Configuration.this.finish();
			}
        });
	}
	
	private void behavioriseScanButton() {
		Button scan=(Button) findViewById(R.id.ConfigurationScan);
		scan.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				startQRCodeScan();
				//((EditText)findViewById(R.id.ConfigurationServerUrl)).setText("coucou");
			}
        });
	}
	private void startQRCodeScan() 
		{
			Intent intent = new Intent("com.google.zxing.client.android.SCAN");
			intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
			startActivityForResult(intent, REQUEST_SCAN);
		}
	
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (requestCode == 0) {
	        if (resultCode == RESULT_OK) {
	            String contents = intent.getStringExtra("SCAN_RESULT");
	        	JSONObject jsonContents;
				try {
					jsonContents = new JSONObject(contents);
					((EditText)findViewById(R.id.ConfigurationServerUrl)).setText(jsonContents.getString("url"));
					((EditText)findViewById(R.id.ConfigurationSessionName)).setText(jsonContents.getString("sessionName"));
					((EditText)findViewById(R.id.ConfigurationLocation)).setText(jsonContents.getString("_location"));
					((EditText)findViewById(R.id.ConfigurationLocationParams)).setText(jsonContents.getString("location_parameter"));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        } else if (resultCode == RESULT_CANCELED) {
	            // Handle cancel
	        }
	    }
	}
}
