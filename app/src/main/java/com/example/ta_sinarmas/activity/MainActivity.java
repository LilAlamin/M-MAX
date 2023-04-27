package com.example.ta_sinarmas.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;

import com.example.ta_sinarmas.ConfirmLogout;
import com.example.ta_sinarmas.ItemMovie;
import com.example.ta_sinarmas.JSONRequest;
import com.example.ta_sinarmas.LoginActivity;
import com.example.ta_sinarmas.R;
import com.example.ta_sinarmas.adapter.MovieAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new MainTask(this).execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //Logout
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                Intent logout = new Intent(MainActivity.this, ConfirmLogout.class);
                startActivity(logout);
                return true;

        }
        return false;
    }
}

class MainTask extends AsyncTask<Void, Void, JSONObject> {
    private Activity activity;
    public MainTask(Activity activity){
        this.activity = activity;
    }
    @Override
    public JSONObject doInBackground(Void... voids){
        JSONObject obj = new JSONRequest()
                .setMethod(JSONRequest.HTTP_GET)
                .setPath("3/movie/popular?api_key=7cd825774cde56bac9a76cd82c020963")
                .execute();
        // https://image.tmdb.org/t/p/w500
        return obj;
    }
    @Override
    public void onPostExecute(JSONObject obj){
        try {
            JSONArray datas = obj.getJSONArray("results");
            List<ItemMovie> list = new ArrayList<>();
            for(int i = 0; i < datas.length(); i++){
                JSONObject data = datas.getJSONObject(i);
                ItemMovie movie = new ItemMovie()
                        .setTitle(data.getString("original_title"))
                        .setOverview(data.getString("overview"))
                        .setPosterPath(data.getString("poster_path"));
                list.add(movie);
            }
            MovieAdapter adapter = new MovieAdapter(activity, list);
            RecyclerView view = activity.findViewById(R.id.rvMovie);
            view.setAdapter(adapter);
            view.setLayoutManager(new LinearLayoutManager(activity));
        } catch (Exception e) {
            Log.w("Error Main", e);
        }
    }
}