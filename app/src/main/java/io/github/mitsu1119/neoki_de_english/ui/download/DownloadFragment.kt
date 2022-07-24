package io.github.mitsu1119.neoki_de_english.ui.download

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import io.github.mitsu1119.neoki_de_english.MainActivity
import io.github.mitsu1119.neoki_de_english.conn.Server
import io.github.mitsu1119.neoki_de_english.databinding.FragmentDownloadBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.File

class DownloadFragment: Fragment() {
    private var _binding: FragmentDownloadBinding? = null

    private lateinit var internalDir: File
    private val binding get() = _binding!!

    @SuppressLint("UnspecifiedImmutableFlag")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val transformViewModel = ViewModelProvider(this).get(DownloadViewModel::class.java)
        _binding = FragmentDownloadBinding.inflate(inflater, container, false)
        val root: View = binding.root
        internalDir = requireContext().filesDir

        val server = Server(MainActivity().coroutineContext)
        val remoteDics = server.getWBlist()
        server.downloadWB("dic1", internalDir)

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}