package com.openclassrooms.entrevoisins.service;

import com.openclassrooms.entrevoisins.model.Neighbour;

import java.util.ArrayList;
import java.util.List;

/**
 * Dummy mock for the Api
 */
public class DummyNeighbourApiService implements  NeighbourApiService {

    private List<Neighbour> neighbours = DummyNeighbourGenerator.generateNeighbours();


    /**
     * {@inheritDoc}
     */
    @Override
    public List<Neighbour> getNeighbours() {
        return neighbours;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteNeighbour(Neighbour neighbour) {
        neighbours.remove(neighbour);
    }

    /**
     * {@inheritDoc}
     * @param neighbour
     */
    @Override
    public void createNeighbour(Neighbour neighbour) {
        neighbours.add(neighbour);
    }

    @Override
    public List<Neighbour> getFavoriteNeighbours(){
        List<Neighbour> favorites = new ArrayList<Neighbour>(0);
        for (int i = 0; i < neighbours.size(); i++) {
            if (neighbours.get(i).isFavorite()) {
                favorites.add(neighbours.get(i));
            }
        }
        return favorites;
    }

    @Override
    public void putOrRemoveUserFromFavorite(Neighbour neighbour,boolean favorite) {
            neighbours.get(neighbours.indexOf(neighbour)).setFavorite(favorite);
    }
}
