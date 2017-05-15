package com.mysterlee.www;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.mysterlee.www.wifi_go.R;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class WifiActivity extends AppCompatActivity {

    private double lat;
    private double lon;
    private String name;
    private String pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi);

        Intent intent = getIntent();
        lat = intent.getDoubleExtra("lat", 0);
        lon = intent.getDoubleExtra("lon", 0);

        insertToDatabase(String.valueOf(lat), String.valueOf(lon));


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






    private void insertToDatabase(String lat, String lon) {

        class InsertData extends AsyncTask<String, Void, String> {
            ProgressDialog loading;


            @Override
            protected  void  onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(WifiActivity.this, "Please Wait", null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                Toast.makeText(WifiActivity.this, s, Toast.LENGTH_SHORT).show();
                loading.dismiss();
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
                        sb.append(line);
                        break;
                    }

                    String[] s = sb.toString().split("|");
                    name = s[0];
                    pass = s[1];

                    TextView txt = (TextView) findViewById(R.id.textView2);

                    return sb.toString();

                }
                catch (Exception e) {
                    return new String("Exception: " + e.getMessage());
                }
            }

        }

        InsertData task = new InsertData();
        task.execute(lat, lon);

    }

}
