package com.example.loading;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.loading.view.SingleViewActivity;

public class ViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        Button btnView = findViewById(R.id.btn_view);
        btnView.setOnClickListener(v -> startActivity(new Intent(ViewActivity.this, SingleViewActivity.class)));

    }
}
