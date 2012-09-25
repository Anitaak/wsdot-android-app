/*
 * Copyright (c) 2012 Washington State Department of Transportation
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 *
 */

package gov.wa.wsdot.android.wsdot.provider;

import gov.wa.wsdot.android.wsdot.provider.WSDOTContract.CachesColumns;
import gov.wa.wsdot.android.wsdot.provider.WSDOTContract.CamerasColumns;
import gov.wa.wsdot.android.wsdot.provider.WSDOTContract.HighwayAlertsColumns;
import gov.wa.wsdot.android.wsdot.provider.WSDOTContract.MountainPassesColumns;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

public class WSDOTDatabase extends SQLiteOpenHelper {

	private static final String DEBUG_TAG = "WSDOTDatabase";
	private static final String DATABASE_NAME = "wsdot.db";
    private static final int DATABASE_VERSION = 1;

    interface Tables {
    	String CACHES = "caches";
        String CAMERAS = "cameras";
        String HIGHWAY_ALERTS = "highway_alerts";
        String MOUNTAIN_PASSES = "mountain_passes";
    }
    
	public WSDOTDatabase(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + Tables.CACHES + " ("
        		+ BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + CachesColumns.CACHE_TABLE_NAME + " TEXT,"
                + CachesColumns.CACHE_LAST_UPDATED + " INTEGER);");
		
		db.execSQL("CREATE TABLE " + Tables.CAMERAS + " ("
        		+ BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
        		+ CamerasColumns.CAMERA_ID + " INTEGER,"
                + CamerasColumns.CAMERA_TITLE + " TEXT,"
                + CamerasColumns.CAMERA_URL + " TEXT,"
                + CamerasColumns.CAMERA_LATITUDE + " REAL,"
                + CamerasColumns.CAMERA_LONGITUDE + " REAL,"
                + CamerasColumns.CAMERA_HAS_VIDEO + " INTEGER NOT NULL default 0,"
                + CamerasColumns.CAMERA_ROAD_NAME + " TEXT,"
                + CamerasColumns.CAMERA_IS_STARRED + " INTEGER NOT NULL default 0);");
		
        db.execSQL("CREATE TABLE " + Tables.HIGHWAY_ALERTS + " ("
        		+ BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
        		+ HighwayAlertsColumns.HIGHWAY_ALERT_ID + " INTEGER,"
                + HighwayAlertsColumns.HIGHWAY_ALERT_HEADLINE + " TEXT,"
                + HighwayAlertsColumns.HIGHWAY_ALERT_LATITUDE + " REAL,"
                + HighwayAlertsColumns.HIGHWAY_ALERT_LONGITUDE + " REAL,"
                + HighwayAlertsColumns.HIGHWAY_ALERT_CATEGORY + " TEXT,"
                + HighwayAlertsColumns.HIGHWAY_ALERT_PRIORITY + " TEXT,"
                + HighwayAlertsColumns.HIGHWAY_ALERT_ROAD_NAME + " TEXT);");

        db.execSQL("CREATE TABLE " + Tables.MOUNTAIN_PASSES + " ("
        		+ BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + MountainPassesColumns.MOUNTAIN_PASS_ID + " INTEGER,"
                + MountainPassesColumns.MOUNTAIN_PASS_NAME + " TEXT,"
                + MountainPassesColumns.MOUNTAIN_PASS_WEATHER_CONDITION + " TEXT,"
                + MountainPassesColumns.MOUNTAIN_PASS_ELEVATION + " TEXT,"
                + MountainPassesColumns.MOUNTAIN_PASS_TRAVEL_ADVISORY_ACTIVE + " TEXT,"
                + MountainPassesColumns.MOUNTAIN_PASS_ROAD_CONDITION + " TEXT,"
                + MountainPassesColumns.MOUNTAIN_PASS_TEMPERATURE + " TEXT,"
                + MountainPassesColumns.MOUNTAIN_PASS_DATE_UPDATED + " TEXT,"
                + MountainPassesColumns.MOUNTAIN_PASS_RESTRICTION_ONE + " TEXT,"
                + MountainPassesColumns.MOUNTAIN_PASS_RESTRICTION_ONE_DIRECTION + " TEXT,"
                + MountainPassesColumns.MOUNTAIN_PASS_RESTRICTION_TWO + " TEXT,"
                + MountainPassesColumns.MOUNTAIN_PASS_RESTRICTION_TWO_DIRECTION + " TEXT,"
                + MountainPassesColumns.MOUNTAIN_PASS_CAMERA + " TEXT,"
                + MountainPassesColumns.MOUNTAIN_PASS_FORECAST + " TEXT,"
                + MountainPassesColumns.MOUNTAIN_PASS_WEATHER_ICON + " INTEGER,"
                + MountainPassesColumns.MOUNTAIN_PASS_IS_STARRED + " INTEGER NOT NULL default 0);");
        
        seedData(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.d(DEBUG_TAG, "onUpgrade() from " + oldVersion + " to " + newVersion);
		db.execSQL("DROP TABLE IF EXISTS " + Tables.CACHES);
		db.execSQL("DROP TABLE IF EXISTS " + Tables.CAMERAS);
		db.execSQL("DROP TABLE IF EXISTS " + Tables.HIGHWAY_ALERTS);
		db.execSQL("DROP TABLE IF EXISTS " + Tables.MOUNTAIN_PASSES);
        
        onCreate(db);		
	}
	
	private void seedData(SQLiteDatabase db) {
		db.execSQL("insert into caches (cache_table_name, cache_last_updated) values ('cameras', 0);");
		db.execSQL("insert into caches (cache_table_name, cache_last_updated) values ('highway_alerts', 0);");
		db.execSQL("insert into caches (cache_table_name, cache_last_updated) values ('mountain_passes', 0);");

		// Front load the mountain pass cameras
		db.execSQL("insert into cameras (id, title, url, latitude, longitude, has_video, road_name, is_starred) values (1134, 'White Pass Summit on US12 @ MP 150.9', 'http://images.wsdot.wa.gov/SC/012VC15094.jpg', 46.63404, -121.40514, 0, 'US 12', 0);");
		db.execSQL("insert into cameras (id, title, url, latitude, longitude, has_video, road_name, is_starred) values (1138, 'US 97 MP 164 Blewett Pass Summit', 'http://images.wsdot.wa.gov/us97/blewett/sumtnorth.jpg', 47.334975, -120.578397, 0, 'US 97', 0);");
		db.execSQL("insert into cameras (id, title, url, latitude, longitude, has_video, road_name, is_starred) values (4030, 'SR 20 MP 214.5 (View East)', 'http://images.wsdot.wa.gov/sr20/louploup/sr20louploup_east.jpg', 48.3904, -119.87925, 0, 'SR 20', 0);");
		db.execSQL("insert into cameras (id, title, url, latitude, longitude, has_video, road_name, is_starred) values (1127, 'Manastash Ridge Summit on I-82 @ MP 7', 'http://images.wsdot.wa.gov/rweather/UMRidge_medium.jpg', 46.89184, -120.43773, 0, 'I-82', 0);");
		db.execSQL("insert into cameras (id, title, url, latitude, longitude, has_video, road_name, is_starred) values (1137, 'Satus Pass on US 97 @ MP 27', 'http://images.wsdot.wa.gov/satus/satus1.jpg', 45.98296, -120.65381, 0, 'US 97', 0);");
		db.execSQL("insert into cameras (id, title, url, latitude, longitude, has_video, road_name, is_starred) values (1161, 'Sherman Pass on SR-20 @ MP 320', 'http://images.wsdot.wa.gov/rweather/shermanpass_medium.jpg', 48.604742, -118.459912, 0, 'SR 20', 0);");
		db.execSQL("insert into cameras (id, title, url, latitude, longitude, has_video, road_name, is_starred) values (9029, 'Denny Creek on I-90 @ MP46.8', 'http://images.wsdot.wa.gov/sc/090VC04680.jpg', 47.396441, -121.49935, 0, 'I-90', 0);");
		db.execSQL("insert into cameras (id, title, url, latitude, longitude, has_video, road_name, is_starred) values (1099, 'Franklin Falls on I-90 @ MP51.3', 'http://images.wsdot.wa.gov/sc/090VC05130.jpg', 47.42246, -121.40991, 0, 'I-90', 0);");
		db.execSQL("insert into cameras (id, title, url, latitude, longitude, has_video, road_name, is_starred) values (1100, 'Snoqualmie Summit on I-90 @ MP52', 'http://images.wsdot.wa.gov/sc/090VC05200.jpg', 47.40818, -121.40592, 0, 'I-90', 0);");
		db.execSQL("insert into cameras (id, title, url, latitude, longitude, has_video, road_name, is_starred) values (1102, 'Hyak on I-90 @ MP55.2', 'http://images.wsdot.wa.gov/sc/090VC05517.jpg', 47.37325, -121.37699, 0, 'I-90', 0);");
		db.execSQL("insert into cameras (id, title, url, latitude, longitude, has_video, road_name, is_starred) values (9018, 'Price Creek on I-90 @ MP61', 'http://images.wsdot.wa.gov/sc/090VC06100.jpg', 47.326814, -121.332553, 0, 'I-90', 0);");
		db.execSQL("insert into cameras (id, title, url, latitude, longitude, has_video, road_name, is_starred) values (9019, 'Easton Hill on I-90 @ MP67.4', 'http://images.wsdot.wa.gov/sc/090VC06740.jpg', 47.264479, -121.284702, 0, 'I-90', 0);");
		db.execSQL("insert into cameras (id, title, url, latitude, longitude, has_video, road_name, is_starred) values (1103, 'Easton on I-90 @ MP70.6', 'http://images.wsdot.wa.gov/sc/090VC07060.jpg', 47.280581, -121.185882, 0, 'I-90', 0);");
		db.execSQL("insert into cameras (id, title, url, latitude, longitude, has_video, road_name, is_starred) values (8062, 'US 2 MP 64 Stevens Pass Summit', 'http://images.wsdot.wa.gov/us2/stevens/sumteast.jpg', 47.7513, -121.10619, 0, 'US 2', 0);");
		db.execSQL("insert into cameras (id, title, url, latitude, longitude, has_video, road_name, is_starred) values (8063, 'US 2 MP 65 Stevens Pass Ski Lodge', 'http://images.wsdot.wa.gov/us2/stvldg/sumtwest.jpg', 47.7513, -121.10619, 0, 'US 2', 0);");
		db.execSQL("insert into cameras (id, title, url, latitude, longitude, has_video, road_name, is_starred) values (9145, 'US 2 MP 62 Old Faithful Avalanche Zone', 'http://images.wsdot.wa.gov/us2/oldfaithful/oldfaithful.jpg', 47.724431, -121.134085, 0, 'US 2', 0);");
	}
	
}
