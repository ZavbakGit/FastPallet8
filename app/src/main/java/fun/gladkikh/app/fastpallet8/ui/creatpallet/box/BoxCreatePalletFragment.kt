package `fun`.gladkikh.app.fastpallet8.ui.creatpallet.box

import `fun`.gladkikh.app.fastpallet8.Constants
import `fun`.gladkikh.app.fastpallet8.R
import `fun`.gladkikh.app.fastpallet8.common.toSimpleDateTime
import `fun`.gladkikh.app.fastpallet8.common.toSimpleFormat
import `fun`.gladkikh.app.fastpallet8.domain.model.entity.creatpallet.BoxCreatePallet
import `fun`.gladkikh.app.fastpallet8.domain.model.entity.creatpallet.PalletCreatePallet
import `fun`.gladkikh.app.fastpallet8.domain.model.entity.creatpallet.ProductCreatePallet
import `fun`.gladkikh.app.fastpallet8.ui.base.BaseFragment
import `fun`.gladkikh.app.fastpallet8.ui.common.Command
import `fun`.gladkikh.app.fastpallet8.ui.creatpallet.WrapperGuidCreatePaleet
import androidx.lifecycle.Observer
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.block_box.*
import kotlinx.android.synthetic.main.block_pallet.*
import kotlinx.android.synthetic.main.block_product.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class BoxCreatePalletFragment : BaseFragment() {

    override val layoutRes = R.layout.create_pallet_fragment_box
    override val viewModel: BoxCreatePalletViewModel by viewModel()

    override fun initSubscription() {
        super.initSubscription()

        if (viewModel.wrapperGuid != null) {
            viewModel.wrapperGuid = viewModel.wrapperGuid
        } else {
            val gson = GsonBuilder().create()
            val strJson = arguments!!.get(Constants.EXTRA_GUID) as String
            viewModel.wrapperGuid =
                gson.fromJson(strJson, WrapperGuidCreatePaleet::class.java) as WrapperGuidCreatePaleet
        }



        viewModel.getProductLiveData().observe(viewLifecycleOwner, Observer {
            renderProduct(it)
        })

        viewModel.getPalletLiveData().observe(viewLifecycleOwner, Observer {
            renderPallet(it)
        })

        viewModel.getBoxLiveData().observe(viewLifecycleOwner, Observer {
            renderBox(it)
        })

        mainActivity.barcodeLiveData.observe(viewLifecycleOwner, Observer {
            viewModel.readBarcode(it)
        })



        tvCountBox.setOnClickListener {
            viewModel.readBarcode("${(10..99).random()}123456789")
        }
    }

    override fun keyDownListener(keyCode: Int) {
        super.keyDownListener(keyCode)
        //viewModel.keyDown(keyCode)
    }

    override fun commandListener(command: Command) {
        super.commandListener(command)
        when (command) {
            is Command.Close -> {
                navigateHandler.popBackStack()
            }

        }
    }

    fun renderProduct(product: ProductCreatePallet?) {
        tvNameProduct.text = product?.nameProduct ?: ""
        tvCountProduct.text = product?.count.toSimpleFormat()
        tvCountPlaceProduct.text = product?.countBox.toSimpleFormat()
        tvCountPalletProduct.text = product?.countPallet.toSimpleFormat()

        tvCountBackProduct.text = product?.countBack.toSimpleFormat()
        tvCountPlaceBackProduct.text = product?.countBoxBack.toSimpleFormat()
    }

    fun renderPallet(pallet: PalletCreatePallet?) {
        tvNumberPallet.text = pallet?.number ?: ""
        tvCountPallet.text = pallet?.count.toSimpleFormat()
        tvCountPlacePallet.text = pallet?.countBox.toSimpleFormat()
        tvCountRowPallet.text = pallet?.countRow.toSimpleFormat()
    }

    fun renderBox(box: BoxCreatePallet?) {

        tvDateBox.text = box?.dateChanged.toSimpleDateTime()
        tvCountBox.text = box?.count.toSimpleFormat()
        tvCountPlaceBox.text = box?.countBox.toSimpleFormat()
    }
}