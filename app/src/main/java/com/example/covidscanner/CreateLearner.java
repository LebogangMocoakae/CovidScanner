package com.example.covidscanner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Build;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;

// Student Number : initials Surname
// 217011326  NEP Constable
// 217008056 MR Motingoe
// 217010608 ZR Khondlo
// 211118141 TR Sihlobo

public class CreateLearner extends AppCompatActivity {

    private View mProgressView;
    private View mCreateFormView;
    private TextView tvLoad;

    EditText etLearnerName, etLearnerSurname, etLearnerGrade;
    TextView tvScanBarcode;
    Button btnSubmitLearner;
    String name, surname, grade, capturedData = "";
    private static int CREATE_LEARNER = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_learner);

        mCreateFormView = findViewById(R.id.add_learner);
        mProgressView = findViewById(R.id.create_progress);
        tvLoad = findViewById(R.id.tvLoad);

        etLearnerName = findViewById(R.id.etLearnerName);
        etLearnerSurname = findViewById(R.id.etLearnerSurname);
        etLearnerGrade = findViewById(R.id.etLearnerGrade);
        tvScanBarcode = findViewById(R.id.tvScanBarcode);
        btnSubmitLearner = findViewById(R.id.btnSubmitLearner);

        tvScanBarcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivityForResult(new Intent(CreateLearner.this, MainActivity.class), CREATE_LEARNER);
            }
        });

        btnSubmitLearner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                name = etLearnerName.getText().toString().trim();
                surname = etLearnerSurname.getText().toString().trim();
                grade = etLearnerGrade.getText().toString().trim();
                if (name.isEmpty() || surname.isEmpty() || grade.isEmpty() || capturedData.isEmpty())
                {
                    Toast.makeText(CreateLearner.this, "Please enter all fields\nMake sure you've scanned barcode", Toast.LENGTH_SHORT).show();
                }
                else {
                    tvLoad.setText(R.string.loading_wait);
                    showProgress(true);

                    Learner learner = new Learner();
                    learner.setName(name);
                    learner.setSurname(surname);
                    learner.setGrade(grade);
                    learner.setCode(capturedData);
                    learner.setSchoolEmail(ApplicationClass.school.getEmail());

                    // saving to Learner table on database
                    Backendless.Persistence.save(learner, new AsyncCallback<Learner>() {
                        @Override
                        public void handleResponse(Learner response) {
                            Toast.makeText(CreateLearner.this, "Learner has been created!", Toast.LENGTH_SHORT).show();
                            showProgress(false);
                            CreateLearner.this.finish();
                        }

                        @Override
                        public void handleFault(BackendlessFault fault) {

                            Toast.makeText(CreateLearner.this, "Error: " + fault.getMessage(), Toast.LENGTH_SHORT).show();
                            showProgress(false);
                        }
                    });
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CREATE_LEARNER)
        {
            if(resultCode == 1)
            {
                showProgress(false);
                capturedData = data.getStringExtra("barcode");
                tvScanBarcode.setText(capturedData);
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

            mCreateFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mCreateFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mCreateFormView.setVisibility(show ? View.GONE : View.VISIBLE);
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
            mCreateFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
}