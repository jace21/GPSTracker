package jvj.client;


import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.TextView;


import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.util.Calendar;

import java.net.Socket;
import java.util.List;
import java.util.Locale;

public class DisplayActivity extends AppCompatActivity {
    private TextView tvTime, tvDate, tvAddress;
    private Socket reUsedSocket;
    private static final int SERVER_PORT = 6969;
    private static final String SERVER_IP = "72.194.114.60";
    private GPSTracker gpsTracker;
    private Thread t;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);
        tvTime = (TextView) findViewById(R.id.TextTime);
        tvDate = (TextView) findViewById(R.id.TextDate);
        tvAddress = (TextView) findViewById(R.id.TextAddress);
        reUsedSocket = SocketHandler.getSocket();
        gpsTracker = new GPSTracker(this);
        t = new Thread(){
            @Override
            public void run(){
                try {
                    while (!isInterrupted()) {
                        reOpenSocket();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                updateTextView();
                                updateLocation();
                            }
                        });
                        Thread.sleep(1000);
                        reUsedSocket.close();
                        Thread.sleep(5000);
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        };
        t.start();
    }
    public void onClickLogout(View view){
        try{
            gpsTracker.stopGPS();
            t.interrupt();
            finish();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    private void reOpenSocket(){
        try {
            InetAddress serverAddr = InetAddress.getByName(SERVER_IP);
            reUsedSocket = new Socket(serverAddr, SERVER_PORT);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    private void updateTextView(){
        try {
            java.util.Date noteTs = Calendar.getInstance().getTime();
            String time = "hh:mm";
            tvTime.setText(DateFormat.format(time, noteTs));
            String date = "dd MM yyyy";
            tvDate.setText(DateFormat.format(date, noteTs));
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(gpsTracker.getLatitude(), gpsTracker.getLongitude(), 1);
            String address = addresses.get(0).getAddressLine(0);
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String postalCode = addresses.get(0).getPostalCode();
            String concate = address + "\n" + city + " " + state + " " + postalCode;
            tvAddress.setText(concate);
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
    private void updateLocation(){
        try {
            PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(reUsedSocket.getOutputStream())), true);
            out.print(tvTime.getText() + ":" + tvDate.getText() + ":" + gpsTracker.getLatitude() + ":" + gpsTracker.getLongitude());
            out.flush();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
}
