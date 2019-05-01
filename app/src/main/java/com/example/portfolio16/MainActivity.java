package com.example.portfolio16;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<State> stateList = new ArrayList<>();
    private StateArrayAdapter stateArrayAdapter;
    private ListView stateLV;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        stateLV = findViewById(R.id.stateListView);
        stateArrayAdapter = new StateArrayAdapter(this, stateList);

        //associate the adapter
        stateLV.setAdapter(stateArrayAdapter);



    }//end onCreate

    //Convert JSON into array list
    private void convertJSONtoList(JSONObject states) {
        //Start off with a clean list
        stateList.clear();

        try {
            JSONArray list = states.getJSONArray("places");
            //loop to parse the JSON array into individual states
            for (int sub = 0; sub < list.length(); sub++) {
                JSONObject stateObj = list.getJSONObject(sub);
                stateList.add(new State(stateObj.getString("Place"), stateObj.getInt("Number")));
            }
        }
        catch (JSONException jse) {
            jse.printStackTrace();
        }

    } //end convert

    //inner asynctask class
    private class StateTask extends AsyncTask<URL, String, JSONObject> {
        @Override
        protected JSONObject doInBackground(URL... urls) {

            HttpURLConnection connection = null;

            try {
                connection = (HttpURLConnection)urls[0].openConnection();

                int responseCode = connection.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    //Read the information from the webpage line by line
                    StringBuilder stringBuilder = new StringBuilder();
                    try {
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                        String line;
                        while ((line = bufferedReader.readLine()) != null) {
                            stringBuilder.append(line);
                        }
                    }
                    catch (IOException ioe) {
                        publishProgress("Connection Error: input");
                        ioe.printStackTrace();
                    }
                    //Create and return the jsonobject
                    return new JSONObject(stringBuilder.toString());

                }
                else {
                    publishProgress("Connection Error: Bad URL");
                }
            }
            catch (Exception e) {
                publishProgress("Connection Error: Bad URL 2");
                e.printStackTrace();
            }

            finally {
                connection.disconnect();
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            Toast.makeText(MainActivity.this, values[0], Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            convertJSONtoList(jsonObject);

            stateArrayAdapter.notifyDataSetChanged();

            //Start at the beginning of the listview
            stateLV.smoothScrollToPosition(0);

        }
    }



    public void getData(View view) {
        String urlString = getString(R.string.web_url);

        try {
            URL url = new URL(urlString);
            //Create Async task
            StateTask stateTask = new StateTask();

            //start the async task
            stateTask.execute(url);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }



}
