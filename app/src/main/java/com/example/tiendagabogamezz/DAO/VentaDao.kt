package com.example.tiendagabogamezz.DAO

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.tiendagabogamezz.Model.Venta
import com.example.tiendagabogamezz.Model.VentaConProducto
@Dao
interface VentaDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(venta: Venta)

    @Query("SELECT * FROM venta")
    suspend fun getAllVentas(): List<Venta>

    @Query("DELETE FROM venta WHERE id = :ventaId")
    suspend fun delete(ventaId: Int)


    @Query("SELECT * FROM Venta WHERE clienteId = :clienteId")
    fun obtenerVentasDeCliente(clienteId: Int): List<Venta>

}
