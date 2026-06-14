package com.earendil.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.activity.enableEdgeToEdge
import com.earendil.app.fragments.InicioFragment
import com.earendil.app.fragments.MasFragment
import com.earendil.app.fragments.NuevaPublicacionFragment
import com.earendil.app.fragments.PerfilFragment
import com.earendil.app.fragments.SocialFragment
import com.google.android.material.bottomnavigation.BottomNavigationView


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
                R.id.nav_publicar -> cargarFragment(NuevaPublicacionFragment())
                R.id.nav_perfil -> cargarFragment(PerfilFragment())
                R.id.nav_mas -> cargarFragment(MasFragment())
            }
            true
        }
    }

    private fun cargarFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.contenedorFragments, fragment)
            .commit()
    }
}