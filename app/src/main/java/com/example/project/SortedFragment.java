package com.example.project;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SortedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SortedFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SortedFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SortedFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SortedFragment newInstance(String param1, String param2) {
        SortedFragment fragment = new SortedFragment();
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
        dataBaseHelper = new DataBaseHelper(getContext(),"R1190207", null,1);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_sorted, container, false);
        LinearLayout toAdd = v.findViewById(R.id.addSorted);
        View Item;
        Cursor cursor = dataBaseHelper.getCitiesByCostAscending();
        List<View> Items = new ArrayList<>();
        while (cursor.moveToNext()){
            Item = inflater.inflate(R.layout.city_view_cost, toAdd, false);
            TextView city = Item.findViewById(R.id.cityCVC);
            TextView country = Item.findViewById(R.id.countryCVC);
            TextView cost = Item.findViewById(R.id.costCVC);
            ImageView fav = Item.findViewById(R.id.imageView8C);
            ImageView background = Item.findViewById(R.id.backgroundC);
            city.setText(cursor.getString(1));
            country.setText(cursor.getString(2));
            Glide.with(Item).load(cursor.getString(3)).into(background);
            cost.setText(cursor.getString(4) + "$");
            TransitionDrawable transitionDrawable = (TransitionDrawable) fav.getDrawable();
            int id = cursor.getInt(0);
            if (dataBaseHelper.isFavorite(MainActivity.user, id)){
                transitionDrawable.reverseTransition(0);
            }
            fav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (dataBaseHelper.isFavorite(MainActivity.user, id)){
                        dataBaseHelper.removeFavoritesById(MainActivity.user, dataBaseHelper.getIDByNameAndCountry(
                                (city.getText().toString()),
                                (country.getText().toString())
                        ));
                    }else{
                        dataBaseHelper.insertFavoritesById(MainActivity.user, dataBaseHelper.getIDByNameAndCountry(
                                (city.getText().toString()),
                                (country.getText().toString())
                        ));
                    }
                    transitionDrawable.reverseTransition(100);
                }
            });
            Item.setPadding(0,0,0,10);
            Item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HomeScreen.travelDestination = dataBaseHelper.getTravelDestination(
                            city.getText().toString(),
                            country.getText().toString());
                    startActivity(new Intent(getActivity(), DestinationActivity.class));
                }
            });
            Items.add(Item);
        }
        ImageView dollarSign = v.findViewById(R.id.dollarSign);
        Items.sort(new LayoutComparator());
        for (View item:Items){
            toAdd.addView(item);
        }
        dollarSign.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.asc_dollar));
        ToggleButton toggleButton = v.findViewById(R.id.toggle);
        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    dollarSign.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.desc_dollar));
                }else{
                    dollarSign.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.asc_dollar));
                }
                LayoutComparator.ascending = !isChecked;
                Items.sort(new LayoutComparator());
                toAdd.removeAllViews();
                for (View item:Items){
                    toAdd.addView(item);
                }
            }
        });
        return v;
    }
}