package com.example.laboralkutxatpv

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.laboralkutxatpv.databinding.FragmentFirstBinding
import android.content.Intent
import android.app.Activity

class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Realizar Compra
        binding.buttonRealizarCompra.setOnClickListener {
            val intent = Intent(requireContext(), VentanaRealizarCompraActivity::class.java)
            startActivity(intent)
        }

        // Gestión de Productos (antes "Clientes Recientes")
        binding.buttonClientes.setOnClickListener {
            val intent = Intent(requireContext(), GestionProductosActivity::class.java)
            startActivity(intent)
        }

        // Cupones Disponibles
        binding.buttonCupones.setOnClickListener {
            val intent = Intent(requireContext(), HistorialVentasActivity::class.java)
            startActivity(intent)
        }

        // Historial de Ventas
        binding.buttonPagarDirecto.setOnClickListener {
            val intent = Intent(requireContext(), IntroducirPago2::class.java)
            startActivityForResult(intent, REQUEST_CODE_INTRODUCIR_PAGO)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val REQUEST_CODE_INTRODUCIR_PAGO = 1001
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_INTRODUCIR_PAGO && resultCode == Activity.RESULT_OK) {
            val importe = data?.getDoubleExtra("importe", 0.0) ?: 0.0
            // Aquí puedes manejar el importe introducido
            Toast.makeText(requireContext(), "Importe introducido: ${String.format("%.2f€", importe)}", Toast.LENGTH_LONG).show()
        }
    }
}
