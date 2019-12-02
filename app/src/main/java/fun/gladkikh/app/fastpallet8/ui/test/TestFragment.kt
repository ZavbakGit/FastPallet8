package `fun`.gladkikh.app.fastpallet8.ui.test

import `fun`.gladkikh.app.fastpallet8.R
import `fun`.gladkikh.app.fastpallet8.ui.base.BaseFragment
import `fun`.gladkikh.app.fastpallet8.ui.creatpallet.WrapperGuidCreatePallet
import kotlinx.android.synthetic.main.test_fragment.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class TestFragment : BaseFragment() {
    override val layoutRes = R.layout.test_fragment
    override val viewModel: TestViewModel by viewModel()

    override fun initSubscription() {
        super.initSubscription()

        btAddTestData.setOnClickListener {
            viewModel.addTestData()
        }
        btRecalc.setOnClickListener {
            viewModel.recalc()
        }
        btOpenBox.setOnClickListener {
            navigateHandler.startCreatePalletBox(
                WrapperGuidCreatePallet(
                    guidDoc = "0",
                    guidProduct = "0_0",
                    guidPallet = "0_0_0",
                    guidBox = "0_0_0_0"
                )
            )
        }
        btOpenPallet.setOnClickListener {
            navigateHandler.startCreatePalletPallet(
                WrapperGuidCreatePallet(
                    guidDoc = "0",
                    guidProduct = "0_0",
                    guidPallet = "0_0_0"
                )
            )
        }


        btOpenProduct.setOnClickListener {
            navigateHandler.startCreatePalletProduct(
                WrapperGuidCreatePallet(
                    guidDoc = "0",
                    guidProduct = "0_0"
                )
            )
        }

        btSettings.setOnClickListener {
            navigateHandler.startSettings()
        }
    }
}