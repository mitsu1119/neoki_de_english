package io.github.mitsu1119.neoki_de_english.ui.download

import android.annotation.SuppressLint
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import io.github.mitsu1119.neoki_de_english.MainActivity
import io.github.mitsu1119.neoki_de_english.conn.Server
import io.github.mitsu1119.neoki_de_english.databinding.FragmentDownloadBinding
import io.github.mitsu1119.neoki_de_english.dictionary.DicSet
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

        val server = Server(MainActivity().coroutineContext)
        val remoteDics = server.getWBlist()

        server.downloadWB("dic1", internalDir)

        binding.button.setOnClickListener {
            createDicAudio("dic1")
        }

        return root
    }

    private fun createDicAudio(dicName: String) {
        val dir = File(internalDir, "dics/${dicName}")
        val words = dir.absolutePath + "/words.txt"

        val sc = Scanner(File(words))
        while (sc.hasNextLine()) {
            val line = sc.nextLine()
            Log.v("m2_dic", "generate... ${line}.mp3")
            speak(line, "${dir.absolutePath}/${line}.mp3")
            if(sc.hasNextLine()) sc.nextLine()
        }
    }

    override fun onInit(status: Int) {
        if(status != TextToSpeech.SUCCESS) {
            Log.e("tts", "初期化失敗")
        } else {
            Log.e("tts", "初期化成功")
        }
    }

    private fun speak(text: String, out: String) {
        val of = File(out)
        if(!of.exists()) {
            tts?.setLanguage(Locale.ENGLISH)
            tts?.synthesizeToFile(text, null, of, "WordsID")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}