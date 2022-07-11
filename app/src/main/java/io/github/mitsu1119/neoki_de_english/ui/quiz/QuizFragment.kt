package io.github.mitsu1119.neoki_de_english.ui.quiz

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import io.github.mitsu1119.neoki_de_english.databinding.FragmentQuizBinding
import io.github.mitsu1119.neoki_de_english.dictionary.DicSet
import io.github.mitsu1119.neoki_de_english.ui.local_dictionary.LocalDictionaryFragmentDirections
import io.github.mitsu1119.neoki_de_english.ui.words.WordsFragmentArgs
import kotlin.concurrent.timer
import kotlin.random.Random

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
        val numberOfQuiz = args.numberOfQuiz
        Log.v("yey", "Quiz: $dicName, $numberOfQuiz")

        val handler = Handler(Looper.getMainLooper())
        timer("update", period = 1000) {
            handler.post {
                transformViewModel.updateTime()
            }
        }

        val tvTime = binding.tvTime
        transformViewModel.nowTime.observe(viewLifecycleOwner) {
            tvTime.text = transformViewModel.nowTime.value
        }


        val internalDir = requireContext().filesDir
        val ds = DicSet.load(internalDir, dicName)
        val word = ds[java.util.Random().nextInt(ds.size)]

        transformViewModel.setQuestion(word.first, word.second)

        val tvEng = binding.tvEng
        tvEng.text = transformViewModel.q.value?.first

        // 選択肢
        val ans = java.util.Random().nextInt(3)
        val btnChoice1 = binding.btnChoice1
        if(ans == 0) btnChoice1.text = transformViewModel.q.value?.second
        else btnChoice1.text = ds[java.util.Random().nextInt(ds.size)].second
        btnChoice1.setOnClickListener {
            if (numberOfQuiz != 4) {
                val action = QuizFragmentDirections.actionToQuiz(dicName, numberOfQuiz + 1)
                findNavController().navigate(action)
            }
        }

        val btnChoice2 = binding.btnChoice2
        if(ans == 1) btnChoice2.text = transformViewModel.q.value?.second
        else btnChoice2.text = ds[java.util.Random().nextInt(ds.size)].second

        val btnChoice3 = binding.btnChoice3
        if(ans == 2) btnChoice3.text = transformViewModel.q.value?.second
        else btnChoice3.text = ds[java.util.Random().nextInt(ds.size)].second

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}