package io.github.mitsu1119.neoki_de_english.ui.words

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.AppCompatEditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import io.github.mitsu1119.neoki_de_english.R
import io.github.mitsu1119.neoki_de_english.databinding.FragmentLocalDictionaryBinding
import io.github.mitsu1119.neoki_de_english.databinding.FragmentWordsBinding
import io.github.mitsu1119.neoki_de_english.dictionary.DicSet
import io.github.mitsu1119.neoki_de_english.ui.local_dictionary.LocalDictionaryViewModel
import org.intellij.lang.annotations.JdkConstants
import java.io.File

class WordsFragment: Fragment() {
    private var _binding: FragmentWordsBinding? = null
    private val binding get() = _binding!!

    private lateinit var internalDir: File

    private val args: WordsFragmentArgs by navArgs()

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
                DicSet.recording(internalDir, dicName, editEnglish.text.toString(), editJapanese.text.toString())
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
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}