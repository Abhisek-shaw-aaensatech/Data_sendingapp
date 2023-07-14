package com.example.testingokhttp

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.testingokhttp.databinding.ActivityMainBinding
import okhttp3.*
import java.io.IOException


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    lateinit var selectedAuthentication:Any
    lateinit var selectedEncryption :Any

    private val encryptionOptions = arrayOf("AES","TkIp") // Replace with your encryption options
    private val authenticationOptions = arrayOf("OptionA", "OptionB", "OptionC") // Replace with your authentication options




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)
        val encryptionAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, encryptionOptions)
        val authenticationAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, authenticationOptions)
        binding.IDEncryption.adapter=encryptionAdapter
        binding.IDAuthentication.adapter=authenticationAdapter
// on select encryption spinner
        binding.IDEncryption.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedEncryption = encryptionOptions[position]
                // Handle the selected encryption
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Handle nothing selected
            }
        }
        // on select authentication spinner
        binding.IDAuthentication.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                 selectedAuthentication = authenticationOptions[position]
                // Handle the selected authentication
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Handle nothing selected
            }
        }
        binding.btnSend.setOnClickListener {
            sendDataToServer()
        }
    }
    private fun sendDataToServer(){
        val ipAddress= binding.Ipadr.text.toString()
        val port = binding.port.text.toString()
        val ssid = binding.ssid.text.toString()
        val password=binding.pswrd.text.toString()

        val client = OkHttpClient()
        val requestBody= FormBody.Builder()
            .add("id_address",ipAddress)
            .add("port",port)
            .add("ssid",ssid)
            .add("Password",password)
            .add("encryption", selectedEncryption.toString() )
            .add("authentication", selectedAuthentication.toString())
            .build()

        val request= Request.Builder()
            .url("http://$ipAddress:$port/getOptimizerId")
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {


            override fun onFailure(call: Call, e: IOException) {

                e.printStackTrace()
                Log.d("abhi","not working"+ e.toString())
                // Handle failure
            }

            override fun onResponse(call: Call, response: Response) {
                Log.d("abhi","working ${response}")

                response.body?.close()
                if (response.isSuccessful) {
                    // Handle successful response
             Toast.makeText(applicationContext, response.body.toString(),Toast.LENGTH_LONG).show()
                } else {
                    // Handle error response
                }
            }
        })
    }
}