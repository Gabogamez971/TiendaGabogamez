package com.example.tiendagabogamezz.Model

import androidx.room.Entity
import androidx.room.PrimaryKey

    @Entity(tableName = "users")
    data class User(
        @PrimaryKey
        val cedula: Int,
        val nombre: String,
        val correo: String
    )