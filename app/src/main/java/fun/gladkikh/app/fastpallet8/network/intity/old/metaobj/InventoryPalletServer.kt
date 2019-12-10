package `fun`.gladkikh.app.fastpallet8.network.intity.old.metaobj

import `fun`.gladkikh.app.fastpallet8.domain.model.Status
import `fun`.gladkikh.app.fastpallet8.domain.model.Type
import `fun`.gladkikh.app.fastpallet8.network.intity.old.MetaObjServer
import java.util.*


class InventoryPalletServer(

    val guid: String?,
    val guidServer: String?,
    val type: Type?,

    val typeFromServer: String?,

    val status: Status?,
    val number: String?,


    val date: Date?,


    val dataChanged: Date?,

    val isWasLoadedLastTime: Boolean?,
    val description: String?,
    val barcode: String?,

    val numberPallet: String?,

    val barcodePallet: String?,

    val stringProduct: ProductServer

) : MetaObjServer()


