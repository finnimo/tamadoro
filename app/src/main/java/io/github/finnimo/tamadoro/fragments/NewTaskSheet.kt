package io.github.finnimo.tamadoro.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.datepicker.MaterialDatePicker
import io.github.finnimo.tamadoro.R
import io.github.finnimo.tamadoro.taskitemdatabase.TaskItem
import io.github.finnimo.tamadoro.taskitemdatabase.TaskItemModelFactory
import io.github.finnimo.tamadoro.taskitemdatabase.TaskViewModel
import io.github.finnimo.tamadoro.taskitemdatabase.TodoApplication
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

class NewTaskSheet(var taskItem: TaskItem?): BottomSheetDialogFragment() {

    private lateinit var taskViewModel: TaskViewModel
    private lateinit var taskNameInput: EditText
    private lateinit var tagInput: EditText
    private lateinit var datePickerBtn: Button
    private lateinit var deleteBtn: Button
    private lateinit var saveBtn: Button
    private var dueDate: LocalDate? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        taskViewModel = ViewModelProvider(
            requireActivity(),
            TaskItemModelFactory((requireActivity().application as TodoApplication).repository)
        ).get(TaskViewModel::class.java)

        //TODO: app is crashing on trying to initialize taskVoiewModel

        taskNameInput = view.findViewById(R.id.name)
        tagInput = view.findViewById(R.id.tagEditText)
        datePickerBtn = view.findViewById(R.id.datePickerBtn)
        deleteBtn = view.findViewById(R.id.deleteBtn)
        saveBtn = view.findViewById(R.id.saveBtn)

        val datePicker: MaterialDatePicker<Long>

        if (taskItem != null) {
            taskNameInput.setText(taskItem!!.name)
            tagInput.setText(taskItem!!.tag)

            deleteBtn.visibility = Button.VISIBLE
            deleteBtn.setOnClickListener {
                taskViewModel.deleteTaskItem(taskItem!!)
                dismiss()
            }

        }

        if ((taskItem != null) and (taskItem?.dueDate() != null)) {

            datePickerBtn.text = taskItem!!.dueDateString
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
        val tag = tagInput.text.toString()
        var name = taskNameInput.text.toString()

        if (taskNameInput.text.toString() == "") {
            name = "New Task"
        }

        val dueDateString = if (dueDate == null) null
        else TaskItem.dateFormatter.format(dueDate)

        if (taskItem == null) {
            val newTask = TaskItem(name, tag, dueDateString)
            taskViewModel.addTaskItem(newTask)
        } else {
            taskItem!!.name = name
            taskItem!!.dueDateString = dueDateString
            taskViewModel.updateTaskItem(taskItem!!)
        }


        dismiss()
    }

}

