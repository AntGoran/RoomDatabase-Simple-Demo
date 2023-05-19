package com.example.roomdemo

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

// Dao tells compiler that this interface's role is to define how to access data in the Room database
@Dao
interface EmployeeDao {

    @Insert
    suspend fun insert(employeeEntity: EmployeeEntity)

    @Update
    suspend fun update(employeeEntity: EmployeeEntity)

    @Delete
    suspend fun delete(employeeEntity: EmployeeEntity)

    // Get all employees
    @Query("SELECT * FROM `employee-table`")
    // Flow holds values that can change in the runtime - part of coroutines
    // It is updating as soon as it happens
    fun fetchAllEmployees(): Flow<List<EmployeeEntity>>

    // Get employee with certain id
    @Query("SELECT * FROM `employee-table`WHERE id= :id")
    fun fetchEmployeeById(id:Int): Flow<EmployeeEntity>
}