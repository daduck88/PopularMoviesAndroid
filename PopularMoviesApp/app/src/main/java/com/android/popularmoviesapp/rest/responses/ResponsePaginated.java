package com.android.popularmoviesapp.rest.responses;

import com.android.popularmoviesapp.data.model.Movie;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Daduck on 7/26/17.
 */

public class ResponsePaginated<K> {

    @SerializedName("page")
    @Expose
    private int page;

    @SerializedName("results")
    @Expose
    private ArrayList<K> results;

    @SerializedName("total_results")
    @Expose
    private long totalResults;

    @SerializedName("total_pages")
    @Expose
    private long totalPages;


    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public ArrayList<K> getResults() {
        return results;
    }

    public void setResults(ArrayList<K> results) {
        this.results = results;
    }

    public long getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(long totalResults) {
        this.totalResults = totalResults;
    }

    public long getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(long totalPages) {
        this.totalPages = totalPages;
    }
}
