@file:Suppress("OPT_IN_IS_NOT_ENABLED")

package com.example.allworker.ui.auth

import android.content.Context
import android.util.Log
import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.allworker.util.TextUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun AuthMain(authModule: FirebaseAuth) {
    val (focusRequester) = FocusRequester.createRefs()
    val keyboardController = LocalSoftwareKeyboardController.current
    val context = LocalContext.current

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        EmailField(
            email = email,
            onEmailChange = { email = it },
            focusRequester = focusRequester
        )
        PasswordField(
            password = password,
            onPassWordChange = { password = it },
            focusRequester = focusRequester,
            keyboardController = keyboardController
        )

        Button(onClick = {
            Toast.makeText(context, "$email , $password", Toast.LENGTH_SHORT).show()
//            signInUser(authModule, email, password, context)
            Firebase.auth.signOut()
            /**
             * 한번 로그인을 하면 signout 전까지 로그인 정보를 가지고 있음
             * 따라서 로그인의 활동을 확인 하는 분기 지점에서 정보를 활용 하는 것이 중요해 보임
             *
             * 로그인 상태 -> email 형태의 데이터
             * 로그아웃 상태 -> null
             * */
            Log.d("login_test", "${authModule.currentUser}")
        }) {
            Text("로그인하기")
        }
    }
}

private fun newUser(authModule: FirebaseAuth, id: String, pw: String, context: Context) {
    authModule.createUserWithEmailAndPassword(id, pw).addOnCompleteListener { task ->
        if (task.isSuccessful) {
            Toast.makeText(context, "생성 완료", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "생성 실패", Toast.LENGTH_SHORT).show()
        }
    }
}

private fun signInUser(authModule: FirebaseAuth, id: String, pw: String, context: Context) {
    authModule.signInWithEmailAndPassword(id, pw).addOnCompleteListener { task ->
        if (task.isSuccessful) {
            Toast.makeText(context, "로그인 완료", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "로그인 실패", Toast.LENGTH_SHORT).show()
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Suppress("UNUSED_EXPRESSION", "EXPERIMENTAL_IS_NOT_ENABLED")
@Composable
fun EmailField(
    email: String,
    onEmailChange: (String) -> Unit,
    focusRequester: FocusRequester
) {

    var emailValidCheck by remember { mutableStateOf(true) }
    val (localFocusRequester) = FocusRequester.createRefs()

    TextField(
        modifier = Modifier
            .focusRequester(localFocusRequester)
            .fillMaxWidth(0.8f),
        value = email,
        onValueChange = { onEmailChange(it) },
        label = { Text("Email") },
        placeholder = { Text("example@gmail.com") },
        singleLine = true,
        trailingIcon = {
            if (email.isBlank().not())
                Icon(
                    imageVector = Icons.Default.Clear,
                    contentDescription = "clear text",
                    modifier = Modifier
                        .size(12.dp)
                        .clickable {
                            onEmailChange("")
                            localFocusRequester.requestFocus()
                        }
                )
            else
                null
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next
        ),
        keyboardActions = KeyboardActions(onNext = {
            emailValidCheck = TextUtil.isValidEmail(email)

            if (emailValidCheck)
                focusRequester.requestFocus()
        })
    )

    if (emailValidCheck.not()) {
        Text(text = "이메일을 확인해 주세요.", color = MaterialTheme.colors.error)
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun PasswordField(
    password: String,
    onPassWordChange: (String) -> Unit,
    focusRequester: FocusRequester,
    keyboardController: SoftwareKeyboardController?
) {
    var isValidPassword by remember { mutableStateOf(true) }

    TextField(
        value = password,
        singleLine = true,
        placeholder = { Text(text = "영문자, 숫자 조합 6자리 이상 13자리 이하", fontSize = 10.sp) },
        onValueChange = { onPassWordChange(it) },
        modifier = Modifier
            .focusRequester(focusRequester)
            .fillMaxWidth(0.8f),
        label = { Text(text = "password") },
        visualTransformation = PasswordVisualTransformation(),
        trailingIcon = {
            if (password.isBlank().not())
                Icon(
                    imageVector = Icons.Default.Clear,
                    contentDescription = "clear text",
                    modifier = Modifier
                        .size(12.dp)
                        .clickable {
                            onPassWordChange("")
                            focusRequester.requestFocus()
                            keyboardController?.show()
                        }
                )
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                isValidPassword = TextUtil.isValidPassword(password)

                if (isValidPassword) {
                    keyboardController?.hide()
                } else {
                    onPassWordChange("")
                }
            }
        )
    )

    if (isValidPassword.not())
        Text(
            text = "비밀번호는 영문 소문자 숫자를 혼합하여 6자리 이상 13자리 이하여야 합니다.",
            color = MaterialTheme.colors.error
        )
    else
        Text(text = "통과하였습니다.")
}