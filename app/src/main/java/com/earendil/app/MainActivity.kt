package com.earendil.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.activity.enableEdgeToEdge
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.earendil.app.fragments.InicioFragment
import com.earendil.app.fragments.MensajesFragment
import com.earendil.app.fragments.NuevaPublicacionFragment
import com.earendil.app.fragments.PerfilFragment
import com.earendil.app.fragments.SocialFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var menu: BottomNavigationView
    private lateinit var drawerLayout: DrawerLayout // La declaramos aquí para usarla globalmente si hace falta

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        drawerLayout = findViewById(R.id.drawer_layout)
        menu = findViewById(R.id.menuInferior)
        val navView = findViewById<NavigationView>(R.id.nav_view)

        // Cargar fragment inicial
        if (savedInstanceState == null) {
            cargarFragment(InicioFragment())
        }

        // Escuchar clics del menú INFERIOR
        menu.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_inicio -> {
                    cargarFragment(InicioFragment())
                    true
                }
                R.id.nav_social -> {
                    cargarFragment(SocialFragment())
                    true
                }
                R.id.nav_perfil -> {
                    cargarFragment(PerfilFragment())
                    true
                }
                R.id.nav_abrir_lateral -> {
                    drawerLayout.openDrawer(GravityCompat.END)
                    false
                }
                else -> false
            }
        }


        navView.setNavigationItemSelectedListener { item ->
            when(item.itemId) {
                R.id.nav_perfil -> cargarFragment(PerfilFragment())
                R.id.nav_notificaciones -> { /* abrir fragment notificaciones */ }
                R.id.Mensajes -> cargarFragment(MensajesFragment())
                R.id.nav_logout -> { /* cerrar sesión */ }
            }
            drawerLayout.closeDrawer(GravityCompat.END)
            true
        }
    }

    private fun cargarFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.contenedorFragments, fragment)
            .commit()
    }
}