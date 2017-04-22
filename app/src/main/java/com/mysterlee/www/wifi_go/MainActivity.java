package com.mysterlee.www.wifi_go;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.mysterlee.www.MapsActivity;
import com.mysterlee.www.Regist;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {

    private EditText editTextId;
    private EditText editTextPass;

    private boolean ok = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextId = (EditText)findViewById(R.id.editId);
        editTextPass = (EditText)findViewById(R.id.editPass);

    }

    public void onClickLogin(View v) {
        Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
        startActivity(intent);
    }

    public void onClickRegist(View v) {
        String id = editTextId.getText().toString();
        String pass = editTextPass.getText().toString();

        insertToDatabase(id, pass);

        if(ok == true) {
            Intent intent = new Intent(getApplicationContext(), Regist.class);
            startActivity(intent);
        }
    }

    private void insertToDatabase(String id, String pass) {

        class InsertData extends AsyncTask<String, Void, String> {
            ProgressDialog loading;


            @Override
            protected  void  onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(MainActivity.this, "Please Wait", null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();

            }

            @Override
            protected  String doInBackground(String... params) {

                try {
                    String id = (String)params[0];
                    String pass = (String)params[1];

                    String link = "https://www.mysterlee.com/wifigo/login.php";
                    String data = URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(id, "UTF-8");
                    data += "&" + URLEncoder.encode("pass", "UTF-8") + "=" + URLEncoder.encode(pass, "UTF-8");

                    URL url = new URL(link);
                    URLConnection conn = url.openConnection();

                    conn.setDoOutput(true);
                    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

                    wr.write(data);
                    wr.flush();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                    StringBuilder sb = new StringBuilder();
                    String line = null;

                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                        break;
                    }
                    if(sb.toString() == "success")
                    {
                        ok = true;
                    }
                    return sb.toString();

                }
                catch (Exception e) {
                    return new String("Exception: " + e.getMessage());
                }
            }

        }

        InsertData task = new InsertData();
        task.execute(id, pass);

    }





}
