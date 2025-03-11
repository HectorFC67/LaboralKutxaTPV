package com.example.laboralkutxatpv

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.laboralkutxatpv.databinding.ActivityGestionProductosBinding
import com.example.laboralkutxatpv.databinding.DialogEditarProductoBinding

class GestionProductosActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGestionProductosBinding
    private lateinit var adapter: GestionProductosAdapter
    private lateinit var productoRepository: ProductoRepository
    private val listaProductos = mutableListOf<ProductoCompleto>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGestionProductosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicializar el repositorio
        productoRepository = ProductoRepository.getInstance(applicationContext)

        // Configurar la toolbar
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Inicializar el RecyclerView
        adapter = GestionProductosAdapter(
            listaProductos,
            onEditarClick = { producto -> mostrarDialogoEditarProducto(producto) },
            onEliminarClick = { producto -> mostrarDialogoConfirmacionEliminar(producto) }
        )
        binding.recyclerViewProductos.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewProductos.adapter = adapter

        // Configurar el FAB para añadir producto
        binding.fabAgregarProducto.setOnClickListener {
            mostrarDialogoNuevoProducto()
        }

        // Configurar la búsqueda
        binding.etBusqueda.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                adapter.filtrarProductos(s.toString(), listaProductos)
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        // Cargar productos desde el repositorio
        cargarProductos()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                // Volver atrás cuando se presiona la flecha de navegación
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    // Función para cargar productos desde el repositorio
    private fun cargarProductos() {
        listaProductos.clear()
        listaProductos.addAll(productoRepository.obtenerTodosLosProductos())
        adapter.notifyDataSetChanged()
        actualizarVisibilidadNoProductos()
    }

    // Función para mostrar el diálogo de nuevo producto
    private fun mostrarDialogoNuevoProducto() {
        val dialogBinding = DialogEditarProductoBinding.inflate(LayoutInflater.from(this))
        
        dialogBinding.tvTituloDialog.text = "Nuevo Producto"
        
        val dialog = AlertDialog.Builder(this)
            .setView(dialogBinding.root)
            .create()
        
        dialogBinding.btnCancelar.setOnClickListener {
            dialog.dismiss()
        }
        
        dialogBinding.btnGuardar.setOnClickListener {
            val nombre = dialogBinding.etNombre.text.toString()
            val precioText = dialogBinding.etPrecio.text.toString()
            val stockText = dialogBinding.etStock.text.toString()
            val descripcion = dialogBinding.etDescripcion.text.toString()
            
            if (nombre.isEmpty() || precioText.isEmpty() || stockText.isEmpty()) {
                Toast.makeText(this, "Por favor, complete los campos obligatorios", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            
            try {
                val precio = precioText.toDouble()
                val stock = stockText.toInt()
                
                // Crear nuevo producto
                val nuevoProducto = ProductoCompleto(
                    nombre = nombre,
                    precio = precio,
                    stock = stock,
                    descripcion = descripcion
                )
                
                // Añadir al repositorio
                val id = productoRepository.agregarProducto(nuevoProducto)
                val productoConId = nuevoProducto.copy(id = id)
                
                // Añadir a la lista local
                listaProductos.add(productoConId)
                adapter.notifyItemInserted(listaProductos.size - 1)
                
                // Actualizar visibilidad del mensaje "No hay productos"
                actualizarVisibilidadNoProductos()
                
                dialog.dismiss()
                Toast.makeText(this, "Producto añadido correctamente", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(this, "Por favor, introduzca valores válidos", Toast.LENGTH_SHORT).show()
            }
        }
        
        dialog.show()
    }
    
    // Función para mostrar el diálogo de edición de producto
    private fun mostrarDialogoEditarProducto(producto: ProductoCompleto) {
        val dialogBinding = DialogEditarProductoBinding.inflate(LayoutInflater.from(this))
        
        dialogBinding.tvTituloDialog.text = "Editar Producto"
        
        // Prellenar con datos del producto
        dialogBinding.etNombre.setText(producto.nombre)
        dialogBinding.etPrecio.setText(producto.precio.toString())
        dialogBinding.etStock.setText(producto.stock.toString())
        dialogBinding.etDescripcion.setText(producto.descripcion)
        
        val dialog = AlertDialog.Builder(this)
            .setView(dialogBinding.root)
            .create()
        
        dialogBinding.btnCancelar.setOnClickListener {
            dialog.dismiss()
        }
        
        dialogBinding.btnGuardar.setOnClickListener {
            val nombre = dialogBinding.etNombre.text.toString()
            val precioText = dialogBinding.etPrecio.text.toString()
            val stockText = dialogBinding.etStock.text.toString()
            val descripcion = dialogBinding.etDescripcion.text.toString()
            
            if (nombre.isEmpty() || precioText.isEmpty() || stockText.isEmpty()) {
                Toast.makeText(this, "Por favor, complete los campos obligatorios", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            
            try {
                val precio = precioText.toDouble()
                val stock = stockText.toInt()
                
                // Actualizar datos del producto
                val productoActualizado = producto.copy(
                    nombre = nombre,
                    precio = precio,
                    stock = stock,
                    descripcion = descripcion
                )
                
                // Actualizar en el repositorio
                productoRepository.actualizarProducto(productoActualizado)
                
                // Actualizar en la lista local
                val position = listaProductos.indexOfFirst { it.id == producto.id }
                if (position != -1) {
                    listaProductos[position] = productoActualizado
                    adapter.notifyItemChanged(position)
                }
                
                dialog.dismiss()
                Toast.makeText(this, "Producto actualizado correctamente", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(this, "Por favor, introduzca valores válidos", Toast.LENGTH_SHORT).show()
            }
        }
        
        dialog.show()
    }
    
    // Función para mostrar diálogo de confirmación al eliminar un producto
    private fun mostrarDialogoConfirmacionEliminar(producto: ProductoCompleto) {
        AlertDialog.Builder(this)
            .setTitle("Eliminar Producto")
            .setMessage("¿Está seguro de que desea eliminar ${producto.nombre}?")
            .setPositiveButton("Eliminar") { _, _ ->
                // Eliminar del repositorio
                productoRepository.eliminarProducto(producto.id)
                
                // Eliminar de la lista local
                val position = listaProductos.indexOfFirst { it.id == producto.id }
                if (position != -1) {
                    listaProductos.removeAt(position)
                    adapter.notifyItemRemoved(position)
                    actualizarVisibilidadNoProductos()
                }
                
                Toast.makeText(this, "Producto eliminado", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }
    
    // Función para actualizar la visibilidad del mensaje "No hay productos"
    private fun actualizarVisibilidadNoProductos() {
        binding.tvNoProductos.visibility = if (listaProductos.isEmpty()) View.VISIBLE else View.GONE
    }
}