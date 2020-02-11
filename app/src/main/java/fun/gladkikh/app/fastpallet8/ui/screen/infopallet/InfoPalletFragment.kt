package `fun`.gladkikh.app.fastpallet8.ui.screen.infopallet

import `fun`.gladkikh.app.fastpallet8.R
import `fun`.gladkikh.app.fastpallet8.common.toSimpleFormat
import `fun`.gladkikh.app.fastpallet8.ui.base.BaseFragment
import `fun`.gladkikh.fastpallet7.ui.base.MyBaseAdapter
import android.content.Context
import android.view.View
import android.widget.TextView
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.list_block.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class InfoPalletFragment : BaseFragment() {

    override val layoutRes = R.layout.infopallet_fragment
    override val viewModel: InfoPalletViewModel by viewModel()

    private lateinit var adapter: Adapter

    override fun initSubscription() {
        super.initSubscription()

        adapter = Adapter(activity as Context)
        listView.adapter = adapter

        viewModel.getListItem().observe(viewLifecycleOwner, Observer {
            renderList(it)
        })

        mainActivity.barcodeLiveData.observe(viewLifecycleOwner, Observer {
            viewModel.readBarcode(it)
        })


    }

    override fun keyDownListener(keyCode: Int, position: Int?) {
        viewModel.callKeyDown(keyCode,listView.selectedItemPosition)
    }



    private fun renderList(list: List<ItemInfoPallet>) {
        adapter.list = list
    }

    private class Adapter(mContext: Context) : MyBaseAdapter<ItemInfoPallet>(mContext) {
        override fun bindView(item: ItemInfoPallet, holder: Any) {
            holder as ViewHolder
            holder.tvNumberPallet.text = "№ ${item.number}"
            holder.tvCountBox.text = item.infoPallet?.count.toSimpleFormat()
            holder.tvCountPlaceBox.text = item.infoPallet?.countBox.toSimpleFormat()
            holder.tvNameProduct.text = item.infoPallet?.nameProduct?:""
        }

        override fun getLayout(): Int = R.layout.item_info_pallet
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