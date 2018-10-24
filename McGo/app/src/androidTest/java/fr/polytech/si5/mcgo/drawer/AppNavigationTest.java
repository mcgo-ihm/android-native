package fr.polytech.si5.mcgo.drawer;

import android.support.test.espresso.NoActivityResumedException;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;

import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import fr.polytech.si5.mcgo.R;
import fr.polytech.si5.mcgo.items.ItemsActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.DrawerActions.open;
import static android.support.test.espresso.contrib.DrawerMatchers.isClosed;
import static android.support.test.espresso.contrib.DrawerMatchers.isOpen;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static fr.polytech.si5.mcgo.TestUtils.getToolbarNavigationContentDescription;
import static fr.polytech.si5.mcgo.action.NavigationViewActions.navigateTo;
import static junit.framework.Assert.fail;

/**
 * Tests for the {@link DrawerLayout} layout component in {@link fr.polytech.si5.mcgo.items.ItemsActivity} which manages
 * navigation within the app.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class AppNavigationTest {

    /**
     * {@link ActivityTestRule} is a JUnit {@link Rule @Rule} to launch your activity under test.
     * <p>
     * <p>
     * Rules are interceptors which are executed for each test method and are important building
     * blocks of Junit tests.
     */
    @Rule
    public ActivityTestRule<ItemsActivity> mActivityTestRule =
            new ActivityTestRule<>(ItemsActivity.class);

    @Test
    public void clickOnFavoriteNavigationItem_ShowFavoriteScreen() {
        openFavoriteScreen();

        // Check that favorite Activity was opened.
        onView(withId(R.id.quick_order_navigation_menu_item)).check(matches(isDisplayed()));
    }

    @Test
    public void clickOnFoodNavigationItem_ShowsFoodScreen() {
        openFavoriteScreen();

        openFoodScreen();

        // Check that food Activity was opened.
        onView(withId(R.id.items_container)).check(matches(isDisplayed()));
    }

    @Test
    public void clickOnAndroidHomeIcon_OpensNavigation() {
        // Check that left drawer is closed at startup
        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT))); // Left Drawer should be closed.

        // Open Drawer
        onView(withContentDescription(getToolbarNavigationContentDescription(
                mActivityTestRule.getActivity(), R.id.toolbar))).perform(click());

        // Check if drawer is open
        onView(withId(R.id.drawer_layout))
                .check(matches(isOpen(Gravity.LEFT))); // Left drawer is open open.
    }

    @Test
    public void Favorite_backNavigatesToTasks() {
        openFavoriteScreen();

        // Press back to go back to the tasks list
        pressBack();

        // Check that Tasks Activity was restored.
        onView(withId(R.id.items_container)).check(matches(isDisplayed()));
    }

    @Test
    public void backFromTasksScreen_ExitsApp() {
        // From the tasks screen, press back should exit the app.
        assertPressingBackExitsApp();
    }

    @Ignore
    public void backFromFoodScreenAfterStats_ExitsApp() {
        // This test checks that TasksActivity is a parent of StatisticsActivity

        // Open the favorite screen
        openFavoriteScreen();

        // Open the food screen to restore the task
        openFoodScreen();

        // Pressing back should exit app
        assertPressingBackExitsApp();
    }

    private void assertPressingBackExitsApp() {
        try {
            pressBack();
            fail("Should kill the app and throw an exception");
        } catch (NoActivityResumedException e) {
            // Test OK
        }
    }

    private void openFoodScreen() {
        // Open Drawer to click on navigation item.
        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT))) // Left Drawer should be closed.
                .perform(open()); // Open Drawer

        // Start tasks list screen.
        onView(withId(R.id.nav_view))
                .perform(navigateTo(R.id.food_navigation_menu_item));
    }

    private void openOrdersScreen() {
        // Open Drawer to click on navigation item.
        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT))) // Left Drawer should be closed.
                .perform(open()); // Open Drawer

        // Start tasks list screen.
        onView(withId(R.id.nav_view))
                .perform(navigateTo(R.id.orders_navigation_menu_item));
    }

    private void openFavoriteScreen() {
        // Open Drawer to click on navigation item.
        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT))) // Left Drawer should be closed.
                .perform(open()); // Open Drawer

        // Start statistics screen.
        onView(withId(R.id.nav_view))
                .perform(navigateTo(R.id.quick_order_navigation_menu_item));
    }
}
