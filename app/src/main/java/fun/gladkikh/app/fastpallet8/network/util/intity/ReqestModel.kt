package `fun`.gladkikh.app.fastpallet8.network.util.intity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ReqestModel(
    @SerializedName("command")
    @Expose
    var command: String,
    @SerializedName("str_data_in")
    @Expose
    var strDataIn: String
)
