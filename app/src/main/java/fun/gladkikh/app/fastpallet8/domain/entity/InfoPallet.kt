package `fun`.gladkikh.app.fastpallet8.domain.entity

data class InfoPallet(
    var code:String?,
    var guid:String?,
    var nameProduct:String?,
    var guidProduct:String?,
    var state:String?,
    var sclad:String?,
    var count:Float?,
    var countBox:Int?,

    var barcode: String? = null,
    var weightStartProduct: String? = null,
    var weightEndProduct: String? = null,
    var weightCoffProduct:String? = null,
    var numberView: Int? = null
)