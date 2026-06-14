package com.earendil.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.activity.enableEdgeToEdge
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.earendil.app.fragments.InicioFragment
import com.earendil.app.fragments.MasFragment
import com.earendil.app.fragments.MensajesFragment
import com.earendil.app.fragments.NuevaPublicacionFragment
import com.earendil.app.fragments.PerfilFragment
import com.earendil.app.fragments.SocialFragment
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView


class MainActivity : AppCompatActivity() {

    private lateinit var menu: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        menu = findViewById(R.id.menuInferior)

        cargarFragment(InicioFragment())

        menu.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_inicio -> cargarFragment(InicioFragment())
                R.id.nav_social -> cargarFragment(SocialFragment())
                R.id.nav_perfil -> cargarFragment(PerfilFragment())
            }
            true
        }

        val drawerLayout = findViewById<DrawerLayout>(R.id.drawer_layout)
        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        val navView = findViewById<NavigationView>(R.id.nav_view)

        toolbar.setNavigationOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

// Escuchar los clics del menú lateral
        navView.setNavigationItemSelectedListener { item ->
            when(item.itemId) {
                R.id.nav_perfil -> cargarFragment(PerfilFragment())
                R.id.nav_notificaciones -> { /* abrir fragment notificaciones */ }
                R.id.Mensajes -> cargarFragment(MensajesFragment())
                R.id.nav_logout -> { /* cerrar sesión */ }
            }
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }
    }

    private fun cargarFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.contenedorFragments, fragment)
            .commit()
    }
}