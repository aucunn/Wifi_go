package com.mysterlee.www;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
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

    private static final int PI = 1;
    String num;

    String myJson;
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        Intent intent = getIntent();
        num = intent.getStringExtra("num");

        insertToDatabase(num);

        webView = (WebView)findViewById(R.id.webView);



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

    public void btnName (View v)
    {
        Intent intent = new Intent(getApplicationContext(), PotoActivity.class);
        intent.putExtra("number", "1");
        intent.putExtra("name", num);
        startActivityForResult(intent, PI);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PI) {

            Intent intent = new Intent(this, UserActivity.class);
            intent.putExtra("num", num);
            startActivity(intent);
            this.finish();

        }
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
            Button editTextName = (Button) findViewById(R.id.buttonName);

            editTextItem.setText(item);
            editTextLv.setText("Lv. " + lv);
            editTextPoint.setText(point.trim()+" Point");
            editTextName.setText(name);

            webView.setVerticalScrollBarEnabled(false);
            webView.setVerticalScrollbarOverlay(false);
            webView.setHorizontalScrollBarEnabled(false);
            webView.setHorizontalScrollbarOverlay(false);
            webView.setInitialScale(100);
            webView.loadDataWithBaseURL(null,creHtmlBody("https://www.mysterlee.com/wifigo/user/" + num + ".jpg"), "text/html", "utf-8", null);



        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public  String creHtmlBody(String imagUrl){
        StringBuffer sb = new StringBuffer("<HTML>");
        sb.append("<HEAD>");
        sb.append("</HEAD>");
        sb.append("<BODY style='margin:0; padding:0; text-align:center;'>");    //중앙정렬
        sb.append("<img width='100%' height='100%' src=\"" + imagUrl+"\">"); //가득차게 나옴
        sb.append("</BODY>");
        sb.append("</HTML>");
        return sb.toString();
    }

}
