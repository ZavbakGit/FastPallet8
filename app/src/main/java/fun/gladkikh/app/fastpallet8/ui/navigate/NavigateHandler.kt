package `fun`.gladkikh.app.fastpallet8.ui.navigate

import `fun`.gladkikh.app.fastpallet8.Constants
import `fun`.gladkikh.app.fastpallet8.R
import `fun`.gladkikh.app.fastpallet8.ui.screen.action.WrapperGuidAction
import `fun`.gladkikh.app.fastpallet8.ui.screen.creatpallet.WrapperGuidCreatePallet
import `fun`.gladkikh.app.fastpallet8.ui.screen.inventorypallet.WrapperGuidInventoryPallet

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.navigation.NavController
import com.google.gson.GsonBuilder

class NavigateHandler(val navController: NavController) {

    fun startSettings() {
        navigate(R.id.settingsFragment, null)
    }

    fun startListDocument() {
        val bundle = Bundle()
        navigate(R.id.documentListFragment, bundle)
    }

    fun startInventoryPalletProductDialog(wrapper: WrapperGuidInventoryPallet) {
        val bundle = Bundle()
        val gson = GsonBuilder().create()
        bundle.putString(Constants.EXTRA_WRAP_GUID, gson.toJson(wrapper))
        navigate(R.id.productDialogInventoryPalletFragment, bundle)
    }


    fun startInventoryPalletBox(wrapper: WrapperGuidInventoryPallet) {
        val bundle = Bundle()
        val gson = GsonBuilder().create()
        bundle.putString(Constants.EXTRA_WRAP_GUID, gson.toJson(wrapper))
        navigate(R.id.boxInventoryPalletFragment, bundle)
    }

    fun startInventoryPallet(wrapper: WrapperGuidInventoryPallet) {
        val bundle = Bundle()
        val gson = GsonBuilder().create()
        bundle.putString(Constants.EXTRA_WRAP_GUID, gson.toJson(wrapper))
        navigate(R.id.docInventoryPalletFragment, bundle)
    }


    fun startActionBox(wrapper: WrapperGuidAction) {
        val bundle = Bundle()
        val gson = GsonBuilder().create()
        bundle.putString(Constants.EXTRA_WRAP_GUID, gson.toJson(wrapper))
        navigate(R.id.boxActionFragment, bundle)
    }

    fun startActionProductDialog(wrapper: WrapperGuidAction) {
        val bundle = Bundle()
        val gson = GsonBuilder().create()
        bundle.putString(Constants.EXTRA_WRAP_GUID, gson.toJson(wrapper))
        navigate(R.id.productDialogActionFragment, bundle)
    }


    fun startActionProduct(wrapper: WrapperGuidAction) {
        val bundle = Bundle()
        val gson = GsonBuilder().create()
        bundle.putString(Constants.EXTRA_WRAP_GUID, gson.toJson(wrapper))
        navigate(R.id.productActionFragment, bundle)
    }


    fun startActionDoc(wrapper: WrapperGuidAction) {
        val bundle = Bundle()
        val gson = GsonBuilder().create()
        bundle.putString(Constants.EXTRA_WRAP_GUID, gson.toJson(wrapper))
        navigate(R.id.docActionFragment, bundle)
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