package com.example.project;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUp extends AppCompatActivity {

    DataBaseHelper dataBaseHelper = new DataBaseHelper(SignUp.this,"R1190207", null,1);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        EditText nEmail = findViewById(R.id.emailAddress);
        EditText nPassword = findViewById(R.id.Password);
        EditText confirmation = findViewById(R.id.confirm);
        EditText fName = findViewById(R.id.FirstName);
        EditText lName = findViewById(R.id.lastName);
        TextView travelDestinations = findViewById(R.id.destinationsDropdown);
        TextView error = findViewById(R.id.error);

        String[] continents = {"Asia", "Africa", "Europe", "North America", "South America", "Antarctica", "Australia"};
        boolean[] selectedContinents = new boolean[continents.length];
        ArrayList<Integer> continentsList = new ArrayList<>();
        travelDestinations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SignUp.this);
                builder.setTitle("Choose your preferred travel continent:");
                builder.setCancelable(false);
                builder.setMultiChoiceItems(continents, selectedContinents, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        if(isChecked){
                            continentsList.add(which);
                            Collections.sort(continentsList);
                        }else{
                            continentsList.remove(Integer.valueOf(which));
                        }
                    }
                });
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.setNeutralButton("Clear All", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Arrays.fill(selectedContinents, false);
                        continentsList.clear();
                    }
                });
                builder.show();
            }
        });

        Button signUp = findViewById(R.id.SignUp);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final boolean[] somethingWrong = {false};
                final String[] errorMessage = new String[1];
                // Register the user
                User newUser = new User();
                GradientDrawable drawable = new GradientDrawable();
                drawable.setShape(GradientDrawable.RECTANGLE);
                drawable.setStroke(2, Color.RED);
                drawable.setCornerRadius(10f);
                Drawable d2 = fName.getBackground();
                if(fName.getText().toString().trim().length() < 3){
                    fName.setBackground(drawable);
                }else{
                    fName.setBackground(d2);
                }
                if (lName.getText().toString().trim().length() < 3) {
                    lName.setBackground(drawable);
                } else {
                    lName.setBackground(d2);
                }
                if (!isEmailValid(nEmail.getText().toString().trim())) {
                    nEmail.setBackground(drawable);
                } else {
                    nEmail.setBackground(d2);
                }
                if (!isPasswordValid(nPassword.getText().toString())) {
                    nPassword.setBackground(drawable);
                } else {
                    nPassword.setBackground(d2);
                }
                if (!isPasswordValid(confirmation.getText().toString()) || !confirmation.getText().toString().equals(nPassword.getText().toString())) {
                    confirmation.setBackground(drawable);
                } else {
                    confirmation.setBackground(d2);
                }

                if (fName.getText().toString().trim().length() < 3){
                    somethingWrong[0] = true;
                    errorMessage[0] = "First Name should be at least 3 characters.";
                }else{
                    newUser.setFirstName(fName.getText().toString().trim());
                }
                if (!somethingWrong[0]) {
                    if (lName.getText().toString().trim().length() < 3) {
                        somethingWrong[0] = true;
                        errorMessage[0] = "Last Name should be at least 3 characters.";
                    } else {
                        newUser.setLastName(lName.getText().toString().trim());
                    }
                    if (!somethingWrong[0]) {
                        if (isEmailValid(nEmail.getText().toString().trim())) {
                            newUser.setEmail(nEmail.getText().toString().trim());
                        } else {
                            somethingWrong[0] = true;
                            errorMessage[0] = "The email format is not correct.";
                        }
                        if (!somethingWrong[0]){
                            if (!isPasswordValid(nPassword.getText().toString())) {
                                somethingWrong[0] = true;
                                errorMessage[0] = "The password must contain at least one number, one lowercase letter, and one uppercase letter.";
                            } else {
                                if (!confirmation.getText().toString().equals(nPassword.getText().toString())) {
                                    somethingWrong[0] = true;
                                    errorMessage[0] = "The password does not match the confirmation.";
                                } else {
                                    newUser.setPassword(nPassword.getText().toString());
                                    if (continentsList.size() < 1){
                                        somethingWrong[0] = true;
                                        errorMessage[0] = "You have to choose at least one continent.";
                                    }else {
                                        dataBaseHelper.insertUser(newUser);
                                        // Add their favorite continents
                                        for (int i = 0; i < continents.length; i++) {
                                            if (selectedContinents[i]){
                                                dataBaseHelper.insertFavoriteContinents(newUser, continents[i]);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                if (somethingWrong[0]) {
                    error.setTextColor(Color.RED);
                    error.setText(errorMessage[0]);
                    Toast.makeText(SignUp.this, "Error in Entered Information", Toast.LENGTH_SHORT).show();
                }else{
                    error.setTextColor(Color.BLACK);
                    error.setText("Signing up is done successfully!");
                    fName.setText("");
                    lName.setText("");
                    nEmail.setText("");
                    nPassword.setText("");
                    confirmation.setText("");
                    continentsList.clear();
                    Arrays.fill(selectedContinents, false);
                }
            }
        });

        Button signIn = findViewById(R.id.haveAnAccount);
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUp.this,MainActivity.class));
            }
        });
    }

    static boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    static boolean isPasswordValid(String password) {
        Pattern pattern = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z]).{8,15}$");
        Matcher matcher = pattern.matcher(password);
        return matcher.find();
    }
}