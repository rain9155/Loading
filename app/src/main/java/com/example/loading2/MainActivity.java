package com.example.loading2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnActivity = findViewById(R.id.btn_activity);
        btnActivity.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, ActivityActivity.class)));

        Button btnFragment = findViewById(R.id.btn_fragment);
        btnFragment.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, FragmentActivity.class)));

        Button btnView = findViewById(R.id.btn_view);
        btnView.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, ViewActivity.class)));

    }
}
