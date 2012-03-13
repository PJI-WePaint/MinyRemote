package miny.remote;
/*
Miny - AndroidRemote

Copyright (c) 2011 LIFL - UniversitŽ Lille 1

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
import android.app.Activity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class HomePositionning {
	private static final int startX = 10;
	private static final int startY = 10;
	
	static int[] buttonIDs= { R.id.imageTextMessage, R.id.imageVocalRecognition, R.id.imageQrCode, R.id.imageGPS, R.id.imageCompass, R.id.imageAccelerometer, R.id.imageLight, R.id.imageQrConfig };
	static int horizontalMargin = 10, verticalMargin = 10;
	
	public static void positionning (int buttonWidth, int buttonHeigth, Activity activity) {
		Display display=activity.getWindowManager().getDefaultDisplay();
		int mainWidth = display.getWidth();
		int x= startX, y = startY;
		int maxHeight=0;
		
		for (int buttonId : buttonIDs) {
			ImageView imageButton=(ImageView) activity.findViewById(buttonId);
			
			// If the new button is too far on the right
			// Then starts a new line
			if (x+buttonWidth>=mainWidth) {
				y=y + maxHeight + verticalMargin;
				x=startX;
				maxHeight = 0;
			}
			
			// update the maximum height of this line
			maxHeight = Math.max(maxHeight, buttonHeigth);
			
			// Set the margin for the top left
			RelativeLayout.LayoutParams marginParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			marginParams.setMargins(x, y, 0, 0);
			imageButton.setLayoutParams(marginParams);
			
			// update the position of x
			x+=buttonWidth+horizontalMargin;
		}
	}
}
