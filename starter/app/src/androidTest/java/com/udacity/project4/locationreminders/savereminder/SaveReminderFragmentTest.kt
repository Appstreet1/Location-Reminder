package com.udacity.project4.locationreminders.savereminder

import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.testing.launchFragment
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import com.udacity.project4.R
import com.udacity.project4.locationreminders.reminderslist.ReminderListFragment
import com.udacity.project4.locationreminders.reminderslist.ReminderListFragmentDirections
import junit.framework.TestCase
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.verify

class SaveReminderFragmentTest {


    @Test
    fun saveReminderFragment_isDisplayed() {
        launchFragmentInContainer<SaveReminderFragment>(Bundle(), R.style.AppTheme)

        onView(withId(R.id.reminderTitle)).check(matches(isDisplayed()))
        onView(withId(R.id.reminderDescription)).check(matches(isDisplayed()))
        onView(withId(R.id.selectLocation)).check(matches(isDisplayed()))
        onView(withId(R.id.saveReminder)).check(matches(isDisplayed()))
    }

    @Test
    fun reminderNoLocation_displaySnackbar() {
        launchFragmentInContainer<SaveReminderFragment>(Bundle(), R.style.AppTheme)

        onView(withId(R.id.reminderTitle)).perform(replaceText("title"))
        onView(withId(R.id.reminderDescription)).perform(replaceText("desc"))
        onView(withId(R.id.saveReminder)).perform(click())

        val snackbarText = R.string.select_location
        onView(withText(snackbarText)).check(matches(isDisplayed()))
    }

    @Test
    fun selectLocationClicked_navigateToSelectLocationFragmnet() {
        val scenario = launchFragmentInContainer<SaveReminderFragment>(Bundle(), R.style.AppTheme)

        val navController = Mockito.mock(NavController::class.java)

        scenario.onFragment { Navigation.setViewNavController(it.view!!, navController) }


        onView(withId(R.id.selectLocation)).perform(click())

        verify(navController).navigate(SaveReminderFragmentDirections.actionSaveReminderFragmentToSelectLocationFragment())
    }
}