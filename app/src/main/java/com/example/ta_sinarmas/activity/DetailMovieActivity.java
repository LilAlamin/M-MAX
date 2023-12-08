package com.example.ta_sinarmas.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
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
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

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
                LatLng transMart = new LatLng(-7.5605602, 110.7676207);

                // Tambahkan penanda pada map
                mMap.addMarker(new MarkerOptions().position(soloParagon).title("Cinema XXI Solo Paragon"));
                mMap.addMarker(new MarkerOptions().position(grandMallSolo).title("Cinema XXI Grand Mall Solo"));
                mMap.addMarker(new MarkerOptions().position(soloSquare).title("Cinema XXI Solo Square"));
                mMap.addMarker(new MarkerOptions().position(transMart).title("CGV TransMart Pabelan"));

                // Aktifkan kontrol zoom dan dragging
                mMap.getUiSettings().setZoomControlsEnabled(true);
                mMap.getUiSettings().setScrollGesturesEnabled(true);

                // Cek dan minta ijin lokasi
                if (ActivityCompat.checkSelfPermission(DetailMovieActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(DetailMovieActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }

                // Dapatkan lokasi terakhir
                mFusedLocationProviderClient.getLastLocation()
                        .addOnSuccessListener(new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                if (location != null) {
                                    // Tambahkan lingkaran biru di lokasi pengguna
                                    LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
                                    mMap.addCircle(new CircleOptions()
                                            .center(userLocation)
                                            .radius(10)  // Adjust the radius as needed
                                            .strokeColor(Color.BLUE)
                                            .fillColor(Color.BLUE));

                                    // Geser kamera ke lokasi pengguna dan bioskop
                                    LatLngBounds.Builder builder = new LatLngBounds.Builder();
                                    builder.include(userLocation);
                                    builder.include(soloParagon);
                                    builder.include(grandMallSolo);
                                    builder.include(soloSquare);
                                    builder.include(transMart);

                                    LatLngBounds bounds = builder.build();
                                    int padding = 200; // Adjust padding as needed for increased zoom
                                    mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, padding));

                                    // Tampilkan lokasi dengan kalimat di TextView
                                    updateLocationText(location);

                                    // Tambahkan button untuk zoom ke lokasi pengguna
                                    mMap.setMyLocationEnabled(true);
                                    mMap.getUiSettings().setMyLocationButtonEnabled(true);
                                }
                            }
                        });
            }
        });
    }


    private void updateLocationText(Location location) {
        Geocoder geocoder = new Geocoder(DetailMovieActivity.this, Locale.getDefault());

        double longitude = location.getLongitude();
        double latitude = location.getLatitude();

        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String locationName = null;
        if (addresses != null && addresses.size() > 0) {
            locationName = addresses.get(0).getLocality();
        }

        TextView locationText = findViewById(R.id.text_location);
        locationText.setText(String.format("Lokasi Anda: %s", locationName != null ? locationName : "Tidak Diketahui"));
    }





}
