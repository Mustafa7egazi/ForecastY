package com.example.forecasty.ui.connection

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.forecasty.R
import com.example.forecasty.databinding.FragmentConnectionErrorBinding
import com.example.forecasty.ui.main_content.ContentActivity
import com.example.forecasty.ui.splash.MainActivity

class ConnectionErrorFragment : Fragment() {
    private lateinit var binding:FragmentConnectionErrorBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(layoutInflater,R.layout.fragment_connection_error, container, false)

        val args:ConnectionErrorFragmentArgs by navArgs()
        val source = args.source
        if (source == "api"){
            binding.connectionErrorLottie.setAnimation(R.raw.api_error)
            binding.header.text = "Request timeout!"
            binding.textView.text = "Server may be busy"
        }


        binding.tryAgainBtn.setOnClickListener{
            Intent(requireActivity(),MainActivity::class.java).also {
                startActivity(it)
            }
        }


        return binding.root
    }
}