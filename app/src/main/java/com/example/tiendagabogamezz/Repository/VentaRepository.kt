package com.example.tiendagabogamezz.Repository

import com.example.tiendagabogamezz.DAO.VentaDao
import com.example.tiendagabogamezz.Model.Venta

class VentaRepository(private val ventaDao: VentaDao) {


    suspend fun insertVenta(venta: Venta) {
        ventaDao.insert(venta)
    }
    suspend fun getAllVentas(): List<Venta> {
        return ventaDao.getAllVentas()
    }
    suspend fun deleteVenta(ventaId: Int) {
        ventaDao.delete(ventaId)
    }


}