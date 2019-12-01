package `fun`.gladkikh.app.fastpallet8.ui.base

import `fun`.gladkikh.app.fastpallet8.ui.common.Command
import `fun`.gladkikh.app.fastpallet8.ui.common.Command.ConfirmDialog
import `fun`.gladkikh.app.fastpallet8.ui.common.Command.EditNumberDialog
import `fun`.gladkikh.app.fastpallet8.ui.common.startConfirmDialog
import `fun`.gladkikh.app.fastpallet8.ui.common.startEditDialogNumber
import `fun`.gladkikh.app.fastpallet8.ui.navigate.NavigateHandler
import `fun`.gladkikh.app.fastpallet8.ui.activity.MainActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer

abstract class BaseFragment : Fragment() {
    protected abstract val layoutRes: Int

    protected lateinit var mainActivity: MainActivity
    protected lateinit var navigateHandler: NavigateHandler
    protected abstract val viewModel: BaseViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(layoutRes, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mainActivity = activity as MainActivity
        navigateHandler = mainActivity.navigateHandler
    }


    override fun onResume() {
        super.onResume()
        initSubscription()
    }

    private val commandObservable = Observer<Command> {
        commandListener(it)
    }

    private val keyDownObservable = Observer<Int> {
        keyDownListener(it)
    }

    open fun keyDownListener(keyCode: Int) {

    }

    open fun commandListener(command: Command) {
        when (command) {
            is ConfirmDialog -> {
                startConfirmDialog(activity!!, command) {
                    viewModel.callBackConfirmDialog(it)
                }
            }
            is EditNumberDialog -> {
                startEditDialogNumber(
                    activity!!
                        .supportFragmentManager, command
                ) {
                    viewModel.callBackEditNumberDialog(it)
                }
            }
        }
    }

    protected open fun initSubscription() {
        viewModel.getMessageChannel().observe(viewLifecycleOwner, Observer {
            mainActivity.showMessage(it)
        })

        viewModel.getMessageErrorChannel().observe(viewLifecycleOwner, Observer {
            mainActivity.showErrorMessage(it)
        })

        viewModel.getShowProgressChannel().observe(viewLifecycleOwner, Observer {
            if (it) {
                mainActivity.showProgress()
            } else {
                mainActivity.hideProgress()
            }
        })

        mainActivity.getKeyDownLiveData().observe(viewLifecycleOwner, keyDownObservable)
        viewModel.getCommandChannel().observe(viewLifecycleOwner, commandObservable)
    }
}