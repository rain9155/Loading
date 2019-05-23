package com.example.loading2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.loading2.fragment.SingleFragmentActivity;
import com.example.loading2.fragment.ViewPagerActivity;

public class FragmentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        Button btnAFragment = findViewById(R.id.btn_fragment);
        btnAFragment.setOnClickListener(v -> startActivity(new Intent(FragmentActivity.this, SingleFragmentActivity.class)));

        Button btnVP = findViewById(R.id.btn_viewpager);
        btnVP.setOnClickListener(v -> startActivity(new Intent(FragmentActivity.this, ViewPagerActivity.class)));

    }
}
