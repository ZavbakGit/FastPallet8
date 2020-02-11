package `fun`.gladkikh.app.fastpallet8.ui.screen.inventorypallet.productdialog

import `fun`.gladkikh.app.fastpallet8.Constants
import `fun`.gladkikh.app.fastpallet8.R
import `fun`.gladkikh.app.fastpallet8.common.getWeightByBarcode
import `fun`.gladkikh.app.fastpallet8.common.toSimpleFormat
import `fun`.gladkikh.app.fastpallet8.domain.entity.inventorypallet.InventoryPallet
import `fun`.gladkikh.app.fastpallet8.ui.base.BaseFragment
import `fun`.gladkikh.app.fastpallet8.ui.common.Command
import `fun`.gladkikh.app.fastpallet8.ui.common.formatTextSelection
import `fun`.gladkikh.app.fastpallet8.ui.screen.inventorypallet.WrapperGuidInventoryPallet


import androidx.lifecycle.Observer
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.product_template_weight_fragment.*

import org.koin.androidx.viewmodel.ext.android.viewModel

class ProductDialogInventoryPalletFragment : BaseFragment() {

    override val layoutRes = R.layout.product_template_weight_fragment
    override val viewModel: ProductDialogInventoryPalletViewModel by viewModel()

    override fun initSubscription() {
        super.initSubscription()

        if (viewModel.wrapperGuid != null) {
            viewModel.wrapperGuid = viewModel.wrapperGuid
        } else {
            val gson = GsonBuilder().create()
            val strJson = arguments!!.get(Constants.EXTRA_WRAP_GUID) as String
            viewModel.wrapperGuid =
                gson.fromJson(
                    strJson,
                    WrapperGuidInventoryPallet::class.java
                ) as WrapperGuidInventoryPallet
        }

        viewModel.getDocLiveData().observe(viewLifecycleOwner, Observer {
            renderDoc(it)
        })



        if (Constants.IS_TEST_BUILD) {
            tvBarcodeProductDialog.setOnClickListener {
                viewModel.readBarcode("${(10..99).random()}123456789")
            }
        }


        mainActivity.barcodeLiveData.observe(viewLifecycleOwner, Observer {
            viewModel.readBarcode(it)
        })

    }

    override fun commandListener(command: Command) {
        super.commandListener(command)
        when (command) {
            is Command.Close -> {
                navigateHandler.popBackStack()
            }
        }
    }

    private fun renderDoc(doc: InventoryPallet?) {
        tvNameProductDialog.text = doc?.nameProduct ?: ""

        val barcode = doc?.weightBarcode ?: ""
        val start = doc?.weightStartProduct ?: 0
        val finish = doc?.weightEndProduct ?: 0
        val coff = doc?.weightCoffProduct ?: 0f



        tvStartProductDialog.text = start.toSimpleFormat()
        tvEndProductDialog.text = finish.toSimpleFormat()
        tvCoffProductDialog.text = coff.toSimpleFormat()

        val weight = getWeightByBarcode(
            barcode = barcode,
            start = start,
            finish = finish,
            coff = coff
        )

        tvBarcodeProductDialog.text = formatTextSelection(barcode, start, finish)

        tvWeightProductDialog.text = weight.toSimpleFormat()
    }
}