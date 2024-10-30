package com.example.tiendagabogamezz.Screen

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.tiendagabogamezz.DAO.ProductoDao
import com.example.tiendagabogamezz.DAO.VentaDao
import com.example.tiendagabogamezz.Model.Producto
import com.example.tiendagabogamezz.Model.Venta
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun VentaScreen(
    navController: NavController,
    productoId: Int,
    clienteId: Int,
    cantidad: Int,
    fecha: String,
    ventaDao: VentaDao,
    productoDao: ProductoDao,
) {
    var ventas by remember { mutableStateOf<List<Venta>>(emptyList()) }
    var loading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }
    var productoComprado by remember { mutableStateOf<Producto?>(null) }

    // Cargar las ventas del cliente al iniciar la pantalla
    LaunchedEffect(clienteId) {
        loading = true
        try {
            // Acceder a la base de datos en un hilo de fondo
            ventas = withContext(Dispatchers.IO) {
                ventaDao.obtenerVentasDeCliente(clienteId)
            }
        } catch (e: Exception) {
            error = e.message
        } finally {
            loading = false
        }
    }

    // Obtener el producto seleccionado
    LaunchedEffect(productoId) {
        productoComprado = withContext(Dispatchers.IO) {
            productoDao.getProductoById(productoId)
        }
    }

    // Mostrar la interfaz de usuario
    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "Historial de Compras", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))

        if (loading) {
            Text(text = "Cargando compras...")
        } else if (error != null) {
            Text(text = "Error: $error", color = Color.Red)
        } else {
            // Mostrar la lista de ventas
            LazyColumn {
                items(ventas) { venta ->
                    Text(
                        text = "Producto ID: ${venta.productoId}, Cantidad: ${venta.cantidad}, Fecha: ${SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(venta.fecha)}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Mostrar producto comprando
        productoComprado?.let {
            Text(text = "Comprando: ${it.nombre}, Precio: ${it.precio}, Cantidad: $cantidad, Fecha: $fecha", style = MaterialTheme.typography.bodyMedium)
        } ?: run {
            Text(text = "Cargando producto seleccionado...", style = MaterialTheme.typography.bodySmall)
        }

        // Botón para registrar la venta
        Button(onClick = {
            val fechaActual = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(fecha)
            if (fechaActual != null) {
                val nuevaVenta = Venta(clienteId = clienteId, productoId = productoId, cantidad = cantidad, fecha = fechaActual)

                CoroutineScope(Dispatchers.IO).launch {
                    ventaDao.insert(nuevaVenta) // Registrar la venta
                    productoDao.reducirStock(productoId, cantidad) // Reducir stock
                    // Navegar al historial de compras del cliente
                    withContext(Dispatchers.Main) {
                        navController.navigate("historial/$clienteId")
                    }
                }
            }
        }) {
            Text("Registrar Venta")
        }
    }
}