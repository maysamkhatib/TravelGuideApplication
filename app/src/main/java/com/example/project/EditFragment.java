package com.example.project;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public EditFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EditFragment newInstance(String param1, String param2) {
        EditFragment fragment = new EditFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    DataBaseHelper dataBaseHelper;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        dataBaseHelper = new DataBaseHelper(getActivity(),"R1190207", null,1);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_edit, container, false);
        Button cancel = v.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().beginTransaction().replace(R.id.frameLayout, new ProfileFragment()).commit();
            }
        });
        EditText first = v.findViewById(R.id.peFirstName);
        first.setText(MainActivity.user.getFirstName());
        EditText last = v.findViewById(R.id.pelastName);
        last.setText(MainActivity.user.getLastName());
        EditText email = v.findViewById(R.id.peemailAddress);
        email.setText(MainActivity.user.getEmail());
        EditText password = v.findViewById(R.id.pePassword);
        password.setText(MainActivity.user.getPassword());
        EditText conf = v.findViewById(R.id.peconfirm);
        conf.setText(MainActivity.user.getPassword());
        TextView travelDestinations = v.findViewById(R.id.pedestinationsDropdown);
        String[] continents = {"Asia", "Africa", "Europe", "North America", "South America", "Antarctica", "Australia"};
        boolean[] selectedContinents = new boolean[continents.length];
        ArrayList<Integer> continentsList = new ArrayList<>();
        travelDestinations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
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

        Button save = v.findViewById(R.id.Save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final boolean[] somethingWrong = {false};
                // Register the user
                User newUser = new User();
                GradientDrawable drawable = new GradientDrawable();
                drawable.setShape(GradientDrawable.RECTANGLE);
                drawable.setStroke(2, Color.RED);
                drawable.setCornerRadius(10f);
                Drawable d2 = first.getBackground();
                if(first.getText().toString().trim().length() < 3){
                    first.setBackground(drawable);
                }else{
                    first.setBackground(d2);
                }
                if (last.getText().toString().trim().length() < 3) {
                    last.setBackground(drawable);
                } else {
                    last.setBackground(d2);
                }
                if (!SignUp.isEmailValid(email.getText().toString().trim())) {
                    email.setBackground(drawable);
                } else {
                    email.setBackground(d2);
                }
                if (!SignUp.isPasswordValid(password.getText().toString())) {
                    password.setBackground(drawable);
                } else {
                    password.setBackground(d2);
                }
                if (!SignUp.isPasswordValid(conf.getText().toString()) || !conf.getText().toString().equals(password.getText().toString())) {
                    conf.setBackground(drawable);
                } else {
                    conf.setBackground(d2);
                }

                if (first.getText().toString().trim().length() < 3){
                    somethingWrong[0] = true;
                }else{
                    newUser.setFirstName(first.getText().toString().trim());
                }
                if (!somethingWrong[0]) {
                    if (last.getText().toString().trim().length() < 3) {
                        somethingWrong[0] = true;
                    } else {
                        newUser.setLastName(last.getText().toString().trim());
                    }
                    if (!somethingWrong[0]) {
                        if (SignUp.isEmailValid(email.getText().toString().trim())) {
                            newUser.setEmail(email.getText().toString().trim());
                        } else {
                            somethingWrong[0] = true;
                        }
                        if (!somethingWrong[0]){
                            if (!SignUp.isPasswordValid(password.getText().toString())) {
                                somethingWrong[0] = true;
                            } else {
                                if (!conf.getText().toString().equals(password.getText().toString())) {
                                    somethingWrong[0] = true;
                                } else {
                                    newUser.setPassword(password.getText().toString());
                                    if (continentsList.size() < 1){
                                        somethingWrong[0] = true;
                                    }else {
                                        dataBaseHelper.editUser(MainActivity.user, newUser);
                                        dataBaseHelper.removeFavorites(newUser);
                                        // Add their favorite continents
                                        for (int i = 0; i < continents.length; i++) {
                                            if (selectedContinents[i]){
                                                dataBaseHelper.insertFavoriteContinents(newUser, continents[i]);
                                            }
                                        }
                                        MainActivity.user = newUser;
                                    }
                                }
                            }
                        }
                    }
                }
                if (somethingWrong[0]) {
                    Toast.makeText(getActivity(), "Error in Entered Information", Toast.LENGTH_SHORT).show();
                }else{
                    continentsList.clear();
                    Arrays.fill(selectedContinents, false);
                    getParentFragmentManager().beginTransaction().replace(R.id.frameLayout, new ProfileFragment()).commit();
                }
            }
        });

        return v;
    }
}