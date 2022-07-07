package io.github.mitsu1119.neoki_de_english.ui.upload

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import io.github.mitsu1119.neoki_de_english.databinding.FragmentUploadBinding

class UploadFragment: Fragment() {
    private var _binding: FragmentUploadBinding? = null

    private val binding get() = _binding!!

    @SuppressLint("UnspecifiedImmutableFlag")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val transformViewModel = ViewModelProvider(this).get(UploadViewModel::class.java)
        _binding = FragmentUploadBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val btnAlarm = binding.btnUpload
        btnAlarm.setOnClickListener { view ->
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}