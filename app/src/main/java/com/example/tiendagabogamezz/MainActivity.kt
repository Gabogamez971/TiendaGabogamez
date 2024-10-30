package com.example.tiendagabogamezz

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.tiendagabogamezz.DAO.ProductoDao
import com.example.tiendagabogamezz.DAO.UserDao
import com.example.tiendagabogamezz.DAO.VentaDao
import com.example.tiendagabogamezz.Database.UserDatabase
import com.example.tiendagabogamezz.Model.Producto
import com.example.tiendagabogamezz.Repository.ProductoRepository
import com.example.tiendagabogamezz.Repository.UserRepository
import com.example.tiendagabogamezz.Repository.VentaRepository
import com.example.tiendagabogamezz.Screen.CustomLoginScreen
import com.example.tiendagabogamezz.Screen.HistorialScreen
import com.example.tiendagabogamezz.Screen.HomeScreen
import com.example.tiendagabogamezz.Screen.ProductoScreen
import com.example.tiendagabogamezz.Screen.VentaScreen
import com.example.tiendagabogamezz.Screen.addHistorialScreen
import com.example.tiendagabogamezz.ui.theme.TiendaGaboGamezzTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
class MainActivity : ComponentActivity() {
    private lateinit var userDao: UserDao
    private lateinit var userRepository: UserRepository
    private lateinit var productoDao: ProductoDao
    private lateinit var productoRepository: ProductoRepository
    private lateinit var ventaDao: VentaDao
    private lateinit var ventaRepository: VentaRepository

    @SuppressLint("UnrememberedMutableState", "CoroutineCreationDuringComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        //deleteDatabase("user_database")

        super.onCreate(savedInstanceState)

        try {
            // Inicializa la base de datos y DAOs
            val db = UserDatabase.getDatabase(applicationContext)
            userDao = db.userDao()
            userRepository = UserRepository(userDao)

            productoDao = db.productoDao()
            productoRepository = ProductoRepository(productoDao)

            ventaDao = db.ventaDao()
            ventaRepository = VentaRepository(ventaDao)

            // Lista de productos iniciales
            val productosIniciales = listOf(
                Producto(
                    id = 1,
                    nombre = "Mouse Gamer",
                    precio = 90000.0,
                    stock = 5,
                    imagenUrl = "https://th.bing.com/th/id/OIP.F-oj7W9agXsYpwPuxlXjtwHaHa?rs=1&pid=ImgDetMain"
                ),
                Producto(
                    id = 2,
                    nombre = "PC Gamer",
                    precio = 1500000.0,
                    stock = 3,
                    imagenUrl = "https://th.bing.com/th/id/OIP.mW-XY5yr8DL5sRkHq_qd0wHaHa?rs=1&pid=ImgDetMain"
                ),
                Producto(
                    id = 3,
                    nombre = "Teclado Gamer",
                    precio = 200000.0,
                    stock = 10,
                    imagenUrl = "https://m.media-amazon.com/images/I/71lDpm1GOJL._AC_SL1500_.jpg"
                )
            )

            // Inserta productos iniciales si no existen en la base de datos
            lifecycleScope.launch {
                try {
                    if (productoRepository.getAllProductos().isEmpty()) {
                        productoRepository.insertProductosIniciales(productosIniciales)
                    }
                } catch (e: Exception) {
                    Log.e("MainActivity", "Error al insertar productos iniciales", e)
                }
            }

            // Configura el contenido de la aplicación
            setContent {
                TiendaGaboGamezzTheme {
                    val navController = rememberNavController()
                    val productos = remember { mutableStateListOf<Producto>() }

                    // Cargar productos al inicio
                    LaunchedEffect(Unit) {
                        productos.clear()
                        productos.addAll(productoRepository.getAllProductos())
                    }

                    NavHost(navController = navController, startDestination = "inicioSesion") {
                        composable("home/{clienteId}") { backStackEntry ->
                            val clienteId = backStackEntry.arguments?.getString("clienteId")?.toInt() ?: 0
                            HomeScreen(navController, clienteId)
                        }
                        composable("inicioSesion") {
                            CustomLoginScreen(navController, userDao)
                        }
                        composable("producto?clienteId={clienteId}") { backStackEntry ->
                            val clienteId = backStackEntry.arguments?.getString("clienteId")?.toInt() ?: 0

                            // Cargar productos al inicio
                            lifecycleScope.launch {
                                productos.clear()
                                productos.addAll(productoRepository.getAllProductos())
                            }

                            ProductoScreen(
                                productos = productos,
                                onProductoSelected = { productoSeleccionado, cantidad ->
                                    // Obtener la fecha actual
                                    val fecha = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                                    navController.navigate("ventas/${productoSeleccionado.id}/$clienteId/$cantidad/$fecha")
                                },
                                onProductoAgregado = { nuevoProducto ->
                                    lifecycleScope.launch {
                                        productoRepository.insert(nuevoProducto)
                                        // Actualizar la lista de productos después de agregar uno nuevo
                                        productos.clear()
                                        productos.addAll(productoRepository.getAllProductos())
                                    }
                                }
                            )
                        }
                        composable("ventas/{productoId}/{clienteId}/{cantidad}/{fecha}") { backStackEntry ->
                            val productoId = backStackEntry.arguments?.getString("productoId")?.toInt() ?: 0
                            val clienteId = backStackEntry.arguments?.getString("clienteId")?.toInt() ?: 0
                            val cantidad = backStackEntry.arguments?.getString("cantidad")?.toInt() ?: 1
                            val fecha = backStackEntry.arguments?.getString("fecha") ?: ""

                            VentaScreen(
                                navController = navController,
                                productoId = productoId,
                                clienteId = clienteId,
                                cantidad = cantidad,
                                fecha = fecha,
                                ventaDao = ventaDao,
                                productoDao = productoDao
                            )
                        }
                        // Nueva ruta para historial de compras
                        composable("historial/{clienteId}") { backStackEntry ->
                            val clienteId = backStackEntry.arguments?.getString("clienteId")?.toInt() ?: 0
                            HistorialScreen(clienteId, ventaDao) // Asegúrate de crear esta pantalla
                        }
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("MainActivity", "Error en onCreate", e)
        }
    }
}