package io.github.mitsu1119.neoki_de_english.ui.result

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import io.github.mitsu1119.neoki_de_english.databinding.FragmentDownloadBinding
import io.github.mitsu1119.neoki_de_english.databinding.FragmentResultBinding
import io.github.mitsu1119.neoki_de_english.ui.download.DownloadViewModel
import io.github.mitsu1119.neoki_de_english.ui.quiz.QuizFragmentArgs

class ResultFragment: Fragment() {
    private var _binding: FragmentResultBinding? = null
    private val binding get() = _binding!!

    private val args: ResultFragmentArgs by navArgs()

    @SuppressLint("UnspecifiedImmutableFlag")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val transformViewModel = ViewModelProvider(this).get(ResultViewModel::class.java)
        _binding = FragmentResultBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val cw = args.cw
        val engs = args.engs
        val answers = args.answers
        val corrects = args.corrects
        Log.e("yey", cw)
        Log.e("yey", engs)
        Log.e("yey", answers)
        Log.e("yey", corrects)

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}