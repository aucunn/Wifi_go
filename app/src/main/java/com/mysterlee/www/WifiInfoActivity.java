package com.mysterlee.www;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.mysterlee.www.wifi_go.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

public class WifiInfoActivity extends AppCompatActivity {

    private double lat;
    private double lon;
    private String id;
    private String pass;


    ArrayList<HashMap<String, String>> replyList;
    ListView list;
    String myJson;

    private WebView webView;
    private EditText editTextCon;
    private String num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_info);

        list = (ListView)findViewById(R.id.replyList);
        replyList = new ArrayList<HashMap<String, String>>();

        editTextCon = (EditText)findViewById(R.id.editTextReply);


        Intent intent = getIntent();
        lat = intent.getDoubleExtra("lat", 0);
        lon = intent.getDoubleExtra("lon", 0);
        num = intent.getStringExtra("num");

        insertToDatabase(String.valueOf(lat), String.valueOf(lon));

        webView = (WebView)findViewById(R.id.webView);

        String image = "http://imgnews.naver.com/image/112/2005/11/25/200511250023.gif";
        webView.loadUrl(image);




/*
        WifiConfiguration wfc = new WifiConfiguration();
        wfc.SSID = "\"".concat(name).concat("\"");
        wfc.status = WifiConfiguration.Status.DISABLED;
        wfc.priority = 40;
        wfc.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
        wfc.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
        wfc.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
        wfc.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
        wfc.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
        wfc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
        wfc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
        wfc.preSharedKey = "\"".concat(pass).concat("\"");
*/




    }


    public void insert(View view)
    {
        String con = editTextCon.getText().toString();

        String link = "https://www.mysterlee.com/wifigo/reply.php";

        try {
            String data = URLEncoder.encode("con", "UTF-8") + "=" + URLEncoder.encode(con, "UTF-8");
            data += "&" + URLEncoder.encode("num", "UTF-8") + "=" + URLEncoder.encode(num, "UTF-8");
            data += "&" + URLEncoder.encode("lat", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(lat), "UTF-8");
            data += "&" + URLEncoder.encode("lon", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(lon), "UTF-8");

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
                sb.append(line+"\n");
            }

        }
        catch (Exception e)
        {

            String k = String.valueOf(e);
        }

    }





/////////////

    private void insertToDatabase(String lat, String lon) {

        class InsertData extends AsyncTask<String, Void, String> {
            ProgressDialog loading;

            /*
                        @Override
                        protected  void  onPreExecute() {
                            super.onPreExecute();
                            loading = ProgressDialog.show(QuestActivity.this, "Please Wait", null, true, true);
                        }
            */
            @Override
            protected void onPostExecute(String s) {
                /*
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
*/
                myJson = s;
                showList();
            }

            @Override
            protected  String doInBackground(String... params) {

                try {
                    String lat = (String)params[0];
                    String lon = (String)params[1];

                    String link = "https://www.mysterlee.com/wifigo/wifi_data.php";

                    String data = URLEncoder.encode("lat", "UTF-8") + "=" + URLEncoder.encode(lat, "UTF-8");
                    data += "&" + URLEncoder.encode("lon", "UTF-8") + "=" + URLEncoder.encode(lon, "UTF-8");

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
                        sb.append(line+"\n");
                    }


                    return sb.toString().trim();

                }
                catch (Exception e) {
                    return new String("Exception: " + e.getMessage());
                }
            }

        }

        InsertData task = new InsertData();
        task.execute(lat, lon);

    }


    protected void showList(){
        try{

            JSONObject jsonObj = new JSONObject(myJson);
            JSONArray var = jsonObj.getJSONArray("wifi");

            id = jsonObj.getString("id");
            pass = jsonObj.getString("pass");


            TextView txt = (TextView) findViewById(R.id.textView2);

            int no = var.length();
/*
                    name = new String[no];
                    con = new String[no];
                    reward = new String[no];
*/
            for(int j = 0; j < no; j++ )
            {
                JSONObject c = var.getJSONObject(j);
                        /*
                        name[j] = c.getString("name");
                        con[j] = c.getString("con");
                        reward[j] = c.getString("reward");
                        */
                String name = c.getString("name");
                String con = c.getString("con");

                HashMap<String, String> quest = new HashMap<String, String>();

                quest.put("name", name);
                quest.put("con", con);

                replyList.add(quest);
            }

            ListAdapter adapter = new SimpleAdapter(WifiInfoActivity.this,
                    replyList, R.layout.re_wifi,
                    new String[]{"name", "con"},
                    new int[]{R.id.textName, R.id.textCon}
            );

            list.setAdapter(adapter);


        }
        catch (JSONException e){
            e.printStackTrace();
        }
    }


}