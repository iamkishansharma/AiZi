package com.heycode.aizi.dashboard.Attempt_Quiz_Section;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.heycode.aizi.CommanClass;
import com.heycode.aizi.R;


public class AttemptTestDetail extends AppCompatActivity {

    TextView Name,Time,QuesNo;
    Button Start;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attempt_test_detail);
        Name=findViewById(R.id.testName);
        Time=findViewById(R.id.testTime);
        QuesNo=findViewById(R.id.testQues);
        Start=findViewById(R.id.StartTest);

        Name.setText(CommanClass.testName);
        QuesNo.setText("1. Total "+CommanClass.Questions.size()+" questions are asked.");
        Time.setText("4. Time allocated is "+CommanClass.time.toString()+" Minutes.");
        Start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AttemptTestDetail.this, AttemptTest.class));
                finish();
            }
        });
    }
}