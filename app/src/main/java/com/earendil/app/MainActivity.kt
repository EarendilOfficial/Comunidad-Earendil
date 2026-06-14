package com.earendil.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.activity.enableEdgeToEdge
import com.example.earendil.fragments.InicioFragment
import com.example.earendil.fragments.MasFragment
import com.example.earendil.fragments.NuevaPublicacionFragment
import com.example.earendil.fragments.PerfilFragment
import com.example.earendil.fragments.SocialFragment
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