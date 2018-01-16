package com.lxd.percentlayout;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.one).setOnClickListener(this);
        findViewById(R.id.two).setOnClickListener(this);
        findViewById(R.id.three).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.one:
                startActivity(new Intent(this, FullScreenPercentActivity.class));
                break;
            case R.id.two:
                startActivity(new Intent(this, AspectRatioActivity.class));
                break;
            case R.id.three:
                startActivity(new Intent(this, ClipActivity.class));
                break;
            }
    }
}
