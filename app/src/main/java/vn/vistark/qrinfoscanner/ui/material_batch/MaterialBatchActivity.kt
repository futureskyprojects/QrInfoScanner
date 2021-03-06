package vn.vistark.qrinfoscanner.ui.material_batch

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_material_batch.*
import kotlinx.android.synthetic.main.activity_material_batch.btmNavLayout
import kotlinx.android.synthetic.main.activity_material_batch.masterLayout
import kotlinx.android.synthetic.main.component_bottom_nav.*
import kotlinx.android.synthetic.main.component_float_add_btn.*
import kotlinx.android.synthetic.main.component_top_search_bar.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import vn.vistark.qrinfoscanner.R
import vn.vistark.qrinfoscanner.core.api.ApiService
import vn.vistark.qrinfoscanner.core.extensions.Retrofit2Extension.Companion.await
import vn.vistark.qrinfoscanner.domain.constants.Config
import vn.vistark.qrinfoscanner.domain.mock_entities.Shipment
import vn.vistark.qrinfoscanner.helpers.alert_helper.AlertHelper.Companion.showAlertConfirm
import vn.vistark.qrinfoscanner.core.extensions.ViewExtension.Companion.clickAnimate
import vn.vistark.qrinfoscanner.core.extensions.keyboard.HideKeyboardExtension.Companion.HideKeyboard
import vn.vistark.qrinfoscanner.core.helpers.DatetimeHelper.Companion.Format
import vn.vistark.qrinfoscanner.core.helpers.VistarkContextWrapper
import vn.vistark.qrinfoscanner.core.overrides.VistarkActivity
import vn.vistark.qrinfoscanner.domain.DTOs.GDSTMaterialBacthCreateDTO
import vn.vistark.qrinfoscanner.domain.api.requests.material_batch.GetMaterialBatchBody
import vn.vistark.qrinfoscanner.domain.entities.GDSTMaterialBacth
import vn.vistark.qrinfoscanner.helpers.BottomNavigationBarHelper.Companion.initGDSTBottomBar
import vn.vistark.qrinfoscanner.helpers.BottomNavigationBarHelper.Companion.initGDSTSmartBottomBar
import vn.vistark.qrinfoscanner.helpers.FloatAddButtonHelper
import vn.vistark.qrinfoscanner.helpers.TopSearchBarHelper.Companion.initGDSTSmartTopSearchBar
import vn.vistark.qrinfoscanner.helpers.TopSearchBarHelper.Companion.initGDSTTopSearchBar
import vn.vistark.qrinfoscanner.helpers.alert_helper.AlertHelper.Companion.showLoadingAlert
import vn.vistark.qrinfoscanner.ui.material_ship.MaterialShipActivity
import java.util.*
import kotlin.collections.ArrayList

class MaterialBatchActivity : VistarkActivity() {
    private var shipmentId: Int = -1

    private val materialBatchs = ArrayList<GDSTMaterialBacth>()
    private lateinit var adapter: MaterialBatchAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_material_batch)

        initGDSTBottomBar(cbnBnvBottomNav, cbnBtnCenter, -1)
        btmNavLayout.initGDSTSmartBottomBar(ambRvMaterialBatch)
        topSearchBar.initGDSTSmartTopSearchBar(ambRvMaterialBatch)

        initTranshipmentData()

        initEvents()

        initRecyclerView()

        syncMaterialBatchs()

        initDataEvents()

        masterLayout.setOnClickListener { HideKeyboard() }
        rlOut.setOnClickListener { HideKeyboard() }
    }

    override fun attachBaseContext(newBase: Context?) {
        if (newBase != null) {
            super.attachBaseContext(VistarkContextWrapper.wrap(newBase, Config.LanguageCode))
        } else {
            super.attachBaseContext(newBase)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initTranshipmentData() {
        shipmentId = intent.getIntExtra(Shipment::class.java.simpleName, -1)
        if (shipmentId <= 0) {
            Toast.makeText(this, getString(R.string.ktxdlhdc), Toast.LENGTH_SHORT).show()
            finish()
            return
        }
        ambTvTitle.text =
            getString(R.string.lnl) + " [#" + shipmentId.toString()
                .padStart(Config.padSize, '0') + "]"
    }

    private fun fastCreate() {
        val loading = this.showLoadingAlert()
        loading.show()
        GlobalScope.launch {
            try {
                val dto = GDSTMaterialBacthCreateDTO(shipmentId)
                val res = ApiService.mAPIServices.postGDSTMaterialBatch(dto).await()
                    ?: throw  Exception("ko phan giai dc")
                runOnUiThread { loading.cancel() }
                runOnUiThread {
                    syncMaterialBatchs()
                    start(res.idBatch)
                }

            } catch (e: Exception) {
                runOnUiThread { loading.cancel() }
                e.printStackTrace()
                runOnUiThread {
                    showAlertConfirm(getString(R.string.tlnlktc))
                }
            }
        }
    }

    private fun initEvents() {
        ambBackButton.clickAnimate {
            finish()
        }
        FloatAddButtonHelper.initialize(cfabIvIcon, cfabLnAddBtn) {
            var maxId = 1
            if (materialBatchs.size > 0)
                maxId += materialBatchs.maxBy { it.id }?.id ?: 0
            val batch = GDSTMaterialBacth(maxId, shipmentId)
            this.showAlertConfirm(
                getString(R.string.bcmtlnlms) + " #${
                    batch.id.toString().padStart(
                        Config.padSize,
                        '0'
                    )
                } " + getString(R.string.vao_luc) + " [${Date().Format("HH:mm dd-MM-yyyy")}]",
                {
                    fastCreate()
                })
        }
    }

    private fun syncMaterialBatchs() {
        materialBatchs.clear()
        val loading = this.showLoadingAlert()
        loading.show()
        GlobalScope.launch {
            try {
                val response = ApiService.mAPIServices.getGDSTMaterialBatch(
                    GetMaterialBatchBody(shipmentId)
                ).await()
                runOnUiThread { loading.cancel() }
                if (response == null)
                    throw Exception("Không phân dải được KQ trả về")

                runOnUiThread {
//                    updateCount(response.size)
                    materialBatchs.addAll(response)
                    adapter.notifyDataSetChanged()

                    if (materialBatchs.isEmpty()) {
                        fastCreate()
                    }

                    aaEdtSearchBar.initGDSTTopSearchBar(materialBatchs) {
                        materialBatchs.clear()
                        materialBatchs.addAll(it)
                        adapter.notifyDataSetChanged()
                    }
                }
            } catch (e: Exception) {
                runOnUiThread { loading.cancel() }
                runOnUiThread {
                    showAlertConfirm(getString(R.string.kldtdlcs))
                }
                e.printStackTrace()
            } finally {

            }
        }
    }

    fun add(s: GDSTMaterialBacth) {
        materialBatchs.add(0, s)
        adapter.notifyDataSetChanged()
    }

    private fun initRecyclerView() {
        ambRvMaterialBatch.setHasFixedSize(true)
        ambRvMaterialBatch.layoutManager = LinearLayoutManager(this)

        adapter = MaterialBatchAdapter(materialBatchs)
        ambRvMaterialBatch.adapter = adapter
    }

    private fun initDataEvents() {
        adapter.onEdit = {
            showAlertConfirm(
                "Bạn có chắc muốn xóa dữ liệu lô nguyên liệu [#${
                    it.id.toString()
                        .padStart(Config.padSize, '0')
                }] hay không?",
                {
                    Toast.makeText(
                        this,
                        "Chức năng này hiện chưa được cho phép",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            )
        }

        adapter.onClick = {
            start(it.id)
        }
    }

    private fun start(materialBatchId: Int) {
        runOnUiThread {
            val intent = Intent(this, MaterialShipActivity::class.java)
            intent.putExtra(GDSTMaterialBacth::class.java.simpleName, materialBatchId)
            startActivity(intent)
        }
    }

    private fun removeBatchView(batch: GDSTMaterialBacth) {
        val index = materialBatchs.indexOfFirst { it.id == batch.id }
        materialBatchs.removeAt(index)
        adapter.notifyDataSetChanged()
    }
}