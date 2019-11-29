package com.ocr.francois.mareu;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.ocr.francois.mareu.model.Meeting;
import com.ocr.francois.mareu.service.MeetingsSorter;
import com.ocr.francois.mareu.ui.MainActivity;
import com.ocr.francois.mareu.ui.MeetingsList.MeetingsListRecyclerViewAdapter;
import com.ocr.francois.mareu.utils.DeleteViewAction;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.assertThat;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static com.ocr.francois.mareu.utils.RecyclerViewItemCountAssertion.withItemCount;
import static org.hamcrest.Matchers.notNullValue;

@RunWith(AndroidJUnit4.class)
public class MeetingsListTest {

    @Rule
    public ActivityTestRule<MainActivity> activityRule =
            new ActivityTestRule(MainActivity.class);
    private MainActivity activity;

    private static Matcher<View> isSorted(final MeetingsSorter.SortParam sortParam) {

        return new TypeSafeMatcher<View>() {

            @Override
            protected boolean matchesSafely(View item) {

                RecyclerView recyclerView = (RecyclerView) item;
                MeetingsListRecyclerViewAdapter adapter = (MeetingsListRecyclerViewAdapter) recyclerView.getAdapter();

                List<Meeting> meetings = adapter.getMeetings();
                boolean result = false;

                for (int i = 0; i < meetings.size() - 1; i++) {
                    switch (sortParam) {
                        case MEETINGROOM:
                            result = meetings.get(i + 1).getMeetingRoom().compareTo(meetings.get(i).getMeetingRoom()) >= 0;
                            break;

                        case DATETIMESTART:
                            result = meetings.get(i + 1).getDateTimeStart().isAfter(meetings.get(i).getDateTimeStart())
                                    || meetings.get(i + 1).getDateTimeStart().equals(meetings.get(i).getDateTimeStart());
                            break;
                    }
                }

                return result;
            }

            @Override
            public void describeTo(Description description) {

            }
        };
    }

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
        onView(ViewMatchers.withId(R.id.fragment_meetings_list_recycler_view)).check(withItemCount(numberOfItems - 1));
    }

    @Test
    public void meetingsList_clickItemAction_shouldDisplayMeetingDetailsFragment() {

        onView(withId(R.id.fragment_meetings_list_recycler_view)).perform(click());
        onView(withId(R.id.fragment_meeting_details)).check(matches(isDisplayed()));
    }

    @Test
    public void meetingsList_clickCreationFab_shouldStartMeetingCreationActivityAndCreateNewMeeting() {

        RecyclerView recyclerView = activity.findViewById(R.id.fragment_meetings_list_recycler_view);
        int numberOfItems = recyclerView.getAdapter().getItemCount();

        onView(withId(R.id.activity_main_creation_fab)).perform(click());
        onView(withId(R.id.activity_meeting_creation_frame_layout)).check(matches(isDisplayed()));

        onView(withId(R.id.fragment_meeting_creation_subject_edit_text)).perform(replaceText("Le sujet de la réunion"), closeSoftKeyboard());

        onView(withId(R.id.fragment_meeting_creation_add_participant_edit_text)).perform(replaceText("test@email.com"), closeSoftKeyboard());
        onView(withId(R.id.fragment_meeting_creation_add_participant_button)).perform(click());

        onView(withId(R.id.menu_meeting_creation_save)).perform(click());

        onView(ViewMatchers.withId(R.id.fragment_meetings_list_recycler_view)).check(withItemCount(numberOfItems + 1));
    }

    @Test
    public void meetingsList_clickSortButton_shouldSortItemsByDateTime() {
        activity.meetingsListFragment.sortParam = MeetingsSorter.SortParam.MEETINGROOM;

        onView(withId(R.id.menu_meetings_list_sort)).perform(click());
        onView(withId(R.id.fragment_meetings_list_recycler_view)).check(matches(isSorted(MeetingsSorter.SortParam.DATETIMESTART)));
    }

    @Test
    public void meetingsList_clickSortButton_shouldSortItemsByMeetingRoom() {
        activity.meetingsListFragment.sortParam = MeetingsSorter.SortParam.DATETIMESTART;

        onView(withId(R.id.menu_meetings_list_sort)).perform(click());
        onView(withId(R.id.fragment_meetings_list_recycler_view)).check(matches(isSorted(MeetingsSorter.SortParam.MEETINGROOM)));
    }
}