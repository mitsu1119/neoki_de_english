package io.github.mitsu1119.neoki_de_english.ui.download

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import io.github.mitsu1119.neoki_de_english.MainActivity
import io.github.mitsu1119.neoki_de_english.conn.Server
import io.github.mitsu1119.neoki_de_english.databinding.FragmentDownloadBinding

class DownloadFragment: Fragment() {
    private var _binding: FragmentDownloadBinding? = null

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

        val server = Server(MainActivity().coroutineContext)

        server.getWBlist()

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}