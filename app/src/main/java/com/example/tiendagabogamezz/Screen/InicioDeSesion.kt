package com.example.tiendagabogamezz.Screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.tiendagabogamezz.DAO.UserDao
import com.example.tiendagabogamezz.Model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomLoginScreen(navController: NavController, userDao: UserDao) {
    var cedula by remember { mutableStateOf("") }
    var nombre by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var showMessage by remember { mutableStateOf("") }

    // Fondo de color para la pantalla
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFECEFF1)) // Color de fondo
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp), // Espaciado alrededor
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Iniciar sesión o Registrarse",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 24.dp) // Espacio debajo del texto
            )

            // Campo para la cédula
            OutlinedTextField(
                value = cedula,
                onValueChange = { cedula = it },
                label = { Text("Cédula") },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedLabelColor = Color(0xFF6200EE), // Color de la etiqueta cuando está en foco
                    unfocusedLabelColor = Color.Gray,
                    focusedTextColor = Color.Black, // Color del texto cuando está en foco
                    unfocusedTextColor = Color.Black // Color del texto cuando no está en foco
                ),
                textStyle = TextStyle(fontSize = 20.sp) // Aumentar el tamaño de la fuente
            )

            Spacer(modifier = Modifier.height(16.dp)) // Espacio entre campos

            // Campo para el nombre (solo para registro)
            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre (para registrarse)") },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedLabelColor = Color(0xFF6200EE),
                    unfocusedLabelColor = Color.Gray,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black
                ),
                textStyle = TextStyle(fontSize = 20.sp) // Aumentar el tamaño de la fuente
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Campo para el correo (solo para registro)
            OutlinedTextField(
                value = correo,
                onValueChange = { correo = it },
                label = { Text("Correo (para registrarse)") },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedLabelColor = Color(0xFF6200EE),
                    unfocusedLabelColor = Color.Gray,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black
                ),
                textStyle = TextStyle(fontSize = 20.sp) // Aumentar el tamaño de la fuente
            )

            Spacer(modifier = Modifier.height(24.dp)) // Espacio antes del botón

            // Botón de inicio de sesión o registro
            Button(
                onClick = {
                    val cedulaInt = cedula.toIntOrNull() // Convertir la cédula a Int
                    if (cedulaInt != null) {
                        CoroutineScope(Dispatchers.IO).launch {
                            val userExists = userDao.exists(cedulaInt.toString()) > 0
                            if (userExists) {
                                // Si el usuario existe, navegar a HomeScreen con clienteId
                                withContext(Dispatchers.Main) {
                                    navController.navigate("home/$cedulaInt") // Pasar clienteId
                                }
                            } else {
                                // Si no existe, registrar el usuario
                                if (nombre.isNotBlank() && correo.isNotBlank()) {
                                    userDao.insert(User(cedula = cedulaInt, nombre = nombre, correo = correo))
                                    withContext(Dispatchers.Main) {
                                        showMessage = "Usuario registrado con éxito"
                                    }
                                } else {
                                    withContext(Dispatchers.Main) {
                                        showMessage = "Complete todos los campos para registrarse"
                                    }
                                }
                            }
                        }
                    } else {
                        showMessage = "Ingrese una cédula válida"
                    }
                },
                modifier = Modifier.fillMaxWidth(), // Botón ocupa el ancho completo
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF6200EE) // Color de fondo del botón
                )
            ) {
                Text(
                    text = "Iniciar Sesión / Registrar",
                    color = Color.White // Color del texto del botón
                )
            }

            // Mensaje informativo o de error
            if (showMessage.isNotEmpty()) {
                Text(
                    text = showMessage,
                    color = Color.Red, // Color del mensaje de error
                    modifier = Modifier.padding(top = 16.dp) // Espacio encima del mensaje
                )
            }
        }
    }
}
