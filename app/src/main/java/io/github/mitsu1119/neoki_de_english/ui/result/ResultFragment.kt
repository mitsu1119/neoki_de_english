package io.github.mitsu1119.neoki_de_english.ui.result

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import io.github.mitsu1119.neoki_de_english.databinding.FragmentDownloadBinding
import io.github.mitsu1119.neoki_de_english.databinding.FragmentResultBinding
import io.github.mitsu1119.neoki_de_english.ui.download.DownloadViewModel
import io.github.mitsu1119.neoki_de_english.ui.quiz.QuizFragmentArgs
import io.github.mitsu1119.neoki_de_english.ui.quiz.QuizFragmentDirections

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

        val engs = args.engs
        val engList = engs.split("&")
        val cw = args.cw
        val answers = args.answers
        val answersList = answers.split("&")
        val corrects = args.corrects
        val correctsList = corrects.split("&")
        Log.e("yey", cw)
        Log.e("yey", engList.size.toString())
        Log.e("yey", answers)
        Log.e("yey", corrects)

        val dicname0 = binding.dicname0
        dicname0.text = engList[0]
        val good0 = binding.good0
        good0.text = correctsList[0]
        val bad0 = binding.bad0
        bad0.text = answersList[0]

        val dicname1 = binding.dicname1
        dicname1.text = engList[1]
        val good1 = binding.good1
        good1.text = correctsList[1]
        val bad1 = binding.bad1
        bad1.text = answersList[1]

        val dicname2 = binding.dicname2
        dicname2.text = engList[2]
        val good2 = binding.good2
        good2.text = correctsList[2]
        val bad2 = binding.bad2
        bad2.text = answersList[2]

        val dicname3 = binding.dicname3
        dicname3.text = engList[3]
        val good3 = binding.good3
        good3.text = correctsList[3]
        val bad3 = binding.bad3
        bad3.text = answersList[3]

        val dicname4 = binding.dicname4
        dicname4.text = engList[4]
        val good4 = binding.good4
        good4.text = correctsList[4]
        val bad4 = binding.bad4
        bad4.text = answersList[4]

        // タイトルへ戻るボタン
        val btnToTitle = binding.btnToTitle
        btnToTitle.setOnClickListener {
            val action = ResultFragmentDirections.actionToTitle()
            findNavController().navigate(action)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}