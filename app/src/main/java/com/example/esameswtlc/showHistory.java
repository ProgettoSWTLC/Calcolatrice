package com.example.esameswtlc;

import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

import static com.example.esameswtlc.R.attr.fastScrollHorizontalTrackDrawable;

public class showHistory extends AppCompatActivity {

    private ArrayList<String> history;
    public static final String GET_OPERATION = "com.example.esameswtlc.GET_OPERATION";
    public static final int RESULT_CODE_GET_OPERATION = 1;
    public static final int DELETE_HISTORY_CODE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_history);
        Intent intent = getIntent();
        this.history = intent.getStringArrayListExtra(MainActivity.HISTORY);


        // Creazione della cronologia
        for (String operation : this.history) {
            MaterialButton newOperationView = new MaterialButton(this, null, R.attr.borderlessButtonStyle);
            newOperationView.setText(operation);
            LinearLayout.LayoutParams parametri = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            parametri.weight = 1.0f;
            parametri.gravity = Gravity.START;
            newOperationView.setLayoutParams(parametri);
            newOperationView.setPadding(0, 0, 0, 10);
            newOperationView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f);
            newOperationView.setLines(1);
            newOperationView.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
            newOperationView.setHorizontallyScrolling(true);
            // Aggiungo event listener
            newOperationView.setOnClickListener(v -> {
                Intent resultIntent = new Intent();
                resultIntent.putExtra(GET_OPERATION, operation);
                setResult(RESULT_CODE_GET_OPERATION, resultIntent);
                finish();
            });
            ((LinearLayout)findViewById(R.id.historyContainer)).addView(newOperationView);
        }
    }

    public void deleteHistory(View view) {
        Intent resultIntent = new Intent();
        setResult(DELETE_HISTORY_CODE, resultIntent);
        finish();
    }
}