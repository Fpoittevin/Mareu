package com.ocr.francois.mareu;

import android.view.View;
import android.widget.DatePicker;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.contrib.PickerActions;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.ocr.francois.mareu.di.DI;
import com.ocr.francois.mareu.model.Meeting;
import com.ocr.francois.mareu.service.MeetingsSorter;
import com.ocr.francois.mareu.ui.MainActivity;
import com.ocr.francois.mareu.ui.MeetingsList.MeetingsListRecyclerViewAdapter;
import com.ocr.francois.mareu.utils.DeleteViewAction;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.TypeSafeMatcher;
import org.joda.time.LocalDate;
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
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withSubstring;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
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

        onView(ViewMatchers.withId(R.id.fragment_meetings_list_recycler_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition(1, new DeleteViewAction()));

        onView(ViewMatchers.withId(R.id.fragment_meetings_list_recycler_view)).check(withItemCount(numberOfItems - 1));
    }

    @Test
    public void meetingsList_clickItemAction_shouldDisplayMeetingDetailsFragment() {

        onView(withId(R.id.fragment_meetings_list_recycler_view)).perform(click());
        onView(withId(R.id.fragment_meeting_details)).check(matches(isDisplayed()));
    }

    @Test
    public void meetingsList_clickCreationFab_shouldStartMeetingCreationActivityAndCreateNewMeeting() {

        onView(withId(R.id.menu_meetings_list)).perform(click());
        onView(withText("toutes les réunions")).perform(click());

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
    public void meetingsList_clickSortByDatesButton_shouldSortItemsByDateTime() {
        onView(withId(R.id.menu_meetings_list)).perform(click());
        onView(withText("trier par dates")).perform(click());

        onView(withId(R.id.fragment_meetings_list_recycler_view)).check(matches(isSorted(MeetingsSorter.SortParam.DATETIMESTART)));
    }

    @Test
    public void meetingsList_clickSortByMeetingsButton_shouldSortItemsByMeetingRoom() {
        onView(withId(R.id.menu_meetings_list)).perform(click());
        onView(withText("trier par salles")).perform(click());

        onView(withId(R.id.fragment_meetings_list_recycler_view)).check(matches(isSorted(MeetingsSorter.SortParam.MEETINGROOM)));
    }

    @Test
    public void meetingsList_clickFilterByMeetingRooms_shouldFilterItemsByMeetingRoom() {
        onView(withId(R.id.menu_meetings_list)).perform(click());
        onView(withText("filtrer par salle")).perform(click());
        onView(withText("Réunion A")).perform(click());

        onView(withId(R.id.fragment_meetings_list_recycler_view))
                .check(matches(hasDescendant(withSubstring("Réunion A"))));
    }

    @Test
    public void meetingsList_clickFilterByDate_shouldFilterItemsByDate() {

        LocalDate date = new LocalDate(2019, 12, 10);

        onView(withId(R.id.menu_meetings_list)).perform(click());
        onView(withText("filtrer par date")).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(date.getYear(), date.getMonthOfYear(), date.getDayOfMonth()));
        onView(withText("OK")).perform(click());

        onView(withId(R.id.fragment_meetings_list_recycler_view))
                .check(matches(hasDescendant(withSubstring(date.toString(activity.getString(R.string.date_pattern))))));

    }

    @Test
    public void meetingsList_clickDisplayAllMeetings_shouldDisplayAllMeetings() {
        int numberOfMeetings = DI.getMeetingApiService().getMeetings().size();

        onView(withId(R.id.menu_meetings_list)).perform(click());
        onView(withText("filtrer par salle")).perform(click());
        onView(withText("Réunion A")).perform(click());

        onView(withId(R.id.menu_meetings_list)).perform(click());
        onView(withText("toutes les réunions")).perform(click());

        onView(ViewMatchers.withId(R.id.fragment_meetings_list_recycler_view)).check(withItemCount(numberOfMeetings));
    }
}