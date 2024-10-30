package com.example.tiendagabogamezz.Database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.tiendagabogamezz.DAO.ProductoDao
import com.example.tiendagabogamezz.DAO.UserDao
import com.example.tiendagabogamezz.DAO.VentaDao
import com.example.tiendagabogamezz.Model.Converters
import com.example.tiendagabogamezz.Model.Producto
import com.example.tiendagabogamezz.Model.User
import com.example.tiendagabogamezz.Model.Venta

@Database(entities = [User::class, Venta::class, Producto::class], version = 1)
@TypeConverters(Converters::class)  // Agrega esta línea
abstract class UserDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun ventaDao(): VentaDao
    abstract fun productoDao(): ProductoDao

    companion object {
        @Volatile
        private var INSTANCE: UserDatabase? = null

        fun getDatabase(context: Context): UserDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    UserDatabase::class.java,
                    "user_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}