package com.example.minumobat.ui.dialog

import android.app.Dialog
import android.os.Bundle
import android.widget.EditText
import android.widget.FrameLayout
import com.example.minumobat.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class DialogEditDescription(var onAdd: (String, String) -> Unit) : BottomSheetDialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)
    }

    override fun setupDialog(dialog: Dialog, style: Int) {
        val v = requireActivity().layoutInflater.inflate(R.layout.dialog_input_description,null)
        val descriptionText : EditText = v.findViewById(R.id.description_edittext)
        val emergencyNumber : EditText = v.findViewById(R.id.phone_emergency_edittext)

        val cancel : FrameLayout = v.findViewById(R.id.cancel_button)
        cancel.setOnClickListener {
            dialog.dismiss()
        }

        val add : FrameLayout = v.findViewById(R.id.add_button)
        add.setOnClickListener {
            onAdd.invoke(descriptionText.text.toString(), emergencyNumber.text.toString())
            dialog.dismiss()
        }

        dialog.setCancelable(false)
        dialog.setContentView(v)
    }
}