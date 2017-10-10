package com.android.popularmoviesapp.app.detail;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.popularmoviesapp.BR;
import com.android.popularmoviesapp.R;
import com.android.popularmoviesapp.data.MovieContract;
import com.android.popularmoviesapp.data.MovieDBUtils;
import com.android.popularmoviesapp.data.MovieProvider;
import com.android.popularmoviesapp.data.model.Movie;
import com.android.popularmoviesapp.data.model.Review;
import com.android.popularmoviesapp.data.model.Video;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by david.cardozo on 4/10/2017.
 */

public class DetailAdapter extends RecyclerView.Adapter<DetailAdapter.BaseDetailHolder> {

    private static final int HEADER_TYPE = 0;
    private static final int VIDEO_TYPE = 1;
    private static final int REVIEW_TYPE = 2;
    private Movie movie;
    private ContentResolver contentResolver;
    private Context ctx;
    private List<Video> videoList;
    private List<Review> reviewsList;

    public DetailAdapter(Movie movie, Context ctx) {
        this.movie = movie;
        contentResolver = ctx.getContentResolver();
        this.ctx = ctx;
    }

    @Override
    public BaseDetailHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ViewDataBinding binding;
        switch (viewType) {
            case HEADER_TYPE:
                binding = DataBindingUtil.inflate(layoutInflater, R.layout.item_movie_detail_header, parent, false);
                return new HeaderHolder(binding);
            case VIDEO_TYPE:
                binding = DataBindingUtil.inflate(layoutInflater, R.layout.item_video, parent, false);
                return new VideoHolder(binding);
            case REVIEW_TYPE:
                binding = DataBindingUtil.inflate(layoutInflater, R.layout.item_review, parent, false);
                return new ReviewHolder(binding);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(BaseDetailHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        int itemCount = 1;//header
        if (videoList != null) {
            itemCount += videoList.size();
        }
        if (reviewsList != null) {
            itemCount += reviewsList.size();
        }
        return itemCount;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return HEADER_TYPE;
        }
        int posotionsToIgnore = 1;
        if (videoList != null && position < (videoList.size() + 1)) {
            return VIDEO_TYPE;
        }
        return REVIEW_TYPE;
    }

    public void setVideos(List<Video> videos) {
        videoList = videos;
        notifyDataSetChanged();
    }

    public void setReviews(ArrayList<Review> reviews) {
        reviewsList = reviews;
        notifyDataSetChanged();
    }

    public abstract class BaseDetailHolder extends RecyclerView.ViewHolder {
        protected ViewDataBinding binding;

        public BaseDetailHolder(View itemView) {
            super(itemView);
        }

        public abstract void onBind(int position);
    }

    private class HeaderHolder extends BaseDetailHolder {

        public HeaderHolder(ViewDataBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            this.binding.setVariable(BR.listener, new OnFavoriteClickListener() {
                @Override
                public void onFavoriteClick() {
                    if (movie.isFavorite()) {
                        removeMovieFromFavorites();
                    } else {
                        addMovieToFavorites();
                    }
                }
            });
        }

        @Override
        public void onBind(int position) {
            binding.setVariable(BR.movie, movie);
            checkMovieOnFavorites();
        }

        private void addMovieToFavorites() {
            ContentValues moviesValues = MovieDBUtils.getContentValuesFromMovie(movie);
            Uri result = contentResolver.insert(MovieContract.MovieEntry.CONTENT_URI, moviesValues);
            if (result.getLastPathSegment().equalsIgnoreCase("" + movie.getId())) {
                movie.setFavorite(true);
                binding.invalidateAll();
                showSnackBar(R.string.added_favorites);
            }
        }

        private void removeMovieFromFavorites() {
            int resultDelete =
                contentResolver.delete(MovieProvider.getUriFromMovie(movie), null, null);
            if (resultDelete == 1) {
                movie.setFavorite(false);
                binding.invalidateAll();
                showSnackBar(R.string.removed_favorites);
            }
        }

        private void checkMovieOnFavorites() {
            Cursor cursorResult =
                contentResolver.query(MovieProvider.getUriFromMovie(movie), null, null, null, null);
            if (cursorResult != null && cursorResult.moveToFirst()) {
                Movie tempMovie = MovieDBUtils.cursorToMovie(cursorResult);
                if (tempMovie != null) {
                    movie.setFavorite(true);
                    binding.executePendingBindings();
                }
            }
        }
    }

    private void showSnackBar(int stringId) {
        Snackbar mySnackbar = Snackbar.make(((DetailActivity) ctx).findViewById(R.id.rv_detail_movie),
                stringId, Snackbar.LENGTH_SHORT);
        mySnackbar.show();
    }

    private class VideoHolder extends BaseDetailHolder {

        public VideoHolder(ViewDataBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            this.binding.setVariable(BR.listener, new VideoClickListener() {
                @Override
                public void onVideoClick(Video video, View view) {
                    Context ctx = view.getContext();
                    String video_path = ctx.getString(R.string.youtube_url) + video.getKey();
                    Uri uri = Uri.parse(video_path);
                    uri = Uri.parse("vnd.youtube:" + uri.getQueryParameter("v"));
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    ctx.startActivity(intent);
                }
            });
        }

        @Override
        public void onBind(int position) {
            Video video = videoList.get(position - 1);
            video.setLabel("Trailer " + position);
            video.setFirst(position == 1);
            binding.setVariable(BR.video, video);

        }
    }

    private class ReviewHolder extends BaseDetailHolder {

        public ReviewHolder(ViewDataBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        @Override
        public void onBind(int position) {
            int positionsToIgnore = 1;
            if (videoList != null) {
                positionsToIgnore += videoList.size();
            }
            Review review = reviewsList.get(position - positionsToIgnore);
            review.setFirst(position == positionsToIgnore);
            binding.setVariable(BR.review, review);
        }
    }

    public interface OnFavoriteClickListener {
        void onFavoriteClick();
    }

    public interface VideoClickListener {
        void onVideoClick(Video video, View view);
    }
}
