package vn.vistark.qrinfoscanner.ui.sign_in

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_account_info.*
import kotlinx.android.synthetic.main.activity_sign_in.*
import kotlinx.android.synthetic.main.activity_sign_in.masterLayout
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.android.synthetic.main.component_float_qr_scan_btn.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.HttpException
import vn.vistark.qrinfoscanner.R
import vn.vistark.qrinfoscanner.core.api.ApiService
import vn.vistark.qrinfoscanner.core.api.AuthIntercepter
import vn.vistark.qrinfoscanner.core.extensions.Retrofit2Extension.Companion.await
import vn.vistark.qrinfoscanner.core.extensions.ViewExtension.Companion.onChanged
import vn.vistark.qrinfoscanner.ui.home.HomeActivity
import vn.vistark.qrinfoscanner.ui.sign_up.SignUpActivity
import vn.vistark.qrinfoscanner.core.extensions.ViewExtension.Companion.clickAnimate
import vn.vistark.qrinfoscanner.core.extensions.keyboard.HideKeyboardExtension.Companion.HideKeyboard
import vn.vistark.qrinfoscanner.core.helpers.VistarkContextWrapper
import vn.vistark.qrinfoscanner.core.overrides.VistarkActivity
import vn.vistark.qrinfoscanner.domain.DTOs.GDSTUserLoginDTO
import vn.vistark.qrinfoscanner.domain.constants.Config
import vn.vistark.qrinfoscanner.domain.constants.GDSTStorage
import vn.vistark.qrinfoscanner.helpers.alert_helper.AlertHelper.Companion.showAlertConfirm
import vn.vistark.qrinfoscanner.helpers.FloatQuickScanButtonHelper
import vn.vistark.qrinfoscanner.helpers.alert_helper.AlertHelper.Companion.showLoadingAlert
import java.lang.Exception

class SignInActivity : VistarkActivity() {

    companion object {
        var SIA: SignInActivity? = null
    }


    override fun attachBaseContext(newBase: Context?) {
        if (newBase != null) {
            super.attachBaseContext(VistarkContextWrapper.wrap(newBase, Config.LanguageCode))
        } else {
            super.attachBaseContext(newBase)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        FloatQuickScanButtonHelper.initialize(asiIvQuickScanIcon, cfqsLnQuickScanBtn)

        // Set tài khoản nếu đã có trước đó
        asiEdtUsername.setText(GDSTStorage.CurrentUser?.username ?: "")

        btnSignIn.clickAnimate {
            if (!validateData())
                return@clickAnimate

            val intent = Intent(this, HomeActivity::class.java)

            val dto = GDSTUserLoginDTO()
            dto.username = asiEdtUsername.text.toString().trim()
            dto.password = asiEdtPassword.text.toString()

            val loading = this.showLoadingAlert()
            loading.show()

            GlobalScope.launch {
                try {
                    val response = ApiService.mAPIServices.postGDSTLogin(dto).await()
                    runOnUiThread { loading.cancel() }
                    if (response == null)
                        throw Exception("Không có KQ Trả về")

                    AuthIntercepter.CurrentToken = response.accessToken
                    AuthIntercepter.CurrentTokenType = response.tokenType
                    GDSTStorage.CurrentUser.password = dto.password

                    startActivity(intent)
                    finish()

                } catch (http: HttpException) {
                    runOnUiThread { loading.cancel() }
                    if (http.response().code() == 401) {
                        runOnUiThread {
                            showAlertConfirm(getString(R.string.ttkhmkkd))
                        }
                    } else throw Exception("Mã HTTP RESPONSE không xác nhận được")
                } catch (e: Exception) {
                    runOnUiThread { loading.cancel() }
                    runOnUiThread {
                        showAlertConfirm(getString(R.string.dnktcvltl))
                    }
                    e.printStackTrace()
                }
            }
        }

        tvSignUp.clickAnimate {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        asiEdtPassword.onChanged {
            asiTvErrorMsg.text = ""
            asiTvErrorMsg.visibility = View.GONE
        }

        asiEdtUsername.onChanged {
            asiTvErrorMsg.text = ""
            asiTvErrorMsg.visibility = View.GONE
        }


        SIA = this

        masterLayout.setOnClickListener { HideKeyboard() }
    }

    private fun validateData(): Boolean {
        if (asiEdtUsername.text.trim().isEmpty()) {
            asiTvErrorMsg.text = getString(R.string.cnttk)
            asiTvErrorMsg.visibility = View.VISIBLE
            asiEdtUsername.error = getString(R.string.cnttk)
            return false
        }
        if (asiEdtPassword.text?.trim()?.isEmpty() == true) {
            asiTvErrorMsg.text = getString(R.string.cnmk)
            asiTvErrorMsg.visibility = View.VISIBLE
            return false
        }
        return true
    }

    fun updateIdentityField(identity: String) {
        asiEdtUsername.setText(identity)
    }

    override fun onBackPressed() {
        showAlertConfirm(getString(R.string.btsmdud), {
            finish()
        })
    }
}