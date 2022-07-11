package io.github.mitsu1119.neoki_de_english.ui.local_dictionary

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatEditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import io.github.mitsu1119.neoki_de_english.R
import io.github.mitsu1119.neoki_de_english.databinding.FragmentLocalDictionaryBinding
import io.github.mitsu1119.neoki_de_english.databinding.FragmentTitleBinding
import io.github.mitsu1119.neoki_de_english.databinding.ItemTransformBinding
import io.github.mitsu1119.neoki_de_english.dictionary.DicSet
import io.github.mitsu1119.neoki_de_english.ui.home.HomeFragment
import io.github.mitsu1119.neoki_de_english.ui.title.TitleViewModel
import java.io.File
import kotlin.contracts.contract

class LocalDictionaryFragment: Fragment() {
    private var _binding: FragmentLocalDictionaryBinding? = null

    private val binding get() = _binding!!

    private lateinit var internalDir: File

    @SuppressLint("UnspecifiedImmutableFlag")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val transformViewModel = ViewModelProvider(this).get(LocalDictionaryViewModel::class.java)
        _binding = FragmentLocalDictionaryBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val recyclerView = binding.recyclerView
        val adapter = LocalDicAdapter()
        recyclerView.adapter = adapter
        transformViewModel.dicNames.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        adapter.itemClickListener = object: LocalDicAdapter.OnItemClickListener {
            override fun onItemClick(holder: LocalDicViewHolder): Boolean {
                val action = LocalDictionaryFragmentDirections.actionToWords(holder.textView.text.toString())
                findNavController().navigate(action)
                return true
            }
        }

        internalDir = requireContext().filesDir
        transformViewModel.loadDicNames(internalDir)

        // 辞書の新規作成
        val btnAdd = binding.btnAdd
        btnAdd.setOnClickListener {
            val dialogLayout = layoutInflater.inflate(R.layout.dialog_main, null)
            val editText = dialogLayout.findViewById<AppCompatEditText>(R.id.textDialog)
            val nameInputDialog = AlertDialog.Builder(context)
            editText.hint = "１文字以上入力してください"
            nameInputDialog.setTitle("辞書の新規作成")
            nameInputDialog.setMessage("辞書名を入力してください")
            nameInputDialog.setView(dialogLayout)

            // OKボタン
            nameInputDialog.setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
                DicSet.create(internalDir, editText.text.toString())
                transformViewModel.addDicName(editText.text.toString())
            }

            // キャンセルボタンでキャンセル
            nameInputDialog.setNegativeButton("キャンセル") { dialog, _ ->
                dialog.dismiss()
            }

            val dialog = nameInputDialog.create()
            dialog.show()

            // 名前入力欄のボタンの設定
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled = false
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.GRAY)
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK)
            editText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
                override fun afterTextChanged(s: Editable?) {
                    // 入力されていなければOKはださない
                    if (s.isNullOrEmpty() || s.length == 0) {
                        dialog.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled = false
                        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.GRAY)
                    } else {
                        dialog.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled = true
                        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK)
                    }
                }
            })
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    class LocalDicAdapter :
        ListAdapter<String, LocalDicViewHolder>(object : DiffUtil.ItemCallback<String>() {

            override fun areItemsTheSame(oldItem: String, newItem: String): Boolean =
                oldItem == newItem

            override fun areContentsTheSame(oldItem: String, newItem: String): Boolean =
                oldItem == newItem
        }) {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocalDicViewHolder {
            val binding = ItemTransformBinding.inflate(LayoutInflater.from(parent.context))
            return LocalDicViewHolder(binding)
        }

        override fun onBindViewHolder(holder: LocalDicViewHolder, position: Int) {
            val name = getItem(position)
            holder.textView.text = name
            holder.itemView.setOnClickListener {
                itemClickListener?.onItemClick(holder)
            }
        }

        var itemClickListener: OnItemClickListener? = null
        interface OnItemClickListener {
            fun onItemClick(holder: LocalDicViewHolder): Boolean
        }
    }

    class LocalDicViewHolder(binding: ItemTransformBinding) :
        RecyclerView.ViewHolder(binding.root) {

        val textView: TextView = binding.textViewItemTransform
    }
}