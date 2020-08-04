package com.nmb.e_pantry

import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.nmb.e_pantry.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView


data class SpajzQueryResult(
    var nyersanyag: String,
    var mennyiseg: Double,
    var mertekegyseg: String
)

data class ReceptQueryResult(
    var nyersanyag: String,
    var mennyiseg: Double,
    var mertekegyseg: String
)

data class RecViewData(
    var id: String,
    var menny: String
)

lateinit var navView: NavigationView
lateinit var navController: NavController
lateinit var drawerLayout: DrawerLayout

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding =
            DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        drawerLayout = binding.drawerLayout
        navView = binding.navigationView
        navController = this.findNavController(R.id.myNavHostFragment)
        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout)
        NavigationUI.setupWithNavController(binding.navigationView, navController)
        navView.setNavigationItemSelectedListener(this)
    }

    override fun onSupportNavigateUp(): Boolean {
        return if (navController.currentDestination!!.id == R.id.startFragment) {
            drawerLayout.openDrawer(GravityCompat.START)
            true
        } else {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawers()
                true
            } else {
                navController.navigate(R.id.startFragment)
                navController.popBackStack(R.id.startFragment, false)
            }
        }
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            if (navController.currentDestination!!.id != R.id.startFragment) {
                onSupportNavigateUp()
            } else {
                super.onBackPressed()
            }
        }
    }

    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.s_megtekint -> if (navController.currentDestination!!.id != R.id.spajzMegtekintFragment) navController.navigate(
                R.id.spajzMegtekintFragment
            )
            R.id.s_hozzáad -> if (navController.currentDestination!!.id != R.id.spajzHozzaadFragment) navController.navigate(
                R.id.spajzHozzaadFragment
            )
            R.id.s_nyers_hozzáad -> if (navController.currentDestination!!.id != R.id.spajzNyersanyagHozzaadFragment) navController.navigate(
                R.id.spajzNyersanyagHozzaadFragment
            )
            R.id.r_megtekint -> if (navController.currentDestination!!.id != R.id.receptMegtekintFragment) navController.navigate(
                R.id.receptMegtekintFragment
            )
            R.id.r_hozzáad -> if (navController.currentDestination!!.id != R.id.receptHozaadFragment) navController.navigate(
                R.id.receptHozaadFragment
            )
            R.id.b_megtekint -> if (navController.currentDestination!!.id != R.id.bevasarlolistaMegtekintFragment) navController.navigate(
                R.id.bevasarlolistaMegtekintFragment
            )
            R.id.b_hozzáad -> if (navController.currentDestination!!.id != R.id.bevasarlolistaHozzaadFragment) navController.navigate(
                R.id.bevasarlolistaHozzaadFragment
            )
            R.id.b_hozzáad_recceptből -> if (navController.currentDestination!!.id != R.id.bevasarlolistaHozzadReceptbolFragment) navController.navigate(
                R.id.bevasarlolistaHozzadReceptbolFragment
            )
            R.id.s_nyers_modosit -> if (navController.currentDestination!!.id != R.id.spajzNyersanyagModositFragment) navController.navigate(
                R.id.spajzNyersanyagModositFragment
            )
        }
        drawerLayout.closeDrawers()
        return true
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            val v: View? = currentFocus
            if (v is EditText) {
                val outRect = Rect()
                v.getGlobalVisibleRect(outRect)
                if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                    v.clearFocus()
                    val imm: InputMethodManager =
                        getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0)
                }
            }
        }
        return super.dispatchTouchEvent(event)
    }
}