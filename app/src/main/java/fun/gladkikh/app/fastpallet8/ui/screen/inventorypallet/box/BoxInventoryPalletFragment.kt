package `fun`.gladkikh.app.fastpallet8.ui.screen.inventorypallet.box

import `fun`.gladkikh.app.fastpallet8.Constants
import `fun`.gladkikh.app.fastpallet8.R
import `fun`.gladkikh.app.fastpallet8.common.toSimpleDateTime
import `fun`.gladkikh.app.fastpallet8.common.toSimpleFormat
import `fun`.gladkikh.app.fastpallet8.domain.entity.inventorypallet.BoxInventoryPallet
import `fun`.gladkikh.app.fastpallet8.domain.entity.inventorypallet.InventoryPallet

import `fun`.gladkikh.app.fastpallet8.ui.base.BaseFragment
import `fun`.gladkikh.app.fastpallet8.ui.common.Command
import `fun`.gladkikh.app.fastpallet8.ui.screen.inventorypallet.doc.WrapperGuidInventoryPallet

import androidx.lifecycle.Observer
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.block_box.*
import kotlinx.android.synthetic.main.block_pallet.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class BoxInventoryPalletFragment : BaseFragment() {

    override val layoutRes = R.layout.inventory_pallet_fragment_box
    override val viewModel: BoxInventoryPalletViewModel by viewModel()

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

    override fun commandListener(command: Command) {
        super.commandListener(command)
        when (command) {
            is Command.Close -> {
                navigateHandler.popBackStack()
            }

        }
    }


    private fun renderDoc(doc: InventoryPallet?) {
        tvNumberPallet.text = doc?.number ?: ""
        tvCountPallet.text = doc?.count.toSimpleFormat()
        tvCountPlacePallet.text = doc?.countBox.toSimpleFormat()
        tvCountRowPallet.text = doc?.countRow.toSimpleFormat()
    }

    private fun renderBox(box: BoxInventoryPallet?) {
        tvDateBox.text = box?.dateChanged.toSimpleDateTime()
        tvCountBox.text = box?.count.toSimpleFormat()
        tvCountPlaceBox.text = box?.countBox.toSimpleFormat()
    }
}