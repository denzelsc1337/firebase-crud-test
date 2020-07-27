package com.example.firebase_crud_test;

import com.google.firebase.database.FirebaseDatabase;

public class MyfirebaseApp extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
    //llamar la persistencia en el manifest
}
