package com.gregre.bbtopdie;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Activity for entering a bug.
 */

public class NewBugActivity extends AppCompatActivity {

    public static final String EXTRA_REPLY = "com.example.android.buglistsql.REPLY";

    private EditText mEditBugView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_bug);
        mEditBugView = findViewById(R.id.edit_bug);

        final Button button = findViewById(R.id.button_save);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent replyIntent = new Intent();
                if (TextUtils.isEmpty(mEditBugView.getText())) {
                    setResult(RESULT_CANCELED, replyIntent);
                } else {
                    String bug = mEditBugView.getText().toString();
                    replyIntent.putExtra(EXTRA_REPLY, bug);
                    setResult(RESULT_OK, replyIntent);
                }
                finish();
            }
        });
    }
}