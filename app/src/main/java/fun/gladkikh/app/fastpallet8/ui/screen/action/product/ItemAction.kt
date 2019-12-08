package `fun`.gladkikh.app.fastpallet8.ui.screen.action.product

import `fun`.gladkikh.app.fastpallet8.domain.model.entity.action.BoxAction
import `fun`.gladkikh.app.fastpallet8.domain.model.entity.action.PalletAction

data class ItemAction(
    val type: TYPE_ITEM_ACTION
    , val guid: String
    , var numberView: Int?
    , val description: String?
    , val count: Float?
    , val countBox: Int?
    , val box: BoxAction? = null
    , val pallet: PalletAction? = null
)

enum class TYPE_ITEM_ACTION {
    BOX, PALLET
}