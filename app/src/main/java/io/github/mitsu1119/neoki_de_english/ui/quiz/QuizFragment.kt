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
import androidx.navigation.fragment.navArgs
import io.github.mitsu1119.neoki_de_english.databinding.FragmentQuizBinding
import io.github.mitsu1119.neoki_de_english.dictionary.DicSet
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
        Log.v("yey", "Quiz: $dicName")

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
        val btnChoice1 = binding.btnChoice1
        btnChoice1.text = ds[java.util.Random().nextInt(ds.size)].second

        val btnChoice2 = binding.btnChoice2
        btnChoice2.text = ds[java.util.Random().nextInt(ds.size)].second

        val btnChoice3 = binding.btnChoice3
        btnChoice3.text = ds[java.util.Random().nextInt(ds.size)].second

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}