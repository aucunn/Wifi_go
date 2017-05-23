package com.mysterlee.www;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.mysterlee.www.wifi_go.MainActivity;
import com.mysterlee.www.wifi_go.R;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class Regist extends AppCompatActivity {

    private EditText editTextId;
    private EditText editTextPass;
    private EditText editTextRePass;
    private EditText editTextName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);

        editTextId = (EditText)findViewById(R.id.editRegistId);
        editTextPass = (EditText)findViewById(R.id.editRegistPass);
        editTextRePass = (EditText)findViewById(R.id.editRePass);
        editTextName = (EditText)findViewById(R.id.editRegistName);

        boolean ok = false;


    }

    public void insert(View view) {
        String id = editTextId.getText().toString();
        String pass = editTextPass.getText().toString();
        String pass2 = editTextRePass.getText().toString();
        String name = editTextName.getText().toString();

        if(pass.equals(pass2)) {
            insertToDatabase(id, pass, name);
        }
        else {
            Toast.makeText(Regist.this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
        }
    }


    private void goLogin(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void insertToDatabase(String id, String pass, String name) {

        class InsertData extends AsyncTask<String, Void, String> {
            ProgressDialog loading;


            @Override
            protected  void  onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(Regist.this, "Please Wait", null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
                if(s.equals("회원가입 성공")){
                    goLogin();
                }

            }

            @Override
            protected  String doInBackground(String... params) {

                try {
                    String id = (String)params[0];
                    String pass = (String)params[1];
                    String name = (String)params[2];

                    String link = "https://www.mysterlee.com/wifigo/regist.php";
                    String data = URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(id, "UTF-8");
                    data += "&" + URLEncoder.encode("pass", "UTF-8") + "=" + URLEncoder.encode(pass, "UTF-8");
                    data += "&" + URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(name, "UTF-8");

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
                    return sb.toString();

                }
                catch (Exception e) {
                    return new String("Exception: " + e.getMessage());
                }
            }

        }

        InsertData task = new InsertData();
        task.execute(id, pass, name);

    }

}
