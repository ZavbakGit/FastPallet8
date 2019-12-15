package `fun`.gladkikh.app.fastpallet8.ui.screen.documentlist

import `fun`.gladkikh.app.fastpallet8.Constants
import `fun`.gladkikh.app.fastpallet8.R
import `fun`.gladkikh.app.fastpallet8.domain.entity.ItemListDocument
import `fun`.gladkikh.app.fastpallet8.ui.base.BaseFragment
import `fun`.gladkikh.app.fastpallet8.ui.common.Command
import `fun`.gladkikh.app.fastpallet8.ui.common.Command.*
import `fun`.gladkikh.app.fastpallet8.ui.screen.action.WrapperGuidAction
import `fun`.gladkikh.app.fastpallet8.ui.screen.creatpallet.WrapperGuidCreatePallet
import `fun`.gladkikh.app.fastpallet8.ui.screen.inventorypallet.WrapperGuidInventoryPallet
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
            is Close -> {
                navigateHandler.popBackStack()
            }
            is AnyCommand -> {
                when (command.code) {
                    Constants.COMMAND_START_MENU -> {
                        showMenu()
                    }
                }
            }
            is OpenForm -> {
               openForm(command)
            }
        }
    }


    private fun openForm(openForm: OpenForm){
        when (openForm.code) {
            Constants.OPEN_DOC_CREATE_PALLET_FORM -> {
                navigateHandler.startCreatePalletDoc(openForm.data as WrapperGuidCreatePallet)
            }
            Constants.OPEN_DOC_ACTION_FORM ->{
                navigateHandler.startActionDoc(openForm.data as WrapperGuidAction)
            }
            Constants.OPEN_DOC_INVENTORY_PALLET_FORM->{
                navigateHandler.startInventoryPallet(openForm.data as WrapperGuidInventoryPallet)
            }

        }
    }

    private fun showMenu() {
        val popupMenu = PopupMenu(activity, tvInfo)

        if (Constants.IS_TEST_BUILD){
            popupMenu.inflate(R.menu.test_main)
        }else{
            popupMenu.inflate(R.menu.main)
        }

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
                    R.id.addInventoryPallet -> {
                        viewModel.addNewInventoryPallet()

                        true
                    }
                    R.id.menuTestDataCreatePallet->{
                        viewModel.addTestDataCreatePallet()
                        true
                    }
                    R.id.menuTestDataAction ->{
                        viewModel.addTestDataAction()
                        true
                    }
                    R.id.menuTestDataInventoryPallet ->{
                        viewModel.addTestDataInventoryPallet()
                        true
                    }



                    else -> false
                }
            }

        popupMenu.setOnDismissListener {
//            Toast.makeText(
//                activity,
//                "onDismiss",
//                Toast.LENGTH_SHORT
//            ).show()
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