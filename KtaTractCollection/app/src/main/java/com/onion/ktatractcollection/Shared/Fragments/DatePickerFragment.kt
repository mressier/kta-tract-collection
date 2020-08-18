package com.onion.ktatractcollection.Shared.Fragments

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import java.util.*

private const val dateId = "date_id"
private const val dateMaxId = "date_max_id"

/**
 * DatePickerFragment
 *
 * Instantiate with:
 *
 * DatePickerFragment.newInstance(date).apply {
 *  setTargetFragment(this@MyFragment, REQUEST_DATE)
 *  show(this@CrimeFragment.requireFragmentManager(), DIALOG_DATE)
 * }
 *
 * And implement Callback method:
 *
 * override fun onDateSelected(date: Date) { ... }
 */
class DatePickerFragment: DialogFragment() {

    /**
     * Callbacks
     */

    interface Callbacks {
        fun onDateSelected(date: Date)
    }

    /**
     * Listener
     */
    private val dateListener = OnDateSetListener { _: DatePicker, year: Int, month: Int, day: Int ->
        val resultDate: Date = GregorianCalendar(year, month, day).time
        targetFragment?.let { fragment ->
            (fragment as Callbacks).onDateSelected(resultDate)
        }
    }

    /**
     * View Life Cycle
     */
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val calendar = Calendar.getInstance()
        val dateArgument = arguments?.getSerializable(dateId) as? Date
        val dateMaxArgument = arguments?.getSerializable(dateMaxId) as? Date

        dateArgument?.let { date -> calendar.time = date }

        val dialog = DatePickerDialog(
            requireContext(),
            dateListener,
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        dateMaxArgument?.let { date -> dialog.datePicker.maxDate = date.time }

        return dialog
    }

    /**
     * Initialization
     */

    companion object {
        fun newInstance(date: Date? = null,
            dateMax: Date? = null): DatePickerFragment {
            val args = Bundle().apply {
                putSerializable(dateId, date)
                putSerializable(dateMaxId, dateMax)
            }
            return DatePickerFragment().apply { arguments = args }
        }
    }

}