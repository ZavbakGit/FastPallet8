package `fun`.gladkikh.app.fastpallet8.ui.activity

import `fun`.gladkikh.app.fastpallet8.R
import `fun`.gladkikh.app.fastpallet8.domain.model.creatpallet.old.CreatePaletModelImpl
import `fun`.gladkikh.app.fastpallet8.domain.model.creatpallet.old.CreatePalletModel
import `fun`.gladkikh.app.fastpallet8.repository.creatpallet.CreatePalletRepository
import `fun`.gladkikh.app.fastpallet8.domain.usecase.testdata.AddTestDataCreatePalletUseCase
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.activity_main1.*
import org.koin.android.ext.android.inject

class MainActivity1 : AppCompatActivity() {

    val repository: CreatePalletRepository by inject()
    val addUseCase: AddTestDataCreatePalletUseCase by inject()

    val model: CreatePalletModel =
        CreatePaletModelImpl(
            repository
        )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main1)

        model.getDoc().observe(this, Observer {
            if (it.error != null) {
                addText(it.error.message!!)
            } else {
                addText(it.data.toString())
            }
        })

        model.getListProduct().observe(this, Observer {
            if (it.error != null) {
                addText(it.error.message!!)
            } else {
                addText(it.data.toString())
            }
        })

        model.getProduct().observe(this, Observer {
            if (it.error != null) {
                addText(it.error.message!!)
            } else {
                addText(it.data.toString())
            }
        })

        btRead.setOnClickListener {
            model.setDoc("1")
            model.setProduct("1_9")
        }

        btAdd.setOnClickListener {
            addUseCase.save()

//            model.saveDoc(
//                CreatePallet(
//                    guid = "1",
//                    dateChanged = Date(),
//                    barcode = "",
//                    isLastLoad = false,
//                    number = "1",
//                    guidServer = "1",
//                    status = Status.LOADED,
//                    description = "1",
//                    date = Date()
//                )
//            ).subscribe {
//                model.setDoc("1")
//            }
//
//
//            (1..9).map {
//                ProductCreatePallet(
//                    guid = "1_" + it,
//                    number = "",
//                    isLastLoad = false,
//                    barcode = "",
//                    dateChanged = Date(),
//                    weightCoffProduct = 0f,
//                    weightEndProduct = 1,
//                    weightStartProduct = 2,
//                    count = null,
//                    countBox = null,
//                    guidProductBack = null,
//                    countBoxBack = null,
//                    countBack = null,
//                    codeProduct = null,
//                    ed = null,
//                    edCoff = null,
//                    weightBarcode = null,
//                    countPallet = null,
//                    nameProduct = null,
//                    countRow = null,
//                    guidDoc = "1"
//                )
//            }.forEach {
//                model.saveProduct(it).subscribe{
//                    model.setDoc("1")
//                }
//            }
        }
    }


    private fun addText(str: String) {
        tvInfo.text = str
    }

}
