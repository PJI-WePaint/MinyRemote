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

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;

public class AccelerometerLoop extends SensorLoop {
	public void initValues() {
		sensorName="accelerometer"; 
		selectedDelta=2;
		titleContent="Accelerometer Events !"; 
	}
	
	protected void setSensor() {
		
		sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
    	List<Sensor> sensors =sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER);
    	if (sensors.size() > 0) {
        	sensor = sensors.get(0);
    	}
        
	}
	
	protected void sendActionToWSE() {
		Communicator.minyDriver.accelerometer(getPreviousZ(), getPreviousY(), getPreviousX());
	}
}
