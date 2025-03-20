package com.example.laboralkutxatpv

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.laboralkutxatpv.databinding.ItemProductoBinding

class ProductoAdapter(
    private val productos: MutableList<Producto>,
    private val onTotalChanged: ((Double) -> Unit)? = null
) : RecyclerView.Adapter<ProductoAdapter.ProductoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductoViewHolder {
        val binding = ItemProductoBinding.inflate(
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

    // Método para calcular el total
    fun calcularTotal(): Double {
        var total = 0.0
        for (producto in productos) {
            total += producto.precio * producto.cantidad
        }
        return total
    }

    // Método para obtener solo los productos que tienen cantidad > 0
    fun obtenerProductosSeleccionados(): List<Producto> {
        return productos.filter { it.cantidad > 0 }
    }

    // Notificar cambio en total
    private fun notificarCambioTotal() {
        onTotalChanged?.invoke(calcularTotal())
    }

    inner class ProductoViewHolder(private val binding: ItemProductoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(producto: Producto) {
            binding.textViewNombre.text = producto.nombre
            binding.textViewCantidad.text = producto.cantidad.toString()
            binding.textViewPrecio.text = String.format("%.2f €", producto.precio)

            // Mostrar información de stock
            val stockInfo = if (producto.stockDisponible > 0) {
                "Disponible: ${producto.stockDisponible} unidades"
            } else {
                "Agotado"
            }
            binding.textViewStock?.text = stockInfo

            // Botón para sumar (ahora usando CardView)
            binding.cardButtonSumar.setOnClickListener {
                // Verificar si hay stock disponible antes de aumentar la cantidad
                if (producto.cantidad < producto.stockDisponible) {
                    producto.cantidad++
                    notifyItemChanged(adapterPosition)
                    notificarCambioTotal() // Notificar cambio en total
                } else {
                    // Mostrar mensaje si se alcanzó el límite de stock
                    Toast.makeText(
                        binding.root.context,
                        "No hay más unidades disponibles",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            // Botón para restar (ahora usando CardView)
            binding.cardButtonRestar.setOnClickListener {
                if (producto.cantidad > 0) {
                    producto.cantidad--
                    notifyItemChanged(adapterPosition)
                    notificarCambioTotal() // Notificar cambio en total
                }
            }
        }
    }
}
