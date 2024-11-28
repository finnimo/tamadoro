package io.github.finnimo.tamadoro

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class NewTaskSheet : BottomSheetDialogFragment() {

    private lateinit var taskViewModel: TaskViewModel
    private lateinit var saveBtn: Button
    private lateinit var taskNameInput: EditText
    private lateinit var taskDescInput: EditText

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        taskViewModel = ViewModelProvider(requireActivity()).get(TaskViewModel::class.java)
        saveBtn = view.findViewById(R.id.saveBtn)
        taskNameInput = view.findViewById(R.id.name)
        taskDescInput = view.findViewById(R.id.desc)
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
        val desc = taskDescInput.text.toString()

        taskViewModel.name.value = name
        taskViewModel.desc.value = desc

        dismiss()
    }

}

