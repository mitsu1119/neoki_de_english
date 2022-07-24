package io.github.mitsu1119.neoki_de_english.ui.upload

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import io.github.mitsu1119.neoki_de_english.MainActivity
import io.github.mitsu1119.neoki_de_english.conn.Server
import io.github.mitsu1119.neoki_de_english.databinding.FragmentUploadBinding
import io.github.mitsu1119.neoki_de_english.ui.dictionary.DictionaryViewModel
import io.github.mitsu1119.neoki_de_english.ui.local_dictionary.LocalDictionaryViewModel
import java.io.File

class UploadFragment: Fragment() {
    private var _binding: FragmentUploadBinding? = null
    private val binding get() = _binding!!

    private lateinit var internalDir: File

    @SuppressLint("UnspecifiedImmutableFlag")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val transformViewModel = ViewModelProvider(this).get(UploadViewModel::class.java)
        _binding = FragmentUploadBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val server = Server(MainActivity().coroutineContext)

        internalDir = requireContext().filesDir

        val dicDir = File(internalDir.absolutePath + "/dics")
        transformViewModel.loadDicNames(dicDir)

        // 辞書選択
        val spinnerItems = transformViewModel.dicNames.value!!.stream().toArray { arrayOfNulls<String>(it) }
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, spinnerItems)
        val spnDic = binding.spnDic
        spnDic.adapter = adapter

        val btnUpload = binding.btnUpload
        btnUpload.setOnClickListener { view ->
            server.uploadWB(spnDic.selectedItem as String, requireContext().filesDir)
            Log.v("yey", "uploaded ${spnDic.selectedItem as String}")
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}