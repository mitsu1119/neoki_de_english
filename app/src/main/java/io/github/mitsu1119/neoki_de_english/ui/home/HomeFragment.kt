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
import io.github.mitsu1119.neoki_de_english.databinding.ItemAlarmBinding
import io.github.mitsu1119.neoki_de_english.ui.create_alarm.CreateAlarmFragment
import io.github.mitsu1119.neoki_de_english.ui.words.WordsFragment
import org.w3c.dom.Text
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

        updateAdapter()

        val btnAdd = binding.btnAdd
        btnAdd.setOnClickListener { view ->
            val dialog = CreateAlarmFragment()
            dialog.show(childFragmentManager, "CreateAlarm")
        }

        return root
    }

    override fun onDialogPositiveClick(dialog: DialogFragment, hour:Int, minute:Int, dayOfWeeks: Array<Boolean>, dic: String) {
        Log.v("yey", "$hour:$minute")
        Log.v("yey", dic)

        // ??????????????????

        val alarm: AlarmManager = context?.getSystemService(ALARM_SERVICE) as AlarmManager
        val pending = Intent(context, AlarmReceiver::class.java).let { intent ->
            intent.addFlags(Intent.FLAG_RECEIVER_FOREGROUND)
            intent.putExtra("dictionary", dic)
            getBroadcast(context, 0, intent, 0)
        }

        // ????????????????????????????????????????????????
        for(i in (0..(dayOfWeeks.size - 1))) {
            if(dayOfWeeks[i] == true) {
                val cl: Calendar = Calendar.getInstance().apply {
                    set(Calendar.DAY_OF_WEEK, (i + 1) % 7)
                    set(Calendar.HOUR_OF_DAY, hour)
                    set(Calendar.MINUTE, minute)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                }
                // ?????????: ?????????????????????????????????
                alarm.setExact(AlarmManager.RTC_WAKEUP, cl.timeInMillis, pending)
                // ??????????????????????????????
                // alarm.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, cl.timeInMillis, pending)
            }
        }
        // ???????????????5??????????????????
        // alarm.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + 1000, pending)

        var dow = ""
        dayOfWeeks.forEach { if(it) dow += "o" else dow += "x" }
        Log.v("m2_setalarm", "dow: $dow")
        val al = AlarmSet.create(internalDir, hour, minute, dow, dic)
        transformViewModel.addAlarm(al)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun updateAdapter() {
        transformViewModel.loadAlarms(internalDir)

        val recyclerView = binding.recyclerviewTransform
        val adapter = TransformAdapter()
        recyclerView.adapter = adapter

        transformViewModel.texts.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }


        adapter.btnRemoveClickListener =
            object : TransformAdapter.OnRemoveClickListener {
                override fun onRemoveClick(position: Int): Boolean {
                    Log.v("m2_alarm", "Remove: $position")
                    transformViewModel.removeAlarm(position, internalDir)
                    updateAdapter()
                    return true
                }
            }
    }

    class TransformAdapter :
        ListAdapter<String, TransformViewHolder>(object : DiffUtil.ItemCallback<String>() {

            override fun areItemsTheSame(oldItem: String, newItem: String): Boolean =
                oldItem == newItem

            override fun areContentsTheSame(oldItem: String, newItem: String): Boolean =
                oldItem == newItem
        }) {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransformViewHolder {
            val binding = ItemAlarmBinding.inflate(LayoutInflater.from(parent.context))
            return TransformViewHolder(binding)
        }

        override fun onBindViewHolder(holder: TransformViewHolder, position: Int) {
            val li = getItem(position).split(",")

            val dow = li[2]
            var str = ""
            if(dow.get(0) == 'o') str += "???"
            else str += "???"
            if(dow.get(1) == 'o') str += "???"
            else str += "???"
            if(dow.get(2) == 'o') str += "???"
            else str += "???"
            if(dow.get(3) == 'o') str += "???"
            else str += "???"
            if(dow.get(4) == 'o') str += "???"
            else str += "???"
            if(dow.get(5) == 'o') str += "???"
            else str += "???"
            if(dow.get(6) == 'o') str += "???"
            else str += "???"

            holder.textView.text = li[0]
            holder.txDic.text = li[1] + "???$str"

            holder.btnRemove.setOnClickListener {
                btnRemoveClickListener?.onRemoveClick(position)
            }
        }

        var btnRemoveClickListener: OnRemoveClickListener? = null

        interface OnRemoveClickListener {
            fun onRemoveClick(position: Int): Boolean
        }
    }

    class TransformViewHolder(binding: ItemAlarmBinding) :
        RecyclerView.ViewHolder(binding.root) {

        val textView: TextView = binding.textViewItemTransform
        val txDic: TextView = binding.textDictionary
        val btnRemove = binding.btnRemove
    }
}