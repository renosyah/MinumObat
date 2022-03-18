package com.example.minumobat.ui.dialog

import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.FrameLayout
import com.example.minumobat.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class DialogEditDescription(var onAdd: (String,String, String) -> Unit) : BottomSheetDialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)
    }

    override fun setupDialog(dialog: Dialog, style: Int) {
        val v = requireActivity().layoutInflater.inflate(R.layout.dialog_input_description,null)
        val descriptionText : EditText = v.findViewById(R.id.description_edittext)
        val doctorName : EditText = v.findViewById(R.id.doctor_name_edittext)
        val emergencyNumber : EditText = v.findViewById(R.id.phone_emergency_edittext)

        val cancel : FrameLayout = v.findViewById(R.id.cancel_button)
        cancel.setOnClickListener {
            dialog.dismiss()
        }

        val add : FrameLayout = v.findViewById(R.id.add_button)
        add.visibility = View.GONE
        add.setOnClickListener {
            onAdd.invoke(
                descriptionText.text.toString(),
                doctorName.text.toString(),
                emergencyNumber.text.toString()
            )
            dialog.dismiss()
        }

        val textWatcher = object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                add.visibility = if (
                    descriptionText.text.trim().toString().isEmpty() ||
                    doctorName.text.trim().toString().isEmpty() ||
                    emergencyNumber.text.trim().toString().isEmpty()
                ) View.GONE else View.VISIBLE
            }

        }

        descriptionText.addTextChangedListener(textWatcher)
        doctorName.addTextChangedListener(textWatcher)
        emergencyNumber.addTextChangedListener(textWatcher)

        dialog.setCancelable(false)
        dialog.setContentView(v)
    }
}