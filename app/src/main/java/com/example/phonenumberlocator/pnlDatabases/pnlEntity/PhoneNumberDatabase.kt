package com.example.phonenumberlocator.pnlDatabases.pnlEntity
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "phone_numbers")
data class PhoneNumberDatabase(
    var phone_number: String,
    var phone_number_country : String,
    var phone_number_network : String,
    var phone_number_name : String,@PrimaryKey(autoGenerate = true) val id: Int = 0):Serializable