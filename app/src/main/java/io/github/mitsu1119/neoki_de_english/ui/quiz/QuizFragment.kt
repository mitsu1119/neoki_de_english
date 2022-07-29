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
        // 最初の一問目（AlarmReceiver.ktにて設定）以外はランダム
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
        val text1 = binding.text1
        if(ans == 0) text1.text = transformViewModel.q.value?.second
        else text1.text = ds[java.util.Random().nextInt(ds.size)].second
        btnChoice1.setOnClickListener {
            answers = answers + text1.text + "&"
            if(text1.text.equals(transformViewModel.q.value?.second)) cw = cw + "o"
            else cw = cw + "x"
            goNext(dicName, numberOfQuiz, cw, engs, answers, corrects)
        }

        val btnChoice2 = binding.btnChoice2
        val text2 = binding.text2
        if(ans == 1) text2.text = transformViewModel.q.value?.second
        else text2.text = ds[java.util.Random().nextInt(ds.size)].second
        btnChoice2.setOnClickListener {
            answers = answers + text2.text + "&"
            if(text2.text.equals(transformViewModel.q.value?.second)) cw = cw + "o"
            else cw = cw + "x"
            goNext(dicName, numberOfQuiz, cw, engs, answers, corrects)
        }

        val btnChoice3 = binding.btnChoice3
        val text3 = binding.text3
        if(ans == 2) text3.text = transformViewModel.q.value?.second
        else text3.text = ds[java.util.Random().nextInt(ds.size)].second
        btnChoice3.setOnClickListener {
            answers = answers + text3.text + "&"
            if(text3.text.equals(transformViewModel.q.value?.second)) cw = cw + "o"
            else cw = cw + "x"
            goNext(dicName, numberOfQuiz, cw, engs, answers, corrects)
        }

        // 音を鳴らす
        requireContext().stopService(Intent(requireContext().applicationContext, MPService::class.java))
        val audioFileName = requireContext().filesDir.absolutePath + "/dics/" + dicName + "/" + eng + ".mp3"
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