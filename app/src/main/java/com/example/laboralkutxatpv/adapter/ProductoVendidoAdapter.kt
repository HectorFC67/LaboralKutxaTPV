package com.example.laboralkutxatpv.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.laboralkutxatpv.R
import com.example.laboralkutxatpv.model.ProductoVendido
import java.text.NumberFormat
import java.util.Locale

class ProductoVendidoAdapter : RecyclerView.Adapter<ProductoVendidoAdapter.ProductoVendidoViewHolder>() {

    private var productos: List<ProductoVendido> = emptyList()
    private val formatoMoneda = NumberFormat.getCurrencyInstance(Locale("es", "ES"))

    class ProductoVendidoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvCantidad: TextView = view.findViewById(R.id.tvCantidad)
        val tvNombreProducto: TextView = view.findViewById(R.id.tvNombreProducto)
        val tvPrecioUnitario: TextView = view.findViewById(R.id.tvPrecioUnitario)
        val tvSubtotal: TextView = view.findViewById(R.id.tvSubtotal)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductoVendidoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_producto_vendido, parent, false)
        return ProductoVendidoViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductoVendidoViewHolder, position: Int) {
        val producto = productos[position]
        
        holder.tvCantidad.text = "${producto.cantidad}x"
        holder.tvNombreProducto.text = producto.nombreProducto
        holder.tvPrecioUnitario.text = formatoMoneda.format(producto.precioUnitario)
        holder.tvSubtotal.text = formatoMoneda.format(producto.subtotal)
    }

    override fun getItemCount(): Int = productos.size

    fun submitList(productos: List<ProductoVendido>) {
        this.productos = productos
        notifyDataSetChanged()
    }
} 