package com.mysterlee.www;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.mysterlee.www.wifi_go.MainActivity;
import com.mysterlee.www.wifi_go.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class UserActivity extends AppCompatActivity {

    String num;

    String myJson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        Intent intent = getIntent();
        num = intent.getStringExtra("num");

        insertToDatabase(num);

    }

    public void onClick(View v) {

        Intent intent = new Intent(UserActivity.this, MainActivity.class);
        startActivity(intent);
        SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = auto.edit();

        editor.clear();
        editor.commit();
        Toast.makeText(UserActivity.this, "로그아웃", Toast.LENGTH_SHORT).show();
        finish();


    }

    private void insertToDatabase(String num) {

        class InsertData extends AsyncTask<String, Void, String> {
            ProgressDialog loading;

            protected void onPostExecute(String s) {

                myJson = s;
                makeMarker();
            }

            @Override
            protected  String doInBackground(String... params) {

                try {
                    String num = (String)params[0];

                    String link = "https://www.mysterlee.com/wifigo/user.php";
                    String data = URLEncoder.encode("num", "UTF-8") + "=" + URLEncoder.encode(num, "UTF-8");

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



                    return sb.toString().trim();

                }
                catch (Exception e) {
                    return new String("Exception: " + e.getMessage());
                }
            }

        }

        InsertData task = new InsertData();
        task.execute(num);

    }

    protected void makeMarker() {
        try {

            JSONObject jsonObj = new JSONObject(myJson);

            String lv = jsonObj.getString("lv");
            String point = jsonObj.getString("point");
            String item = jsonObj.getString("item");
            String name = jsonObj.getString("name");


            TextView editTextLv = (TextView)findViewById(R.id.textViewLv);
            TextView editTextPoint = (TextView)findViewById(R.id.textViewPoint);
            TextView editTextItem = (TextView)findViewById(R.id.textViewItem);
            TextView editTextName = (TextView)findViewById(R.id.textViewName);

            editTextItem.setText(item);
            editTextLv.setText("Lv. " + lv);
            editTextPoint.setText(point.trim()+" Point");
            editTextName.setText(name);


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
