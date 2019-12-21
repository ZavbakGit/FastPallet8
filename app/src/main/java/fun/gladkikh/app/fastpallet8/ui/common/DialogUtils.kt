package `fun`.gladkikh.app.fastpallet8.ui.common

import `fun`.gladkikh.app.fastpallet8.ui.common.Command.ConfirmDialog
import `fun`.gladkikh.app.fastpallet8.ui.common.Command.EditNumberDialog
import android.content.Context
import android.graphics.Color
import android.text.InputType
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentManager

fun startConfirmDialog(
    context: Context,
    startConfirmDialog: ConfirmDialog,
    positiveFun: (startConfirmDialog: ConfirmDialog) -> Unit
) {
    AlertDialog.Builder(context)
        .setTitle("Вы уверены!")
        .setMessage(startConfirmDialog.message)
        .setNegativeButton(
            android.R.string.cancel,
            null
        ) // dismisses by default
        .setPositiveButton(android.R.string.ok) { dialog, which ->
            positiveFun(startConfirmDialog)
        }
        .show()
}

fun startEditDialogNumber(
    supportFragmentManager: FragmentManager,
    startEditNumberDialog: EditNumberDialog,
    positiveFun: (startEditNumberDialog: EditNumberDialog) -> Unit
) {

    val inputType = if (startEditNumberDialog.decimal) {
        (InputType.TYPE_CLASS_NUMBER + InputType.TYPE_NUMBER_FLAG_DECIMAL)
    } else {
        InputType.TYPE_CLASS_NUMBER
    }

    val dialog = EditTextDialog.newInstance(
        text = startEditNumberDialog.data,
        hint = startEditNumberDialog.message,
        title = startEditNumberDialog.message,
        inputType = inputType
    )
    dialog.onOk = {
        val str = dialog.editText.text.toString()
        positiveFun(startEditNumberDialog.copy(data = str))
    }

    dialog.show(supportFragmentManager, "editDescription")
}


fun formatTextSelection(str: String, start: Int?, finish: Int?): SpannableStringBuilder {
    val text = SpannableStringBuilder(str)
    val style = ForegroundColorSpan(Color.rgb(255, 0, 0))

    try {
        text.setSpan(
            style,
            start!! - 1,
            finish!!,
            Spannable.SPAN_INCLUSIVE_INCLUSIVE
        )
    } catch (e: Exception) {
        return SpannableStringBuilder(str)
    }

    return text
}



