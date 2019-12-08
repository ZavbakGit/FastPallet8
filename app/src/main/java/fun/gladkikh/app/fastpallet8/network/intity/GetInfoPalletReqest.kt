package `fun`.gladkikh.app.fastpallet8.network.intity


import `fun`.gladkikh.app.fastpallet8.network.util.intity.ReqestObj
import `fun`.gladkikh.app.fastpallet8.network.util.intity.ResponseObj
import okhttp3.Response

class GetInfoPalletReqest(
    var codeTSD: String,
    var list: List<String>
): ReqestObj

class GetInfoPalletResponse(var list: List<Item>): ResponseObj

class Item(
    var PaletCode:String? = null,
    var PaletGuid:String? = null,
    var PaletTovar:String? = null,
    var PaletState:String? = null,
    var PaletStore:String? = null,
    var PaletWeight:String? = null,
    var PaletPlacesCount:String? = null,
    var nameProduct:String? = null,

    var barcode: String? = null,
    var weightStartProduct: String? = null,
    var weightEndProduct: String? = null,
    var weightCoffProduct:String? = null
)
