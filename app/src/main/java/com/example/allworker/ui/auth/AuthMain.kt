package com.example.allworker.ui.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.allworker.util.TextUtil

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun AuthMain() {
    val (focusRequester) = FocusRequester.createRefs()
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        EmailField(focusRequester = focusRequester)
        PasswordField(focusRequester = focusRequester, keyboardController = keyboardController)
    }
}

@Composable
fun EmailField(
    focusRequester: FocusRequester
) {
    var email by remember { mutableStateOf("")}
    var emailValidCheck by remember { mutableStateOf(true)}
    
    TextField(
        value = email,
        onValueChange = {email = it},
        label = { Text("Email")},
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
        keyboardActions = KeyboardActions(onNext = {
            emailValidCheck = TextUtil.isValidEmail(email)

            if(emailValidCheck)
                focusRequester.requestFocus()
        })
    )
    
    if(emailValidCheck.not()) {
        Text(text = "이메일을 확인해 주세요.", color = MaterialTheme.colors.error)
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun PasswordField(
    focusRequester: FocusRequester,
    keyboardController: SoftwareKeyboardController?
) {
    var password by remember { mutableStateOf("")}

    TextField(
        value = password,
        onValueChange = {password = it},
        modifier = Modifier.focusRequester(focusRequester),
        label = { Text(text = "password")},
        visualTransformation = PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(
            onDone = { keyboardController?.hide()}
        )
    )
}