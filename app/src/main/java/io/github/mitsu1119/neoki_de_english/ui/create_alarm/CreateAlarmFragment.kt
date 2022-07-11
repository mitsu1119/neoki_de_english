package io.github.mitsu1119.neoki_de_english.ui.create_alarm

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import io.github.mitsu1119.neoki_de_english.R
import kotlin.ClassCastException

class CreateAlarmFragment: DialogFragment() {
    interface NoticeDialogLister {
        fun onDialogPositiveClick(dialog:DialogFragment, st1:String, st2:String)
    }

    var mLister:NoticeDialogLister? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            val fragment = parentFragment
            mLister = fragment as NoticeDialogLister
        } catch(e: ClassCastException) {
            throw ClassCastException("${context.toString()} must implement NoticeDialogListener")
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)
        val inflater = requireActivity().layoutInflater
        val signinView = inflater.inflate(R.layout.fragment_create_alarm, null)

        builder.setView(signinView)
            .setTitle("アラーム作成")
            .setPositiveButton("done") { dialog, id ->
                val st1 = signinView.findViewById<EditText>(R.id.st1).text
                val st2 = signinView.findViewById<EditText>(R.id.st2).text
                mLister?.onDialogPositiveClick(this, st1.toString(), st2.toString())
                dismiss()
            }
            .setNegativeButton("cancel") { dialog, id ->
                dismiss()
            }

        return builder.create()
    }

    override fun onDetach() {
        super.onDetach()
        mLister = null
    }
}