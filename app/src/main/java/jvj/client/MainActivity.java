package jvj.client;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

import android.widget.EditText;
import android.widget.Toast;
import android.content.Intent;

public class MainActivity extends AppCompatActivity {
    private Socket socket;
    private static final int SERVER_PORT = 6969;
    private static final String SERVER_IP = "72.194.114.60";
    private Thread t;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        t = new Thread(new ClientThread());
        t.start();
    }
    public void onClick(View view){
        try{
            EditText login = (EditText) findViewById(R.id.EditText01);
            EditText password = (EditText) findViewById(R.id.EditText02);
            String log = login.getText().toString();
            String pass = password.getText().toString();
            if(!log.matches("") && !pass.matches("")){
                PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())),true);
                out.print(log + ":" + pass);
                out.flush();
                t.interrupt();
                Intent intent = new Intent(this, DisplayActivity.class);
                startActivity(intent);
            }
            else{
                Context context = getApplicationContext();
                CharSequence text = "Wrong user ID or Password";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context,text,duration);
                toast.show();
            }
        }
        catch(Exception E) {
            E.printStackTrace();
        }
    }
    class ClientThread implements Runnable{
        @Override
        public void run(){
            try{
                InetAddress serverAddr = InetAddress.getByName(SERVER_IP);
                socket = new Socket(serverAddr, SERVER_PORT);
                SocketHandler.setSocket(socket);
            }
            catch(IOException E){
                E.printStackTrace();
            }
        }
    }
}
