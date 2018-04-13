/*
 * Copyright (C) 2018 Marcus Pimenta
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.pimenta.bestv.manager;

import android.support.annotation.NonNull;

import com.pimenta.bestv.connector.TmdbConnector;
import com.pimenta.bestv.model.Movie;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by marcus on 05-03-2018.
 */
public class MovieManagerImpl implements MovieManager {

    private TmdbConnector mTmdbConnector;

    @Inject
    public MovieManagerImpl(TmdbConnector tmdbConnector) {
        mTmdbConnector = tmdbConnector;
    }

    @Override
    public boolean isFavorite(final Movie movie) {
        final Movie movieFind = Movie.getByTmdbId(movie.getTmdbId());
        if (movieFind != null) {
            movie.setId(movieFind.getId());
            return true;
        }
        return false;
    }

    @Override
    public boolean hasFavoriteMovie() {
        final List<Movie> favoriteMovies = Movie.getAll();
        return favoriteMovies != null ? favoriteMovies.size() > 0 : false;
    }

    @Override
    public boolean saveFavoriteMovie(@NonNull final Movie movie) {
        return movie.create() > 0;
    }

    @Override
    public boolean deleteFavoriteMovie(@NonNull final Movie movie) {
        return movie.delete() > 0;
    }

    @Override
    public List<Movie> getFavoriteMovies() {
        final List<Movie> favoriteMovies = Movie.getAll();
        final List<Movie> movies = new ArrayList<>();
        for (final Movie movie : favoriteMovies) {
            final Movie detailMovie = mTmdbConnector.getMovie(movie.getTmdbId());
            if (detailMovie != null) {
                detailMovie.setFavorite(true);
                movies.add(detailMovie);
            }
        }
        return movies;
    }
}