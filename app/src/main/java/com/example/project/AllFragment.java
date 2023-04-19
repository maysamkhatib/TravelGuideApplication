package com.example.project;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AllFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AllFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AllFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AllFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AllFragment newInstance(String param1, String param2) {
        AllFragment fragment = new AllFragment();
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
        View v = inflater.inflate(R.layout.fragment_all, container, false);
        LinearLayout toAdd = v.findViewById(R.id.allLayout);
        Cursor cursor = dataBaseHelper.getCitiesByContinent();
        View layout = null, Item;
        String continent = "null";
        while (cursor.moveToNext()) {
            if (continent.equals("null") || !Objects.equals(cursor.getString(4), continent)) {
                if (!continent.equals("null"))
                    toAdd.addView(layout);
                continent = cursor.getString(4);
                layout = inflater.inflate(R.layout.continent_layout, toAdd, false);
                TextView cont = layout.findViewById(R.id.continentCL);
                cont.setText(continent);
            }
            LinearLayout addItem = layout.findViewById(R.id.items);
            Item = inflater.inflate(R.layout.city_view, toAdd, false);
            TextView city = Item.findViewById(R.id.cityCV);
            TextView country = Item.findViewById(R.id.countryCV);
            ImageView fav = Item.findViewById(R.id.imageView8);
            ImageView background = Item.findViewById(R.id.background);
            city.setText(cursor.getString(1));
            country.setText(cursor.getString(2));
            Glide.with(Item).load(cursor.getString(3)).into(background);
            TransitionDrawable transitionDrawable = (TransitionDrawable) fav.getDrawable();
            int id = cursor.getInt(0);
            if (dataBaseHelper.isFavorite(MainActivity.user, id)){
                transitionDrawable.reverseTransition(0);
            }
            fav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // to add
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
            addItem.addView(Item);
        }
        toAdd.addView(layout);
        return v;
    }
}