package io.github.mitsu1119.neoki_de_english.ui.quiz

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import io.github.mitsu1119.neoki_de_english.databinding.FragmentQuizBinding
import io.github.mitsu1119.neoki_de_english.ui.words.WordsFragmentArgs

class QuizFragment: Fragment() {
    private var _binding: FragmentQuizBinding? = null
    private val binding get() = _binding!!

    private val args: QuizFragmentArgs by navArgs()

    @SuppressLint("UnspecifiedImmutableFlag")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val transformViewModel = ViewModelProvider(this).get(QuizViewModel::class.java)
        _binding = FragmentQuizBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val dicName = args.dicName
        Log.v("yey", "Quiz: $dicName")

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}