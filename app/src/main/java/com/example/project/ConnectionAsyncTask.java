package com.example.project;

import android.app.Activity;
import android.os.AsyncTask;

import java.util.List;

public class ConnectionAsyncTask extends AsyncTask<String, String, String> {

    Activity activity;

    public ConnectionAsyncTask(Activity activity) {
        this.activity = activity;
    }

    @Override
    protected String doInBackground(String... strings) {
        return HttpManager.getData(strings[0]);
    }

    @Override
    protected void onPostExecute(String s){
        super.onPostExecute(s);
        List<TravelDestination> destinations = DestinationJsonParser.getObjectFromJson(s);
        assert destinations != null;
        ((MainActivity) activity).fillDestinations(destinations);
    }
}
