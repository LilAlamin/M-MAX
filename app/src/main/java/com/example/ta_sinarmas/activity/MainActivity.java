package com.example.ta_sinarmas.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;

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

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        // get reference to SearchView
        MenuItem searchItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        // set listener to perform search
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // perform search with query
                new SearchTask(MainActivity.this).execute(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return true;
    }

    //Logout
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setIcon(R.drawable.baseline_exit_to_app_24);
                builder.setTitle("Confirm Logout");
                builder.setMessage("Do You Want To Logout?");
                builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                builder.setNegativeButton("No",null);

                //Mengatur Warna Teks Yes
                SpannableString spannableString =new SpannableString("yes");
                spannableString.setSpan(new ForegroundColorSpan(Color.RED),0,spannableString.length(),0);
                builder.setPositiveButton(spannableString, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                });
                //Mengatur Warna Teks No
                SpannableString spannableNo = new SpannableString("No");
                spannableNo.setSpan(new ForegroundColorSpan(Color.RED),0,spannableNo.length(),0);
                builder.setNegativeButton(spannableNo, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        builder.setNegativeButton("No",null);
                    }
                });
                final AlertDialog alertDialog = builder.create();
                alertDialog.show();
                return true;

            case R.id.contact:
                String emailAddress = "ronaldrplb@gmail.com";
                String subject = "Subject Email";
                String body = "Isi email...";
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:" + emailAddress));
                intent.putExtra(Intent.EXTRA_SUBJECT, subject);
                intent.putExtra(Intent.EXTRA_TEXT, body);
                startActivity(intent);
                return true;

//            case R.id.support:
//                Intent sup = new Intent(MainActivity.this, SupportActivity.class);
//                startActivity(sup);
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
class SearchTask extends AsyncTask<String, Void, JSONObject> {
    private Activity activity;
    public SearchTask(Activity activity){
        this.activity = activity;
    }

    @Override
    protected JSONObject doInBackground(String... params) {
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
            Log.w("Error Main", e);
        }
    }
}