package me.augusto.passwordgenerator

import android.content.ClipData
import android.content.ClipboardManager
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.slider.Slider
import java.security.SecureRandom


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        bindSliderText()

        findViewById<Button>(R.id.generateButton).setOnClickListener {
            generatePassword()
        }

        findViewById<ImageButton>(R.id.copyButton).setOnClickListener {
            copyPassword()
        }
    }

    fun bindSliderText(){
        val passSizeText = findViewById<EditText>(R.id.passwordSizeText)
        val passSizeSlider = findViewById<Slider>(R.id.passwordSizeSlider)
        passSizeText.setText(passSizeSlider.value.toInt().toString())

        passSizeSlider.addOnChangeListener { slider, value, fromUser ->
            if (fromUser) {
                passSizeText.setText(value.toInt().toString())
            }
        }

        passSizeText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val value = s.toString().toFloatOrNull()
                if (value != null && value >= passSizeSlider.valueFrom && value <= passSizeSlider.valueTo) {
                    passSizeSlider.value = value.toInt().toFloat()
                }
            }
        })
    }

    fun generatePassword(){
        val passwordSize = findViewById<Slider>(R.id.passwordSizeSlider).value.toInt()
        val enabledCharacters = StringBuilder()
        if(findViewById<SwitchCompat>(R.id.lowercaseSwitch).isChecked){
            enabledCharacters.append("abcdefghijklmnopqrstuvwxyz")
        }
        if(findViewById<SwitchCompat>(R.id.uppercaseSwitch).isChecked){
            enabledCharacters.append("ABCDEFGHIJKLMNOPQRSTUVWXYZ")
        }
        if(findViewById<SwitchCompat>(R.id.numbersSwitch).isChecked){
            enabledCharacters.append("0123456789")
        }
        if(findViewById<SwitchCompat>(R.id.specialCharsSwitch).isChecked){
            enabledCharacters.append("!@#$%^&*()-_=+")
        }

        if(enabledCharacters.isEmpty()){
            Toast.makeText(applicationContext, "Select an option to use!", Toast.LENGTH_SHORT).show();
            return;
        }

        val secRand = SecureRandom()
        val password = StringBuilder(passwordSize)

        for(i in 0 until passwordSize){
            val randIndex = secRand.nextInt(enabledCharacters.length)
            password.append(enabledCharacters[randIndex])
        }

        findViewById<TextView>(R.id.passwordText).text = password
    }

    fun copyPassword(){
        val c = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        val password = findViewById<TextView>(R.id.passwordText).text.toString()
        c.setPrimaryClip(ClipData.newPlainText("password", password))
        Toast.makeText(applicationContext, "Password copied", Toast.LENGTH_SHORT).show()
    }
}