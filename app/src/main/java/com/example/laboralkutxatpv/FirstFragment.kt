package com.example.laboralkutxatpv

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.laboralkutxatpv.databinding.FragmentFirstBinding

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

        // Escanear Tarjeta
        binding.buttonEscanear.setOnClickListener {
            Toast.makeText(requireContext(), "Escanear Tarjeta pulsado", Toast.LENGTH_SHORT).show()
            // Ejemplo: findNavController().navigate(R.id.action_FirstFragment_to_escanearTarjetaFragment)
        }

        // Clientes Recientes
        binding.buttonClientes.setOnClickListener {
            Toast.makeText(requireContext(), "Clientes Recientes pulsado", Toast.LENGTH_SHORT).show()
        }

        // Cupones Disponibles
        binding.buttonCupones.setOnClickListener {
            Toast.makeText(requireContext(), "Cupones Disponibles pulsado", Toast.LENGTH_SHORT).show()
        }

        // Historial de Ventas
        binding.buttonHistorial.setOnClickListener {
            Toast.makeText(requireContext(), "Historial de Ventas pulsado", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
