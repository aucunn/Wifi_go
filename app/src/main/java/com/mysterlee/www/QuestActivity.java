package com.mysterlee.www;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
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

public class QuestActivity extends AppCompatActivity {

    private String num;
    /*

    private String[] name;
    private String[] con;
    private String[] reward;
    */
    ArrayList<HashMap<String, String>> questList;
    ListView list;
    String myJson;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quest);

        list = (ListView)findViewById(R.id.questList);
        questList = new ArrayList<HashMap<String, String>>();

        Intent intent = getIntent();
        num = intent.getStringExtra("num");

        insertToDatabase(num);
        /*
        DisplayMetrics outMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
        int dpi = ((outMetrics.densityDpi)/160);

        LinearLayout linearLayout = (LinearLayout)findViewById(R.id.questLayout);

        LinearLayout.LayoutParams horizonParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams leftParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
        LinearLayout.LayoutParams rightParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 2);





        insertToDatabase(num);

        for(int i=0; i < no; i++) {
            LinearLayout horizon = new LinearLayout(this);
            horizon.setOrientation(LinearLayout.HORIZONTAL);
            horizon.setLayoutParams(horizonParam);

            LinearLayout leftLyout = new LinearLayout(this);
            leftLyout.setOrientation(LinearLayout.VERTICAL);
            leftLyout.setLayoutParams(leftParam);


            LinearLayout rightLyout = new LinearLayout(this);
            rightLyout.setOrientation(LinearLayout.VERTICAL);
            rightLyout.setLayoutParams(rightParam);


            TextView questTitle = new TextView(this);
            questTitle.setText(name[i]);
            questTitle.setTextSize(26);
            leftLyout.addView(questTitle);

            TextView questCon = new TextView(this);
            questCon.setText(con[i]);
            questTitle.setTextSize(16);
            leftLyout.addView(questCon);

            TextView questRe = new TextView(this);
            questRe.setText(reward[i]);
            rightLyout.addView(questRe);

            Button btn = new Button(this);
            btn.setText("완료");
            rightLyout.addView(btn);
/*
            btn.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (true){//퀘스트 완료체크
                        //데비터베이스에 완료 퀘스트 등록하기
                        //보상주기
                        //시각화해서 보여주기
                    }
                }
            });
*//*

            linearLayout.addView(horizon);
            horizon.addView(leftLyout);
            horizon.addView(rightLyout);
        }

        */
    }

    protected void showList(){
        try{

            JSONObject jsonObj = new JSONObject(myJson);
            JSONArray var = jsonObj.getJSONArray("quest");

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
                String reward = c.getString("reward");

                HashMap<String, String> quest = new HashMap<String, String>();

                quest.put("name", name);
                quest.put("con", con);
                quest.put("reward", reward);

                questList.add(quest);
            }

            ListAdapter adapter = new SimpleAdapter(QuestActivity.this,
                    questList, R.layout.quest_list,
                    new String[]{"name", "con", "reward"},
                    new int[]{R.id.questName, R.id.questCon, R.id.questReward}
            );

            list.setAdapter(adapter);

            AdapterView.OnItemClickListener qusetComp = new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    Toast.makeText(QuestActivity.this, "보상", Toast.LENGTH_SHORT).show();
                    //보상관련

                }
            };

            //list.setOnClickListener((View.OnClickListener) qusetComp);


        }
        catch (JSONException e){
            e.printStackTrace();
        }
    }




    private void insertToDatabase(String num) {

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
                    String num = (String)params[0];

                    String link = "https://www.mysterlee.com/wifigo/quest.php";
                    String data = URLEncoder.encode("no", "UTF-8") + "=" + URLEncoder.encode(num, "UTF-8");

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
        task.execute(num);

    }


}
