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

// kelas untuk menghandle
// tampilan dialog untuk
// mengisi deskripsi
// nama dokter dan
// nomor darurat
class DialogEditDescription(var onAdd: (String,String, String) -> Unit) : BottomSheetDialogFragment() {

    // fungsi saat dialog dibuat
    // set style yang digunakan
    // menggunakan style bottom dialog sheet
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)
    }

    // fungsi yang dipanggil saat
    // dialog akan dibuat dan di setup
    override fun setupDialog(dialog: Dialog, style: Int) {

        // inisialisasi view
        // edittext deskripsi
        // nama dokter dan nomor
        val v = requireActivity().layoutInflater.inflate(R.layout.dialog_input_description,null)
        val descriptionText : EditText = v.findViewById(R.id.description_edittext)
        val doctorName : EditText = v.findViewById(R.id.doctor_name_edittext)
        val emergencyNumber : EditText = v.findViewById(R.id.phone_emergency_edittext)

        // inisialisasi tombol
        // cancel saat di klik
        // dismiss dialog
        val cancel : FrameLayout = v.findViewById(R.id.cancel_button)
        cancel.setOnClickListener {
            dialog.dismiss()
        }

        // inisialisasi tombol
        // tambahkan saat diklik
        // kembalikan data serta
        // dismiss dialog
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

        // inisialisasi variabel
        // untuk monitor setiap kali
        // user mengetik
        // apa bila semua valid maka
        // tombol add akan di tampilkan
        val textWatcher = object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {
                add.visibility = if (
                    descriptionText.text.trim().toString().isEmpty() ||
                    doctorName.text.trim().toString().isEmpty() ||
                    emergencyNumber.text.trim().toString().isEmpty()
                ) View.GONE else View.VISIBLE
            }

        }

        // set form yang akan di monitor
        descriptionText.addTextChangedListener(textWatcher)
        doctorName.addTextChangedListener(textWatcher)
        emergencyNumber.addTextChangedListener(textWatcher)

        // tampilkan dialog
        dialog.setCancelable(false)
        dialog.setContentView(v)
    }
}