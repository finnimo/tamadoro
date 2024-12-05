package io.github.finnimo.tamadoro

import android.os.Bundle
import android.service.autofill.Validators.and
import android.text.Editable
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.datepicker.MaterialDatePicker
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale
import java.util.TimeZone
import java.util.UUID

class NewTaskSheet(var taskItem: TaskItem?): BottomSheetDialogFragment() {

    private lateinit var taskViewModel: TaskViewModel
    private lateinit var saveBtn: Button
    private lateinit var taskNameInput: EditText
    private lateinit var taskSheetTitle: TextView
    private lateinit var datePickerBtn: Button
    private var dueDate: LocalDate? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        taskViewModel = ViewModelProvider(
            requireActivity(),
            TaskItemModelFactory((requireActivity().application as TodoApplication).repository)
        ).get(TaskViewModel::class.java)

        //TODO: app is crashing on trying to initialize taskVoiewModel

        saveBtn = view.findViewById(R.id.saveBtn)
        taskNameInput = view.findViewById(R.id.name)
        taskSheetTitle = view.findViewById(R.id.taskSheetTitle)
        datePickerBtn = view.findViewById(R.id.datePickerBtn)

        //if we're editing a pre existing task:
        if (taskItem != null) {
            taskSheetTitle.text = "Edit Task"
            val editable = Editable.Factory.getInstance()
            taskNameInput.text = editable.newEditable(taskItem!!.name)

            if (taskItem!!.dueDate() != null) {
                dueDate = taskItem!!.dueDate()
            }

        } else {
            taskSheetTitle.text = "New Task"
        }


        val datePicker: MaterialDatePicker<Long>

        if ((taskItem != null) and (taskItem?.dueDate() != null)) {
            val zoneID: ZoneId = ZoneId.systemDefault()
            val prevDueDate = taskItem!!.dueDate()!!.atStartOfDay(zoneID).toInstant().toEpochMilli()
            datePicker =
                MaterialDatePicker.Builder.datePicker()
                    .setTitleText("Select date")
                    .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                    .setSelection(prevDueDate)
                    .build()
        } else {
            datePicker =
                MaterialDatePicker.Builder.datePicker()
                    .setTitleText("Select date")
                    .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                    .build()
        }

        datePickerBtn.setOnClickListener {
            datePicker.show(childFragmentManager, "datePicker")
        }

        datePicker.addOnPositiveButtonClickListener { selectedDate ->

            dueDate = Instant.ofEpochMilli(selectedDate)
                .atZone(ZoneId.systemDefault())
                .toLocalDate()

        }


        saveBtn.setOnClickListener {
            saveAction()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_task_sheet, container, false)
    }

    private fun saveAction() {
     val name = taskNameInput.text.toString()
        val dueDateString = if (dueDate == null) null
        else TaskItem.dateFormatter.format(dueDate)

        if (taskItem == null) {
            val newTask = TaskItem(name, dueDateString)
            taskViewModel.addTaskItem(newTask)
        } else {
            taskItem!!.name = name
            taskItem!!.dueDateString = dueDateString
            taskViewModel.updateTaskItem(taskItem!!)
        }


        dismiss()
    }

}

