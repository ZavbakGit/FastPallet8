package `fun`.gladkikh.app.fastpallet8.ui.screen.action.product

import `fun`.gladkikh.app.fastpallet8.Constants
import `fun`.gladkikh.app.fastpallet8.R
import `fun`.gladkikh.app.fastpallet8.common.toSimpleFormat
import `fun`.gladkikh.app.fastpallet8.domain.entity.action.ProductAction
import `fun`.gladkikh.app.fastpallet8.ui.base.BaseFragment
import `fun`.gladkikh.app.fastpallet8.ui.common.Command
import `fun`.gladkikh.app.fastpallet8.ui.screen.action.WrapperGuidAction
import `fun`.gladkikh.fastpallet7.ui.base.MyBaseAdapter
import android.content.Context
import android.view.View
import android.widget.TextView
import androidx.lifecycle.Observer
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.block_product.*
import kotlinx.android.synthetic.main.list_block.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class ProductActionFragment : BaseFragment() {

    override val layoutRes = R.layout.action_fragment_product

    override val viewModel: ProductActionViewModel by viewModel()

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
                    WrapperGuidAction::class.java
                ) as WrapperGuidAction
        }


        adapter = Adapter(activity as Context)
        listView.adapter = adapter

        viewModel.getProductLiveData().observe(viewLifecycleOwner, Observer {
            renderProduct(it)
        })



        viewModel.getListPalletLiveData().observe(viewLifecycleOwner, Observer {
            renderList(it)
        })

        listView.setOnItemClickListener { _, _, i, _ ->
            viewModel.callKeyDown(position = i)
        }

        if (Constants.IS_TEST_BUILD){
            tvCountProduct.setOnClickListener {
                viewModel.readBarcode("${(10..99).random()}123456789")
            }

            tvCountPalletProduct.setOnClickListener {
                viewModel.readBarcode("<pal>0214${(10..99).random()}</pal>")
            }
        }


        mainActivity.barcodeLiveData.observe(viewLifecycleOwner, Observer {
            viewModel.readBarcode(it)
        })


    }

    override fun keyDownListener(keyCode: Int, position: Int?) {
        viewModel.callKeyDown(keyCode, listView.selectedItemPosition)
    }

    private fun renderList(list: List<ItemAction>) {
        adapter.list = list
    }

    override fun commandListener(command: Command) {
        super.commandListener(command)
        when (command) {
            is Command.Close -> {
                navigateHandler.popBackStack()
            }
            is Command.OpenForm->{
                openForm(command)
            }
        }

    }

    private fun openForm(openForm: Command.OpenForm){
        when (openForm.code) {
            Constants.OPEN_PRODUCT_ACTION_DIALOG_FORM -> {
                navigateHandler.startActionProductDialog(openForm.data as WrapperGuidAction)
            }
            Constants.OPEN_BOX_ACTION_FORM -> {
                navigateHandler.startActionBox(openForm.data as WrapperGuidAction)
            }
        }
    }

    private fun renderProduct(product: ProductAction?) {
        tvNameProduct.text = product?.nameProduct ?: ""
        tvCountProduct.text = product?.count.toSimpleFormat()
        tvCountPlaceProduct.text = product?.countBox.toSimpleFormat()
        tvCountPalletProduct.text = product?.countPallet.toSimpleFormat()

        tvCountBackProduct.text = product?.countBack.toSimpleFormat()
        tvCountPlaceBackProduct.text = product?.countBoxBack.toSimpleFormat()
    }


    private class Adapter(mContext: Context) : MyBaseAdapter<ItemAction>(mContext) {
        override fun bindView(item: ItemAction, holder: Any) {
            holder as ViewHolder
            holder.tvNumberPallet.text = "${item.description?:""}"
            holder.tvCountBox.text = item.count.toSimpleFormat()
            holder.tvCountPlaceBox.text = item.countBox.toSimpleFormat()
            holder.tvNumberView.text = item.numberView.toSimpleFormat()
            holder.tvNumberView.text = item.numberView.toSimpleFormat()
            holder.tvNameProduct.text = item.nameProduct?:""

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
        var tvNameProduct: TextView = view.findViewById(R.id.tvNameProduct)
    }

}