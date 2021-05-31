package com.jvm.sampletoast;

import androidx.annotation.StyleRes;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.jvm.dynamictoast.DynamicToast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.errorBtn).setOnClickListener(this);
        findViewById(R.id.successBtn).setOnClickListener(this);
        findViewById(R.id.infoBtn).setOnClickListener(this);
        findViewById(R.id.defaultBtn).setOnClickListener(this);
        findViewById(R.id.warningBtn).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.errorBtn){
            showToast(R.style.ErrorToastStyle);
        }else if (v.getId() == R.id.successBtn){
            showToast(R.style.SuccessToastStyle);
        }else if (v.getId() == R.id.infoBtn){
            showToast(R.style.InfoToastStyle);
        }else if (v.getId() == R.id.warningBtn){
            showToast(R.style.WarningToastStyle);
        }else if (v.getId() == R.id.defaultBtn){
            showToast(R.style.DefaultStyle);
        }
    }

    private void showToast(@StyleRes int style){
        DynamicToast.makeText(MainActivity.this,"Error","Please select a rating to continue.",
                style).show();
        /*DynamicToast.Builder dynamicToast = new DynamicToast.Builder(MainActivity.this)
                .text("test toast")
                .build();
        dynamicToast.show();

        DynamicToast.Builder dynamicToast1 = new DynamicToast.Builder(MainActivity.this)
                .titleText("Error")
                .text("Please select a rating to continue.")
                .build();

        dynamicToast1.show();*/
    }
}