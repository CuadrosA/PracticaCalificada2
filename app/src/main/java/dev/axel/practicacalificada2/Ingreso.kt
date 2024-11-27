package dev.axel.practicacalificada2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore

class Ingreso : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        enableEdgeToEdge()
        setContentView(R.layout.activity_ingreso)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val Login_et_email: EditText = findViewById(R.id.login_et_email)
        val Login_et_psw: EditText = findViewById(R.id.login_et_psw)
        val Login_btn_login: Button = findViewById(R.id.login_btn_login)
        val Login_btn_register: Button = findViewById(R.id.login_btn_register)
        val auth = Firebase.auth
        val db = FirebaseFirestore.getInstance()

        // En caso se quiera registrar
        Login_btn_register.setOnClickListener {
            startActivity(Intent(this, Register::class.java))
        }

        Login_btn_login.setOnClickListener {
            val dni = Login_et_email.text.toString()
            val clave = Login_et_psw.text.toString()
            if (dni.isNotEmpty() && clave.isNotEmpty()) {
                db.collection("users")
                    .whereEqualTo("dni", dni).whereEqualTo("clave", clave)
                    .get()
                    .addOnSuccessListener { documents ->
                        val nombre = documents.first().getString("nombre")
                        Snackbar.make(findViewById(android.R.id.content), "ACCESO PERMITIDO", Snackbar.LENGTH_SHORT).show()
                        Snackbar.make(findViewById(android.R.id.content), "Bienvenido $nombre", Snackbar.LENGTH_SHORT).show()
                        startActivity(Intent(this, Accedido::class.java))
                    }
                    .addOnFailureListener { e ->
                        Snackbar.make(findViewById(android.R.id.content), "EL USUARIO Y/O CLAVE NO EXISTE EN EL SISTEMA", Snackbar.LENGTH_SHORT).show()
                    }
            }
        }
    }
}