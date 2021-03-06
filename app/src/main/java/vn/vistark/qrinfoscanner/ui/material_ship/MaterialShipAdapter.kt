package vn.vistark.qrinfoscanner.ui.material_ship

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import vn.vistark.qrinfoscanner.R
import vn.vistark.qrinfoscanner.domain.mock_entities.MaterialShip
import vn.vistark.qrinfoscanner.core.extensions.ViewExtension.Companion.clickAnimate
import vn.vistark.qrinfoscanner.core.interfaces.IClickable
import vn.vistark.qrinfoscanner.core.interfaces.IDeletable
import vn.vistark.qrinfoscanner.domain.entities.GDSTMaterialShip

class MaterialShipAdapter(private val materialships: ArrayList<GDSTMaterialShip>) :
    RecyclerView.Adapter<MaterialShipHolder>(), IClickable<GDSTMaterialShip>,
    IDeletable<GDSTMaterialShip> {

    override var onClick: ((GDSTMaterialShip) -> Unit)? = null
    override var onEdit: ((GDSTMaterialShip) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MaterialShipHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_layout_material_ship, parent, false)
        return MaterialShipHolder(v)
    }

    override fun getItemCount(): Int {
        return materialships.count()
    }

    override fun onBindViewHolder(holder: MaterialShipHolder, position: Int) {
        val materialBatch = materialships[position]
        holder.bind(materialBatch)

        holder.ilmsdIvEditIcon.clickAnimate {
            onEdit?.invoke(materialBatch)
        }
        holder.ilmsLnRoot.clickAnimate {
            onClick?.invoke(materialBatch)
        }
    }

}