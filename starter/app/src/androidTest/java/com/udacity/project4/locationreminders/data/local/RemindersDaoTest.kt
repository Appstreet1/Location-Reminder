package com.udacity.project4.locationreminders.data.local

import android.provider.CalendarContract
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.SmallTest;
import com.udacity.project4.locationreminders.data.dto.ReminderDTO

import org.junit.Before;
import org.junit.Rule;
import org.junit.runner.RunWith;

import kotlinx.coroutines.ExperimentalCoroutinesApi;
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Test

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
//Unit test the DAO
@SmallTest
class RemindersDaoTest {

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: RemindersDatabase

    @Before
    fun initDb() {
        // Using an in-memory database so that the information stored here disappears when the
        // process is killed.
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(), RemindersDatabase::class.java
        ).build()
    }

    @After
    fun closeDb() = database.close()

    @Test
    fun saveReminder_andGetById() = runBlockingTest {

        //Given
        val reminder = ReminderDTO("title", "desc", "loc", 0.0, 0.0)

        database.reminderDao().saveReminder(reminder)

        //When
        val loaded = database.reminderDao().getReminderById(reminder.id)

        //Then
        assertThat<ReminderDTO>(loaded as ReminderDTO, notNullValue())
        assertThat(loaded.id, `is` (reminder.id))
        assertThat(loaded.title, `is`(reminder.title))
        assertThat(loaded.description, `is`(reminder.description))
    }

    @Test
    fun getReminders() = runBlockingTest {
        //Given
        val reminders = database.reminderDao().getReminders()

        //When
        val loaded = database.reminderDao().getReminders()

        //Then
        assertThat(loaded, `is`(reminders))
    }

    @Test
    fun noReminderFound_shouldReturnError() = runBlockingTest{
        //Given
        val reminder = ReminderDTO("title", "desc", "loc", 0.0, 0.0, "Id")

        //When
        val loaded = database.reminderDao().getReminderById(reminder.id)

        //Then
        assertThat(loaded, `is`(nullValue()))
    }
}