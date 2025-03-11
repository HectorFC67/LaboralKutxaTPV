package com.example.laboralkutxatpv

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.laboralkutxatpv.databinding.ItemProductoBinding

class ProductoAdapter(private val productos: MutableList<Producto>) :
    RecyclerView.Adapter<ProductoAdapter.ProductoViewHolder>() {

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

    inner class ProductoViewHolder(private val binding: ItemProductoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(producto: Producto) {
            binding.textViewNombre.text = producto.nombre
            binding.textViewCantidad.text = producto.cantidad.toString()

            // Botón para sumar
            binding.buttonSumar.setOnClickListener {
                producto.cantidad++
                notifyItemChanged(adapterPosition)
            }

            // Botón para restar
            binding.buttonRestar.setOnClickListener {
                if (producto.cantidad > 0) {
                    producto.cantidad--
                    notifyItemChanged(adapterPosition)
                }
            }
        }
    }
}
