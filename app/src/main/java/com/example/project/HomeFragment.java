package com.example.project;

import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        TextView city = v.findViewById(R.id.cityHome);
        TextView country = v.findViewById(R.id.countryCV);
        ImageView image = v.findViewById(R.id.cityImageHome);
        TextView continent = v.findViewById(R.id.continentName);
        TextView longitude = v.findViewById(R.id.longitudeHome);
        TextView latitude = v.findViewById(R.id.latitudeHome);
        TextView cost = v.findViewById(R.id.costOfLivingHome);
        TextView desc = v.findViewById(R.id.descritionHome);
        int nOfCities = dataBaseHelper.getNumberOfCitiesFromFavoriteContinents(MainActivity.user);
        if (nOfCities < 1){
            LinearLayout linearLayout = (LinearLayout) v.findViewById(R.id.homeContentLayout);
            for (int i = 0; i < linearLayout.getChildCount(); i++) {
                View child = linearLayout.getChildAt(i);
                if (child instanceof TextView) {
                    ((TextView) child).setText("");
                }
            }
            continent.setText("Sorry, there is no cities from your favorite continent!\nCome later!");
        }else {
            int random = new Random().nextInt(nOfCities);
            int i = 0;
            Cursor cursor = dataBaseHelper.getFavoriteContinentCities(MainActivity.user);
            while (cursor.moveToNext()) {
                if (i++ == random) {
                    city.setText(cursor.getString(1));
                    country.setText(cursor.getString(2));
                    continent.setText(cursor.getString(3));
                    longitude.setText(String.valueOf(cursor.getDouble(4)));
                    latitude.setText(String.valueOf(cursor.getDouble(5)));
                    cost.setText(String.valueOf(cursor.getInt(6)));
                    Glide.with(v).load(cursor.getString(7)).into(image);
                    desc.setText(cursor.getString(8));
                    break;
                }
            }
        }
        return v;
    }
}