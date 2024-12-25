package com.crbt.ui.core.ui

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.ContactsContract
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.crbt.designsystem.components.CustomInputField
import com.crbt.designsystem.components.InputType
import com.crbt.designsystem.components.TextFieldType
import com.crbt.designsystem.icon.CrbtIcons
import com.crbt.ui.core.ui.validationStates.PhoneNumberValidationState
import com.example.crbtjetcompose.core.designsystem.R

@Composable
fun GiftPurchasePhoneNumber(
    onPhoneNumberChanged: (String, Boolean) -> Unit,
    modifier: Modifier = Modifier,
    context: Context = LocalContext.current
) {
    val phoneNumberState by remember {
        mutableStateOf(
            PhoneNumberValidationState(),
        )
    }
    val focusManager = LocalFocusManager.current

    val contactPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val contactUri = result.data?.data
            if (contactUri != null) {
                val phoneNumber = getPhoneNumberFromUri(context, contactUri)
                if (phoneNumber != null) {
                    phoneNumberState.text = phoneNumber
                    onPhoneNumberChanged(phoneNumber, phoneNumberState.isValid)
                    phoneNumberState.onFocusChange(true)
                    phoneNumberState.enableShowErrors()
                }
            }
        }
    }

    fun openContacts() {
        val intent = Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI)
        contactPickerLauncher.launch(intent)
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                openContacts()
            } else {
                Toast.makeText(context, "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    )

    Row(
        modifier = modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        CustomInputField(
            onValueChange = {
                phoneNumberState.text = it
                onPhoneNumberChanged(it, phoneNumberState.isValid)
            },
            value = phoneNumberState.text,
            inputType = InputType.PHONE_NUMBER,
            textFieldType = TextFieldType.OUTLINED,
            label = stringResource(id = R.string.core_designsystem_phone_number_placeholder),
            leadingIcon = {
                Icon(
                    imageVector = CrbtIcons.Phone,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface
                )
            },
            colors = OutlinedTextFieldDefaults.colors(),
            onClear = {
                phoneNumberState.text = ""
                onPhoneNumberChanged(phoneNumberState.text, phoneNumberState.isValid)
            },
            showsErrors = phoneNumberState.showErrors(),
            errorText = phoneNumberState.getError() ?: "",
            keyboardActions = KeyboardActions(
                onDone = {
                    phoneNumberState.enableShowErrors()
                    onPhoneNumberChanged(phoneNumberState.text, phoneNumberState.isValid)
                    focusManager.clearFocus()
                }
            ),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            modifier = Modifier
                .weight(1f)
                .onFocusChanged { focusState ->
                    phoneNumberState.onFocusChange(focusState.isFocused)
                    if (!focusState.isFocused) {
                        phoneNumberState.enableShowErrors()
                    }
                },
        )
        Spacer(modifier = Modifier.width(8.dp))
        IconButton(
            onClick = {
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CONTACTS)
                    == PackageManager.PERMISSION_GRANTED
                ) {
                    openContacts()
                } else {
                    launcher.launch(Manifest.permission.READ_CONTACTS)
                }
            },
            modifier = Modifier
                .size(48.dp)
                .offset(y = (-4).dp),
            colors = IconButtonDefaults.filledTonalIconButtonColors()
        ) {
            Icon(
                imageVector = CrbtIcons.Contacts,
                contentDescription = CrbtIcons.Contacts.name,
                tint = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

fun getPhoneNumberFromUri(context: Context, contactUri: Uri): String? {
    val projection = arrayOf(ContactsContract.CommonDataKinds.Phone.NUMBER)
    val cursor = context.contentResolver.query(contactUri, projection, null, null, null)
    cursor?.use {
        if (it.moveToFirst()) {
            val phoneNumberIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
            return it.getString(phoneNumberIndex)
        }
    }
    return null
}

@Preview
@Composable
fun GiftPurchasePhoneNumberPreview() {
    GiftPurchasePhoneNumber(
        onPhoneNumberChanged = { _, _ -> }
    )
}