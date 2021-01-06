package com.udacity.project4.locationreminders.savereminder

import android.content.Context
import android.os.Build
import android.provider.Settings.Global.getString
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.udacity.project4.R
import com.udacity.project4.locationreminders.MainCoroutineRule
import com.udacity.project4.locationreminders.data.FakeDataSource
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.getOrAwaitValue
import com.udacity.project4.locationreminders.reminderslist.ReminderDataItem
import kotlinx.coroutines.Dispatchers
import org.hamcrest.MatcherAssert.assertThat


import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.nullValue
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.stopKoin
import org.robolectric.annotation.Config


@Config(sdk = [Build.VERSION_CODES.O_MR1])
@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class SaveReminderViewModelTest {

    private lateinit var saveReminderViewModel: SaveReminderViewModel
    private lateinit var dataSource: FakeDataSource


    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    //needed to be added whenever testing livedata
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUpViewModel() {
        dataSource = FakeDataSource()
        saveReminderViewModel =
            SaveReminderViewModel(ApplicationProvider.getApplicationContext(), dataSource)
    }

    @After
    fun tearDownKoin() {
        stopKoin()
    }

    @Test
    fun testOnClear() = mainCoroutineRule.runBlockingTest {
        //when
        saveReminderViewModel.onClear()

        //then
        assertThat(saveReminderViewModel.reminderTitle.getOrAwaitValue(), `is` (nullValue()))
        assertThat(saveReminderViewModel.reminderDescription.getOrAwaitValue(), `is` (nullValue()))
        assertThat(saveReminderViewModel.reminderSelectedLocationStr.getOrAwaitValue(), `is` (nullValue()))
        assertThat(saveReminderViewModel.selectedPOI.getOrAwaitValue(), `is` (nullValue()))
        assertThat(saveReminderViewModel.latitude.getOrAwaitValue(), `is` (nullValue()))
        assertThat(saveReminderViewModel.longitude.getOrAwaitValue(), `is` (nullValue()))
    }

    @Test
    fun enteredData_validData_returnsTrue() {
        //given
        val title = "title"
        val location = "location"
        val reminderDataItem = ReminderDataItem(
            title, null, location,
            null, null
        )

        // When data entered is valid
        val enteredData = saveReminderViewModel.enteredData(reminderDataItem)

        // Then the method return true
        assertThat(enteredData, `is`(true))
    }

    @Test
    fun enteredData_noLocation_returnFalse() {
        //given
        val title = "title"
        val reminderDataItem = ReminderDataItem(
            title, null, null,
            null, null
        )

        //when
        val enteredData = saveReminderViewModel.enteredData(reminderDataItem)
        val snackBarText = saveReminderViewModel.showSnackBarInt.getOrAwaitValue()

        //then
        assertThat(enteredData, `is`(false))
        assertThat(snackBarText, `is`(R.string.err_select_location))
    }

    @Test
    fun enteredData_noTitle_returnFalse() {
        //given
        val location = "location"
        val reminderDataItem = ReminderDataItem(
            null, null, location,
            null, null
        )

        //when
        val enteredData = saveReminderViewModel.enteredData(reminderDataItem)
        val snackBarText = saveReminderViewModel.showSnackBarInt.getOrAwaitValue()

        //then
        assertThat(enteredData, `is`(false))
        assertThat(snackBarText, `is`(R.string.err_enter_title))
    }

    @Test
    fun check_loading() = mainCoroutineRule.runBlockingTest{

        val reminder = ReminderDataItem("title", "desc", "loc", 0.0, 0.0)

        mainCoroutineRule.pauseDispatcher()
        saveReminderViewModel.validateAndSaveReminder(reminder)
        val showLoadingBefore = saveReminderViewModel.showLoading.getOrAwaitValue()
        assertThat(showLoadingBefore, `is`(true))

        mainCoroutineRule.resumeDispatcher()
        val showLoadingAfter = saveReminderViewModel.showLoading.getOrAwaitValue()
        assertThat(showLoadingAfter, `is`(false))
    }

//    @Test
//    fun noReminders_callError(){
//        dataSource.setReturnError(true)
//        dataSource.deleteAllReminders()
//    }
}