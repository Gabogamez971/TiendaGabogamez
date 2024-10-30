package com.example.tiendagabogamezz.DAO

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.tiendagabogamezz.Model.Producto

@Dao
interface ProductoDao {


    @Query("SELECT * FROM productos")
    suspend fun getAllProductos(): List<Producto>

    @Query("SELECT * FROM productos WHERE id = :id")
    suspend fun getProductoById(id: Int): Producto

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(producto: Producto)

    @Query("DELETE FROM productos WHERE id = :productoId")
    suspend fun delete(productoId: Int)

    @Query("UPDATE productos SET stock = stock - :cantidad WHERE id = :productoId")
    suspend fun reducirStock(productoId: Int, cantidad: Int)



}