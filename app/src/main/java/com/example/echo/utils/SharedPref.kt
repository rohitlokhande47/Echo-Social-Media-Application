package com.example.echo.utils

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.util.Log
import com.cloudinary.Url

object SharedPref {
    fun storeData(
        name: String,
        email: String,
        bio: String,
        userName: String,
        imageUrl: String,
        context: Context,
    ) {
        val sharedPreferences = context.getSharedPreferences("users", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("name", name)
        editor.putString("email", email)
        editor.putString("bio", bio)
        editor.putString("userName", userName)
        editor.putString("imageUrl", imageUrl)
        editor.apply()
    }

    fun getUserName(context: Context): String {
        val sharedPreferences = context.getSharedPreferences("users", MODE_PRIVATE)
        return sharedPreferences.getString("userName", "")!!
    }

    fun getName(context: Context): String {
        val sharedPreferences = context.getSharedPreferences("users", MODE_PRIVATE)
        return sharedPreferences.getString("name", "")!!
    }

    fun getEmail(context: Context): String {
        val sharedPreferences = context.getSharedPreferences("users", MODE_PRIVATE)
        return sharedPreferences.getString("Email", "")!!
    }

    fun getBio(context: Context): String {
        val sharedPreferences = context.getSharedPreferences("users", MODE_PRIVATE)
        return sharedPreferences.getString("bio", "")!!
    }

    fun getImage(context: Context): String {
        val sharedPreferences = context.getSharedPreferences("users", MODE_PRIVATE)
        val imageUrl = sharedPreferences.getString("imageUrl", "")!!
        Log.d("SharedPref", "Image URL: $imageUrl")
        return imageUrl
    }
}
