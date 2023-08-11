package com.example.phonenumberlocator.pnlDatabases.pnlDao


import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.phonenumberlocator.pnlDatabases.pnlEntity.PhoneNumberDatabase

@Dao
interface PhoneNumberDao  {
    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMyMobileNumber(contact: PhoneNumberDatabase)
    @Query("SELECT * FROM phone_numbers")
    fun getAllMobileNumber() : LiveData<MutableList<PhoneNumberDatabase>>
    @Update(onConflict = OnConflictStrategy.IGNORE)
    fun update(groupLocation: PhoneNumberDatabase)
    @Delete
    fun deleteThisNumber(phoneNumber: PhoneNumberDatabase)
}