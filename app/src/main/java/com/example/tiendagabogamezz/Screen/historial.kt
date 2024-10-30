package com.example.tiendagabogamezz.Screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tiendagabogamezz.DAO.VentaDao
import com.example.tiendagabogamezz.Model.Venta
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun HistorialScreen(clienteId: Int, ventaDao: VentaDao) {
    val viewModel: HistorialViewModel = viewModel(factory = HistorialViewModelFactory(ventaDao))
    val historialCompras by remember { mutableStateOf(viewModel.historialCompras) }

    // Cargar el historial de compras
    LaunchedEffect(clienteId) {
        viewModel.cargarHistorial(clienteId)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "Historial de Compras",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (historialCompras.isEmpty()) {
            Text(text = "No hay compras registradas.", modifier = Modifier.align(Alignment.CenterHorizontally))
        } else {
            LazyColumn {
                items(historialCompras) { venta ->
                    HistorialItem(venta)
                }
            }
        }
    }
}

@Composable
fun HistorialItem(venta: Venta) {
    // Formatear la fecha para una mejor presentación
    val fechaFormateada = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(venta.fecha)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "ID de Venta: ${venta.id}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Producto ID: ${venta.productoId}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Cantidad: ${venta.cantidad}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Fecha: $fechaFormateada", style = MaterialTheme.typography.bodyMedium)
        }
    }
}