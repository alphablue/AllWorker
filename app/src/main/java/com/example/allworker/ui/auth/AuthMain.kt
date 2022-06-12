@file:Suppress("OPT_IN_IS_NOT_ENABLED")

package com.example.allworker.ui.auth

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.allworker.util.TextUtil

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun AuthMain() {
    val (focusRequester) = FocusRequester.createRefs()
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        EmailField(focusRequester = focusRequester)
        PasswordField(focusRequester = focusRequester, keyboardController = keyboardController)
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Suppress("UNUSED_EXPRESSION", "EXPERIMENTAL_IS_NOT_ENABLED")
@Composable
fun EmailField(
    focusRequester: FocusRequester
) {
    var email by remember { mutableStateOf("")}
    var emailValidCheck by remember { mutableStateOf(true)}
    val (localFocusRequester) = FocusRequester.createRefs()

    TextField(
        modifier = Modifier
            .focusRequester(localFocusRequester)
            .fillMaxWidth(0.8f),
        value = email,
        onValueChange = {email = it},
        label = { Text("Email")},
        placeholder = { Text("example@gmail.com") },
        singleLine = true,
        trailingIcon = {
            if(email.isBlank().not())
                Icon(
                    imageVector = Icons.Default.Clear,
                    contentDescription = "clear text",
                    modifier = Modifier
                        .size(12.dp)
                        .clickable {
                            email = ""
                            localFocusRequester.requestFocus()
                        }
                )
            else
                null
                       },
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
    var isValidPassword by remember { mutableStateOf(true)}

    TextField(
        value = password,
        singleLine = true,
        placeholder = { Text(text = "영문자, 숫자 조합 6자리 이상 13자리 이하", fontSize = 10.sp)},
        onValueChange = {password = it},
        modifier = Modifier
            .focusRequester(focusRequester)
            .fillMaxWidth(0.8f),
        label = { Text(text = "password")},
        visualTransformation = PasswordVisualTransformation(),
        trailingIcon = {
            if(password.isBlank().not())
                Icon(
                    imageVector = Icons.Default.Clear,
                    contentDescription = "clear text",
                    modifier = Modifier
                        .size(12.dp)
                        .clickable {
                            password = ""
                            focusRequester.requestFocus()
                            keyboardController?.show()
                        }
                )
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(
            onDone = {
                isValidPassword = TextUtil.isValidPassword(password)

                if (isValidPassword) {
                    keyboardController?.hide()
                }
                else {
                    password = ""
                }
            }
        )
    )

    if(isValidPassword.not())
        Text(
            text = "비밀번호는 영문 소문자 숫자를 혼합하여 6자리 이상 13자리 이하여야 합니다.",
            color = MaterialTheme.colors.error
        )
    else
        Text(text = "통과하였습니다.")
}