package com.turck.t1mobiledemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.m3.sdk.scannerlib.Barcode
import com.m3.sdk.scannerlib.BarcodeListener
import com.m3.sdk.scannerlib.BarcodeManager
import kotlinx.android.synthetic.main.activity_bcdscanner.*


class BCDScannerActivity : AppCompatActivity() {
    private var m3Barcode: Barcode? = null
    private var m3Listener: BarcodeListener? = null
    private var m3Manager: BarcodeManager? = null
    private var m3Symbology: Barcode.Symbology? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bcdscanner)

        m3Barcode = Barcode(this)
        m3Manager = BarcodeManager(this)
        m3Symbology = m3Barcode!!.getSymbologyInstance()

        Log.i("BCDScannerActivity", "onCreate")

        btnStartRead.setOnClickListener(::onButtonClick)
        btnStopRead.setOnClickListener(::onButtonClick)
        btnScanEnable.setOnClickListener(::onButtonClick)
        btnScanDisable.setOnClickListener(::onButtonClick)

        m3Listener = object : BarcodeListener {

            override fun onGetSymbology(nSymbol: Int, nVal: Int) {
                Log.i("BCDScannerActivity", "onGetSymbology result=$nSymbol, $nVal")
//                edSymNum.setText(Integer.toString(nSymbol))
//                edValNum.setText(Integer.toString(nVal))

            }

            override fun onBarcode(strBarcode: String) {
                Log.i("BCDScannerActivity", "result=$strBarcode")
                // mTvResult.setText(strBarcode);

            }

            override fun onBarcode(barcode: String, codeType: String) {
                Log.i("BCDScannerActivity", "result=$barcode")
                scanresult.setText("data: $barcode type: $codeType")

            }

        }

        m3Manager!!.addListener(m3Listener)

    }

    private fun onButtonClick(v: View) {
        when (v.id) {
            R.id.btnScanEnable -> {
                m3Barcode?.setScanner(true)
            }
            R.id.btnScanDisable -> {
                m3Barcode?.setScanner(false)
            }
            R.id.btnStartRead -> {
                m3Barcode?.scanStart()
            }
            R.id.btnStopRead -> {
                m3Barcode?.scanDispose()
            }
            else -> {
                Log.i("BCDScannerActivity", "Not defined button_click")
            }
        }

    }
}
