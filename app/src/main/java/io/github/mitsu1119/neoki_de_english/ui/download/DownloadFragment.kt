package io.github.mitsu1119.neoki_de_english.ui.download

import android.annotation.SuppressLint
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import io.github.mitsu1119.neoki_de_english.MainActivity
import io.github.mitsu1119.neoki_de_english.conn.Server
import io.github.mitsu1119.neoki_de_english.databinding.FragmentDownloadBinding
import io.github.mitsu1119.neoki_de_english.databinding.ItemDicDownloadBinding
import io.github.mitsu1119.neoki_de_english.dictionary.DicSet
import io.github.mitsu1119.neoki_de_english.ui.local_dictionary.LocalDictionaryFragment
import io.github.mitsu1119.neoki_de_english.ui.local_dictionary.LocalDictionaryFragmentDirections
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*

class DownloadFragment: Fragment(), TextToSpeech.OnInitListener {
    private var _binding: FragmentDownloadBinding? = null

    private lateinit var internalDir: File
    private val binding get() = _binding!!

    private var tts: TextToSpeech? = null

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

        tts = TextToSpeech(requireContext(), this)

        val recyclerView = binding.recyclerView
        val adapter = DownloadAdapter()
        recyclerView.adapter = adapter
        transformViewModel.dicNames.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        val server = Server(MainActivity().coroutineContext)
        val remoteDics = server.getWBlist()
        val remoteDicList = remoteDics.split(",")
        remoteDicList.forEach { Log.v("m2_download", it); transformViewModel.addDicName(it) }

        adapter.itemClickListener = object: DownloadAdapter.OnItemClickListener {
            override fun onItemClick(holder: DownloadViewHolder): Boolean {
                Log.v("m2_download", "download... ${holder.textView.text}")
                server.downloadWB(holder.textView.text.toString(), internalDir, tts)
                return true
            }
        }

        return root
    }

    override fun onInit(status: Int) {
        if(status != TextToSpeech.SUCCESS) {
            Log.e("tts", "???????????????")
        } else {
            Log.e("tts", "???????????????")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    class DownloadAdapter :
        ListAdapter<String, DownloadViewHolder>(object : DiffUtil.ItemCallback<String>() {

            override fun areItemsTheSame(oldItem: String, newItem: String): Boolean =
                oldItem == newItem

            override fun areContentsTheSame(oldItem: String, newItem: String): Boolean =
                oldItem == newItem
        }) {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DownloadViewHolder {
            val binding = ItemDicDownloadBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return DownloadViewHolder(binding)
        }

        override fun onBindViewHolder(holder: DownloadViewHolder, position: Int) {
            val name = getItem(position)
            holder.textView.text = name
            holder.itemView.setOnClickListener {
                itemClickListener?.onItemClick(holder)
            }
        }

        var itemClickListener: OnItemClickListener? = null
        interface OnItemClickListener {
            fun onItemClick(holder: DownloadViewHolder): Boolean
        }
    }

    class DownloadViewHolder(binding: ItemDicDownloadBinding) :
        RecyclerView.ViewHolder(binding.root) {

        val textView: TextView = binding.textViewItemTransform
    }
}