package com.android.popularmoviesapp.data;

import android.support.test.runner.AndroidJUnit4;

import com.android.popularmoviesapp.R;
import com.android.popularmoviesapp.app.App;
import com.android.popularmoviesapp.data.model.Movie;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

/**
 * Created by Daduck on 8/25/17.
 */

@RunWith(AndroidJUnit4.class)
public class TestDataBase {

    @Test
    public void dataBase_CRUD() throws Exception {
        Movie movie = TestStaticMovies.getMovie1();
        boolean createSuccessful = MovieDBUtils.addMovie(movie);
        assertTrue("Not create movie " + movie.getTitle(), createSuccessful);

        Movie movie2 = TestStaticMovies.getMovie2();
        createSuccessful = MovieDBUtils.addMovie(movie2);
        assertTrue("Not create movie " + movie2.getTitle(), createSuccessful);

        Movie movie3 = TestStaticMovies.getMovie3();
        createSuccessful = MovieDBUtils.addMovie(movie3);
        assertTrue("Not create movie " + movie3.getTitle(), createSuccessful);

        Movie movie1 = MovieDBUtils.getMovie(movie.getId());
        assertNotNull("Not found movie " + movie.getTitle(), movie1);

        List<Movie> movies = MovieDBUtils.getMovies();

        assertEquals("DB not match", 3, movies.size());

        boolean deleteSuccessful = MovieDBUtils.deleteMovie(movie);
        assertTrue("Not delete movie " + movie.getTitle(), deleteSuccessful);
        deleteSuccessful = MovieDBUtils.deleteMovie(movie2);
        assertTrue("Not delete movie " + movie2.getTitle(), deleteSuccessful);
        deleteSuccessful = MovieDBUtils.deleteMovie(movie3);
        assertTrue("Not delete movie " + movie3.getTitle(), deleteSuccessful);
        movie1.setId(0);
        deleteSuccessful = MovieDBUtils.deleteMovie(movie1);
        assertTrue("Not delete movie " + movie1.getTitle(), deleteSuccessful);
    }
}
