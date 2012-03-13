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
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class SensorLoop extends Activity {
	protected SensorManager sensorManager;
	protected Sensor sensor;
	protected int previousX=0, previousY=0, previousZ=0;
	protected boolean loopMode=false;
	protected EditText defaultDelta;
	protected Button loopButton;
	protected TextView xValue, yValue, zValue;
	protected TextView title;
	protected String sensorName=""; // example = "compass"
	protected int selectedDelta=10;
	protected String titleContent=""; // example = "Compass Events !"
	
	private final SensorEventListener sensorListener = new SensorEventListener()         {
		public void onSensorChanged(SensorEvent event) {
			int currentX=(int) event.values[SensorManager.DATA_X];
			int currentY=(int) event.values[SensorManager.DATA_Y];
			int currentZ=(int) event.values[SensorManager.DATA_Z];
			displayX(currentX);
			displayY(currentY);
			displayZ(currentZ);
			if (isLoopMode()) {
				if (delta(currentX, currentY, currentZ)) {
					setPreviousX(currentX);
					setPreviousY(currentY);
					setPreviousZ(currentZ);
					sendActionToWSE();
				}
			}
			else {
				setPreviousX(currentX);
				setPreviousY(currentY);
				setPreviousZ(currentZ);
			}
		}

		

		public void onAccuracyChanged(Sensor sensor, int accuracy) {}
	};
	
	private void displayZ(int currentZ) {
		zValue.setText(currentZ+"");
	}

	protected void displayY(int currentY) {
		yValue.setText(currentY+"");
	}

	private void displayX(int currentX) {
		xValue.setText(currentX+"");
	}
	// =================================================================================
	//
	// METHODS TO CHANGE
	//
	
	public void initValues() {
		/*sensorName="compass"; 
		selectedDelta=10;
		titleContent="Compass Events !";*/ 
	}
	
	protected void setSensor() {
		/*
		sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
    	List<Sensor> sensors =sensorManager.getSensorList(Sensor.TYPE_ORIENTATION);
    	if (sensors.size() > 0) {
        	sensor = sensors.get(0);
        }*/
	}
	
	protected void sendActionToWSE() {
		//Communicator.minyDriver.compass(getPreviousZ(), getPreviousY(), getPreviousX());
	}

	 // =================================================================================
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sensor_loop_layout);
        initValues();
        loopButton=(Button)this.findViewById(R.id.LoopButton);
        title=(TextView)this.findViewById(R.id.title);
        title.setText(titleContent);
        setSensor();
        this.listenSensorEvents();
        behaviouriseDeltaTextEdit();
        getIndicatorValueViews();
 	}

	private void getIndicatorValueViews() {
		xValue=(TextView)this.findViewById(R.id.xValue);
        yValue=(TextView)this.findViewById(R.id.yValue);
        zValue=(TextView)this.findViewById(R.id.zValue);
	}



	private void behaviouriseDeltaTextEdit() {
		defaultDelta=(EditText)this.findViewById(R.id.DefaultDeltaValueEdit);
		defaultDelta.setText(selectedDelta+"");
        defaultDelta.addTextChangedListener(new TextWatcher() {

			public void afterTextChanged(Editable s) {
				setSelectedDelta(Integer.parseInt(defaultDelta.getText()+""));
		    }

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {}

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {}
        	
        });
	}
	
	protected void onStop() {
		super.onStop();
		this.unlistenSensorEvents();
	}
	
	protected void onResume() {
		super.onResume();
		this.listenSensorEvents();
	}

	public void listenSensorEvents () {
		sensorManager.registerListener(sensorListener, sensor, SensorManager.SENSOR_DELAY_NORMAL);
	}
	
	public void unlistenSensorEvents () {
		sensorManager.unregisterListener(sensorListener);
	}
	
	public int getPreviousX() {
		return previousX;
	}

	public void setPreviousX(int previousX) {
		this.previousX = previousX;
	}

	public int getPreviousY() {
		return previousY;
	}

	public void setPreviousY(int previousY) {
		this.previousY = previousY;
	}

	public int getPreviousZ() {
		return previousZ;
	}

	public void setPreviousZ(int previousZ) {
		this.previousZ = previousZ;
	}
	
	public int deltaX(int currentX) {
		return Math.abs(Math.abs(previousX)-Math.abs(currentX));
	}
	
	public int deltaY(int currentY) {
		return Math.abs(Math.abs(previousY)-Math.abs(currentY));
	}
	
	public int deltaZ(int currentZ) {
		return Math.abs(Math.abs(previousZ)-Math.abs(currentZ));
	}
	
	public boolean delta(int currentX, int currentY, int currentZ) {
		return (deltaX(currentX)>=getSelectedDelta() ||
				deltaY(currentY)>=getSelectedDelta() ||
				deltaZ(currentZ)>=getSelectedDelta() );
	}
	
	public boolean isLoopMode() { return loopMode; }
	
	public int getSelectedDelta () { return selectedDelta; }

	public void loopClicked(View view) {
		if (!loopMode) {
			loopMode=true;
			loopButtonDisplaysStop();
		}
		else {
			loopMode=false;
			loopButtonDisplaysStart();
		}
	}

	private void loopButtonDisplaysStart() {
		loopButton.setText("Start sending "+sensorName+" events");
	}

	private void loopButtonDisplaysStop() {
		loopButton.setText("Stop sending "+sensorName+" events");
	}

	public void setSelectedDelta(int selectedDelta) {
		this.selectedDelta = selectedDelta;
	}

}
