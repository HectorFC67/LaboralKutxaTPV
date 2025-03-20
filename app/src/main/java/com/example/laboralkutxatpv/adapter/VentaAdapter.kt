package com.example.laboralkutxatpv.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.laboralkutxatpv.R
import com.example.laboralkutxatpv.model.Venta
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Locale

class VentaAdapter(private val onVentaClick: (Venta) -> Unit) : RecyclerView.Adapter<VentaAdapter.VentaViewHolder>() {

    private var ventas: List<Venta> = emptyList()
    private val formatoFecha = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    private val formatoHora = SimpleDateFormat("HH:mm", Locale.getDefault())
    private val formatoMoneda = NumberFormat.getCurrencyInstance(Locale("es", "ES"))
    
    // Set para guardar qué elementos están expandidos
    private val elementosExpandidos = mutableSetOf<Long>()

    class VentaViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvFecha: TextView = view.findViewById(R.id.tvFecha)
        val tvHora: TextView = view.findViewById(R.id.tvHora)
        val tvMetodoPago: TextView = view.findViewById(R.id.tvMetodoPago)
        val tvMonto: TextView = view.findViewById(R.id.tvMonto)
        val tvCupon: TextView = view.findViewById(R.id.tvCupon)
        val layoutDetalles: LinearLayout = view.findViewById(R.id.layoutDetalles)
        val rvProductos: RecyclerView = view.findViewById(R.id.rvProductos)
        val btnExpandir: ImageButton = view.findViewById(R.id.btnExpandir)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VentaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_venta, parent, false)
        return VentaViewHolder(view)
    }

    override fun onBindViewHolder(holder: VentaViewHolder, position: Int) {
        val venta = ventas[position]
        
        // Configurar datos básicos
        holder.tvFecha.text = formatoFecha.format(venta.fecha)
        holder.tvHora.text = formatoHora.format(venta.fecha)
        holder.tvMetodoPago.text = venta.metodoPago
        holder.tvMonto.text = formatoMoneda.format(venta.montoTotal)
        holder.tvCupon.text = venta.cuponUtilizado
        
        // Configurar estado expandido/colapsado
        val estaExpandido = elementosExpandidos.contains(venta.id)
        holder.layoutDetalles.visibility = if (estaExpandido) View.VISIBLE else View.GONE
        holder.btnExpandir.setImageResource(
            if (estaExpandido) android.R.drawable.arrow_up_float
            else android.R.drawable.arrow_down_float
        )
        
        // Configurar RecyclerView de productos
        if (estaExpandido) {
            val productoAdapter = ProductoVendidoAdapter()
            holder.rvProductos.apply {
                layoutManager = LinearLayoutManager(holder.itemView.context)
                adapter = productoAdapter
            }
            productoAdapter.submitList(venta.productos)
        }
        
        // Configurar botón expandir/colapsar
        holder.btnExpandir.setOnClickListener {
            toggleExpansion(venta.id, holder)
        }
        
        // Configurar clic en toda la tarjeta
        holder.itemView.setOnClickListener {
            onVentaClick(venta)
            toggleExpansion(venta.id, holder)
        }
    }
    
    private fun toggleExpansion(ventaId: Long, holder: VentaViewHolder) {
        if (elementosExpandidos.contains(ventaId)) {
            elementosExpandidos.remove(ventaId)
            holder.layoutDetalles.visibility = View.GONE
            holder.btnExpandir.setImageResource(android.R.drawable.arrow_down_float)
        } else {
            elementosExpandidos.add(ventaId)
            holder.layoutDetalles.visibility = View.VISIBLE
            holder.btnExpandir.setImageResource(android.R.drawable.arrow_up_float)
            
            // Configurar RecyclerView de productos si es necesario
            val venta = ventas.find { it.id == ventaId }
            venta?.let {
                val productoAdapter = ProductoVendidoAdapter()
                holder.rvProductos.apply {
                    layoutManager = LinearLayoutManager(holder.itemView.context)
                    adapter = productoAdapter
                }
                productoAdapter.submitList(it.productos)
            }
        }
    }

    override fun getItemCount(): Int = ventas.size

    fun submitList(ventas: List<Venta>) {
        this.ventas = ventas
        notifyDataSetChanged()
    }
} 