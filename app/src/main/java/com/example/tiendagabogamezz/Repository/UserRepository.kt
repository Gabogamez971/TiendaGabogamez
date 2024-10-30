package com.example.tiendagabogamezz.Repository

import com.example.tiendagabogamezz.DAO.UserDao
import com.example.tiendagabogamezz.Model.User

class UserRepository(private val userDao: UserDao) {
    suspend fun insertUser(user: User) {
        userDao.insert(user)
    }
    suspend fun getAllUsers(): List<User> {
        return userDao.getAllUsers()
    }
    suspend fun deleteUser(userCedula: String) {
        userDao.delete(userCedula)
    }

}