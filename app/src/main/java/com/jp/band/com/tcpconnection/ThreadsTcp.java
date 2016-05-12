package com.jp.band.com.tcpconnection;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by kai on 18/3/16.
 */
public class ThreadsTcp extends AppCompatActivity {
    private TextView tvServerMessage;
    ClientAsyncTask clientAST;
    private EditText editTextmsg;
    private Button bSend;
    String clientMessage;


    Socket socket;
    Boolean firstTime = true;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        new RektKid().execute();




        bSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clientMessage =  editTextmsg.getText().toString();

                //Create an instance of AsyncTask

                clientAST = new ClientAsyncTask();
                //Pass the server ip, port and client message to the AsyncTask

                //clientAST.execute(new String[] { "1.2.3.4", "8899",clientMessage });
                //both the asyncz run simultaneously.
                clientAST.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new String[] { "1.2.3.4", "8899",clientMessage});



            }
        });
    }
    private void init() {
        tvServerMessage = (TextView) findViewById(R.id.textViewServerMessage);
        editTextmsg = (EditText) findViewById(R.id.clientMessage);
        bSend = (Button)findViewById(R.id.bSend);
    }





    class ClientAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String result = null;
            try {
                //Create a client socket and define internet address and the port of the server

                //Write to the server params[2] is the message sent by the client
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                out.println(params[2]);




            } catch (NumberFormatException e) {
                e.printStackTrace();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            //Write server message to the text view
            if (s != null && !s.equals("")) {
                tvServerMessage.setText(s);


            }
        }

    }




    class RektKid extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String result = null;
            try {
                //Create a client socket and define internet address and the port of the server
                //socket = new Socket("1.2.3.4", 8899);
                //Get the input stream of the client socket
                if(firstTime) {
                    socket = new Socket("1.2.3.4", 8899);
                    firstTime = false;
                    Log.d("once", "I GUESS");

                }
                InputStream is = socket.getInputStream();
                //Buffer the data coming from the input stream
                BufferedReader br = new BufferedReader(
                        new InputStreamReader(is));
                //Read data in the input buffer

                result  = br.readLine();
                //Close the client socket
               // socket.close();
            } catch (NumberFormatException e) {
                e.printStackTrace();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }
        @Override
        protected void onPostExecute(String s) {
            //Write server message to the text view
            tvServerMessage.setText(s);
            new RektKid().execute();


        }
    }

}
