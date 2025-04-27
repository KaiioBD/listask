package com.example.listask.view.onboarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.listask.R
import com.example.listask.databinding.FragmentRegisterBinding
import com.example.listask.utils.FragmentCommunicator
import com.example.listask.viewModel.RegisterViewModel

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<RegisterViewModel>()
    private lateinit var communicator: FragmentCommunicator
    private var isValid: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        communicator = requireActivity() as OnboardingActivity
        setupView()
        setupObservers()
        return binding.root

    }

    private fun setupView() {
        binding.iconButton.setOnClickListener {
            findNavController().navigate(R.id.action_RegisterFragment_to_LoginFragment)
        }

        binding.registrar.setOnClickListener {
            if (isValid) {
                requestRegister()
            } else {
                Toast.makeText(activity, "Ingreso invalido", Toast.LENGTH_SHORT).show()
            }
        }

        binding.emailTextField.editText?.addTextChangedListener {
            binding.emailTextField.error = if (it.isNullOrEmpty()) {
                isValid = false
                "Por favor introduce un correo"
            } else {
                isValid = true
                null
            }
        }

        binding.passwordTextField.editText?.addTextChangedListener {
            binding.passwordTextField.error = if (it.isNullOrEmpty()) {
                isValid = false
                "Por favor introduce una contraseña"
            } else {
                isValid = true
                null
            }
        }
    }

    private fun setupObservers() {
        viewModel.loaderState.observe(viewLifecycleOwner) { loaderState ->
            communicator.showLoader(loaderState)
        }

        viewModel.signUpSuccess.observe(viewLifecycleOwner) { success ->
            if (success) {
                findNavController().navigate(R.id.action_RegisterFragment_to_LoginFragment)
            }
        }
    }


    private fun requestRegister() {
        viewModel.requestSignUp(
            binding.emailTextField.editText?.text.toString(),
            binding.passwordTextField.editText?.text.toString()
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}