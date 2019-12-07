package `fun`.gladkikh.app.fastpallet8.ui.creatpallet.product

import `fun`.gladkikh.app.fastpallet8.Constants
import `fun`.gladkikh.app.fastpallet8.R
import `fun`.gladkikh.app.fastpallet8.common.toSimpleFormat
import `fun`.gladkikh.app.fastpallet8.domain.model.entity.creatpallet.PalletCreatePallet
import `fun`.gladkikh.app.fastpallet8.domain.model.entity.creatpallet.ProductCreatePallet
import `fun`.gladkikh.app.fastpallet8.ui.base.BaseFragment
import `fun`.gladkikh.app.fastpallet8.ui.common.Command
import `fun`.gladkikh.app.fastpallet8.ui.creatpallet.WrapperGuidCreatePallet
import `fun`.gladkikh.fastpallet7.ui.base.MyBaseAdapter
import android.content.Context
import android.view.View
import android.widget.TextView
import androidx.lifecycle.Observer
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.block_product.*
import kotlinx.android.synthetic.main.list_block.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class ProductCreatePalletFragment : BaseFragment() {

    override val layoutRes = R.layout.create_pallet_fragment_product
    override val viewModel: ProductCreatePalletViewModel by viewModel()

    private lateinit var adapter: Adapter

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


        adapter = Adapter(activity as Context)
        listView.adapter = adapter

        viewModel.getProductLiveData().observe(viewLifecycleOwner, Observer {
            renderProduct(it)
        })



        viewModel.getListPalletLiveData().observe(viewLifecycleOwner, Observer {
            renderList(it)
        })

        tvCountProduct.setOnClickListener {
            viewModel.readBarcode("<pal>0214${(10..99).random()}</pal>")
            //viewModel.readBarcode("<pal>0214${84}</pal>")
        }

        mainActivity.barcodeLiveData.observe(viewLifecycleOwner, Observer {
            viewModel.readBarcode(it)
        })

        listView.setOnItemClickListener { _, _, i, _ ->
            viewModel.callKeyDown(position = i)
        }

    }

    override fun keyDownListener(keyCode: Int, position: Int?) {
        viewModel.callKeyDown(keyCode, listView.selectedItemPosition)
    }

    private fun renderList(list: List<PalletCreatePallet>) {
        adapter.list = list
    }

    override fun commandListener(command: Command) {
        super.commandListener(command)
        when (command) {
            is Command.Close -> {
                navigateHandler.popBackStack()
            }
            is Command.OpenForm -> {
                when (command.code) {
                    Constants.OPEN_PALLET_FORM -> {
                        navigateHandler.startCreatePalletPallet(command.data as WrapperGuidCreatePallet)
                    }
                }
            }
        }
    }

    private fun renderProduct(product: ProductCreatePallet?) {
        tvNameProduct.text = product?.nameProduct ?: ""
        tvCountProduct.text = product?.count.toSimpleFormat()
        tvCountPlaceProduct.text = product?.countBox.toSimpleFormat()
        tvCountPalletProduct.text = product?.countPallet.toSimpleFormat()

        tvCountBackProduct.text = product?.countBack.toSimpleFormat()
        tvCountPlaceBackProduct.text = product?.countBoxBack.toSimpleFormat()
    }


    private class Adapter(mContext: Context) : MyBaseAdapter<PalletCreatePallet>(mContext) {
        override fun bindView(item: PalletCreatePallet, holder: Any) {
            holder as ViewHolder
            holder.tvNumberPallet.text = "№ ${item.number}"
            holder.tvCountBox.text = item.count.toSimpleFormat()
            holder.tvCountPlaceBox.text = item.countBox.toSimpleFormat()
            holder.tvNumberView.text = item.numberView.toSimpleFormat()

        }

        override fun getLayout(): Int = R.layout.item_pallet
        override fun createViewHolder(view: View): Any =
            ViewHolder(
                view
            )
    }

    private class ViewHolder(view: View) {
        var tvNumberPallet: TextView = view.findViewById(R.id.tvNumberPallet)
        var tvCountBox: TextView = view.findViewById(R.id.tvCountBox)
        var tvCountPlaceBox: TextView = view.findViewById(R.id.tvCountPlaceBox)
        var tvNumberView: TextView = view.findViewById(R.id.tvNumberView)
    }

}