package com.example.tiendagabogamezz.Model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.Date

@Entity
data class Venta(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val productoId: Int,
    val clienteId: Int,
    val cantidad: Int,
    val fecha: Date
)
