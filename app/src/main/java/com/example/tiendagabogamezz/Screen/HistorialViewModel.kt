package com.example.tiendagabogamezz.Screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tiendagabogamezz.DAO.VentaDao
import com.example.tiendagabogamezz.Model.Venta
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HistorialViewModel(private val ventaDao: VentaDao) : ViewModel() {
    var historialCompras: List<Venta> = emptyList()
        private set

    fun cargarHistorial(clienteId: Int) {
        viewModelScope.launch {
            // Cargar los datos en un hilo de fondo
            historialCompras = withContext(Dispatchers.IO) {
                ventaDao.obtenerVentasDeCliente(clienteId)
            }
        }
    }
}