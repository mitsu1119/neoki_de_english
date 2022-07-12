package io.github.mitsu1119.neoki_de_english.ui.quiz

import android.annotation.SuppressLint
import android.content.Intent
import android.media.MediaPlayer
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
import io.github.mitsu1119.neoki_de_english.alarm.MPService
import io.github.mitsu1119.neoki_de_english.databinding.FragmentQuizBinding
import io.github.mitsu1119.neoki_de_english.dictionary.DicSet
import kotlin.concurrent.timer

class QuizFragment: Fragment() {
    private var _binding: FragmentQuizBinding? = null
    private val binding get() = _binding!!

    private val args: QuizFragmentArgs by navArgs()

    private lateinit var mp: MediaPlayer

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
        var cw = args.cw
        var engs = args.engs
        var answers = args.answers
        var corrects = args.corrects
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

        // 問題生成
        val ds = DicSet.load(internalDir, dicName)
        val word = ds[java.util.Random().nextInt(ds.size)]
        var eng = word.first
        var jp = word.second

        if(numberOfQuiz == 0) {
            eng = engs
            engs = ""
            jp = answers
            answers = ""
        }

        transformViewModel.setQuestion(eng, jp)
        engs = engs + eng + "&"
        corrects = corrects + jp + "&"

        val tvEng = binding.tvEng
        tvEng.text = transformViewModel.q.value?.first

        // 選択肢
        val ans = java.util.Random().nextInt(3)
        val btnChoice1 = binding.btnChoice1
        if(ans == 0) btnChoice1.text = transformViewModel.q.value?.second
        else btnChoice1.text = ds[java.util.Random().nextInt(ds.size)].second
        btnChoice1.setOnClickListener {
            answers = answers + btnChoice1.text + "&"
            if(btnChoice1.text.equals(transformViewModel.q.value?.second)) cw = cw + "o"
            else cw = cw + "x"
            goNext(dicName, numberOfQuiz, cw, engs, answers, corrects)
        }

        val btnChoice2 = binding.btnChoice2
        if(ans == 1) btnChoice2.text = transformViewModel.q.value?.second
        else btnChoice2.text = ds[java.util.Random().nextInt(ds.size)].second
        btnChoice2.setOnClickListener {
            answers = answers + btnChoice2.text + "&"
            if(btnChoice2.text.equals(transformViewModel.q.value?.second)) cw = cw + "o"
            else cw = cw + "x"
            goNext(dicName, numberOfQuiz, cw, engs, answers, corrects)
        }

        val btnChoice3 = binding.btnChoice3
        if(ans == 2) btnChoice3.text = transformViewModel.q.value?.second
        else btnChoice3.text = ds[java.util.Random().nextInt(ds.size)].second
        btnChoice3.setOnClickListener {
            answers = answers + btnChoice3.text + "&"
            if(btnChoice3.text.equals(transformViewModel.q.value?.second)) cw = cw + "o"
            else cw = cw + "x"
            goNext(dicName, numberOfQuiz, cw, engs, answers, corrects)
        }

        // 音を鳴らす
        requireContext().stopService(Intent(requireContext().applicationContext, MPService::class.java))
        val audioFileName = requireContext().filesDir.absolutePath + "/dics/" + dicName + "/" + eng + ".wav"
        mp = MediaPlayer().apply {
            setDataSource(audioFileName)
            isLooping = true
        }
        mp.prepare()
        mp.start()

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mp.stop()
        _binding = null
    }

    private fun goNext(dicName: String, numberOfQuiz: Int, cw: String, engs: String, answers: String, corrects: String) {
        if (numberOfQuiz == 4) {
            val action = QuizFragmentDirections.actionToResult(cw, engs, answers, corrects)
            findNavController().navigate(action)
        } else {
            val action = QuizFragmentDirections.actionToQuiz(dicName, numberOfQuiz + 1, cw, engs, answers, corrects,)
            findNavController().navigate(action)
        }

    }
}