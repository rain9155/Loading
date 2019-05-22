package com.example.loading;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.loading.activity.ActionBarActivity;
import com.example.loading.activity.WrapActivity;

public class MainActivity extends AppCompatActivity {

    Button btnActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnActivity = findViewById(R.id.btn_activity);
        btnActivity.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, WrapActivity.class)));

    }
}
