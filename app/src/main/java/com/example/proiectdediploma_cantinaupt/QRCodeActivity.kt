package com.example.proiectdediploma_cantinaupt

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.example.proiectdediploma_cantinaupt.databinding.ActivityQrcodeBinding


import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions



class QRCodeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQrcodeBinding
    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()){

        isGranted: Boolean ->
        if(isGranted){
            showCamera()
        }
        else{
            Log.e("QRCodeActivity", "Failed to retrieve permission")

        }

    }
    private val scanLauncher = registerForActivityResult(ScanContract()){
        result: ScanIntentResult ->
       run{
            if (result.contents == null) {

            }
            else{
                setResult(result.contents)
            }
        }

    }
private fun setResult(string: String){
    binding.textResult.text = string
}
    private fun showCamera() {
        val option = ScanOptions()
        option.setDesiredBarcodeFormats(ScanOptions.QR_CODE)
        option.setPrompt("Scan QR code")
        option.setCameraId(0)
        option.setBeepEnabled(false)
        option.setBarcodeImageEnabled(true)
        option.setOrientationLocked(true)

        scanLauncher.launch(option)

    }



    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQrcodeBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.Scanner.setOnClickListener {
            checkPermissionCamera()

         }

    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun checkPermissionCamera() {
        if(ContextCompat.checkSelfPermission( this, android.Manifest.permission.CAMERA )== PackageManager.PERMISSION_GRANTED){

            showCamera()
        }
        else if(shouldShowRequestPermissionRationale(android.Manifest.permission.CAMERA)){
            Toast.makeText(this, "Camera permission requested", Toast.LENGTH_SHORT)
        }
        else{
            requestPermissionLauncher.launch(android.Manifest.permission.CAMERA)
        }
    }


}