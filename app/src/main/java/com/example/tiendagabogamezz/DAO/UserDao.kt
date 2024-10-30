package com.example.tiendagabogamezz.DAO

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.tiendagabogamezz.Model.User

@Dao
interface UserDao {



        @Insert(onConflict = OnConflictStrategy.REPLACE)
        suspend fun insert(user: User)

        @Query("SELECT * FROM users")
        suspend fun getAllUsers(): List<User>

        @Query("DELETE FROM users WHERE cedula = :userCedula")
        suspend fun delete(userCedula: String)


        // Nueva consulta para verificar si ya existe un usuario con la cédula dada
        @Query("SELECT COUNT(*) FROM users WHERE cedula = :userCedula")
        suspend fun exists(userCedula: String): Int

}
