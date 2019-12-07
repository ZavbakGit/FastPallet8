package `fun`.gladkikh.app.fastpallet8.ui.documentlist

import `fun`.gladkikh.app.fastpallet8.Constants
import `fun`.gladkikh.app.fastpallet8.R
import `fun`.gladkikh.app.fastpallet8.domain.model.entity.ItemListDocument
import `fun`.gladkikh.app.fastpallet8.ui.base.BaseFragment
import `fun`.gladkikh.app.fastpallet8.ui.common.Command
import `fun`.gladkikh.app.fastpallet8.ui.creatpallet.WrapperGuidCreatePallet
import `fun`.gladkikh.fastpallet7.ui.base.MyBaseAdapter
import android.content.Context
import android.view.View
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer

import kotlinx.android.synthetic.main.document_fragment.*
import kotlinx.android.synthetic.main.list_block.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class DocumentListFragment : BaseFragment() {

    override val layoutRes = R.layout.document_fragment
    override val viewModel: DocumentListViewModel by viewModel()

    private lateinit var adapter: Adapter

    override fun initSubscription() {
        super.initSubscription()

        adapter = Adapter(activity as Context)
        listView.adapter = adapter

        viewModel.getListDocumentLiveData().observe(viewLifecycleOwner, Observer {
            renderList(it)
        })

        listView.setOnItemClickListener { _, _, i, _ ->
            viewModel.callKeyDown(position = i)
        }



    }

    override fun keyDownListener(keyCode: Int, position: Int?) {
        viewModel.callKeyDown(keyCode,listView.selectedItemPosition)
    }

    override fun commandListener(command: Command) {
        super.commandListener(command)
        when (command) {
            is Command.Close -> {
                navigateHandler.popBackStack()
            }
            is Command.AnyCommand -> {
                when (command.code) {
                    Constants.COMMAND_START_MENU -> {
                        showMenu()
                    }
                }
            }
            is Command.OpenForm -> {
                when (command.code) {
                    Constants.OPEN_DOC_CREATE_PALLET -> {
                        navigateHandler.startCreatePalletDoc(command.data as WrapperGuidCreatePallet)
                    }
                }
            }
        }
    }


    private fun showMenu() {
        val popupMenu = PopupMenu(activity, tvInfo)
        popupMenu.inflate(R.menu.main)
        popupMenu
            .setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.menuDownload -> {
                        viewModel.loadDocuments()
                        true
                    }
                    R.id.menuInfoPallet -> {
                        Toast.makeText(
                            activity,
                            "Информация по паллете",
                            Toast.LENGTH_SHORT
                        ).show()
                        true
                    }
                    R.id.menuSettings -> {
                        navigateHandler.startSettings()

                        true
                    }
                    else -> false
                }
            }

        popupMenu.setOnDismissListener {
            Toast.makeText(
                activity,
                "onDismiss",
                Toast.LENGTH_SHORT
            ).show()
        }
        popupMenu.show()

    }


    private fun renderList(list: List<ItemListDocument>) {
        adapter.list = list
    }

    private class Adapter(mContext: Context) : MyBaseAdapter<ItemListDocument>(mContext) {
        override fun bindView(item: ItemListDocument, holder: Any) {
            holder as ViewHolder
            holder.tvDescription.text = item.description
            holder.tvStatus.text = item.status?.fullName ?: ""
        }

        override fun getLayout(): Int = R.layout.item_document
        override fun createViewHolder(view: View): Any =
            ViewHolder(
                view
            )
    }

    private class ViewHolder(view: View) {
        var tvDescription: TextView = view.findViewById(R.id.tvDescription)
        var tvStatus: TextView = view.findViewById(R.id.tvStatus)

    }

}