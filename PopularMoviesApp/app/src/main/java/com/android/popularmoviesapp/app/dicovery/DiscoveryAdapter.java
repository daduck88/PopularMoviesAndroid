package com.android.popularmoviesapp.app.dicovery;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.popularmoviesapp.R;
import com.android.popularmoviesapp.app.App;
import com.android.popularmoviesapp.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Daduck on 7/26/17.
 */

public class DiscoveryAdapter extends RecyclerView.Adapter<DiscoveryAdapter.ViewHolder> {
    private final OnDiscoverItemClickListener listener;
    private ArrayList<Movie> items = new ArrayList<>();
    private String urlImagePath;

    public interface OnDiscoverItemClickListener {
        void onItemClick(Movie movie);
    }

    public DiscoveryAdapter(OnDiscoverItemClickListener listener){
        this.listener = listener;
        this.urlImagePath = App.context.getString(R.string.url_image_thumb_path);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setItems(ArrayList<Movie> newItems) {
        items = newItems;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ImageView iVItemMovie;

        public ViewHolder(View itemView) {
            super(itemView);
            iVItemMovie = (ImageView) itemView.findViewById(R.id.iv_item_movie);
            itemView.setOnClickListener(this);
        }
        public void bind(Movie movie){
            String path = urlImagePath + movie.getPosterPath();
            Picasso.with(itemView.getContext()).load(path).into(iVItemMovie);
        }

        @Override
        public void onClick(View v) {
            Movie movie = items.get(getAdapterPosition());
            listener.onItemClick(movie);
        }
    }
}
