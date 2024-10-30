package com.example.tiendagabogamezz.Screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tiendagabogamezz.DAO.VentaDao

class HistorialViewModelFactory(private val ventaDao: VentaDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HistorialViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HistorialViewModel(ventaDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}