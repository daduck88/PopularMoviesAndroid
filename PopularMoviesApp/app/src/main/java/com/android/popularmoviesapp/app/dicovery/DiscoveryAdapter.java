package com.android.popularmoviesapp.app.dicovery;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.databinding.library.baseAdapters.BR;
import com.android.popularmoviesapp.R;
import com.android.popularmoviesapp.app.App;
import com.android.popularmoviesapp.model.Movie;

import java.util.ArrayList;

/**
 * Created by Daduck on 7/26/17.
 */

public class DiscoveryAdapter extends RecyclerView.Adapter<DiscoveryAdapter.ViewHolder> {
    private final OnDiscoverItemClickListener listener;
    private ArrayList<Movie> items = new ArrayList<>();

    public interface OnDiscoverItemClickListener {
        void onItemClick(Movie movie);
    }

    public DiscoveryAdapter(OnDiscoverItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ViewDataBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.item_movie, parent, false);
        return new ViewHolder(binding);
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

    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        private final ViewDataBinding binding;

        public ViewHolder(ViewDataBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            this.binding.setVariable(BR.listener, listener);
        }

        public void bind(Movie movie) {
            binding.setVariable(BR.movie, movie);
            binding.executePendingBindings();
        }
    }
}
