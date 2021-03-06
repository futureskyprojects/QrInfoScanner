package vn.vistark.qrinfoscanner.ui.ship_collection

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_ship_collection.*
import kotlinx.android.synthetic.main.component_bottom_nav.*
import kotlinx.android.synthetic.main.component_top_search_bar.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import vn.vistark.qrinfoscanner.R
import vn.vistark.qrinfoscanner.core.api.ApiService
import vn.vistark.qrinfoscanner.core.extensions.Retrofit2Extension.Companion.await
import vn.vistark.qrinfoscanner.core.extensions.ViewExtension.Companion.clickAnimate
import vn.vistark.qrinfoscanner.core.extensions.keyboard.HideKeyboardExtension.Companion.HideKeyboard
import vn.vistark.qrinfoscanner.core.helpers.VistarkContextWrapper
import vn.vistark.qrinfoscanner.core.overrides.VistarkActivity
import vn.vistark.qrinfoscanner.domain.constants.Config
import vn.vistark.qrinfoscanner.domain.entities.GDSTShip
import vn.vistark.qrinfoscanner.helpers.BottomNavigationBarHelper.Companion.initGDSTBottomBar
import vn.vistark.qrinfoscanner.helpers.BottomNavigationBarHelper.Companion.initGDSTSmartBottomBar
import vn.vistark.qrinfoscanner.helpers.TopSearchBarHelper.Companion.initGDSTSmartTopSearchBar
import vn.vistark.qrinfoscanner.helpers.TopSearchBarHelper.Companion.initGDSTTopSearchBar
import vn.vistark.qrinfoscanner.helpers.alert_helper.AlertHelper.Companion.showAlertConfirm
import vn.vistark.qrinfoscanner.helpers.alert_helper.AlertHelper.Companion.showLoadingAlert

class ShipCollectionActivity : VistarkActivity() {

    val ships: ArrayList<GDSTShip> = ArrayList()
    lateinit var adapter: ShipCollectionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_ship_collection)

        initGDSTBottomBar(cbnBnvBottomNav, cbnBtnCenter, -1)
        btmNavLayout.initGDSTSmartBottomBar(ascRvVessels)
        topSearchBar.initGDSTSmartTopSearchBar(ascRvVessels)

        updateCount(0)

        initEvents()

        initRecyclerView()

        syncShips()
    }

    private fun initEvents() {
        masterLayout.setOnClickListener {
            HideKeyboard()
        }
        ascBackButton.clickAnimate {
            finish()
        }
    }

    private fun updateCount(count: Int = 0) {
        ascTvName.text = getString(R.string.dlt) + " (" + count + ")"
    }

    private fun syncShips() {
        val loading = this.showLoadingAlert()
        loading.show()
        GlobalScope.launch {
            try {
                val response = ApiService.mAPIServices.getGDSTShip().await()
                runOnUiThread { loading.cancel() }
                if (response == null)
                    throw Exception("Không phân dải được KQ trả về")

                runOnUiThread {
                    updateCount(response.size)
                    response.forEach { ship ->
                        ships.add(ship)
                        adapter.notifyDataSetChanged()
                    }

                    aaEdtSearchBar.initGDSTTopSearchBar(ships) {
                        ships.clear()
                        ships.addAll(it)
                        adapter.notifyDataSetChanged()
                    }
                }
            } catch (e: Exception) {
                runOnUiThread { loading.cancel() }
                runOnUiThread {
                    showAlertConfirm(getString(R.string.kldtdlcs))
                }
                e.printStackTrace()
            }
        }
    }

    override fun attachBaseContext(newBase: Context?) {
        if (newBase != null) {
            super.attachBaseContext(VistarkContextWrapper.wrap(newBase, Config.LanguageCode))
        } else {
            super.attachBaseContext(newBase)
        }
    }

    private fun initRecyclerView() {
        ascRvVessels.setHasFixedSize(true)
        ascRvVessels.layoutManager = LinearLayoutManager(this)

        adapter = ShipCollectionAdapter(ships)

        ascRvVessels.adapter = adapter
    }
}