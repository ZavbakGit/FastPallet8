package `fun`.gladkikh.app.fastpallet8.common


import java.text.DecimalFormatSymbols
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

fun Date?.toSimpleDateTime(): String {
    return if (this == null) {
        ""
    } else {
        val format = SimpleDateFormat("dd.MM.yyyy hh:mm:ss")
        format.format(this)
    }
}


fun Float?.toSimpleFormat(): String {
    return if (this == null) {
        val df = NumberFormat.getInstance()

        //val df = DecimalFormat("### ###.###")
        //val df = DecimalFormatSymbols(Locale.getDefault())

        df.format(0)

    } else {

        val df = NumberFormat.getInstance()

        //val df = DecimalFormat("### ###.###")
        //val df = DecimalFormatSymbols(Locale.getDefault())

        df.format(this)
        //df.format(this).replace(",", " ")
    }
}

fun Int?.toSimpleFormat(): String {
    return if (this == null) {
        "0"
    } else {
        //String.format("%,d", this)
        val df = NumberFormat.getInstance()
        df.format(this)
    }
}


fun Date.toSimpleDate(): String {
    return if (this == null) {
        ""
    } else {
        val format = SimpleDateFormat("dd.MM.yyyy")
        format.format(this)
    }
}

fun String.getDecimalStr(): String {
    return "[^\\d,.]".toRegex().replace(this, "").replace(",", ".")
}

fun String.getIntByParseStr(): Int {
    return this.getDecimalStr().toIntOrNull() ?: 0
}

fun String.getFloatByParseStr(): Float {
    return this.getDecimalStr().toFloatOrNull() ?: 0f
}