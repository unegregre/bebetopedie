package com.gregre.bbtopdie;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.gregre.bbtopdie.bug.BugListAdapter;
import com.gregre.bbtopdie.bug.BugViewModel;
import com.gregre.bbtopdie.db.Bug;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class MainActivity extends AppCompatActivity {

    public static final int NEW_BUG_ACTIVITY_REQUEST_CODE = 1;

    private BugViewModel mBugViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        final BugListAdapter adapter = new BugListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Get a new or existing ViewModel from the ViewModelProvider.
        mBugViewModel = new ViewModelProvider(this).get(BugViewModel.class);

        // Add an observer on the LiveData returned by getAlphabetizedBugs.
        // The onChanged() method fires when the observed data changes and the activity is
        // in the foreground.
        mBugViewModel.getAllBugs().observe(this, new Observer<List<Bug>>() {
            @Override
            public void onChanged(@Nullable final List<Bug> bugs) {
                // Update the cached copy of the bugs in the adapter.
                adapter.setBugs(bugs);
            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NewBugActivity.class);
                startActivityForResult(intent, NEW_BUG_ACTIVITY_REQUEST_CODE);
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NEW_BUG_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            Bug bug = new Bug(data.getStringExtra(NewBugActivity.EXTRA_REPLY),"0", "None", "None");
            mBugViewModel.insert(bug);
        } else {
            Toast.makeText(
                    getApplicationContext(),
                    R.string.empty_not_saved,
                    Toast.LENGTH_LONG).show();
        }
    }
}