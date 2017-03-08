package com.wordpress.gatarblog.dzwonnik;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button button = (Button) findViewById(R.id.addNewRingChange);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToVolumeChange();
            }
        });
    }

    private void goToVolumeChange(){
        Intent intent = new Intent(this,RingtoneStateSetActivity.class);
        startActivity(intent);
    }


}
