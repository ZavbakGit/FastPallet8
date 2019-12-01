package `fun`.gladkikh.app.fastpallet8.ui.common


import `fun`.gladkikh.app.fastpallet8.R
import android.app.Dialog
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment


class EditTextDialog : DialogFragment() {
    companion object {
        private const val TAG = "EditTextDialog"

        private const val EXTRA_TITLE = "title"
        private const val EXTRA_HINT = "hint"
        private const val EXTRA_INPUT_TYPE = "input_type"
        private const val EXTRA_TEXT = "text"

        fun newInstance(
            title: String? = null,
            hint: String? = null,
            text: String? = null,
            inputType: Int
        ): EditTextDialog {

            val dialog = EditTextDialog()
            val args = Bundle().apply {
                putString(EXTRA_TITLE, title)
                putString(EXTRA_HINT, hint)
                putString(EXTRA_TEXT, text)
                putInt(EXTRA_INPUT_TYPE, inputType)
            }
            dialog.arguments = args
            return dialog
        }
    }


    var onOk: (() -> Unit)? = null
    var onCancel: (() -> Unit)? = null

    lateinit var editText: EditText

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val title = arguments?.getString(EXTRA_TITLE)
        val hint = arguments?.getString(EXTRA_HINT)
        val text = arguments?.getString(EXTRA_TEXT)
        val inputType = arguments?.getInt(EXTRA_INPUT_TYPE)

        val view = activity!!.layoutInflater.inflate(R.layout.number_dialog_fragment, null)

        editText = view.findViewById(R.id.editText)
        editText.hint = hint
        editText.inputType = inputType!!

        if (text != null) {
            editText.setText(text)
            editText.setSelection(text.length)
            //editText.append(text)
        }

        val builder = AlertDialog.Builder(context!!)
            .setTitle(title)
            .setView(view)
            .setPositiveButton(android.R.string.ok) { _, _ ->
                onOk?.invoke()
            }
            .setNegativeButton(android.R.string.cancel) { _, _ ->
                onCancel?.invoke()
            }
        val dialog = builder.create()

        //dialog.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)

        return dialog
    }
}