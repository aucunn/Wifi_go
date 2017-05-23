package com.mysterlee.www.wifi_go;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.mysterlee.www.NaviActivity;
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
    private int num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextId = (EditText)findViewById(R.id.editId);
        editTextPass = (EditText)findViewById(R.id.editPass);

    }

    public void onClickLogin(View v) {
        String id = editTextId.getText().toString();
        String pass = editTextPass.getText().toString();

        insertToDatabase(id, pass);


    }

    public void login(){
        if(num >= 1) {
            Intent intent = new Intent(this, NaviActivity.class);
            intent.putExtra("num", String.valueOf(num));
            startActivity(intent);
        }
        else{
            Toast.makeText(this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    public void onClickRegist(View v) {

        Intent intent = new Intent(getApplicationContext(), Regist.class);
        startActivity(intent);
    }

    private void insertToDatabase(String id, String pass) {

        class InsertData extends AsyncTask<String, Void, String> {
            ProgressDialog loading;


            @Override
            protected  void  onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(MainActivity.this, "로딩중...", null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                login();
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
                    num = Integer.parseInt(sb.toString());


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
