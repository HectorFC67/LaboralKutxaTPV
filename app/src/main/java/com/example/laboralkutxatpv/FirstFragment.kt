package com.example.laboralkutxatpv

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.laboralkutxatpv.databinding.FragmentFirstBinding
import android.content.Intent

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
            Toast.makeText(requireContext(), "Historial de compras pulsado", Toast.LENGTH_SHORT).show()
        }

        // Historial de Ventas
        binding.buttonHistorial.setOnClickListener {
            val intent = Intent(requireContext(), QRCodeScannerActivity::class.java)
            startActivity(intent)
        }
        // Historial de Ventas
        binding.buttonPagarDirecto.setOnClickListener {
            val intent = Intent(requireContext(), QRCodeScannerActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
