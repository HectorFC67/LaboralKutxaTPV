package com.example.laboralkutxatpv

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.laboralkutxatpv.adapter.VentaAdapter
import com.example.laboralkutxatpv.model.Venta
import com.example.laboralkutxatpv.repository.VentaRepository
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class HistorialVentasActivity : AppCompatActivity() {

    private lateinit var rvVentas: RecyclerView
    private lateinit var tvNoVentas: TextView
    private lateinit var btnFechaInicio: Button
    private lateinit var btnFechaFin: Button
    private lateinit var spinnerMetodoPago: Spinner
    private lateinit var btnVolver: ImageButton
    
    private lateinit var ventaAdapter: VentaAdapter
    private lateinit var repository: VentaRepository
    
    private val formatoFecha = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    private val calendar = Calendar.getInstance()
    
    // Fechas por defecto: últimos 30 días
    private var fechaInicio: Date = calendar.apply { add(Calendar.DAY_OF_MONTH, -30) }.time
    private var fechaFin: Date = Calendar.getInstance().time
    
    // Método de pago seleccionado
    private var metodoPagoSeleccionado: String = "Todos"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_historial_ventas)
        
        // Inicializar repositorio
        repository = VentaRepository.getInstance()
        
        // Inicializar vistas
        initViews()
        setupListeners()
        setupRecyclerView()
        setupSpinner()
        
        // Cargar datos iniciales
        cargarVentas()
    }
    
    private fun initViews() {
        rvVentas = findViewById(R.id.rvVentas)
        tvNoVentas = findViewById(R.id.tvNoVentas)
        btnFechaInicio = findViewById(R.id.btnFechaInicio)
        btnFechaFin = findViewById(R.id.btnFechaFin)
        spinnerMetodoPago = findViewById(R.id.spinnerMetodoPago)
        btnVolver = findViewById(R.id.btnVolver)
        
        // Configurar fechas iniciales en botones
        btnFechaInicio.text = formatoFecha.format(fechaInicio)
        btnFechaFin.text = formatoFecha.format(fechaFin)
    }
    
    private fun setupListeners() {
        btnFechaInicio.setOnClickListener {
            showDatePicker(true)
        }
        
        btnFechaFin.setOnClickListener {
            showDatePicker(false)
        }
        
        btnVolver.setOnClickListener {
            finish()
        }
    }
    
    private fun setupRecyclerView() {
        ventaAdapter = VentaAdapter { venta ->
            // Acción al hacer clic en una venta (además de expandir)
            Toast.makeText(this, "Venta #${venta.id} seleccionada", Toast.LENGTH_SHORT).show()
        }
        
        rvVentas.apply {
            layoutManager = LinearLayoutManager(this@HistorialVentasActivity)
            adapter = ventaAdapter
        }
    }
    
    private fun setupSpinner() {
        val metodosPago = arrayOf("Todos", "Efectivo", "Tarjeta", "Bizum")
        
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            metodosPago
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        
        spinnerMetodoPago.adapter = adapter
        
        spinnerMetodoPago.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                metodoPagoSeleccionado = metodosPago[position]
                cargarVentas()
            }
            
            override fun onNothingSelected(parent: AdapterView<*>?) {
                // No hacer nada
            }
        }
    }
    
    private fun cargarVentas() {
        val ventas = if (metodoPagoSeleccionado == "Todos") {
            repository.filtrarPorFecha(fechaInicio, fechaFin)
        } else {
            repository.filtrarPorFecha(fechaInicio, fechaFin)
                .filter { it.metodoPago.equals(metodoPagoSeleccionado, ignoreCase = true) }
        }
        
        if (ventas.isEmpty()) {
            tvNoVentas.visibility = View.VISIBLE
            rvVentas.visibility = View.GONE
        } else {
            tvNoVentas.visibility = View.GONE
            rvVentas.visibility = View.VISIBLE
            ventaAdapter.submitList(ventas)
        }
    }
    
    private fun showDatePicker(esInicio: Boolean) {
        val fecha = if (esInicio) fechaInicio else fechaFin
        calendar.time = fecha
        
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        
        DatePickerDialog(this, { _, yearSelected, monthSelected, daySelected ->
            calendar.set(yearSelected, monthSelected, daySelected)
            val fechaSeleccionada = calendar.time
            
            if (esInicio) {
                if (fechaSeleccionada.after(fechaFin)) {
                    // No permitir fechas de inicio posteriores a la fecha fin
                    Toast.makeText(
                        this,
                        "La fecha de inicio no puede ser posterior a la fecha fin",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@DatePickerDialog
                }
                fechaInicio = fechaSeleccionada
                btnFechaInicio.text = formatoFecha.format(fechaInicio)
            } else {
                if (fechaSeleccionada.before(fechaInicio)) {
                    // No permitir fechas de fin anteriores a la fecha inicio
                    Toast.makeText(
                        this,
                        "La fecha fin no puede ser anterior a la fecha de inicio",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@DatePickerDialog
                }
                fechaFin = fechaSeleccionada
                btnFechaFin.text = formatoFecha.format(fechaFin)
            }
            
            // Recargar ventas con nuevo filtro de fechas
            cargarVentas()
            
        }, year, month, day).show()
    }
} 