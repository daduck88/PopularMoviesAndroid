package com.android.popularmoviesapp.data;

import com.android.popularmoviesapp.data.model.Movie;

/**
 * Created by Daduck on 9/12/17.
 */

public class TestStaticMovies {
    public static Movie getMovie1(){
        Movie movie1 = new Movie();
        movie1.setId(321612);
        movie1.setTitle("Beauty and the Beast");
        movie1.setPosterPath("/tWqifoYuwLETmmasnGHO7xBjEtt.jpg");
        return movie1;
    }
    public static Movie getMovie2(){
        Movie movie2 = new Movie();
        movie2.setId(211672);
        movie2.setTitle("Minions");
        movie2.setPosterPath("/q0R4crx2SehcEEQEkYObktdeFy.jpg");
        return movie2;
    }
    public static Movie getMovie3(){
        Movie movie3 = new Movie();
        movie3.setId(315635);
        movie3.setTitle("Spider-Man: Homecoming");
        movie3.setPosterPath("/c24sv2weTHPsmDa7jEMN0m2P3RT.jpg");
        return movie3;
    }

}
