package com.example.project;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Comparator;

public class LayoutComparator implements Comparator<View> {
    public static boolean ascending = true;
    @Override
    public int compare(View v1, View v2) {
        TextView cost1 = v1.findViewById(R.id.costCVC);
        TextView cost2 = v2.findViewById(R.id.costCVC);
        if (ascending)
            return Integer.valueOf(cost1.getText().toString().split("\\$")[0]).compareTo(Integer.valueOf(cost2.getText().toString().split("\\$")[0]));
        return Integer.valueOf(cost2.getText().toString().split("\\$")[0]).compareTo(Integer.valueOf(cost1.getText().toString().split("\\$")[0]));
    }

}
