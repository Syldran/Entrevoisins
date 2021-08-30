package com.openclassrooms.entrevoisins.service;

import com.openclassrooms.entrevoisins.di.DI;
import com.openclassrooms.entrevoisins.model.Neighbour;

import org.hamcrest.collection.IsIterableContainingInAnyOrder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * Unit test on Neighbour service
 */
@RunWith(JUnit4.class)
public class NeighbourServiceTest {

    private NeighbourApiService service;

    @Before
    public void setup() {
        service = DI.getNewInstanceApiService();
    }

    @Test
    public void getNeighboursWithSuccess() {
        //Check that getNeighbours() really return our data from DUMMY_NEIGHBOURS
        List<Neighbour> neighbours = service.getNeighbours();
        List<Neighbour> expectedNeighbours = DummyNeighbourGenerator.DUMMY_NEIGHBOURS;
        assertThat(neighbours, IsIterableContainingInAnyOrder.containsInAnyOrder(expectedNeighbours.toArray()));
    }

    @Test
    public void deleteNeighbourWithSuccess() {
        //Check the neighbour isn't contained anymore after being deleted
        Neighbour neighbourToDelete = service.getNeighbours().get(0);
        service.deleteNeighbour(neighbourToDelete);
        assertFalse(service.getNeighbours().contains(neighbourToDelete));
    }

    @Test
    public void addNeighbourWithSuccess() {
        //Check the neighbour is contained after being added
        Neighbour neighbourToAdd = new Neighbour(111,"Fabien",null,null,null,null);
        service.createNeighbour(neighbourToAdd);
        assertTrue(service.getNeighbours().contains(neighbourToAdd));
    }

    @Test
    public void putOrRemoveNeighbourFavoriteWithSuccess(){
        //Check neighbour at position 2 is now in favorite list after being put to favorite
        Neighbour neighbour = service.getNeighbours().get(2);
        service.putOrRemoveUserFromFavorite(neighbour,true);
        assertTrue(service.getFavoriteNeighbours().contains(neighbour));
        //Check neighbours at position 2 is not in favorite list anymore after losing its favorite status
        service.putOrRemoveUserFromFavorite(neighbour, false);
        assertFalse(service.getFavoriteNeighbours().contains(neighbour));
    }
}
