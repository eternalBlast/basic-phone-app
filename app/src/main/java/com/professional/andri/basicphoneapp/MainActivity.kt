package com.professional.andri.basicphoneapp

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast


class MainActivity : AppCompatActivity() {
    private var callButton: Button? = null
    private var smsButton: Button? = null
    private lateinit var phoneText: EditText
    private lateinit var smsText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //inflate
        callButton = findViewById(R.id.call_button) as Button
        smsButton = findViewById(R.id.sms_button) as Button
        phoneText = findViewById(R.id.phone_input) as EditText
        smsText = findViewById(R.id.sms_input) as EditText

        //listeners
        callButton!!.setOnClickListener{
            isPermissionGranted()
        }

        smsButton!!.setOnClickListener{
            sendSMS()
        }
    }

    fun isPermissionGranted(): Boolean {
        if (Build.VERSION.SDK_INT >= 23) {
            return if (checkSelfPermission(android.Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                Log.v("TAG", "Permission is granted")
                val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneText.text.toString()))
                startActivity(intent)
                true
            } else {

                Log.v("TAG", "Permission is revoked")
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CALL_PHONE), 1)
                false
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v("TAG", "Permission is granted")
            return true
        }
    }


    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {

            1 -> {

                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(applicationContext, "Permission granted", Toast.LENGTH_SHORT).show()
//                    callPhone()
                } else {
                    Toast.makeText(applicationContext, "Permission denied", Toast.LENGTH_SHORT).show()
                }
                return
            }
        }
    }

    fun sendSMS() {
        val smsIntent = Intent(android.content.Intent.ACTION_VIEW)
        smsIntent.type = "vnd.android-dir/mms-sms"
        smsIntent.putExtra("address", phoneText.text.toString())
        smsIntent.putExtra("sms_body", smsText.text.toString())
        startActivity(smsIntent)
    }
}
