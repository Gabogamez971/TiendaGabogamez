package com.example.tiendagabogamezz.Screen

import android.widget.ImageButton
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.tiendagabogamezz.R

@Composable
fun HomeScreen(navController: NavHostController, clienteId: Int) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Bienvenido",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 24.dp), // Espacio adicional debajo del título
            color = MaterialTheme.colorScheme.primary // Color del texto
        )

        // Botón 1 - Página 1
        ImageButton(
            imageRes = R.drawable.productostecnolo,
            description = "Ir a Página 1",
            onClick = { navController.navigate("producto?clienteId=$clienteId") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Botón 2 - Página de Historial
        ImageButton(
            imageRes = R.drawable.compratecnologica,
            description = "Ir a Página de Historial",
            onClick = { navController.navigate("historial/$clienteId") }
        )
    }
}

@Composable
fun ImageButton(imageRes: Int, description: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .size(200.dp)
            .padding(8.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp) // Establecer elevación directamente aquí
    ) {
        Box {
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = description,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            // Superposición de texto opcional
            Text(
                text = description,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp)
                    .background(Color.Black.copy(alpha = 0.5f), shape = RoundedCornerShape(8.dp)),
                color = Color.White
            )
        }
    }
}