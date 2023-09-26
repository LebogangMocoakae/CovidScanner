package com.example.covidscanner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;

import java.util.List;
// Student Number : initials Surname
// 217011326  NEP Constable
// 217008056 MR Motingoe
// 217010608 ZR Khondlo
// 211118141 TR Sihlobo
public class ScreeningList extends AppCompatActivity {

    private static int SCREEN_LIST = 201; // request code to start activity for a result from the menu activity
    private View mProgressView;
    private View mLoginFormView;
    private TextView tvLoad;
    String capturedData; // this will hold the barcode received from the camera
    RecyclerView screeningList;
    RecyclerView.Adapter myAdapter;
    RecyclerView.LayoutManager layoutManager;
    List<Learner> list;
    String name = "", surname = "", grade = "", code = "";
    TextView tvName_Sur, tvGrade2, tvCode2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screening_list);

        mLoginFormView = findViewById(R.id.screening_list);
        mProgressView = findViewById(R.id.list_progress);
        tvLoad = findViewById(R.id.tvLoadList);

        tvName_Sur = findViewById(R.id.tvName_Sur);
        tvGrade2 = findViewById(R.id.tvGrade2);
        tvCode2 = findViewById(R.id.tvCode2);

        screeningList = findViewById(R.id.screeningList);
        screeningList.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        screeningList.setLayoutManager(layoutManager);

        // starting MainActivity to get barcode from camera
        startActivityForResult(new Intent(ScreeningList.this, MainActivity.class), SCREEN_LIST);
    }

    // receiving the barcode, from the camera scanner
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == SCREEN_LIST)
        {
            if(resultCode == 1)
            {
                showProgress(true);
                tvLoad.setText(R.string.retrieving_data);
                capturedData = data.getStringExtra("barcode");

                String whereClause = "code = '" + capturedData + "'";

                // where clause to use in order to search in the database
                DataQueryBuilder queryBuilder = DataQueryBuilder.create();
                queryBuilder.setWhereClause(whereClause);
                queryBuilder.setGroupBy("name");

                // searching for barcode in database, on Learner table
                Backendless.Persistence.of(Learner.class).find(queryBuilder, new AsyncCallback<List<Learner>>() {
                    @Override
                    public void handleResponse(List<Learner> response) {

                        ApplicationClass.learners = response;
                        list = response;

                        for(int i = 0; i < list.size();i++)
                        {
                            name = list.get(i).getName().toString();
                            surname = list.get(i).getSurname().toString();
                            grade = list.get(i).getGrade().toString();
                            code = list.get(i).getCode().toString();
                        }
                        showProgress(false);
                        tvName_Sur.setText(name + " " + surname);
                        tvGrade2.setText(grade);
                        tvCode2.setText(code);
                    }

                    @Override
                    public void handleFault(BackendlessFault fault) {
                        Toast.makeText(ScreeningList.this, "Error: " + fault.getMessage(), Toast.LENGTH_SHORT).show();
                        showProgress(false);
                    }
                });

                tvLoad.setText("Getting all screenings...please wait...");
                String whereClause2 = "code = '" + capturedData + "'";
                // where clause to use in order to search in the database
                DataQueryBuilder queryBuilder2 = DataQueryBuilder.create();
                queryBuilder.setWhereClause(whereClause2);
                queryBuilder.setGroupBy("code");
                // searching for barcode in database, on Screening table that is linked to a Learner
                Backendless.Persistence.of(Screening.class).find(queryBuilder2, new AsyncCallback<List<Screening>>() {
                    @Override
                    public void handleResponse(List<Screening> response) {

                        ApplicationClass.screenings = response;
                        myAdapter = new ScreenAdapter(ScreeningList.this, ApplicationClass.screenings);
                        screeningList.setAdapter(myAdapter); // setting up the recyclerView
                        showProgress(false);
                    }

                    @Override
                    public void handleFault(BackendlessFault fault) {
                        Toast.makeText(ScreeningList.this, "Error: " + fault.getMessage(), Toast.LENGTH_SHORT).show();
                        showProgress(false);
                    }
                });
            }
        }
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });

            tvLoad.setVisibility(show ? View.VISIBLE : View.GONE);
            tvLoad.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    tvLoad.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            tvLoad.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
}