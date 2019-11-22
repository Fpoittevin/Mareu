package com.ocr.francois.mareu;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;


import com.ocr.francois.mareu.ui.MainActivity;
import com.ocr.francois.mareu.utils.DeleteViewAction;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.assertThat;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.notNullValue;

@RunWith(AndroidJUnit4.class)
public class MeetingsListTest {

    private MainActivity activity;

    @Rule
    public ActivityTestRule<MainActivity> activityRule =
            new ActivityTestRule(MainActivity.class);

    @Before
    public void setup() {
        activity = activityRule.getActivity();
        assertThat(activity, notNullValue());
    }

    @Test
    public void myNeighboursList_deleteAction_shouldRemoveItem() {
        RecyclerView recyclerView = activity.findViewById(R.id.fragment_meetings_list_recycler_view);
        int numberOfItems = recyclerView.getAdapter().getItemCount();

        // When perform a click on a delete icon
        onView(ViewMatchers.withId(R.id.fragment_meetings_list_recycler_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition(1, new DeleteViewAction()));
        // Then : the number of element is 11
        //onView(ViewMatchers.withId(R.id.fragment_meetings_list_recycler_view)).check(withItemCount(ITEMS_COUNT-1));
    }

    @Test
    public void meetingsList_clickItemAction_shouldDisplayMeetingDetailsFragment() {

        onView(withId(R.id.fragment_meetings_list_recycler_view)).perform(click());
        onView(withId(R.id.fragment_meeting_details)).check(matches(isDisplayed()));
    }

    @Test
    public void meetingsList_clickCreationFab_shouldStartMeetingCreationActivity() {

        onView(withId(R.id.activity_main_creation_fab)).perform(click());
        onView(withId(R.id.activity_meeting_creation_frame_layout)).check(matches(isDisplayed()));
    }
}