package jvj.client;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;


/**
 * Created by Jay Jay on 12/29/2015.
 *
 */
public class GPSTracker extends Service implements LocationListener {
    private final Context myContext;
    boolean isGPSEnabled = false;
    boolean isNetworkEnabled = false;
    boolean canGetLocation = false;

    Location location;
    double latitude;
    double longitude;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 1;
    private static final long MIN_TIME_BW_UPDATES = 100 * 60;
    protected LocationManager locationManager;

    public GPSTracker(Context context){
        myContext = context;
        getLocation();
    }
    public Location getLocation(){
        try{
            locationManager = (LocationManager)myContext.getSystemService(LOCATION_SERVICE);
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            if(!isGPSEnabled && !isNetworkEnabled){}
            else{
                this.canGetLocation = true;
                if(isNetworkEnabled){
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    if(locationManager != null){
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if(location != null){
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }
                }
                if(isGPSEnabled){
                    if(location == null){
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        if(locationManager != null){
                            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if(location != null){
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
                        }
                    }
                }
            }
        }
        catch(SecurityException e){
            e.printStackTrace();
        }
        return location;
    }
    public void stopGPS(){
        try {
            if (locationManager != null) {
                locationManager.removeUpdates(GPSTracker.this);
            }
        }
        catch(SecurityException e){
            e.printStackTrace();
        }
    }
    public double getLatitude(){
        if(location != null){
            latitude = location.getLatitude();
        }
        return latitude;
    }
    public double getLongitude(){
        if(location != null){
            longitude = location.getLongitude();
        }
        return longitude;
    }
    @Override
    public void onLocationChanged(Location location) {
        if(location != null) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
        }
    }
    @Override
    public void onProviderDisabled(String provider) {}
    @Override
    public void onProviderEnabled(String provider) {}
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}
    @Override
    public IBinder onBind(Intent arg0) {return null;}
}
