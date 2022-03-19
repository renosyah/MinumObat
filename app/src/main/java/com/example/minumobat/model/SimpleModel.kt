package com.example.minumobat.model

// bentuk sederhana untuk menampilkan label dan nomor
// implementasi di pemilihan waktu/jam dan menit
class SimpleModel(var label : String = "", var value : Int = 0, var flag : Int = FLAG_NONE){
    companion object{
        val FLAG_NONE = 0
        val FLAG_SELECTED = 1
        val FLAG_UNSELECTED = 2
        val FLAG_UNREACH = 3

    }
}