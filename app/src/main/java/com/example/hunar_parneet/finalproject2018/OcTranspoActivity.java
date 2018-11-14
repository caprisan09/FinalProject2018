package com.example.hunar_parneet.finalproject2018;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class OcTranspoActivity extends Activity {

    private EditText editText;
    private Button button;
    private ListView listView;
    private TextView heading;
    Toast toast;
    Context ctx = this;
    int duration = Toast.LENGTH_LONG;
    CharSequence text = "Searching for the bus route";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.oc_transpo_activity);
        editText = findViewById(R.id.editText);
        button = findViewById(R.id.button);
        listView = findViewById(R.id.list);
        heading = findViewById(R.id.heading);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toast = Toast.makeText(ctx, text, duration);
                toast.show();
            }
        });



    }
}
