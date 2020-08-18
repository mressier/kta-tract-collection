package com.onion.ktatractcollection.Shared.Fragments

import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.DialogFragment

private const val hourId = "hour_id"
private const val minuteId = "minute_id"

/**
 * TimerPickerFragment
 *
 * Instantiate with:
 *
 * DatePickerFragment.newInstance(crime.date).apply {
 *  setTargetFragment(this@MyFragmentFragment, REQUEST_DATE)
 *  show(this@CrimeFragment.requireFragmentManager(), DIALOG_DATE)
 * }
 *
 * And implement Callback method:
 *
 * override fun onDateSelected(date: Date) { ... }
*/
class TimePickerFragment: DialogFragment() {

    private val timeListener = TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
        targetFragment?.let { fragment ->
            (fragment as? Callbacks)?.onTimeSelected(hourOfDay, minute)
        }
    }

    /**
     * View Life Cycle
     */
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val hour = arguments?.getSerializable(hourId) as? Int
        val minute = arguments?.getSerializable(minuteId) as? Int

        return TimePickerDialog(
            requireContext(),
            timeListener,
            hour ?: 0,
            minute ?: 0,
            true
        )
    }

    /**
     * Initialization
     */
    companion object {
        fun newInstance(hour: Int, minute: Int): TimePickerFragment {
            val args = Bundle().apply {
                putSerializable(hourId, hour)
                putSerializable(minuteId, minute)
            }
            return TimePickerFragment().apply { arguments = args }
        }
    }

    /**
     * Callback
     */
    interface Callbacks {
        fun onTimeSelected(hour: Int, minute: Int)
    }
}