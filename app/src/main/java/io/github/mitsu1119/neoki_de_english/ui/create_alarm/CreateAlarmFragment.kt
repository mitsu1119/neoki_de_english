package io.github.mitsu1119.neoki_de_english.ui.create_alarm

import android.app.AlarmManager
import android.app.AlertDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.core.content.getSystemService
import androidx.fragment.app.DialogFragment
import io.github.mitsu1119.neoki_de_english.R
import io.github.mitsu1119.neoki_de_english.dictionary.DicSet
import java.io.File
import kotlin.ClassCastException
import kotlin.math.sign

class CreateAlarmFragment: DialogFragment() {
    interface NoticeDialogLister {
        fun onDialogPositiveClick(dialog:DialogFragment, hour:Int, minute:Int, dayOfWeeks: Array<Boolean>)
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

        // 曜日
        val cbs = arrayOf(signinView.findViewById<CheckBox>(R.id.cbMon),
            signinView.findViewById<CheckBox>(R.id.cbTue),
            signinView.findViewById<CheckBox>(R.id.cbWed),
            signinView.findViewById<CheckBox>(R.id.cbThu),
            signinView.findViewById<CheckBox>(R.id.cbFri),
            signinView.findViewById<CheckBox>(R.id.cbSat),
            signinView.findViewById<CheckBox>(R.id.cbSun),
        )

        // 辞書
        val internalDir = requireContext().filesDir
        val dicDir = File(internalDir.absolutePath + "/dics")
        val dicNames = mutableListOf<String>()
        for(name in DicSet.getDicNames(dicDir)) {
            if(!dicNames.contains(name)) dicNames.add(name)
        }
        val spnDic = signinView.findViewById<Spinner>(R.id.spnDic)
        val spinnerItems = dicNames.stream().toArray() { arrayOfNulls<String>(it) }
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, spinnerItems)
        spnDic.adapter = adapter


        builder.setView(signinView)
            .setTitle("アラーム作成")
            .setPositiveButton("done") { dialog, id ->
                val tp = signinView.findViewById<TimePicker>(R.id.tp)
                val dayOfWeeks = arrayOf(
                    cbs[0].isChecked,
                    cbs[1].isChecked,
                    cbs[2].isChecked,
                    cbs[3].isChecked,
                    cbs[4].isChecked,
                    cbs[5].isChecked,
                    cbs[6].isChecked,
                )
                mLister?.onDialogPositiveClick(this, tp.hour, tp.minute, dayOfWeeks)
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