package com.example.celebrity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;


public class MainActivity extends AppCompatActivity {
    EditText search;
    TextView result;
    @SuppressLint("StaticFieldLeak")


    public class DownloadTask extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... strings) {
            String result="";
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection connect = (HttpURLConnection) url.openConnection();
                InputStream in=connect.getInputStream();
                InputStreamReader send=new InputStreamReader(in);
                int data=send.read();
                while(data!=-1){
                    char s=(char) data;
                    result+=s;
                    data=send.read();
                }
                return result;
            }
            catch(Exception e){
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    public void run() {
                        final Toast toast = Toast.makeText(MainActivity.this,"Cant find the city",Toast.LENGTH_SHORT);
                        toast.show();
                    }
                });
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try{
                JSONObject sole=new JSONObject(s);
                String hell=sole.getString("weather");
                JSONArray arr=new JSONArray(hell);
                String mess="";
                for(int i=0;i<arr.length();i++){
                    JSONObject whoa=arr.getJSONObject(i);
                    String sup=whoa.getString("main");
                    String des=whoa.getString("description");
                    if(!sup.equals("")&&!des.equals("")){
                        mess+=sup+":"+des+"\r\n";
                    }
                }
                if(!mess.equals("")){
                    result.setText(mess);
                }
                else{

                }

            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }
        public void onClick(View view){
        try {
            DownloadTask task = new DownloadTask();
            String name = URLEncoder.encode(search.getText().toString(), "UTF-8");
            task.execute("https://openweathermap.org/data/2.5/weather?q=" + name + "&appid=439d4b804bc8187953eb36d2a8c26a02");
            InputMethodManager sure = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            sure.hideSoftInputFromWindow(search.getWindowToken(), 0);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        search=findViewById(R.id.editTextTextPersonName);
        result=findViewById(R.id.textView2);
    }
}