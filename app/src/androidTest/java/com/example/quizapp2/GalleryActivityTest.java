package com.example.quizapp2;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import static org.hamcrest.core.IsAnything.anything;
import static org.junit.Assert.assertEquals;

import android.widget.GridView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class GalleryActivityTest {

    // Test for adding a photo
    @Rule
    public ActivityScenarioRule<GalleryActivity> activityActivityScenarioRule = new ActivityScenarioRule<>(GalleryActivity.class);

    @Before
    public void setUp() {
        // Initialize Espresso-Intents before the test runs
        Intents.init();
    }

    @After
    public void tearDown() {
        // Release the intents after the tests have run
        Intents.release();
    }


    @Test
    public void TestAddPhoto() {

    }

    @Test
    public void TestDeletePhoto() {
        activityActivityScenarioRule.getScenario().onActivity(activity -> {
            GridView view = activity.findViewById(R.id.gridView);
            ImageAdapter adapter = (ImageAdapter) view.getAdapter();

            int count = adapter.getCount();

            onData(anything()).inAdapterView(withId(R.id.gridView))
                    .atPosition(0)
                    .perform(click());



            int afterCount = adapter.getCount();
            // verify that the count is decremented by 1 after deletion
            assertEquals(count - 1, afterCount);

        });





    }




}
