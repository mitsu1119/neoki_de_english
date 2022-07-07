package io.github.mitsu1119.neoki_de_english.ui.dictionary

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import io.github.mitsu1119.neoki_de_english.R
import io.github.mitsu1119.neoki_de_english.databinding.FragmentDictionaryBinding
import io.github.mitsu1119.neoki_de_english.databinding.FragmentTitleBinding
import io.github.mitsu1119.neoki_de_english.ui.title.TitleViewModel

class DictionaryFragment : Fragment() {

    private var _binding: FragmentDictionaryBinding? = null

    private val binding get() = _binding!!

    @SuppressLint("UnspecifiedImmutableFlag")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val transformViewModel = ViewModelProvider(this).get(DictionaryViewModel::class.java)
        _binding = FragmentDictionaryBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val btnLocalDic = binding.btnLocalDic
        btnLocalDic.setOnClickListener { view ->
            findNavController().navigate(R.id.action_to_local_dictionary)
        }

        val btnUpload = binding.btnUpload
        btnUpload.setOnClickListener { view ->
            findNavController().navigate(R.id.action_to_upload)
        }

        val btnDownload = binding.btnDownload
        btnDownload.setOnClickListener { view ->
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}