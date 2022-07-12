package io.github.mitsu1119.neoki_de_english.ui.words

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatEditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import io.github.mitsu1119.neoki_de_english.R
import io.github.mitsu1119.neoki_de_english.databinding.FragmentWordsBinding
import io.github.mitsu1119.neoki_de_english.databinding.ItemTransformBinding
import io.github.mitsu1119.neoki_de_english.databinding.ItemWordsBinding
import io.github.mitsu1119.neoki_de_english.dictionary.DicSet
import org.intellij.lang.annotations.JdkConstants
import org.w3c.dom.Text
import java.io.File
import java.util.*

class WordsFragment: Fragment(), TextToSpeech.OnInitListener {
    private var _binding: FragmentWordsBinding? = null
    private val binding get() = _binding!!

    private lateinit var internalDir: File

    private val args: WordsFragmentArgs by navArgs()

    private var tts: TextToSpeech? = null

    @SuppressLint("UnspecifiedImmutableFlag")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val transformViewModel = ViewModelProvider(this).get(WordsViewModel::class.java)
        _binding = FragmentWordsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        internalDir = requireContext().filesDir

        val dicName = args.dicName
        transformViewModel.setDicName(dicName)
        transformViewModel.loadWords(internalDir, dicName)

        tts = TextToSpeech(requireContext(), this)

        val recyclerView = binding.recyclerView
        val adapter = WordsAdapter()
        recyclerView.adapter = adapter
        transformViewModel.words.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        adapter.itemClickListener =
            object : WordsAdapter.OnItemClickListener {
                override fun onItemClick(holder: WordsViewHolder): Boolean {
                    return true
                }
            }

        adapter.cbRemoveClickListener =
            object: WordsAdapter.OnCheckedChangeListener {
                override fun onCheckedChanged(position: Int): Boolean {
                    transformViewModel.cbChange(position)
                    return true
                }
            }

        // 単語の新規作成
        val btnAdd = binding.btnAdd
        btnAdd.setOnClickListener { view ->
            val dialogLayout = layoutInflater.inflate(R.layout.dialog_word, null)
            val editEnglish = dialogLayout.findViewById<AppCompatEditText>(R.id.textEnglish)
            val editJapanese = dialogLayout.findViewById<AppCompatEditText>(R.id.textJapanese)
            val nameInputDialog = AlertDialog.Builder(context)
            editEnglish.hint = "英語を１文字以上入力してください"
            editJapanese.hint = "日本語訳"
            nameInputDialog.setTitle("単語の新規登録")
            nameInputDialog.setMessage("単語を入力して下さい")
            nameInputDialog.setView(dialogLayout)

            // OKボタン
            nameInputDialog.setPositiveButton("OK") { dialog, _ ->
                speak(editEnglish.text.toString(), internalDir.absolutePath + "/dics/" + dicName + "/" + editEnglish.text.toString() + ".wav")
                DicSet.recording(internalDir, "/dics/" + dicName, editEnglish.text.toString(), editJapanese.text.toString())
                transformViewModel.setWord(Word(editEnglish.text.toString(), false))
                dialog.dismiss()
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
            editEnglish.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
                override fun afterTextChanged(s: Editable?) {
                    // 英語が入力されていなければOKはださない
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

        // 単語削除
        val btnRemove = binding.btnRemove
        btnRemove.setOnClickListener {
            // transformViewModel.removeWords(internalDir, transformViewModel.getChecked())
        }

        return root
    }

    override fun onInit(status: Int) {
        if(status != TextToSpeech.SUCCESS) {
            Log.e("tts", "初期化失敗")
        } else {
            Log.e("tts", "初期化成功")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun speak(text: String, out: String) {
        val of = File(out)
        if(!of.exists()) {
            tts?.setLanguage(Locale.ENGLISH)
            tts?.synthesizeToFile(text, null, of, "WordsID")
        }
    }

    class WordsAdapter :
        ListAdapter<Word, WordsViewHolder>(object : DiffUtil.ItemCallback<Word>() {

            override fun areItemsTheSame(oldItem: Word, newItem: Word): Boolean =
                oldItem == newItem

            override fun areContentsTheSame(oldItem: Word, newItem: Word): Boolean =
                oldItem == newItem
        }) {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordsViewHolder {
            val binding = ItemWordsBinding.inflate(LayoutInflater.from(parent.context))
            return WordsViewHolder(binding)
        }

        override fun onBindViewHolder(holder: WordsViewHolder, position: Int) {
            val word = getItem(position)

            holder.textView.text = word.str
            holder.itemView.setOnClickListener {
                itemClickListener?.onItemClick(holder)
            }

            holder.cbRemove.setOnClickListener {
                cbRemoveClickListener?.onCheckedChanged(position)
            }
        }

        var itemClickListener: OnItemClickListener? = null
        var cbRemoveClickListener: OnCheckedChangeListener? = null

        interface OnItemClickListener {
            fun onItemClick(holder: WordsViewHolder): Boolean
        }

        interface OnCheckedChangeListener {
            fun onCheckedChanged(position: Int): Boolean
        }
    }

    class WordsViewHolder(binding: ItemWordsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        val textView: TextView = binding.textViewItemWords
        val cbRemove: CheckBox = binding.cbRemove
    }
}