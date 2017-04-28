package com.mysterlee.www;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mysterlee.www.wifi_go.R;

public class QuestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quest);

        DisplayMetrics outMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
        int dpi = ((outMetrics.densityDpi)/160);

        LinearLayout linearLayout = (LinearLayout)findViewById(R.id.questLayout);

        LinearLayout.LayoutParams horizonParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams leftParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
        LinearLayout.LayoutParams rightParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 2);




        for(int i=0; i < 미완료퀘스트수; i++) {
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
            questTitle.setText("퀘스트 제목");
            questTitle.setTextSize(26);
            leftLyout.addView(questTitle);

            TextView questCon = new TextView(this);
            questCon.setText("퀘스트 내용\n두줄\n세줄");
            questTitle.setTextSize(16);
            leftLyout.addView(questCon);

            TextView questRe = new TextView(this);
            questRe.setText("보상\n두줄\n세줄");
            rightLyout.addView(questRe);

            Button btn = new Button(this);
            btn.setText("완료");
            rightLyout.addView(btn);

            btn.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (true){//퀘스트 완료체크
                        //데비터베이스에 완료 퀘스트 등록하기
                        //보상주기
                        //시각화해서 보여주기
                    }
                }
            });


            linearLayout.addView(horizon);
            horizon.addView(leftLyout);
            horizon.addView(rightLyout);
        }

    }
}
