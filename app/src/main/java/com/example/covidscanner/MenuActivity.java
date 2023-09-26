package com.example.covidscanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

// Student Number : initials Surname
// 217011326 NEP Constable
// 217008056 MR Motingoe
// 217010608 ZR Khondlo
// 211118141 TR Sihlobo
public class MenuActivity extends AppCompatActivity {

    Button btnScan, btnSeeAll, btnNewLearner;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        //tvLoad.setTExt("Busy signing you out...please wait...");
        Toast.makeText(MenuActivity.this, "Busy signing you out...please wait...", Toast.LENGTH_SHORT).show();

        switch (item.getItemId())
        {
            case R.id.logout:
                Backendless.UserService.logout(new AsyncCallback<Void>() {
                    @Override
                    public void handleResponse(Void response) {

                        //showProgress(false);
                        Toast.makeText(MenuActivity.this, "School signed out successfully!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(MenuActivity.this, Login.class));
                        MenuActivity.this.finish();
                    }

                    @Override
                    public void handleFault(BackendlessFault fault) {
                        Toast.makeText(MenuActivity.this, "Error: " + fault.getMessage(), Toast.LENGTH_SHORT).show();
                        //showProgress(false);
                    }
                });
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        btnScan = findViewById(R.id.btnScan);
        btnSeeAll = findViewById(R.id.btnSeeAll);
        btnNewLearner = findViewById(R.id.btnNewLearner);

        String userId = Backendless.UserService.loggedInUser();
        // To retrieve the school's name directly from Backendless, in order to display it on the ActionBar
        Backendless.UserService.findById(userId, new AsyncCallback<BackendlessUser>() {
            @Override
            public void handleResponse(BackendlessUser response) {

                String schoolName = response.getProperty("name") + "";
                ActionBar actionBar = getSupportActionBar();
                actionBar.setTitle(schoolName);
            }
            @Override
            public void handleFault(BackendlessFault fault) {
                Toast.makeText(MenuActivity.this, "Error: " + fault.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MenuActivity.this, RecordScreening.class));
            }
        });

        btnSeeAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MenuActivity.this, ScreeningList.class));
            }
        });

        btnNewLearner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MenuActivity.this, CreateLearner.class));
            }
        });
    }

}