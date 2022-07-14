package io.github.mitsu1119.neoki_de_english.ui.home

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent.getBroadcast
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import io.github.mitsu1119.neoki_de_english.alarm.AlarmReceiver
import io.github.mitsu1119.neoki_de_english.alarm.AlarmSet
import io.github.mitsu1119.neoki_de_english.databinding.FragmentHomeBinding
import io.github.mitsu1119.neoki_de_english.databinding.ItemHomeBinding
import io.github.mitsu1119.neoki_de_english.ui.create_alarm.CreateAlarmFragment
import io.github.mitsu1119.neoki_de_english.ui.words.WordsFragment
import java.io.File
import java.util.*


/**
 * Fragment that demonstrates a responsive layout pattern where the format of the content
 * transforms depending on the size of the screen. Specifically this Fragment shows items in
 * the [RecyclerView] using LinearLayoutManager in a small screen
 * and shows items using GridLayoutManager in a large screen.
 */
class HomeFragment : Fragment(), CreateAlarmFragment.NoticeDialogLister {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var internalDir: File
    private lateinit var transformViewModel: HomeViewModel

    @SuppressLint("UnspecifiedImmutableFlag")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        transformViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        internalDir = requireContext().filesDir
        transformViewModel.loadAlarms(internalDir)

        val recyclerView = binding.recyclerviewTransform
        val adapter = TransformAdapter()
        recyclerView.adapter = adapter

        adapter.btnRemoveClickListener =
            object: TransformAdapter.OnRemoveClickListener {
                override fun onRemoveClick(position: Int): Boolean {
                    Log.v("yey", "Remove: $position")
                    return true
                }
            }

        transformViewModel.texts.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        val btnAdd = binding.btnAdd
        btnAdd.setOnClickListener { view ->
            val dialog = CreateAlarmFragment()
            dialog.show(childFragmentManager, "CreateAlarm")
        }

        return root
    }

    override fun onDialogPositiveClick(dialog: DialogFragment, hour:Int, minute:Int, dayOfWeeks: Array<Boolean>, dic: String) {
        Log.v("yey", "$hour:$minute")
        Log.v("yey", dayOfWeeks.contentToString())
        Log.v("yey", dic)

        // アラーム追加

        val alarm: AlarmManager = context?.getSystemService(ALARM_SERVICE) as AlarmManager
        val pending = Intent(context, AlarmReceiver::class.java).let { intent ->
            intent.addFlags(Intent.FLAG_RECEIVER_FOREGROUND)
            intent.putExtra("dictionary", dic)
            getBroadcast(context, 0, intent, 0)
        }

        val cl = Calendar.getInstance()
        cl.timeInMillis  = System.currentTimeMillis()
        cl.set(Calendar.HOUR_OF_DAY, hour)
        cl.set(Calendar.MINUTE, minute)
        cl.set(Calendar.SECOND, 0)
        cl.set(Calendar.MILLISECOND, 0)

        // アラームを入力された時刻にセット
        // alarm.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, cl.timeInMillis, pending)

        // アラームを5秒後にセット
        alarm.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + 1000, pending)
        Log.v("yey", "Set Alarm")

        // TODO("曜日でリピート")

        val al = AlarmSet.create(internalDir, hour, minute)
        transformViewModel.addAlarm(al)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    class TransformAdapter :
        ListAdapter<String, TransformViewHolder>(object : DiffUtil.ItemCallback<String>() {

            override fun areItemsTheSame(oldItem: String, newItem: String): Boolean =
                oldItem == newItem

            override fun areContentsTheSame(oldItem: String, newItem: String): Boolean =
                oldItem == newItem
        }) {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransformViewHolder {
            val binding = ItemHomeBinding.inflate(LayoutInflater.from(parent.context))
            return TransformViewHolder(binding)
        }

        override fun onBindViewHolder(holder: TransformViewHolder, position: Int) {
            holder.textView.text = getItem(position)
            holder.btnRemove.setOnClickListener {
                btnRemoveClickListener?.onRemoveClick(position)
            }
        }

        var btnRemoveClickListener: OnRemoveClickListener? = null

        interface OnRemoveClickListener {
            fun onRemoveClick(position: Int): Boolean
        }
    }

    class TransformViewHolder(binding: ItemHomeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        val textView: TextView = binding.textViewItemTransform
        val btnRemove = binding.btnRemove

    }
}