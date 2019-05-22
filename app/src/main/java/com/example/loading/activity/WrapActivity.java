package com.example.loading.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.loading.R;

public class WrapActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wrap);

        Button btnActionBar = findViewById(R.id.btn_action_bar);
        btnActionBar.setOnClickListener(v -> startActivity(new Intent(WrapActivity.this, ActionBarActivity.class)));

        Button btnNoBar = findViewById(R.id.btn_no_action_bar);
        btnNoBar.setOnClickListener(v -> startActivity(new Intent(WrapActivity.this, ToolBarActivity.class)));

    }
}
