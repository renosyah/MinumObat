package com.example.minumobat.ui.layout

import android.content.Context
import android.view.View
import com.example.minumobat.model.time_picker_model.TimeModel

class JamSiang(
    c: Context,
    id: Int,
    v: View,
    image: Int,
    text: String,
    rangeHour: ArrayList<Int>,
    onScroll: (TimeModel) -> Unit,
    onDescriptionClick: (Int) -> Unit
) : LayoutDetailSchedule(c, id, v, image, text, rangeHour, onScroll, onDescriptionClick)