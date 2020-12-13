package vn.vistark.qrinfoscanner.ui.material_ship

import android.annotation.SuppressLint
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import vn.vistark.qrinfoscanner.R
import vn.vistark.qrinfoscanner.domain.constants.RuntimeStorage
import vn.vistark.qrinfoscanner.domain.mock_entities.CertificationAndLicense
import vn.vistark.qrinfoscanner.domain.mock_entities.VesselData
import vn.vistark.qrinfoscanner.core.mockup.CommonMockup.Companion.MockupGet
import vn.vistark.qrinfoscanner.domain.constants.GDSTStorage
import vn.vistark.qrinfoscanner.domain.entities.GDSTMaterialShip
import java.util.*

class MaterialShipHolder(v: View) : RecyclerView.ViewHolder(v) {
    val ilmsLnRoot: LinearLayout = v.findViewById(R.id.ilmsLnRoot)
    val ilmsdIvDeleteIcon: ImageView = v.findViewById(R.id.ilmsdIvDeleteIcon)

    private val ilmsTvMaterialShipId: TextView = v.findViewById(R.id.ilmsTvMaterialShipId)
    private val ilmsTvMaterialShipname: TextView = v.findViewById(R.id.ilmsTvMaterialShipname)

    private val ilmsTvFIP: TextView = v.findViewById(R.id.ilmsTvFIP)
    private val ilmsTvTripDate: TextView = v.findViewById(R.id.ilmsTvTripDate)
    private val ilmsTvGrearType: TextView = v.findViewById(R.id.ilmsTvGrearType)
    private val ilmsTvProductMethod: TextView = v.findViewById(R.id.ilmsTvProductMethod)
    private val ilmsTvLanding: TextView = v.findViewById(R.id.ilmsTvLanding)

    @SuppressLint("SetTextI18n")
    fun bind(materialShip: GDSTMaterialShip) {
        ilmsTvMaterialShipname.text = "Tàu nguyên liệu #${materialShip.id}"
        ilmsTvMaterialShipId.text = "#${materialShip.id}"

        val fip =
            GDSTStorage.GDSTFipCodes?.firstOrNull { x -> x.id == materialShip.fipcodeId }?.title
                ?: ""
        ilmsTvFIP.text = "FIP: $fip"

        ilmsTvTripDate.text = "Ngày đi: ${materialShip.dateGo}"

        val gearType =
            GDSTStorage.GDSTGearTypes?.firstOrNull { x -> x.id == materialShip.gearId }?.title ?: ""

        ilmsTvGrearType.text = "Ngư cụ: $gearType"

        val method =
            GDSTStorage.GDSTProductForms?.firstOrNull { x -> x.id == materialShip.prodctMethod }?.title
                ?: ""

        ilmsTvProductMethod.text = "Phương thức khai thác: $method"

        val location =
            GDSTStorage.GDSTLocations?.firstOrNull { x -> x.id == materialShip.upFishing }?.title
                ?: ""

        ilmsTvLanding.text =
            "Lên cá: $location (${materialShip.dateUpFishing})"
    }
}