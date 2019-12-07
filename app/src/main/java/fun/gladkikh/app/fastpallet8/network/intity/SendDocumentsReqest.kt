package `fun`.gladkikh.app.fastpallet8.network.intity


import `fun`.gladkikh.app.fastpallet8.network.intity.old.MetaObjServer
import `fun`.gladkikh.app.fastpallet8.network.util.intity.ReqestObj
import `fun`.gladkikh.app.fastpallet8.network.util.intity.ResponseObj
import com.google.gson.annotations.SerializedName


data class SendDocumentsReqest(
    @SerializedName("codeTSD") val codeTSD: String,
    val list: List<MetaObjServer>
) : ReqestObj

data class SendDocumentsResponse(val listConfirm: List<ItemConfimResponse>) : ResponseObj

data class ItemConfimResponse(
    var guid: String,
    var type: String,
    var status: String
)