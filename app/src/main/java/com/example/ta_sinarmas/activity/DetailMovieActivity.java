package com.example.ta_sinarmas.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.ta_sinarmas.R;

public class DetailMovieActivity extends AppCompatActivity {
    TextView judul,deskripsi,rate;
    ImageView poster;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_movie);

        judul = findViewById(R.id.movie_title);
        deskripsi = findViewById(R.id.movie_overview);
        poster = findViewById(R.id.movie_poster);
        rate = findViewById(R.id.movie_rating);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if (bundle != null) {
            String judulFilm = bundle.getString("movieTitle");
            String overview_mov = bundle.getString("movieOverview");
            String image_path = bundle.getString("movieImagePath");
            String rating = bundle.getString("Rating");

            judul.setText(judulFilm);
            deskripsi.setText(overview_mov);
            rate.setText(rating);

            Glide.with(this)
                    .load("https://image.tmdb.org/t/p/w500" + image_path)
                    .into(poster);
        }
        



    }
}