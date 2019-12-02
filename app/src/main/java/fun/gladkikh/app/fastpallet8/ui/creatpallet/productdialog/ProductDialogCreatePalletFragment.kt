package `fun`.gladkikh.app.fastpallet8.ui.creatpallet.productdialog

import `fun`.gladkikh.app.fastpallet8.Constants
import `fun`.gladkikh.app.fastpallet8.R
import `fun`.gladkikh.app.fastpallet8.common.getWeightByBarcode
import `fun`.gladkikh.app.fastpallet8.common.toSimpleFormat
import `fun`.gladkikh.app.fastpallet8.domain.model.entity.creatpallet.ProductCreatePallet
import `fun`.gladkikh.app.fastpallet8.ui.base.BaseFragment
import `fun`.gladkikh.app.fastpallet8.ui.common.Command
import `fun`.gladkikh.app.fastpallet8.ui.creatpallet.WrapperGuidCreatePallet

import androidx.lifecycle.Observer
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.product_template_weight_fragment.*

import org.koin.androidx.viewmodel.ext.android.viewModel

class ProductDialogCreatePalletFragment : BaseFragment() {

    override val layoutRes = R.layout.product_template_weight_fragment
    override val viewModel: ProductDialogCreatePalletViewModel by viewModel()

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
                    WrapperGuidCreatePallet::class.java
                ) as WrapperGuidCreatePallet
        }

        viewModel.getProductLiveData().observe(viewLifecycleOwner, Observer {
            renderProduct(it)
        })


        tvBarcodeProductDialog.setOnClickListener {
            viewModel.readBarcode("${(10..99).random()}123456789")
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

    private fun renderProduct(product: ProductCreatePallet?) {
        tvNameProductDialog.text = product?.nameProduct ?: ""

        val barcode = product?.barcode ?: ""
        val start = product?.weightStartProduct ?: 0
        val finish = product?.weightEndProduct ?: 0
        val coff = product?.weightCoffProduct ?: 0f


        tvBarcodeProductDialog.text = barcode
        tvStartProductDialog.text = start.toSimpleFormat()
        tvEndProductDialog.text = finish.toSimpleFormat()
        tvCoffProductDialog.text = coff.toSimpleFormat()

        val weight = getWeightByBarcode(
            barcode = barcode,
            start = start,
            finish = finish,
            coff = coff
        )

        tvWeightProductDialog.text = weight.toSimpleFormat()
    }
}