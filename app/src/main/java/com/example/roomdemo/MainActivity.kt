package com.example.roomdemo

import android.app.AlertDialog
import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.roomdemo.databinding.ActivityMainBinding
import com.example.roomdemo.databinding.DialogUpdateBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private var binding: ActivityMainBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        // Set up DAO
        val employeeDao = (application as EmployeeApp).db.employeeDao
        binding?.btnAdd?.setOnClickListener {
            addRecord(employeeDao)
        }

        // Call  setupListOfDataIntoRecyclerView in the background
        lifecycleScope.launch {
            // Gives us all employees created
            employeeDao.fetchAllEmployees().collect {
                // Create ArrayList out of List
                val list = ArrayList(it)
                // Pass the list and employeeDao to the method
                setupListOfDataIntoRecyclerView(list, employeeDao)
            }
        }
    }

    // Add record to database
    fun addRecord(employeeDao: EmployeeDao) {
        val name = binding?.etName?.text.toString()
        val email = binding?.etEmailId?.text.toString()

        if (name.isNotEmpty() && email.isNotEmpty()) {
            lifecycleScope.launch {
                employeeDao.insert(EmployeeEntity(0, name, email))
                Toast.makeText(this@MainActivity, "Record saved", Toast.LENGTH_LONG).show()
                binding?.etName?.text?.clear()
                binding?.etEmailId?.text?.clear()
            }
        } else {
            Toast.makeText(this, "Name or email can not be blank", Toast.LENGTH_LONG).show()
        }
    }

    private fun setupListOfDataIntoRecyclerView(
        employeesList: ArrayList<EmployeeEntity>,
        employeeDao: EmployeeDao
    ) {
        // Check if employee list is empty
        if (employeesList.isNotEmpty()) {
            // Display data into recycler view
            val itemAdapter = ItemAdapter(employeesList,
                // Creating anonymous function what we need to pass to adapter
                { updateId -> updateRecordDialog(updateId, employeeDao)},
                { deleteId -> lifecycleScope.launch{deleteRecordAlertDialog(deleteId, employeeDao)} }
            )
//            // Set up RecyclerView layoutManager
            binding?.rvItemsList?.layoutManager = LinearLayoutManager(this)
            // Assign itemAdapter as adapter for RecycleView
            binding?.rvItemsList?.adapter = itemAdapter
            // Make sure recyclerView is made visible
            binding?.rvItemsList?.visibility = View.VISIBLE
            // and that textView (No records available) invisible
            binding?.tvNoRecordsAvailable?.visibility = View.GONE
        } else {
            // Make sure recyclerView is made invisible
            binding?.rvItemsList?.visibility = View.GONE
            // and that textView (No records available) visible
            binding?.tvNoRecordsAvailable?.visibility = View.VISIBLE
        }
    }

    // Update data function
    private fun updateRecordDialog(id: Int, employeeDao: EmployeeDao) {
        val updateDialog = Dialog(this, R.style.Theme_Dialog)
        // Set to cancel only when clicked on dialog
        updateDialog.setCancelable(false)
        // Bind xml file (dialog_update.xml)
        val binding = DialogUpdateBinding.inflate(layoutInflater)
        // Set dialog to look like dialog xml file
        updateDialog.setContentView(binding.root)

        // Populate data in the edit text dialog
        lifecycleScope.launch {
            // Take data from certain ID and populate dialog with name and email
            employeeDao.fetchEmployeeById(id).collect {
                if (it != null) {
                    binding.etUpdateName.setText(it.name)
                    binding.etUpdateEmailId.setText(it.email)
                }
            }
        }

        // What happens when update button clicked
        binding.tvUpdate.setOnClickListener {
            // Get name and email from editText
            val name = binding.etUpdateName.text.toString()
            val email = binding.etUpdateEmailId.text.toString()
            // Check if it is empty and store something
            if (name.isNotEmpty() && email.isNotEmpty()) {
                lifecycleScope.launch {
                    // Update id, name, email
                    employeeDao.update(EmployeeEntity(id, name, email))
                    // Let the user know we updated the record
                    Toast.makeText(applicationContext, "Record Updated", Toast.LENGTH_LONG).show()
                    // Dismiss dialog
                    updateDialog.dismiss()
                }
            } else {
                // If it is empty display taost
                Toast.makeText(
                    applicationContext,
                    "Name or Email can not be blank",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        // If cancel button is clicked
        binding.tvCancel.setOnClickListener {
            updateDialog.dismiss()
        }
        // Show dialog
        updateDialog.show()
    }

    // Delete function
    // Creating dialog
    private fun deleteRecordAlertDialog(id: Int, employeeDao: EmployeeDao) {
        // Create builder object
        val builder = AlertDialog.Builder(this)
        // Set title for dialog
        builder.setTitle("Delete record")
        // Set message for alert dialog
//        builder.setMessage("Are you sure you want to delete ${employee.name}")
        builder.setIcon(android.R.drawable.ic_dialog_alert)
        // Set the positive and negative button (YES and NO)
        builder.setPositiveButton("Yes") { dialogInterface, _ ->
           lifecycleScope.launch {
                // Delete entity with certain id
                employeeDao.delete(EmployeeEntity(id))
                // Let the user know
                Toast.makeText(
                    applicationContext,
                    "Record deleted successfully.",
                    Toast.LENGTH_LONG
                ).show()
            }
            dialogInterface.dismiss()
        }
        // Dismiss negative button
        builder.setNegativeButton("No") { dialogInterface, _ ->
            dialogInterface.dismiss()
        }

        // Set up alert dialog
        val alertDialog: AlertDialog = builder.create()
        // Set not cancelable
        alertDialog.setCancelable(false)
        alertDialog.show()
    }
}



















