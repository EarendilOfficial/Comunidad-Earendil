package com.earendil.app

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.earendil.app.fragments.InicioFragment
import com.earendil.app.fragments.MensajesFragment
import com.earendil.app.fragments.NotificationsFragment
import com.earendil.app.fragments.PerfilFragment
import com.earendil.app.fragments.SocialFragment
import com.earendil.app.session.SessionManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var menuInferior: BottomNavigationView
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var sessionManager: SessionManager // La subimos aquí para inicializarla en onCreate

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Inicializar SessionManager
        sessionManager = SessionManager(this)

        drawerLayout = findViewById(R.id.drawer_layout)
        menuInferior = findViewById(R.id.menuInferior)
        val navView = findViewById<NavigationView>(R.id.nav_view)

        // Cargar fragment inicial (Evita recargar si el teléfono gira)
        if (savedInstanceState == null) {
            cargarFragment(InicioFragment())
        }

        // Escuchar clics del menú INFERIOR
        menuInferior.setOnItemSelectedListener { item ->
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
                    // Si tu Drawer Layout tiene layout_gravity="end" en el XML, usa END.
                    // Si abre desde la izquierda, usa START.
                    drawerLayout.openDrawer(GravityCompat.END)
                    false // Devolvemos false para que este botón no se quede marcado como "seleccionado visualmente"
                }
                else -> false
            }
        }

        // Escuchar clics del menú LATERAL (Drawer)
        navView.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_perfil -> {
                    cargarFragment(PerfilFragment())
                    // SINCRONIZACIÓN: Cambiamos el ícono seleccionado abajo
                    menuInferior.selectedItemId = R.id.nav_perfil
                }
                R.id.nav_notificaciones -> {
                    cargarFragment(NotificationsFragment())
                    // Si no hay botón de notificaciones abajo, puedes desmarcar el menú inferior o dejarlo como está
                }
                R.id.Mensajes -> {
                    cargarFragment(MensajesFragment())
                }
                R.id.nav_logout -> {
                    closeSession()
                }
            }
            drawerLayout.closeDrawer(GravityCompat.END)
            true
        }
    }

    private fun closeSession() {
        // 1. Limpiar el token de SharedPreferences de forma segura
        sessionManager.clearSession()

        // 2. Redirigir inmediatamente a LoginActivity
        val intent = Intent(this, LoginActivity::class.java)
        // Estas flags limpian todo el historial de pantallas para que si el usuario le da "atrás" en el cel,
        // no pueda volver a meterse a la MainActivity sin logearse.
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish() // Destruye esta MainActivity
    }

    private fun cargarFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.contenedorFragments, fragment)
            .commit()
    }
}