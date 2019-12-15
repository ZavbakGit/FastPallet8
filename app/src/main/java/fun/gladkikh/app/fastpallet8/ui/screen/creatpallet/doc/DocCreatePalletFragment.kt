package `fun`.gladkikh.app.fastpallet8.ui.screen.creatpallet.doc

import `fun`.gladkikh.app.fastpallet8.Constants
import `fun`.gladkikh.app.fastpallet8.R
import `fun`.gladkikh.app.fastpallet8.common.toSimpleFormat
import `fun`.gladkikh.app.fastpallet8.domain.entity.creatpallet.CreatePallet
import `fun`.gladkikh.app.fastpallet8.domain.entity.creatpallet.ProductCreatePallet
import `fun`.gladkikh.app.fastpallet8.ui.base.BaseFragment
import `fun`.gladkikh.app.fastpallet8.ui.common.Command
import `fun`.gladkikh.app.fastpallet8.ui.screen.creatpallet.WrapperGuidCreatePallet
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
                    Constants.OPEN_PRODUCT_CREATE_PALLET_FORM ->{
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
            holder.tvNameProduct.text = item.nameProduct

//            holder.tvCountDoc.text = item.countBack.toSimpleFormat()
//            holder.tvPlaceDoc.text = item.countBoxBack.toSimpleFormat()
//
//            holder.tvCountFact.text = item.count.toSimpleFormat()
//            holder.tvPalceFact.text = item.countBox.toSimpleFormat()
//            holder.tvPalletFact.text = item.countPallet.toSimpleFormat()



        }

        override fun getLayout(): Int = R.layout.item_product_n
        override fun createViewHolder(view: View): Any =
            ViewHolder(
                view
            )
    }

    private class ViewHolder(view: View) {
        var tvNameProduct: TextView = view.findViewById(R.id.tvNameProduct)
        var tvNumberView: TextView = view.findViewById(R.id.tvNumberView)

//        var tvCountDoc: TextView = view.findViewById(R.id.tvCountDoc)
//        var tvPlaceDoc: TextView = view.findViewById(R.id.tvPlaceDoc)
//
//
//        var tvCountFact: TextView = view.findViewById(R.id.tvCountFact)
//        var tvPalceFact: TextView = view.findViewById(R.id.tvPalceFact)
//        var tvPalletFact: TextView = view.findViewById(R.id.tvPalletFact)
    }

}