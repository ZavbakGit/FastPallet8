package `fun`.gladkikh.app.fastpallet8.ui.screen.inventorypallet.doc

import `fun`.gladkikh.app.fastpallet8.Constants
import `fun`.gladkikh.app.fastpallet8.R
import `fun`.gladkikh.app.fastpallet8.common.toSimpleDateTime
import `fun`.gladkikh.app.fastpallet8.common.toSimpleFormat
import `fun`.gladkikh.app.fastpallet8.domain.entity.creatpallet.ProductCreatePallet
import `fun`.gladkikh.app.fastpallet8.domain.entity.inventorypallet.BoxInventoryPallet
import `fun`.gladkikh.app.fastpallet8.domain.entity.inventorypallet.InventoryPallet
import `fun`.gladkikh.app.fastpallet8.ui.base.BaseFragment
import `fun`.gladkikh.app.fastpallet8.ui.common.Command
import `fun`.gladkikh.app.fastpallet8.ui.common.Command.*
import `fun`.gladkikh.app.fastpallet8.ui.screen.creatpallet.WrapperGuidCreatePallet
import `fun`.gladkikh.fastpallet7.ui.base.MyBaseAdapter
import android.content.Context
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.TextView
import androidx.lifecycle.Observer
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.block_pallet.*
import kotlinx.android.synthetic.main.block_product.*
import kotlinx.android.synthetic.main.create_pallet_fragment_pallet.*
import kotlinx.android.synthetic.main.list_block.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class DocInventoryPalletFragment : BaseFragment() {

    override val layoutRes = R.layout.inventory_pallet_fragment_doc
    override val viewModel: DocInventoryPalletViewModel by viewModel()

    private lateinit var adapter: Adapter

    override fun initSubscription() {
        super.initSubscription()

        if (viewModel.wrapperGuid != null) {
            viewModel.wrapperGuid = viewModel.wrapperGuid
        } else {
            val gson = GsonBuilder().create()
            val strJson = arguments!!.get(Constants.EXTRA_WRAP_GUID) as String
            viewModel.wrapperGuid =
                gson.fromJson(strJson, WrapperGuidInventoryPallet::class.java) as WrapperGuidInventoryPallet
        }


        adapter =
            Adapter(
                activity as Context
            )
        listView.adapter = adapter

        viewModel.getDocLiveData().observe(viewLifecycleOwner, Observer {
            renderDoc(it)
        })



        viewModel.getListBoxLiveData().observe(viewLifecycleOwner, Observer {
            renderList(it)
        })

        tvCountPallet.setOnClickListener {
            viewModel.readBarcode("${(10..99).random()}123456789")
        }

        mainActivity.barcodeLiveData.observe(viewLifecycleOwner, Observer {
            viewModel.readBarcode(it)
        })

        listView.setOnItemClickListener { _, _, i, _ ->
            viewModel.callKeyDown(position = i)
        }

    }

    override fun keyDownListener(keyCode: Int, position: Int?) {
        viewModel.callKeyDown(keyCode,listView.selectedItemPosition)
    }

    private fun renderList(list: List<BoxInventoryPallet>) {
        adapter.list = list
    }

    override fun commandListener(command: Command) {
        super.commandListener(command)
        when (command) {
            is AnyCommand ->{
                if (command.code == Constants.COMMAND_HIDE_FORM){
                    if (frameLayoutProduct.visibility == GONE) {
                        frameLayoutProduct.visibility = VISIBLE
                    } else {
                        frameLayoutProduct.visibility = GONE
                    }
                }
            }
            is Close -> {
                navigateHandler.popBackStack()
            }
            is OpenForm -> {
                when(command.code){
                    Constants.OPEN_BOX_INVENTORY_PALLET_FORM ->{
                        navigateHandler.
                            startInventoryPalletBox(command.data as WrapperGuidInventoryPallet)
                    }
                    Constants.OPEN_PRODUCT_CREATE_PALLET_DIALOG_FORM->{
                        navigateHandler.
                            startProductDialogCreatePallet(command.data as WrapperGuidCreatePallet)
                    }
                }
            }
        }
    }



    private fun renderDoc(doc: InventoryPallet?) {
        tvNumberPallet.text = (doc?.numberPallet ?: "") +"\n"+ (doc?.nameProduct?:"")
        tvCountPallet.text = doc?.count.toSimpleFormat()
        tvCountPlacePallet.text = doc?.countBox.toSimpleFormat()
        tvCountRowPallet.text = doc?.countRow.toSimpleFormat()
    }

    private class Adapter(mContext: Context) : MyBaseAdapter<BoxInventoryPallet>(mContext) {
        override fun bindView(item: BoxInventoryPallet, holder: Any) {
            holder as ViewHolder
            holder.tvDateBox.text = item.dateChanged.toSimpleDateTime()
            holder.tvCountBox.text = item.count.toSimpleFormat()
            holder.tvCountPlaceBox.text = item.countBox.toSimpleFormat()
            holder.tvNumberBox.text = item.numberView.toSimpleFormat()

        }

        override fun getLayout(): Int = R.layout.item_box
        override fun createViewHolder(view: View): Any =
            ViewHolder(
                view
            )
    }

    private class ViewHolder(view: View) {
        var tvDateBox: TextView = view.findViewById(R.id.tvDateBox)
        var tvCountBox: TextView = view.findViewById(R.id.tvCountBox)
        var tvCountPlaceBox: TextView = view.findViewById(R.id.tvCountPlaceBox)
        var tvNumberBox: TextView = view.findViewById(R.id.tvNumberView)
    }

}