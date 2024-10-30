package com.example.tiendagabogamezz.Screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tiendagabogamezz.Model.Producto
import com.example.tiendagabogamezz.Model.Venta
import com.example.tiendagabogamezz.Repository.ProductoRepository
import com.example.tiendagabogamezz.Repository.VentaRepository
import com.google.type.Date
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProductoViewModel(
    private val productoRepository: ProductoRepository,
    private val ventaRepository: VentaRepository
) : ViewModel() {

    // Estado para almacenar la lista de productos
    private val _productos = MutableStateFlow<List<Producto>>(emptyList())
    val productos: StateFlow<List<Producto>> = _productos.asStateFlow()

    // Producto seleccionado para la venta
    private val _productoSeleccionado = MutableStateFlow<Producto?>(null)
    val productoSeleccionado: StateFlow<Producto?> = _productoSeleccionado.asStateFlow()

    // Cargar productos al inicio
    init {
        viewModelScope.launch {
            _productos.value = productoRepository.getAllProductos()
        }
    }

    // Función para seleccionar un producto
    fun seleccionarProducto(producto: Producto, cantidad: Int) {
        if (cantidad <= producto.stock) {
            _productoSeleccionado.value = producto.copy(stock = cantidad)
        } else {
            // Maneja el caso de stock insuficiente
            // Podrías emitir un evento de error
        }
    }

    // Confirmar y registrar la venta
    fun confirmarVenta(clienteId: Int, cantidad: Int) {
        viewModelScope.launch {
            _productoSeleccionado.value?.let { producto ->
                if (cantidad <= producto.stock) {
                    // Crear la venta y reducir el stock
                    val nuevaVenta = Venta(
                        productoId = producto.id,
                        clienteId = clienteId,
                        cantidad = cantidad,
                        fecha = java.util.Date()
                    )
                    ventaRepository.insertVenta(nuevaVenta)
                    productoRepository.reducirStock(producto.id, cantidad)

                    // Actualizar productos y limpiar producto seleccionado
                    _productos.value = productoRepository.getAllProductos()
                    _productoSeleccionado.value = null
                } else {
                    // Emitir error si el stock es insuficiente
                }
            }
        }
    }
}
