package com.example.modernartmidterm

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatDialogFragment

class DialogBox: AppCompatDialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)
        builder.setTitle("Modern Art")
        builder.setMessage("Inspired by Modern Art masters such as Piet Mondrian and Ben Nicholson")

        return builder.create()
    }
}