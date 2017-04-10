package com.mysterlee.www.wifi_go;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.mysterlee.www.MapsActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
        EditText editId = (EditText)findViewById(R.id.editId);
        EditText editPass = (EditText)findViewById(R.id.editPass);
        Button btnLogin = (Button)findViewById(R.id.buttonLogin);

        editId.setText("테스트 입니다.");
        */

    }

    public void onClickLogin(View v) {
        Intent intent_01 = new Intent(getApplicationContext(), MapsActivity.class);
        startActivity(intent_01);
    }
}
