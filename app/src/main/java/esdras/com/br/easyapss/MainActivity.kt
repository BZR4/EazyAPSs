package esdras.com.br.easyapss

import android.Manifest
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.telephony.SmsManager
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception

class MainActivity : AppCompatActivity(), TextWatcher {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        verifyPermissions()
    }

    companion object {
        val SMS_PERMISSION = 1000
        val PHONE_STATE = 2000
    }

    fun sendSMS(view: View){
        try {
            val number =  String.format("+%s%s%s",editTextDDI.text, editTextDDD.text, editTextNumber.text)
            val message: String = editTextMessage.text.toString()
            val smsManager = SmsManager.getDefault()
            smsManager.sendTextMessage(number,null,message,null,null)
            Toast.makeText(this,"SMS enviado com sucesso!",Toast.LENGTH_LONG).show()
        }catch (e: Exception){
            Log.d("Execption: ",e.localizedMessage)
            Toast.makeText(this,"Exception: "+e.localizedMessage,Toast.LENGTH_LONG).show()
        }
    }

    fun verifyPermissions(){
        //Deve verificar permissao?
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED){
            // Sem explicacao, podemo solicitar a permissao
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.SEND_SMS)){
                //Explanation
                val dialog = AlertDialog.Builder(this)
                    .setTitle("Titulo Janela")
                    .setMessage("Explicacao da permissao...")
                    .setPositiveButton("Liberar", DialogInterface.OnClickListener { dialog, which ->
                        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.SEND_SMS), SMS_PERMISSION)
//                        Toast.makeText(this,"Liberado",Toast.LENGTH_LONG).show()
                    })
                    .setNegativeButton("Negar", null)
                    .setNeutralButton("Cancelar", null)
                dialog.show()

            }else{
                val permissions = arrayOf(Manifest.permission.SEND_SMS)
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_PHONE_STATE, Manifest.permission.SEND_SMS),
                    SMS_PERMISSION)
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            SMS_PERMISSION -> {
                if(grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this,"Permissão concedida!",Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun afterTextChanged(s: Editable?) {

    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        textViewCountLetters.text = String.format("%d/%s",s?.length,"Max Size")
    }
}
