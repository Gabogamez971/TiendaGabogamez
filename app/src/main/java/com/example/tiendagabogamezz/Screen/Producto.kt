package com.example.tiendagabogamezz.Screen



import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width

import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.example.tiendagabogamezz.Model.Producto


@Composable
fun ProductoScreen(
    productos: List<Producto>,
    onProductoSelected: (Producto, Int) -> Unit,
    onProductoAgregado: (Producto) -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }
    var stock by remember { mutableStateOf("") }
    var imagenUrl by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text("Agregar Nuevo Producto", style = MaterialTheme.typography.headlineSmall)

        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre") },
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
        )

        OutlinedTextField(
            value = precio,
            onValueChange = { precio = it },
            label = { Text("Precio") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
        )

        OutlinedTextField(
            value = stock,
            onValueChange = { stock = it },
            label = { Text("Stock") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
        )

        OutlinedTextField(
            value = imagenUrl,
            onValueChange = { imagenUrl = it },
            label = { Text("URL de Imagen") },
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
        )

        Button(
            onClick = {
                val nuevoProducto = Producto(
                    nombre = nombre,
                    precio = precio.toDoubleOrNull() ?: 0.0,
                    stock = stock.toIntOrNull() ?: 0,
                    imagenUrl = imagenUrl // Ahora usa la URL
                )
                onProductoAgregado(nuevoProducto)

                // Limpiar los campos después de agregar el producto
                nombre = ""
                precio = ""
                stock = ""
                imagenUrl = ""
            },
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            Text("Agregar Producto")
        }

        Divider(modifier = Modifier.padding(vertical = 8.dp))

        Text("Lista de Productos", style = MaterialTheme.typography.headlineSmall)

        productos.forEach { producto ->
            ProductoItem(
                producto = producto,
                onProductoClick = { cantidad -> onProductoSelected(producto, cantidad) }
            )
        }
    }
}

@Composable
fun ProductoItem(
    producto: Producto,
    onProductoClick: (Int) -> Unit
) {
    var cantidad by remember { mutableStateOf(1) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onProductoClick(cantidad) },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = rememberImagePainter(producto.imagenUrl),
            contentDescription = producto.nombre,
            modifier = Modifier.size(64.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(text = producto.nombre, style = MaterialTheme.typography.bodyLarge)
            Text(text = "Precio: ${producto.precio} CLP", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Stock: ${producto.stock}", style = MaterialTheme.typography.bodyMedium)
            OutlinedTextField(
                value = cantidad.toString(),
                onValueChange = {
                    cantidad = it.toIntOrNull() ?: 1
                },
                label = { Text("Cantidad") },
                modifier = Modifier.width(100.dp)
            )
        }
    }
}
