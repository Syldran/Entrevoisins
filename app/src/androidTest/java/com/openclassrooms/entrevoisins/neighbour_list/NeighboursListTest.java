
package com.openclassrooms.entrevoisins.neighbour_list;

import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.openclassrooms.entrevoisins.R;
import com.openclassrooms.entrevoisins.ui.neighbour_list.ListNeighbourActivity;
import com.openclassrooms.entrevoisins.utils.DeleteViewAction;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.action.ViewActions.swipeRight;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.assertThat;
import static android.support.test.espresso.matcher.ViewMatchers.hasChildCount;
import static android.support.test.espresso.matcher.ViewMatchers.hasMinimumChildCount;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static com.openclassrooms.entrevoisins.utils.RecyclerViewItemCountAssertion.withItemCount;
import static com.openclassrooms.entrevoisins.utils.WaitViewAction.waitFor;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.core.IsNull.notNullValue;



/**
 * Test class for list of neighbours
 */
@RunWith(AndroidJUnit4.class)
public class NeighboursListTest {

    // This is fixed
    private static final int ITEMS_COUNT = 12;

    private ListNeighbourActivity mActivity;


    @Rule
    public ActivityTestRule<ListNeighbourActivity> mActivityRule =
            new ActivityTestRule(ListNeighbourActivity.class);


    @Before
    public void setUp() {
        mActivity = mActivityRule.getActivity();
        assertThat(mActivity, notNullValue());
    }

    /**
     * We ensure that our recyclerview is displaying at least on item
     */
    @Test
    public void myNeighboursList_shouldNotBeEmpty_butFavoriteList_shouldBe() {
        // First scroll to the position that needs to be matched and click on it.
        onView(allOf(ViewMatchers.withId(R.id.list_neighbours), ViewMatchers.isDisplayed()))
                .check(matches(hasMinimumChildCount(1)));

        onView(ViewMatchers.withId(R.id.container)).perform(swipeLeft());
        onView(isRoot()).perform(waitFor(1000));

        onView(allOf(ViewMatchers.withId(R.id.list_neighbours), ViewMatchers.isDisplayed()))
                .check(matches(hasChildCount(0)));

        onView(ViewMatchers.withId(R.id.container)).perform(swipeRight());
        onView(isRoot()).perform(waitFor(1000));
    }

    /**
     * When we delete an item, the item is no more shown
     */
    @Test
    public void myNeighboursList_deleteAction_shouldRemoveItem_andAddNeighbours_shouldAddItem() {
        // Given : We remove the element at position 2
        onView(allOf(ViewMatchers.withId(R.id.list_neighbours), ViewMatchers.isDisplayed()))
                .check(withItemCount(ITEMS_COUNT));
        // When perform a click on a delete icon
        onView(allOf(ViewMatchers.withId(R.id.list_neighbours), ViewMatchers.isDisplayed()))
                .perform(RecyclerViewActions.actionOnItemAtPosition(1, new DeleteViewAction()));
        // Then : the number of element is 11
        onView(allOf(ViewMatchers.withId(R.id.list_neighbours), ViewMatchers.isDisplayed()))
                .check(withItemCount(ITEMS_COUNT-1));

        // Click on button add_neighbour
        onView(ViewMatchers.withId(R.id.add_neighbour))
                .perform(click());
        // Check Activity_add_neighbour is opened
        assertThat(ViewMatchers.withId(R.layout.activity_add_neighbour), notNullValue());

        // New neighbour is created with name Fabien
        onView(ViewMatchers.withId(R.id.name)).perform(click());
        onView(ViewMatchers.withId(R.id.name)).perform(typeText("Fabien"));
        onView(isRoot()).perform(waitFor(1000));
        pressBack();
        onView(ViewMatchers.withId(R.id.create)).perform(click());

        // Check item count is incremented after add neighbour
        onView(allOf(ViewMatchers.withId(R.id.list_neighbours), ViewMatchers.isDisplayed()))
                .check(withItemCount(ITEMS_COUNT));


    }

    @Test
    public void favoriteListOnlyMadeOfFavorites() {
        //Open detail of neighbour at position 2
        onView(allOf(ViewMatchers.withId(R.id.list_neighbours), ViewMatchers.isDisplayed()))
                .perform(RecyclerViewActions.actionOnItemAtPosition(2, click()));
        //check Activity_detail_neighbour is opened with a neighbour name
        assertThat(ViewMatchers.withId(R.layout.activity_detail_neighbour), notNullValue());
        assertThat(ViewMatchers.withId(R.id.detail_name), notNullValue());
        //Click on star to add to favorites
        onView(ViewMatchers.withId(R.id.detail_favorite)).perform(click());
        pressBack();
        onView(isRoot()).perform(waitFor(1000));
        //Display favorite list
        onView(ViewMatchers.withId(R.id.container)).perform(swipeLeft());
        onView(isRoot()).perform(waitFor(1000));
        //check we do have one favorite now
        onView(allOf(ViewMatchers.withId(R.id.list_neighbours), ViewMatchers.isDisplayed()))
                .check(withItemCount(1));
        onView(ViewMatchers.withId(R.id.container)).perform(swipeRight());
        onView(isRoot()).perform(waitFor(1000));
    }

    public static void pressBack() {
        onView(isRoot()).perform(ViewActions.pressBack());
    }
}