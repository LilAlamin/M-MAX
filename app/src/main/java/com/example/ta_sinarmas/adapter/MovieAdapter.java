package com.example.ta_sinarmas.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ta_sinarmas.ItemMovie;
import com.example.ta_sinarmas.R;
import com.example.ta_sinarmas.activity.DetailMovieActivity;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MyViewHolder> {
    private Context context;
    private List<ItemMovie> resultList;

    public MovieAdapter(Context context, List<ItemMovie> resultList) {
        this.context = context;
        this.resultList = resultList;
    }

    @NonNull
    @Override
    public MovieAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        RelativeLayout view = (RelativeLayout) inflater.inflate(R.layout.item_movie, null);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieAdapter.MyViewHolder holder, int position) {
        holder.tvTitle.setText(resultList.get(position).getTitle());
        holder.tvDescription.setText(resultList.get(position).getOverview());
        Glide.with(context).load("https://image.tmdb.org/t/p/w500" + resultList.get(position).getPosterPath()).into(holder.imgposter);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the selected movie
                ItemMovie selectedItem = resultList.get(holder.getAdapterPosition());

                // Create Intent to open detail page
                Intent intent = new Intent(context, DetailMovieActivity.class);
                // Add selected movie data to Intent
                intent.putExtra("movieTitle", selectedItem.title);
                intent.putExtra("movieImagePath", selectedItem.posterPath);
                intent.putExtra("movieOverview", selectedItem.overview);
                // Start detail activity
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return resultList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imgposter;
        TextView tvTitle, tvDescription;
        public MyViewHolder(@NonNull RelativeLayout itemView) {
            super(itemView);
            imgposter = itemView.findViewById(R.id.imgMovie);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDescription = itemView.findViewById(R.id.tvDeskripsi);
        }
    }
}
