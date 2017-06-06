package com.mysterlee.www;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
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

/**
 * Created by aucun on 2017-06-06.
 */

public class Tab1 extends Fragment{

    private WebView webView;
    String myJson;

    private String id;
    private String pass;

    private double lat;
    private double lon;

    View view;

    public Tab1(double late, double lone) {
        lat = late;
        lon = lone;
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.tap1, container, false);




        webView = (WebView)view.findViewById(R.id.webView);

        String image = "http://imgnews.naver.com/image/112/2005/11/25/200511250023.gif";
        webView.loadUrl(image);

        insertToDatabase(String.valueOf(lat), String.valueOf(lon));


        return view;





    }

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

                    String link = "https://www.mysterlee.com/wifigo/wifi_data1.php";

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
            String tcon = jsonObj.getString("con");

            TextView textView = (TextView)view.findViewById(R.id.textViewCon2);
            textView.setText(tcon);



        }
        catch (JSONException e){
            e.printStackTrace();
        }
    }




}
