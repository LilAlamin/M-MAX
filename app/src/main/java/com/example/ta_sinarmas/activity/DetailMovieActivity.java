package com.example.ta_sinarmas.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.ta_sinarmas.R;

public class DetailMovieActivity extends AppCompatActivity {
    TextView judul, deskripsi, rate;
    ImageView poster;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_movie);

        judul = findViewById(R.id.movie_title);
        deskripsi = findViewById(R.id.movie_overview);
        poster = findViewById(R.id.movie_poster);

        // Menampilkan tombol back pada action bar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);

            Intent intent = getIntent();
            Bundle bundle = intent.getExtras();

            if (bundle != null) {
                String judulFilm = bundle.getString("movieTitle");
                String overview_mov = bundle.getString("movieOverview");
                String image_path = bundle.getString("movieImagePath");

                judul.setText(judulFilm);
                deskripsi.setText(overview_mov);

                Glide.with(this)
                        .load("https://image.tmdb.org/t/p/w500" + image_path)
                        .into(poster);
            }


        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            // Tombol back di action bar ditekan
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
