package com.example.laboralkutxatpv

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.laboralkutxatpv.databinding.ItemProductoGestionBinding

class GestionProductosAdapter(
    private var productos: MutableList<ProductoCompleto>,
    private val onEditarClick: (ProductoCompleto) -> Unit,
    private val onEliminarClick: (ProductoCompleto) -> Unit
) : RecyclerView.Adapter<GestionProductosAdapter.ProductoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductoViewHolder {
        val binding = ItemProductoGestionBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ProductoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductoViewHolder, position: Int) {
        val producto = productos[position]
        holder.bind(producto)
    }

    override fun getItemCount(): Int = productos.size

    // Método para actualizar la lista de productos
    fun actualizarProductos(nuevosProductos: List<ProductoCompleto>) {
        productos.clear()
        productos.addAll(nuevosProductos)
        notifyDataSetChanged()
    }

    // Método para filtrar productos según término de búsqueda
    fun filtrarProductos(query: String, listaCompleta: List<ProductoCompleto>) {
        val productosFiltrados = if (query.isEmpty()) {
            listaCompleta
        } else {
            listaCompleta.filter {
                it.nombre.contains(query, ignoreCase = true) ||
                it.descripcion.contains(query, ignoreCase = true)
            }
        }
        actualizarProductos(productosFiltrados)
    }

    inner class ProductoViewHolder(private val binding: ItemProductoGestionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(producto: ProductoCompleto) {
            binding.tvNombreProducto.text = producto.nombre
            binding.tvPrecioProducto.text = String.format("%.2f €", producto.precio)
            binding.tvStockProducto.text = "Stock: ${producto.stock} unidades"

            // Configurar los botones
            binding.btnEditarProducto.setOnClickListener {
                onEditarClick(producto)
            }

            binding.btnEliminarProducto.setOnClickListener {
                onEliminarClick(producto)
            }
        }
    }
}