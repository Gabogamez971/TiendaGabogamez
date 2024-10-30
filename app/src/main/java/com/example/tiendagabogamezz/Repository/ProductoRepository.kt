package com.example.tiendagabogamezz.Repository

import com.example.tiendagabogamezz.DAO.ProductoDao
import com.example.tiendagabogamezz.Model.Producto

class ProductoRepository(private val productoDao: ProductoDao) {

    suspend fun reducirStock(productoId: Int, cantidad: Int) {
        productoDao.reducirStock(productoId, cantidad)
    }

    suspend fun getProductoById(id: Int): Producto? {
        return productoDao.getProductoById(id) // Asegúrate de tener este método en tu DAO
    }

    suspend fun insert(producto: Producto) {
        productoDao.insert(producto)
    }

    suspend fun delete(productoId: Int) {
        productoDao.delete(productoId)
    }

    // Nueva función para insertar productos iniciales
    suspend fun insertProductosIniciales(productos: List<Producto>) {
        productos.forEach { productoDao.insert(it) }
    }

    // Nueva función para obtener todos los productos
    suspend fun getAllProductos(): List<Producto> {
        return productoDao.getAllProductos()
    }
}