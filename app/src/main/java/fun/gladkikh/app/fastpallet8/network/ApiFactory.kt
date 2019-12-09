package `fun`.gladkikh.app.fastpallet8.network

import `fun`.gladkikh.app.fastpallet8.network.util.LogJSONInterceptor
import `fun`.gladkikh.app.fastpallet8.network.util.intity.ReqestModel
import `fun`.gladkikh.app.fastpallet8.network.util.intity.ReqestObj
import `fun`.gladkikh.app.fastpallet8.network.util.intity.ResponseModel
import `fun`.gladkikh.app.fastpallet8.network.util.intity.ResponseObj
import `fun`.gladkikh.app.fastpallet8.Constants
import `fun`.gladkikh.app.fastpallet8.domain.entity.SettingApp
import `fun`.gladkikh.app.fastpallet8.network.util.AutorithationUtil
import `fun`.gladkikh.app.fastpallet8.repository.setting.SettingsRepository
import android.annotation.SuppressLint
import com.google.gson.GsonBuilder
import io.reactivex.Single
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST


/**
 *The interface which provides methods to get result of webservices
 */
class ApiFactory(private val settingsRepository: SettingsRepository) {



    private val gson =
        GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
            .create()

    private val configInterceptor = Interceptor {
        val configUrl = it.request().url()
            .newBuilder()
            .addQueryParameter("uid", Constants.UID)
            .addQueryParameter("platform", "Android")
            .addQueryParameter("app_version", Constants.APP_VERSION)
            .addQueryParameter("version_os", Constants.OS_VERSION)
            .build()

        val configRequest = it.request()
            .newBuilder()
            .url(configUrl)
            .build()

        it.proceed(configRequest)
    }

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val authClient = OkHttpClient().newBuilder()
        .addInterceptor(configInterceptor)
        .addInterceptor(LogJSONInterceptor())
        .addInterceptor(loggingInterceptor)
        .build()




    private fun getRetrofit(settingApp: SettingApp):Retrofit{
       return Retrofit.Builder()
            .baseUrl(settingApp.host ?: "")
            .client(authClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }


    private fun getApi(settingsPref: SettingApp) = getRetrofit(settingsPref).create(Api::class.java)

    interface Api {
        @POST("host")
        fun getDataFromServer(
            @Header("Authorization") auth: String,
            @Body sendParamJson: ReqestModel
        ): Single<Response<ResponseModel>>
    }


    @SuppressLint("CheckResult")
    fun <T : ResponseObj> request(
        command: String,
        objRequest: ReqestObj,
        classResponse: Class<T>
    ): Single<ResponseObj> {

        val requestModel = ReqestModel(
            command = command,
            strDataIn = gson.toJson(objRequest)
        )

        val settingApp = settingsRepository.getSetting()

        return Single.just(settingApp)
            .map {
                //Проверка на код тсд
                if (it.code.isNullOrEmpty()) {
                    throw Throwable("Не заполнен код ТСД")
                }
                return@map it
            }
            .map {
                //Строка авторизации
                return@map AutorithationUtil.getStringAutorization(
                    it.login,
                    it.pass
                )
            }.flatMap {
                val api = getApi(settingApp)
                api.getDataFromServer(
                    auth = it,
                    sendParamJson = requestModel
                )
            }
            .doOnError {
                println(it)
            }
            .flatMap {
                when {
                    !it.isSuccessful -> Single.error<Throwable>(Throwable(it.errorBody().toString()))
                    else -> Single.just(it.body())
                }
            }.flatMap {
                val responseModel = it as ResponseModel

                when {
                    !(responseModel.success ?: true) -> Single.error<Throwable>(
                        Throwable(
                            responseModel.messError
                        )
                    )
                    else -> Single.just(responseModel)
                }
            }.map {
                gson.fromJson((it as ResponseModel).response ?: "", classResponse)
            }
    }
}