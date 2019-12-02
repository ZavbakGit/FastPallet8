package `fun`.gladkikh.app.fastpallet8.ui.navigate

import `fun`.gladkikh.app.fastpallet8.Constants
import `fun`.gladkikh.app.fastpallet8.R
import `fun`.gladkikh.app.fastpallet8.ui.creatpallet.WrapperGuidCreatePallet

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.navigation.NavController
import com.google.gson.GsonBuilder

class NavigateHandler(val navController: NavController) {

    companion object{
        const val PRODUCT_DIALOG_FORM = 1
        const val PRODUCT_BOX_FORM = 2
    }

    fun startSettings() {
        navigate(R.id.settingsFragment, null)
    }

    fun startCreatePalletDoc(wrapperGuidCreatePaleet: WrapperGuidCreatePallet) {
        val bundle = Bundle()
        val gson = GsonBuilder().create()
        bundle.putString(Constants.EXTRA_WRAP_GUID, gson.toJson(wrapperGuidCreatePaleet))
        navigate(R.id.docCreatePalletFragment, bundle)
    }

    fun startCreatePalletProduct(wrapperGuidCreatePaleet: WrapperGuidCreatePallet) {
        val bundle = Bundle()
        val gson = GsonBuilder().create()
        bundle.putString(Constants.EXTRA_WRAP_GUID, gson.toJson(wrapperGuidCreatePaleet))
        navigate(R.id.productCreatePalletFragment, bundle)
    }

    fun startCreatePalletBox(wrapperGuidCreatePaleet: WrapperGuidCreatePallet) {
        val bundle = Bundle()
        val gson = GsonBuilder().create()
        bundle.putString(Constants.EXTRA_WRAP_GUID, gson.toJson(wrapperGuidCreatePaleet))
        navigate(R.id.boxCreatePalletFragment, bundle)
    }

    fun startCreatePalletPallet(wrapperGuidCreatePaleet: WrapperGuidCreatePallet) {
        val bundle = Bundle()
        val gson = GsonBuilder().create()
        bundle.putString(Constants.EXTRA_WRAP_GUID, gson.toJson(wrapperGuidCreatePaleet))
        navigate(R.id.palletCreatePalletFragment, bundle)
    }

    fun startProductDialogCreatePallet(wrapperGuidCreatePaleet: WrapperGuidCreatePallet) {
        val bundle = Bundle()
        val gson = GsonBuilder().create()
        bundle.putString(Constants.EXTRA_WRAP_GUID, gson.toJson(wrapperGuidCreatePaleet))
        navigate(R.id.productDialogCreatePalletFragment, bundle)
    }


    fun popBackStack() {
        navController.popBackStack()
    }

    private fun navigate(@IdRes resId: Int, args: Bundle?) {
        navController.navigate(resId, args)
    }


}