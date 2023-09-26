package com.example.covidscanner;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

// Student Number : initials Surname
// 217011326  NEP Constable
// 217008056 MR Motingoe
// 217010608 ZR Khondlo
// 211118141 TR Sihlobo

public class RecordScreening extends AppCompatActivity {

    private View mProgressView;
    private View mLoginFormView;
    private TextView tvLoad, tvN_S, tvGrade, tvCode;
    private static int CHECK_LEARNER = 201;
    String capturedData;
    boolean answer1 = false, answer2 = false, answer3 = false;
    List<Learner> list;
    CheckBox cbHighRisk, cbContact, cbSymptoms;
    EditText etEnterTemperature;
    Button btnSubmitTemp;
    String name = "", surname = "", grade = "", code = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_screening);

        mLoginFormView = findViewById(R.id.record_screen);
        mProgressView = findViewById(R.id.login_progress);
        tvLoad = findViewById(R.id.tvLoad);
        tvN_S = findViewById(R.id.tvN_S);
        tvGrade = findViewById(R.id.tvGrade);
        tvCode = findViewById(R.id.tvCode);

        cbHighRisk = findViewById(R.id.cbHighRisk);
        cbContact = findViewById(R.id.cbContact);
        cbSymptoms = findViewById(R.id.cbSymptoms);
        etEnterTemperature = findViewById(R.id.etEnterTemperature);
        btnSubmitTemp = findViewById(R.id.btnSubmitTemp);

        startActivityForResult(new Intent(RecordScreening.this, MainActivity.class), CHECK_LEARNER);

        btnSubmitTemp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (etEnterTemperature.getText().toString().isEmpty())
                {
                    Toast.makeText(RecordScreening.this, "Enter learner temperature!", Toast.LENGTH_SHORT).show();
                }
                else {
                    tvLoad.setText(R.string.submit_screening);
                    showProgress(true);
                    // creating screening object
                    Screening screening = new Screening();
                    screening.setSchoolEmail(ApplicationClass.school.getEmail()); // setting school email to Screening object

                    if(cbHighRisk.isChecked())
                    {
                        answer1 = true;
                    }
                    else {
                        answer1 = false;
                    }
                    if(cbContact.isChecked())
                    {
                        answer2 = true;
                    }
                    else {
                        answer2 = false;
                    }
                    if(cbSymptoms.isChecked())
                    {
                        answer3 = true;
                    }
                    else {
                        answer3 = false;
                    }

                    screening.setHighRiskCountry(answer1);
                    screening.setInContactWithCovidPeople(answer2);
                    screening.setHaveSymptoms(answer3);
                    screening.setCode(code); // assigning barcode of Learner to the Screening object
                    screening.setTemperature(Double.parseDouble(etEnterTemperature.getText().toString().trim()));

                    // saving to Screening table on database
                    Backendless.Persistence.save(screening, new AsyncCallback<Screening>() {
                        @Override
                        public void handleResponse(Screening response) {
                            Toast.makeText(RecordScreening.this, "Screening submitted!", Toast.LENGTH_SHORT).show();
                            showProgress(false);
                            RecordScreening.this.finish();
                        }

                        @Override
                        public void handleFault(BackendlessFault fault) {
                            Toast.makeText(RecordScreening.this, "Error: " + fault.getMessage(), Toast.LENGTH_SHORT).show();
                            showProgress(false);
                            RecordScreening.this.finish();
                        }
                    });
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CHECK_LEARNER)
        {
            if(resultCode == 1)
            {
                showProgress(true);
                tvLoad.setText(R.string.retrieving_data);
                capturedData = data.getStringExtra("barcode");

                String whereClause = "code = '" + capturedData + "'";

                DataQueryBuilder queryBuilder = DataQueryBuilder.create();
                queryBuilder.setWhereClause(whereClause);
                queryBuilder.setGroupBy("code");

                // searching for barcode in database, on Learner table
                Backendless.Persistence.of(Learner.class).find(queryBuilder,  new AsyncCallback<List<Learner>>() {
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
                        tvN_S.setText(name + " " + surname);
                        tvGrade.setText("Grade " + grade);
                        tvCode.setText(code);
                    }

                    @Override
                    public void handleFault(BackendlessFault fault) {

                        Toast.makeText(RecordScreening.this, "Error: " + fault.getMessage(), Toast.LENGTH_SHORT).show();
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