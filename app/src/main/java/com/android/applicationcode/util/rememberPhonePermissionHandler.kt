package com.android.applicationcode.util

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.net.toUri
import com.android.applicationcode.R

@Composable
fun rememberPhoneCallHandler(context: Context): (String) -> Unit {
    var phoneNumber by remember { mutableStateOf<String?>(null) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            phoneNumber?.let { phone ->
                val callIntent = Intent(Intent.ACTION_CALL, "tel:$phone".toUri())
                context.startActivity(callIntent)
            }
        } else {
            Toast.makeText(context, context.getString(R.string.permission_denied), Toast.LENGTH_SHORT).show()
        }
    }

    return remember {
        { dealerPhoneNumber: String ->
            if (androidx.core.content.ContextCompat.checkSelfPermission(
                    context, android.Manifest.permission.CALL_PHONE
                ) == android.content.pm.PackageManager.PERMISSION_GRANTED
            ) {
                val callIntent = Intent(Intent.ACTION_CALL, "tel:$dealerPhoneNumber".toUri())
                context.startActivity(callIntent)
            } else {
                phoneNumber = dealerPhoneNumber
                launcher.launch(android.Manifest.permission.CALL_PHONE)
            }
        }
    }
}
