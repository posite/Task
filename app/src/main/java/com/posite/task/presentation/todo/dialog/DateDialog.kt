package com.posite.task.presentation.todo.dialog

import android.R
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.posite.task.TaskApplication
import com.posite.task.databinding.DialogDateBinding
import java.util.Calendar
import java.util.Locale

class DateDialog(private val calendar: Calendar) : DialogFragment() {
    private val color by lazy {
        ContextCompat.getColor(requireContext(), com.posite.task.R.color.highlight)
    }
    private var year = calendar.get(Calendar.YEAR)
    private var month = calendar.get(Calendar.MONTH)
    private var day = calendar.get(Calendar.DAY_OF_MONTH)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCancelable = true
    }

    private var binding: DialogDateBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogDateBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setLayout()

        binding?.apply {
            dateEdit.text = DATE_FORMATTER.format(calendar.time)
            timeEdit.text = TIME_FORMATTER.format(calendar.time)
            dateEdit.setOnClickListener {
                chooseDate()
            }

            timeEdit.setOnClickListener {
                chooseTime()
            }

            finishInputDateBtn.setOnClickListener {
                dismiss()
            }
        }

    }

    private fun chooseDate() {
        val dialog = DatePickerDialog(
            requireContext(),
            R.style.Theme_Material_Light_Dialog,
            { _, cYear, cMonth, cDay ->
                Log.d("date", cDay.toString())
                run {
                    calendar.set(
                        cYear,
                        cMonth,
                        cDay,
                        calendar.get(Calendar.HOUR_OF_DAY),
                        calendar.get(Calendar.MINUTE)
                    )
                    binding!!.dateEdit.text = DATE_FORMATTER.format(calendar.time)
                    year = cYear
                    month = cMonth
                    day = cDay
                }
            },
            year,
            month,
            day
        )
        dialog.show()
        dialog.getButton(DatePickerDialog.BUTTON_POSITIVE)
            .setTextColor(color)
        dialog.getButton(DatePickerDialog.BUTTON_NEGATIVE)
            .setTextColor(color)
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        if (binding!!.dateEdit.text.isNotEmpty() && binding!!.timeEdit.text.isNotEmpty()) {
            (requireActivity() as OnInputListener).sendResult(calendar)
        }

    }


    private fun chooseTime() {
        val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
            calendar.set(Calendar.HOUR_OF_DAY, hour)
            calendar.set(Calendar.MINUTE, minute)

            binding!!.timeEdit.text = TIME_FORMATTER.format(calendar.time)
        }
        val dialog = TimePickerDialog(
            context,
            R.style.Theme_Material_Light_Dialog,
            timeSetListener,
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true
        )
        dialog.show()
        dialog.getButton(DatePickerDialog.BUTTON_POSITIVE)
            .setTextColor(color)
        dialog.getButton(DatePickerDialog.BUTTON_NEGATIVE)
            .setTextColor(color)

    }

    private fun setLayout() {
        requireNotNull(dialog).apply {
            requireNotNull(window).apply {
                setLayout(
                    resources.getDimensionPixelSize(com.posite.task.R.dimen.dialog_Width),
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }
        }
    }

    interface OnInputListener {
        fun sendResult(date: Calendar)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    companion object {
        private val DATE_FORMATTER =
            SimpleDateFormat(
                TaskApplication.getString(com.posite.task.R.string.date_format),
                Locale.ENGLISH
            )

        private val TIME_FORMATTER = SimpleDateFormat(
            TaskApplication.getString(com.posite.task.R.string.time_format),
            Locale.ENGLISH
        )
    }
}
