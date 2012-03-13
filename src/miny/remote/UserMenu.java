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
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

public class UserMenu {
	
	public final static int PREFERENCES = 1;
	public final static int CONNECT = 2;
	Home home;

	public UserMenu(Home activity) {
		this.home=activity;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()){

			case PREFERENCES:
				Intent myIntent = new Intent(home, Preferences.class);
				home.startActivity(myIntent);
				return true;

			case CONNECT:
				home.connectToWSE();
				return true;
		}
		return false;
	}
	
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuItem item = null;
		item=menu.add(Menu.NONE,PREFERENCES,Menu.FIRST,"WSE server preferences");
		item.setIcon(R.drawable.server);
		item=menu.add(Menu.NONE,CONNECT,Menu.FIRST,"Connect to WSE");
		item.setIcon(R.drawable.connect);
		return true;
	}

}
