package com.example.ta_sinarmas.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.ta_sinarmas.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

public class DetailMovieActivity extends AppCompatActivity {
    TextView judul, deskripsi, rate;
    ImageView poster;
    private GoogleMap mMap;
    SupportMapFragment mapFragment;
    private FusedLocationProviderClient mFusedLocationProviderClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_movie);

        judul = findViewById(R.id.movie_title);
        deskripsi = findViewById(R.id.movie_overview);
        poster = findViewById(R.id.movie_poster);
        Button trailerButton = findViewById(R.id.btn_trailer);

        showMap();
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        trailerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Membuat string pencarian untuk Youtube
                String youtubeSearchQuery = "https://www.youtube.com/results?search_query=" + Uri.encode(judul.getText().toString() + " trailer");

                // Membuat intent untuk menampilkan hasil pencarian YouTube dalam browser
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(youtubeSearchQuery + "&autoplay=1"));

                // Memulai intent
                startActivity(intent);
            }
        });

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

    private void showMap() {
        final SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_fragment);
        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;

                // Lokasi Solo Paragon dan Grand Mall Solo
                LatLng soloParagon = new LatLng(-7.562458, 110.809975);
                LatLng grandMallSolo = new LatLng(-7.56635, 110.80737);
                LatLng soloSquare = new LatLng(-7.560813, 110.78862);
                LatLng transMart = new LatLng(-7.5605602,110.7676207);

                // Tambahkan penanda pada map
                mMap.addMarker(new MarkerOptions().position(soloParagon).title("Cinema XXI Solo Paragon"));
                mMap.addMarker(new MarkerOptions().position(grandMallSolo).title("Cinema XXI Grand Mall Solo"));
                mMap.addMarker(new MarkerOptions().position(soloSquare).title("Cinema XXI Solo Square"));
                mMap.addMarker(new MarkerOptions().position(transMart).title("CGV TransMart Pabelan"));

                // Aktifkan kontrol zoom dan dragging
                mMap.getUiSettings().setZoomControlsEnabled(true);
                mMap.getUiSettings().setScrollGesturesEnabled(true);

                // Buat batas yang mencakup semua lokasi di map
                final LatLngBounds.Builder builder = new LatLngBounds.Builder();
                builder.include(soloParagon);
                builder.include(grandMallSolo);
                builder.include(soloSquare);
                builder.include(transMart);

                // Cek dan minta ijin lokasi
                if (ActivityCompat.checkSelfPermission(DetailMovieActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(DetailMovieActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }

                mMap.setMyLocationEnabled(true); // Baris baru ini

                // Dapat lokasi terakhir
                mFusedLocationProviderClient.getLastLocation()
                        .addOnSuccessListener(new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                if (location != null) {
                                    // Tinggalkan baris ini jika Anda tidak ingin menambahkan marker di lokasi pengguna
                                    LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
//                                    mMap.addMarker(new MarkerOptions().position(userLocation).title("Lokasi Anda Saat Ini"));
                                    builder.include(userLocation);
                                }

                                supportMapFragment.getView().addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                                    @Override
                                    public void onLayoutChange(View v, int left, int top, int right, int bottom,
                                                               int oldLeft, int oldTop, int oldRight, int oldBottom) {
                                        v.removeOnLayoutChangeListener(this);

                                        final LatLngBounds bounds = builder.build();
                                        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
                                    }
                                });
                            }
                        });
            }
        });
    }





}
