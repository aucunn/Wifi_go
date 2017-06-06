package com.mysterlee.www;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

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

/**
 * Created by aucun on 2017-06-06.
 */

public class Tab2 extends Fragment {


    private double lat;
    private double lon;
    private String num;

    String myJson;
    View view;
    ListAdapter adapter;

    EditText editTextCon;
    ArrayList<HashMap<String, String>> replyList;
    ListView list;

    public Tab2(double late, double lone, String num2) {
        lat = late;
        lon = lone;
        num = num2;
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.tap2, container, false);

        editTextCon = (EditText)view.findViewById(R.id.editTextReply);
        list = (ListView) view.findViewById(R.id.replyListView);
        replyList = new ArrayList<HashMap<String, String>>();


        Button button = (Button)view.findViewById(R.id.buttonReply);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String con = editTextCon.getText().toString();

                class insertReply extends AsyncTask<String, Void, String>
                {

                    @Override
                    protected String doInBackground(String... params) {
                        try {

                            String con = (String)params[0];

                            String link = "https://www.mysterlee.com/wifigo/reply.php";
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

                            return sb.toString().trim();



                        }
                        catch (Exception e)
                        {
                            Toast.makeText(getActivity(), String.valueOf(e), Toast.LENGTH_SHORT).show();

                        }

                        return null;
                    }

                }
                insertReply task = new insertReply();
                task.execute(con);
                Intent intent = new Intent(getActivity(), WifiActivity.class);
                intent.putExtra("num", num);
                intent.putExtra("lat", lat);
                intent.putExtra("lon", lon);
                startActivity(intent);
                getActivity().finish();

            }


        });

        insertToDatabase(String.valueOf(lat), String.valueOf(lon));

        return view;
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

                    String link = "https://www.mysterlee.com/wifigo/wifi_data2.php";

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




            //TextView txt = (TextView) findViewById(R.id.textView2);

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

            adapter = new SimpleAdapter(getActivity(),
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
