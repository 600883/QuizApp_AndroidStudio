package com.example.quizapp2;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.core.IsAnything.anything;
import static org.junit.Assert.assertEquals;

import android.view.View;
import android.widget.GridView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


    // Test for adding a photo

    @RunWith(AndroidJUnit4.class)
    public class GalleryActivityTest {

        @Rule
        public ActivityScenarioRule<GalleryActivity> activityRule =
                new ActivityScenarioRule<>(GalleryActivity.class);

        @Test
        public void clickImage_DeletesItFromGridView() {
            //  we have a GridView with images and the GridView has an id 'R.id.gridview'
            // and the delete operation happens immediately without any background processing

            // First we'll find out the initial count of images
            final int[] initialCount = {0};
            onView(withId(R.id.gridView)).perform(new ViewAction() {
                @Override
                public Matcher<View> getConstraints() {
                    return isAssignableFrom(GridView.class);
                }

                @Override
                public String getDescription() {
                    return "Get item count from GridView";
                }

                @Override
                public void perform(UiController uiController, View view) {
                    GridView gridView = (GridView) view;
                    initialCount[0] = gridView.getAdapter().getCount();
                }
            });

            // We perform a click on the first image
            onData(anything())
                    .inAdapterView(withId(R.id.gridView))
                    .atPosition(0)
                    .perform(click());

            // Now we check if the count is reduced by 1, assuming immediate delete
            onView(withId(R.id.gridView)).check(matches(hasItemCount(initialCount[0] - 1)));
        }

        private static Matcher<View> hasItemCount(final int expectedCount) {
            return new TypeSafeMatcher<View>() {
                @Override
                protected boolean matchesSafely(View view) {
                    return ((GridView) view).getCount() == expectedCount;
                }

                @Override
                public void describeTo(Description description) {
                    description.appendText("GridView should have " + expectedCount + " items");
                }
            };
        }
    }

