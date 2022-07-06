package io.github.mitsu1119.neoki_de_english.ui.home

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent.getBroadcast
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.content.Context.ALARM_SERVICE
import android.content.Intent
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
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import io.github.mitsu1119.neoki_de_english.R
import io.github.mitsu1119.neoki_de_english.alarm.AlarmReceiver
import io.github.mitsu1119.neoki_de_english.databinding.FragmentHomeBinding
import io.github.mitsu1119.neoki_de_english.databinding.ItemTransformBinding
import java.util.*


/**
 * Fragment that demonstrates a responsive layout pattern where the format of the content
 * transforms depending on the size of the screen. Specifically this Fragment shows items in
 * the [RecyclerView] using LinearLayoutManager in a small screen
 * and shows items using GridLayoutManager in a large screen.
 */
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    @SuppressLint("UnspecifiedImmutableFlag")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val transformViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val recyclerView = binding.recyclerviewTransform
        val adapter = TransformAdapter()
        recyclerView.adapter = adapter
        transformViewModel.texts.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        val btnAdd = binding.btnAdd
        btnAdd.setOnClickListener { view ->
            findNavController().navigate(R.id.action_to_create_alarm)


            //　アラーム追加
            /* transformViewModel.addAlarm()

            val timePickerDialog = TimePickerDialog(context,
                { view, hourOfDay, minute ->
                    Toast.makeText(
                        context,
                        "$hourOfDay:$minute",
                        Toast.LENGTH_SHORT
                    ).show()

                    val alarm: AlarmManager = context?.getSystemService(ALARM_SERVICE) as AlarmManager
                    val pending = Intent(context, AlarmReceiver::class.java).let { intent ->
                        intent.addFlags(Intent.FLAG_RECEIVER_FOREGROUND)
                        getBroadcast(context, 0, intent, 0)
                    }

                    // アラームを入力された時刻にセット
                    val cl = Calendar.getInstance()
                    cl.timeInMillis  = System.currentTimeMillis()
                    cl.set(Calendar.HOUR_OF_DAY, hourOfDay)
                    cl.set(Calendar.MINUTE, minute)
                    cl.set(Calendar.SECOND, 0)
                    cl.set(Calendar.MILLISECOND, 0)
                    // alarm.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, cl.timeInMillis, pending)

                    // アラームを5秒後にセット
                    alarm.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + 5000, pending)

                    Toast.makeText(context, "Set Alarm ", Toast.LENGTH_SHORT).show()
                }, 0, 0, true
            )
            timePickerDialog.show()
             */
        }

        return root
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
            val binding = ItemTransformBinding.inflate(LayoutInflater.from(parent.context))
            return TransformViewHolder(binding)
        }

        override fun onBindViewHolder(holder: TransformViewHolder, position: Int) {
            holder.textView.text = getItem(position)
        }
    }

    class TransformViewHolder(binding: ItemTransformBinding) :
        RecyclerView.ViewHolder(binding.root) {

        val textView: TextView = binding.textViewItemTransform
    }
}