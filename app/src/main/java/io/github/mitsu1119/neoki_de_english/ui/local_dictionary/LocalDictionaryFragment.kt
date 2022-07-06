package io.github.mitsu1119.neoki_de_english.ui.local_dictionary

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import io.github.mitsu1119.neoki_de_english.R
import io.github.mitsu1119.neoki_de_english.databinding.FragmentLocalDictionaryBinding
import io.github.mitsu1119.neoki_de_english.databinding.FragmentTitleBinding
import io.github.mitsu1119.neoki_de_english.dictionary.DicSet
import io.github.mitsu1119.neoki_de_english.ui.title.TitleViewModel
import java.io.File

class LocalDictionaryFragment: Fragment() {
    private var _binding: FragmentLocalDictionaryBinding? = null

    private val binding get() = _binding!!

    private lateinit var internalDir: File

    @SuppressLint("UnspecifiedImmutableFlag")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val transformViewModel = ViewModelProvider(this).get(LocalDictionaryViewModel::class.java)
        _binding = FragmentLocalDictionaryBinding.inflate(inflater, container, false)
        val root: View = binding.root

        internalDir = requireContext().filesDir
        transformViewModel.loadDicNames(internalDir)

        val btnAdd = binding.btnAdd
        btnAdd.setOnClickListener {
            DicSet.create(internalDir, "test")
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}