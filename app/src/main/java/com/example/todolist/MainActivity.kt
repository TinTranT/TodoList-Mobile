package com.example.todolist

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var taskEditText: EditText
    private lateinit var dateTextView: TextView
    private lateinit var addButton: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var taskAdapter: TaskAdapter
    private val tasks = mutableListOf<Task>()
    private var selectedDate: Date = Calendar.getInstance().time
    private val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize views
        taskEditText = findViewById(R.id.edit_task)
        dateTextView = findViewById(R.id.text_date)
        addButton = findViewById(R.id.btn_add_task)
        recyclerView = findViewById(R.id.recycler_tasks)

        // Set up RecyclerView
        taskAdapter = TaskAdapter(tasks)
        recyclerView.adapter = taskAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Display initial date
        dateTextView.text = dateFormat.format(selectedDate)

        // Set up date picker
        dateTextView.setOnClickListener {
            showDatePickerDialog()
        }

        // Set up add button
        addButton.setOnClickListener {
            addTask()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_sort_name -> {
                sortTasksByName()
                true
            }
            R.id.menu_sort_date -> {
                sortTasksByDate()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        calendar.time = selectedDate

        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->
                calendar.set(selectedYear, selectedMonth, selectedDay)
                selectedDate = calendar.time
                dateTextView.text = dateFormat.format(selectedDate)
            },
            year,
            month,
            day
        )

        datePickerDialog.show()
    }

    private fun addTask() {
        val taskName = taskEditText.text.toString().trim()

        if (taskName.isEmpty()) {
            Toast.makeText(this, "Please enter a task name", Toast.LENGTH_SHORT).show()
            return
        }

        val task = Task(taskName, selectedDate)
        tasks.add(task)
        taskAdapter.notifyItemInserted(tasks.size - 1)

        // Clear input fields
        taskEditText.text.clear()
        selectedDate = Calendar.getInstance().time
        dateTextView.text = dateFormat.format(selectedDate)

        Toast.makeText(this, "Task added", Toast.LENGTH_SHORT).show()
    }

    private fun sortTasksByName() {
        tasks.sortBy { it.name }
        taskAdapter.notifyDataSetChanged()
        Toast.makeText(this, "Sorted by name", Toast.LENGTH_SHORT).show()
    }

    private fun sortTasksByDate() {
        tasks.sortBy { it.dueDate }
        taskAdapter.notifyDataSetChanged()
        Toast.makeText(this, "Sorted by date", Toast.LENGTH_SHORT).show()
    }
}