package com.mysterlee.www;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.mysterlee.www.wifi_go.R;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Random;

public class MakerActivity extends AppCompatActivity {

    private static final int PI = 1;
    RadioGroup radioGroup;
    EditText name;
    EditText pass;
    EditText con;

    String num;
    double lat;
    double lon;

    String poto = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maker);

        radioGroup = (RadioGroup)findViewById(R.id.radioGroup);
        name = (EditText)findViewById(R.id.editTextName);
        pass = (EditText)findViewById(R.id.editTextPass);
        con = (EditText)findViewById(R.id.editTextCon);

        Intent intent = getIntent();
        num = intent.getStringExtra("num");
        lat = intent.getDoubleExtra("lat", 0);
        lon = intent.getDoubleExtra("lon", 0);


    }

    public void onClickSave(View view)
    {
        int checkedRadioButtonId =radioGroup.getCheckedRadioButtonId();
        RadioButton radioButton = (RadioButton)findViewById(checkedRadioButtonId);

        insertToDatabase(radioButton.getText().toString(), name.getText().toString(), pass.getText().toString(),
                String.valueOf(lat), String.valueOf(lon), num, con.getText().toString());
        onBackPressed();

    }

    private void insertToDatabase(String color, String name, String pass, String lat, String lon, String num, String con) {

        class InsertData extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... params) {

                try {
                    String color = (String) params[0];
                    String name = (String) params[1];
                    String pass = (String) params[2];
                    String lat = (String) params[3];
                    String lon = (String) params[4];
                    String num = (String) params[5];
                    String con = (String) params[6];

                    String link = "https://www.mysterlee.com/wifigo/make.php";
                    String data = URLEncoder.encode("color", "UTF-8") + "=" + URLEncoder.encode(color, "UTF-8");
                    data += "&" + URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(name, "UTF-8");
                    data += "&" + URLEncoder.encode("pass", "UTF-8") + "=" + URLEncoder.encode(pass, "UTF-8");
                    data += "&" + URLEncoder.encode("lat", "UTF-8") + "=" + URLEncoder.encode(lat, "UTF-8");
                    data += "&" + URLEncoder.encode("lon", "UTF-8") + "=" + URLEncoder.encode(lon, "UTF-8");
                    data += "&" + URLEncoder.encode("num", "UTF-8") + "=" + URLEncoder.encode(num, "UTF-8");
                    data += "&" + URLEncoder.encode("con", "UTF-8") + "=" + URLEncoder.encode(con, "UTF-8");
                    data += "&" + URLEncoder.encode("poto", "UTF-8") + "=" + URLEncoder.encode(poto, "UTF-8");

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
                        sb.append(line + "\n");
                    }


                    return sb.toString().trim();

                } catch (Exception e) {
                    return new String("Exception: " + e.getMessage());
                }
            }

        }

        InsertData task = new InsertData();
        task.execute(color, name, pass, lat, lon, num, con);

    }

    public void btnImage (View v)
    {
        String name = null;


        Random rnd =new Random();
        StringBuffer buf =new StringBuffer();
        for(int i=0;i<20;i++){
            if(rnd.nextBoolean()){
                buf.append((char)((int)(rnd.nextInt(26))+97));
            }else{
                buf.append((rnd.nextInt(10)));
            }
        }

        name = buf.toString();
        
        Intent intent = new Intent(getApplicationContext(), PotoActivity.class);
        intent.putExtra("number", "2");
        intent.putExtra("name", name);
        startActivityForResult(intent, PI);


    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == PI) {
            poto = data.getStringExtra("var");

        }
    }






}
