package io.github.mitsu1119.neoki_de_english.ui.words

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import io.github.mitsu1119.neoki_de_english.databinding.FragmentLocalDictionaryBinding
import io.github.mitsu1119.neoki_de_english.databinding.FragmentWordsBinding
import io.github.mitsu1119.neoki_de_english.ui.local_dictionary.LocalDictionaryViewModel
import org.intellij.lang.annotations.JdkConstants
import java.io.File

class WordsFragment: Fragment() {
    private var _binding: FragmentWordsBinding? = null
    private val binding get() = _binding!!

    private lateinit var internalDir: File

    private val args: WordsFragmentArgs by navArgs()

    @SuppressLint("UnspecifiedImmutableFlag")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val transformViewModel = ViewModelProvider(this).get(WordsViewModel::class.java)
        _binding = FragmentWordsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        Toast.makeText(context, args.dicName, Toast.LENGTH_SHORT).show()

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}