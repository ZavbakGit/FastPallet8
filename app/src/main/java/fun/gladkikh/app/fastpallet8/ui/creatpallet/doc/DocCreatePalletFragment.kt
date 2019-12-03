package `fun`.gladkikh.app.fastpallet8.ui.creatpallet.doc

import `fun`.gladkikh.app.fastpallet8.Constants
import `fun`.gladkikh.app.fastpallet8.R
import `fun`.gladkikh.app.fastpallet8.common.toSimpleFormat
import `fun`.gladkikh.app.fastpallet8.domain.model.entity.creatpallet.CreatePallet
import `fun`.gladkikh.app.fastpallet8.domain.model.entity.creatpallet.ProductCreatePallet
import `fun`.gladkikh.app.fastpallet8.ui.base.BaseFragment
import `fun`.gladkikh.app.fastpallet8.ui.common.Command
import `fun`.gladkikh.app.fastpallet8.ui.creatpallet.WrapperGuidCreatePallet
import `fun`.gladkikh.app.fastpallet8.ui.navigate.NavigateHandler
import `fun`.gladkikh.fastpallet7.ui.base.MyBaseAdapter
import android.content.Context
import android.view.View
import android.widget.TextView
import androidx.lifecycle.Observer
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.block_doc.*
import kotlinx.android.synthetic.main.list_block.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class DocCreatePalletFragment : BaseFragment() {

    override val layoutRes = R.layout.create_pallet_fragment_doc
    override val viewModel: DocCreatePalletViewModel by viewModel()

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

        viewModel.getDocLiveData().observe(viewLifecycleOwner, Observer {
            renderDoc(it)
        })


        viewModel.getListProductLiveData().observe(viewLifecycleOwner, Observer {
            renderList(it)
        })


        listView.setOnItemClickListener { _, _, i, _ ->
            viewModel.callKeyDown(position = i)
        }

    }

    override fun keyDownListener(keyCode: Int, position: Int?) {
        viewModel.callKeyDown(keyCode, listView.selectedItemPosition)
    }

    private fun renderList(list: List<ProductCreatePallet>) {
        adapter.list = list
    }

    override fun commandListener(command: Command) {
        super.commandListener(command)
        when (command) {
            is Command.Close -> {
                navigateHandler.popBackStack()
            }
            is Command.OpenForm -> {
                when(command.code){
                    Constants.OPEN_PRODUCT_FORM ->{
                        navigateHandler.
                            startCreatePalletProduct(command.data as WrapperGuidCreatePallet)
                    }
                }
            }
        }
    }

    private fun renderDoc(doc: CreatePallet?) {
        tvStatus.text = doc?.status?.fullName?:""
        tvDoc.text = doc?.description ?: ""
    }


    private class Adapter(mContext: Context) : MyBaseAdapter<ProductCreatePallet>(mContext) {
        override fun bindView(item: ProductCreatePallet, holder: Any) {
            holder as ViewHolder

            holder.tvCountBackProduct.text = item.countBack.toSimpleFormat()
            holder.tvCountPlaceBackProduct.text = item.countBoxBack.toSimpleFormat()
            holder.tvCountProduct.text = item.count.toSimpleFormat()
            holder.tvCountPlaceProduct.text = item.countBox.toSimpleFormat()
            holder.tvCountPalletProduct.text = item.countPallet.toSimpleFormat()

            holder.tvNameProduct.text = item.nameProduct

        }

        override fun getLayout(): Int = R.layout.item_product
        override fun createViewHolder(view: View): Any =
            ViewHolder(
                view
            )
    }

    private class ViewHolder(view: View) {
        var tvCountBackProduct: TextView = view.findViewById(R.id.tvCountBackProduct)
        var tvCountPlaceBackProduct: TextView = view.findViewById(R.id.tvCountPlaceBackProduct)

        var tvCountProduct: TextView = view.findViewById(R.id.tvCountProduct)
        var tvCountPlaceProduct: TextView = view.findViewById(R.id.tvCountPlaceProduct)
        var tvCountPalletProduct: TextView = view.findViewById(R.id.tvCountPalletProduct)
        var tvNameProduct: TextView = view.findViewById(R.id.tvNameProduct)
    }

}