package com.example.liuwangshu.moonclassloader;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ClassLoader loader = MainActivity.class.getClassLoader();
        while (loader != null) {
            Log.d("liuwangshu",loader.toString());
            loader = loader.getParent();
        }
    }
}
