package com.example.loading2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.loading2.activity.ActionBarActivity;
import com.example.loading2.activity.ToolBarActivity;

public class ActivityActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity);

        Button btnActionBar = findViewById(R.id.btn_action_bar);
        btnActionBar.setOnClickListener(v -> startActivity(new Intent(ActivityActivity.this, ActionBarActivity.class)));

        Button btnNoBar = findViewById(R.id.btn_no_action_bar);
        btnNoBar.setOnClickListener(v -> startActivity(new Intent(ActivityActivity.this, ToolBarActivity.class)));

    }
}
