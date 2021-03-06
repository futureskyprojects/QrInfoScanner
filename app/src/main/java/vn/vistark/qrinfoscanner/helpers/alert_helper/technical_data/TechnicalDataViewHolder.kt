package vn.vistark.qrinfoscanner.helpers.alert_helper.technical_data

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import vn.vistark.qrinfoscanner.R
import vn.vistark.qrinfoscanner.domain.mock_entities.CertificationAndLicense
import vn.vistark.qrinfoscanner.domain.mock_entities.MaterialShip
import vn.vistark.qrinfoscanner.domain.mock_entities.VesselData

class TechnicalDataViewHolder(v: View) {

    val autdIvClose: ImageView = v.findViewById(R.id.autdIvClose)
    val autdTvErrorMsg: TextView = v.findViewById(R.id.autdTvErrorMsg)
    val autdTvDialogName: TextView = v.findViewById(R.id.autdTvDialogName)

    val autdTvGeolocation: TextView = v.findViewById(R.id.autdTvGeolocation)

    val autdTvTranshipmentLocation: TextView = v.findViewById(R.id.autdTvTranshipmentLocation)

    val autdBtnConfirm: TextView = v.findViewById(R.id.autdBtnConfirm)
    // -------------------------------- //

    val autdEdtEventId: EditText =
        v.findViewById(R.id.autdEdtEventId)

    val autdEdtKDELinking: EditText =
        v.findViewById(R.id.autdEdtKDELinking)

    val autdTvProductForm: TextView =
        v.findViewById(R.id.autdTvProductForm)

    val autdScIsTranshipment: SwitchCompat = v.findViewById(R.id.autdScIsTranshipment)

    val autdLnTranshipmentRoot: LinearLayout = v.findViewById(R.id.autdLnTranshipmentRoot)

    val autdTvTranshipmentDate: TextView =
        v.findViewById(R.id.autdTvTranshipmentDate)

    val autdTvEventDate: TextView =
        v.findViewById(R.id.autdTvEventDate)

    fun onCheckedChange(onChanged: (Boolean) -> Unit) {
        autdScIsTranshipment.setOnCheckedChangeListener { buttonView, isChecked ->
            autdLnTranshipmentRoot.visibility = if (!isChecked) View.GONE else View.VISIBLE
            onChanged.invoke(isChecked)
        }
    }

    fun updateError(err: String = ""): Boolean {
        if (err.isNotEmpty()) {
            this.autdTvErrorMsg.text = err
            this.autdTvErrorMsg.visibility = View.VISIBLE
            return false
        } else {
            this.autdTvErrorMsg.text = ""
            this.autdTvErrorMsg.visibility = View.GONE
            return true
        }
    }


    fun clearErrorOnTextChanger(edt: EditText) {
        edt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                // Clear Error state
                updateError()
            }
        })
    }

}