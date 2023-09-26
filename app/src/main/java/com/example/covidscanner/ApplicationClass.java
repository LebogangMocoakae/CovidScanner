package com.example.covidscanner;

import android.app.Application;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;

import java.util.List;

public class ApplicationClass extends Application
{
    public static final String APPLICATION_ID = "140F5792-3547-36B6-FFB4-B095E9543500";
    public static final String API_KEY = "80ACBA1C-507D-45DC-98A9-EA5FFEBE99AC";
    public static final String SERVER_URL = "https://eu-api.backendless.com";

    public static BackendlessUser school;
    public static List<Learner> learners;
    public static List<Screening> screenings;

    @Override
    public void onCreate() {
        super.onCreate();

        Backendless.setUrl( SERVER_URL );
        Backendless.initApp( getApplicationContext(),
                APPLICATION_ID,
                API_KEY );
    }
}
