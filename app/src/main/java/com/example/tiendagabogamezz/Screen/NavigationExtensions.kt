package com.example.tiendagabogamezz.Screen

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.NavController
import com.example.tiendagabogamezz.DAO.VentaDao

fun NavGraphBuilder.addHistorialScreen(navController: NavController, ventaDao: VentaDao) {
    composable("historial/{clienteId}") { backStackEntry ->
        val clienteId = backStackEntry.arguments?.getString("clienteId")?.toIntOrNull() ?: return@composable
        HistorialScreen(clienteId = clienteId, ventaDao = ventaDao)
    }
}