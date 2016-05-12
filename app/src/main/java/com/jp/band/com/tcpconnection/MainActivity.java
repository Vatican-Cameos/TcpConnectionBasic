package com.jp.band.com.tcpconnection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyStore;

import android.support.v7.app.ActionBarActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity{
    private TextView tvServerMessage;
    private EditText editTextmsg;
    private Button bSend;
    Socket socket;
    ClientAsyncTask clientAST;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        onClickListners();
         clientAST = new ClientAsyncTask();
        //Pass the server ip, port and client message to the AsyncTask
        clientAST.execute(new String[] { "1.2.3.4", "8899","" });





    }




    private void onClickListners() {
        bSend.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if(socket != null && socket.isConnected()){
                    try {

                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                String msg = editTextmsg.getText().toString();
                //Create an instance of AsyncTask

                 clientAST = new ClientAsyncTask();
                //Pass the server ip, port and client message to the AsyncTask

                clientAST.execute(new String[] { "1.2.3.4", "8899",msg });

            }
        });
    }

    private void init() {
        tvServerMessage = (TextView) findViewById(R.id.textViewServerMessage);
        editTextmsg = (EditText) findViewById(R.id.clientMessage);
        bSend = (Button)findViewById(R.id.bSend);
    }


    /**
     * AsyncTask which handles the communication with the server
     */
    class ClientAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String result = null;
            try {
                //Create a client socket and define internet address and the port of the server
                socket = new Socket("1.2.3.4", 8899);
                //Get the input stream of the client socket
                InputStream is = socket.getInputStream();
                //Get the output stream of the client socket
                PrintWriter out = new PrintWriter(socket.getOutputStream(),true);
                //Write data to the output stream of the client socket
                if(!params[2].equals(""))
                out.println(params[2]);


                //Buffer the data coming from the input stream
                BufferedReader br = new BufferedReader(
                        new InputStreamReader(is));
                //Read data in the input buffer
                 result  = br.readLine();

                //Close the client socket
                //socket.close();
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
            if(s != null && !s.equals("")) {
                tvServerMessage.setText(s);
                ClientAsyncTask clientAST = new ClientAsyncTask();
                //Pass the server ip, port and client message to the AsyncTask
                //clientAST.cancel(true);
                clientAST.execute(new String[]{"1.2.3.4", "8899", ""});
            }else{
                //clientAST.cancel(true);
            }

        }
    }



    class RektKid extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String result = null;
            try {
                //Create a client socket and define internet address and the port of the server
                socket = new Socket("1.2.3.4", 8899);
                //Get the input stream of the client socket
                InputStream is = socket.getInputStream();
                //Get the output stream of the client socket
               // PrintWriter out = new PrintWriter(socket.getOutputStream(),true);
                //Write data to the output stream of the client socket
                //if(!params[2].equals(""))
                  //  out.println(params[2]);


                //Buffer the data coming from the input stream
                BufferedReader br = new BufferedReader(
                        new InputStreamReader(is));
                //Read data in the input buffer

                result  = br.readLine();

                //Close the client socket
                socket.close();
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