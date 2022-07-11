package io.github.mitsu1119.neoki_de_english.ui.title

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import io.github.mitsu1119.neoki_de_english.R
import io.github.mitsu1119.neoki_de_english.databinding.FragmentTitleBinding
import io.github.mitsu1119.neoki_de_english.ui.home.HomeFragment
import java.util.*

class TitleFragment : Fragment() {

    private var _binding: FragmentTitleBinding? = null

    private val binding get() = _binding!!

    @SuppressLint("UnspecifiedImmutableFlag")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val transformViewModel = ViewModelProvider(this).get(TitleViewModel::class.java)
        _binding = FragmentTitleBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val btnAlarm = binding.btnAlarm
        btnAlarm.setOnClickListener { view ->
            findNavController().navigate(R.id.action_to_home)
        }

        val btnDictionary = binding.btnDic
        btnDictionary.setOnClickListener { view ->
            findNavController().navigate(R.id.action_to_dictionary)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}