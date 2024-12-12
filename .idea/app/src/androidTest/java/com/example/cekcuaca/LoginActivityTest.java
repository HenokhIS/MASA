package com.example.cekcuaca;

import static androidx.test.espresso.Espresso.onView;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;

import android.view.View;

import androidx.test.espresso.ViewAction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class LoginActivityTest {

    @Rule
    public ActivityScenarioRule<LoginActivity> activityRule =
            new ActivityScenarioRule<>(LoginActivity.class);

    @Test
    public void testRegisterLoginAndMainFragment() {
        String username = "testuser";
        String password = "password123";

        // Navigate to Register screen
        onView(withId(R.id.btn_register)).perform(click());

        // Fill registration form
        onView(withId(R.id.et_usernameRegister)).perform(typeText(username), closeSoftKeyboard());
        onView(withId(R.id.et_passwordRegister)).perform(typeText(password), closeSoftKeyboard());
        onView(withId(R.id.btn_register2)).perform(click());

        // Perform login
        onView(withId(R.id.et_nim)).perform(typeText(username), closeSoftKeyboard());
        onView(withId(R.id.et_password)).perform(typeText(password), closeSoftKeyboard());
        onView(withId(R.id.btn_login)).perform(click());

        // Verify MainFragment UI
        onView(isRoot()).perform(waitFor(3000)); // Tunggu 3 detik
        onView(withId(R.id.idRLHome)).check(matches(isDisplayed()));
        onView(withId(R.id.idTVCityName)).check(matches(isDisplayed()));
        onView(withId(R.id.idRVWeather)).check(matches(isDisplayed()));
    }

    public static ViewAction waitFor(final long millis) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isRoot();
            }

            @Override
            public String getDescription() {
                return "wait for " + millis + " milliseconds";
            }

            @Override
            public void perform(UiController uiController, View view) {
                uiController.loopMainThreadForAtLeast(millis);
            }
        };
    }

    @Test
    public void testAccountFragmentUI() {
        // Navigate to Account Fragment
        onView(withId(R.id.account_btn)).perform(click());

        // Verify Account Detail section
        onView(withId(R.id.tvAccountDetail)).check(matches(isDisplayed()));
        onView(withId(R.id.tvAccountDetail)).check(matches(withText("Account Detail")));

        // Verify Username section
        onView(withId(R.id.tvUsernameLabel)).check(matches(isDisplayed()));
        onView(withId(R.id.tvUsernameLabel)).check(matches(withText("Username:")));
        onView(withId(R.id.tvUsername)).check(matches(isDisplayed()));

        // Verify Password section
        onView(withId(R.id.tvPasswordLabel)).check(matches(isDisplayed()));
        onView(withId(R.id.tvPasswordLabel)).check(matches(withText("Password:")));
        onView(withId(R.id.tvPassword)).check(matches(isDisplayed()));

        // Toggle password visibility
        onView(withId(R.id.ivShowPassword)).perform(click());
    }

    @Test
    public void testSettingsFragmentUI() {
        // Navigate to Settings Fragment
        onView(withId(R.id.settings_btn)).perform(click());

        // Change Username
        String newUsername = "newuser";
        onView(withId(R.id.editTextUsername)).perform(typeText(newUsername), closeSoftKeyboard());
        onView(withId(R.id.buttonChangeUsername)).perform(click());

        // Change Password
        String newPassword = "newpassword";
        onView(withId(R.id.editTextPassword)).perform(typeText(newPassword), closeSoftKeyboard());
        onView(withId(R.id.buttonChangePassword)).perform(click());

        // Verify Logout button functionality
        onView(withId(R.id.buttonLogout)).perform(click());
        onView(withId(R.id.btn_login)).check(matches(isDisplayed())); // Ensure redirected to login screen
    }
}
