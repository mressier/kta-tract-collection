package com.onion.ktatractcollection.shared.fragments

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.Dialog
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import java.util.*

private const val PARAM_DATE_ID = "date_id"
private const val PARAM_DATE_MAX_ID = "date_max_id"
private const val PARAM_REQUEST_ID = "request_id"

/**
 * DatePickerFragment
 *
 * Instantiate with:
 *
 * DatePickerFragment.newInstance(date).apply {
 *  setTargetFragment(this@MyFragment, REQUEST_DATE)
 *  show(this@MyFragment.requireFragmentManager(), DIALOG_DATE)
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
        fun onDateSelected(date: Date, requestId: Int)
    }

    /**
     * Properties
     */

    private var requestId: Int = 0

    /**
     * Listener
     */

    private val dateListener = OnDateSetListener { _: DatePicker, year: Int, month: Int, day: Int ->
        val resultDate: Date = GregorianCalendar(year, month, day).time
        targetFragment?.let { fragment ->
            println(targetRequestCode)
            (fragment as Callbacks).onDateSelected(resultDate, requestId)
        }
    }

    /**
     * View Life Cycle
     */
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val calendar = Calendar.getInstance()
        val dateArgument = arguments?.getSerializable(PARAM_DATE_ID) as? Date
        val dateMaxArgument = arguments?.getSerializable(PARAM_DATE_MAX_ID) as? Date
        requestId = arguments?.getInt(PARAM_REQUEST_ID) ?: 0

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
        fun newInstance(
            date: Date? = null,
            dateMax: Date? = null,
            requestId: Int
        ): DatePickerFragment {
            val args = Bundle().apply {
                putSerializable(PARAM_DATE_ID, date)
                putSerializable(PARAM_DATE_MAX_ID, dateMax)
                putSerializable(PARAM_REQUEST_ID, requestId)
            }
            return DatePickerFragment().apply { arguments = args }
        }
    }

}