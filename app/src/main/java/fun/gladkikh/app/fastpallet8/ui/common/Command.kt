package `fun`.gladkikh.app.fastpallet8.ui.common

sealed class Command {
    object Close : Command()
    data class OpenForm(val formCode:Int? = null,val data: Any? = null) : Command()
    data class ConfirmDialog(
        val message: String,
        val requestCode: Int,
        val data: Any? = null
    ) : Command()
    data class EditNumberDialog(
        val message: String,
        val requestCode: Int,
        val decimal: Boolean = false,
        val data: String? = null
    ) : Command()
}


