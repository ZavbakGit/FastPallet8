package `fun`.gladkikh.app.fastpallet8.ui.navigate

import `fun`.gladkikh.app.fastpallet8.Constants
import `fun`.gladkikh.app.fastpallet8.R
import `fun`.gladkikh.app.fastpallet8.ui.creatpallet.WrapperGuidCreatePaleet

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.navigation.NavController
import com.google.gson.GsonBuilder

class NavigateHandler(val navController: NavController) {

    fun startSettings() {
        navigate(R.id.settingsFragment, null)
    }


    fun startCreatePalletBox(wrapperGuidCreatePaleet: WrapperGuidCreatePaleet)
    {
        val bundle = Bundle()
        val gson = GsonBuilder().create()
        bundle.putString(Constants.EXTRA_GUID, gson.toJson(wrapperGuidCreatePaleet))
        navigate(R.id.boxCreatePalletFragment, bundle)
    }

    fun popBackStack() {
        navController.popBackStack()
    }

    private fun navigate(@IdRes resId: Int, args: Bundle?) {
        navController.navigate(resId, args)
    }


}