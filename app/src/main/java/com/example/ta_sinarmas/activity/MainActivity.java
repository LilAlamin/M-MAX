package com.example.ta_sinarmas.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.Toast;

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
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        new MainTask(this).execute();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                new SearchTask(MainActivity.this).execute(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

//    @Override
//    public void onBackPressed() {
//        MenuItem searchItem = menu.findItem(R.id.search);
//        SearchView searchView = (SearchView) searchItem.getActionView();
//
//        if (searchView != null && !searchView.isIconified()) { // check if search view is open and not iconified
//            searchView.setQuery("", false); // reset the text in the search view
//            searchView.setIconified(true); // close the search view
//            class MainTask extends AsyncTask<Void, Void, JSONObject> {
//                private Activity activity;
//                public MainTask(Activity activity){
//                    this.activity = activity;
//                }
//                @Override
//                public JSONObject doInBackground(Void... voids){
//                    JSONObject obj = new JSONRequest()
//                            .setMethod(JSONRequest.HTTP_GET)
//                            .setPath("3/movie/popular?api_key=7cd825774cde56bac9a76cd82c020963")
//                            .execute();
//                    // https://image.tmdb.org/t/p/w500
//                    return obj;
//                }
//                @Override
//                public void onPostExecute(JSONObject obj){
//                    try {
//                        JSONArray datas = obj.getJSONArray("results");
//                        List<ItemMovie> list = new ArrayList<>();
//                        for(int i = 0; i < datas.length(); i++){
//                            JSONObject data = datas.getJSONObject(i);
//                            ItemMovie movie = new ItemMovie()
//                                    .setTitle(data.getString("original_title"))
//                                    .setOverview(data.getString("overview"))
//                                    .setPosterPath(data.getString("poster_path"));
//                            list.add(movie);
//                        }
//                        MovieAdapter adapter = new MovieAdapter(activity, list);
//                        RecyclerView view = activity.findViewById(R.id.rvMovie);
//                        view.setAdapter(adapter);
//                        view.setLayoutManager(new LinearLayoutManager(activity));
//                    } catch (Exception e) {
//                        Log.w("Error Main", e);
//                    }
//                }
//            }
//            new MainTask(this).execute();
//        } else {
//            super.onBackPressed(); // finish the activity and return to the previous screen
//        }
//
//
//        super.onBackPressed();
//    }

    //Logout
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                //Membuat Pilihan Dan action pada alert box
//
//                builder.setMessage(R.string.dialog_message).setTitle(R.string.title_message);
//
//                builder.setMessage("Apakah Anda Ingin Logout?")
//                        .setCancelable(false)
//                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                Intent logout = new Intent(MainActivity.this,LoginActivity.class);
//                                startActivity(logout);
//                                Toast.makeText(MainActivity.this,"Anda akan Logout",Toast.LENGTH_SHORT);
//                            }
//                        })
//                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.cancel();
//                                Toast.makeText(MainActivity.this,"Anda Tidak Jadi Logout",Toast.LENGTH_SHORT);
//                            }
//                        });

                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setIcon(R.drawable.baseline_exit_to_app_24);
                builder.setTitle(R.string.title_message);
                builder.setMessage(R.string.dialog_message);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent logout = new Intent(MainActivity.this,LoginActivity.class);
                        startActivity(logout);
                        Toast.makeText(MainActivity.this,"Anda Berhasil Logout",Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                //Buat Dialog Box
                AlertDialog alertDialog = builder.create();
                alertDialog.setTitle("Logout");
                alertDialog.show();
                return true;

        }
        return false;
    }
}

//Menampilkan Data Dari TMDB
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

//fungsi memanggil data dari search TMDB
class SearchTask extends AsyncTask<String, Void, JSONObject> {
    private Activity activity;
    public SearchTask(Activity activity){
        this.activity = activity;
    }
    @Override
    public JSONObject doInBackground(String... params){
        String query = params[0];
        JSONObject obj = new JSONRequest()
                .setMethod(JSONRequest.HTTP_GET)
                .setPath("3/search/movie?api_key=7cd825774cde56bac9a76cd82c020963&query=" + query)
                .execute();
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
            Log.w("Error Search", e);
        }
    }
}