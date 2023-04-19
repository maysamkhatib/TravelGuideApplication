package com.example.project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    DataBaseHelper dataBaseHelper = new DataBaseHelper(MainActivity.this,"R1190207", null,1);
    public static User user = new User();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // get the data
        ConnectionAsyncTask connectionAsyncTask = new ConnectionAsyncTask(MainActivity.this);
        connectionAsyncTask.execute("https://run.mocky.io/v3/d1a9c002-6e88-4d1e-9f39-930615876bca");
        // buttons and text views
        EditText email = findViewById(R.id.email);
        EditText password = findViewById(R.id.password);
        Button signIn = findViewById(R.id.signIn);
        Button signUp = findViewById(R.id.goToSignUp);
        CheckBox rememberMe = findViewById(R.id.rememberMe);
        rememberMe.setChecked(true);
        SharedPrefManager sharedPrefManager = SharedPrefManager.getInstance(this);
        if (!Objects.equals(sharedPrefManager.readString("email", "noValue"), "noValue")){
            email.setText(sharedPrefManager.readString("email", "noValue"));
        }
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String enteredEmail = email.getText().toString().trim();
                String enterPassword = password.getText().toString().trim();
                if (!dataBaseHelper.isRegistered(enteredEmail)){
                    Toast.makeText(MainActivity.this, "This email is not registered!", Toast.LENGTH_SHORT).show();
                }else if(!dataBaseHelper.correctSignIn(enteredEmail, enterPassword)){
                    Toast.makeText(MainActivity.this, "Incorrect password!", Toast.LENGTH_SHORT).show();
                }else{
                    if (rememberMe.isChecked())
                        sharedPrefManager.writeString("email", email.getText().toString().trim());
                    else
                        sharedPrefManager.writeString("email", "noValue");
                    user = dataBaseHelper.getUserByEmail(email.getText().toString().trim());
                    startActivity(new Intent(MainActivity.this, HomeScreen.class));
                }
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SignUp.class));
            }
        });
    }

    public void fillDestinations(List<TravelDestination> destinations) {
        for (int i = 0; i < destinations.size(); i++) {
            dataBaseHelper.insertTravelDestination(destinations.get(i));
        }
    }
}