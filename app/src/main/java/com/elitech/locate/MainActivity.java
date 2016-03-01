package com.elitech.locate;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        AppCompatButton gps = (AppCompatButton) findViewById(R.id.gps);
        AppCompatButton baidu = (AppCompatButton) findViewById(R.id.baidu);
        AppCompatButton amap = (AppCompatButton) findViewById(R.id.amap);
        AppCompatButton tencent = (AppCompatButton) findViewById(R.id.tencent);

        gps.setOnClickListener(this);
        baidu.setOnClickListener(this);
        amap.setOnClickListener(this);
        tencent.setOnClickListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.gps:
                Toast.makeText(this,"GPS定位不准确，并且室内无法使用GPS定位，而自己实现的网络定位还没成功",Toast.LENGTH_SHORT).show();
                break;
            case R.id.baidu:
                startActivity(new Intent(this,BaiduActivity.class));
                break;
            case R.id.amap:
                startActivity(new Intent(this,GaodeActivity.class));
                break;
            case R.id.tencent:
                startActivity(new Intent(this,TencentActivity.class));
                break;
            default:
                break;
        }
    }
}
