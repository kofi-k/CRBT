package com.example.crbtjetcompose

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.crbtjetcompose.data.ProfileData
import androidx.navigation.compose.rememberNavController
import com.example.crbtjetcompose.ui.theme.CRBTJetComposeTheme

class OnBoardingActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CRBTJetComposeTheme {
                val navController = rememberNavController()
                NavHost(navController, startDestination = "onboarding") {
                    composable("onboarding") { OnboardingScreen(onButtonClicked = { navController.navigate("chooseLanguage") }) }
                    composable("chooseLanguage") { ChooseLanguageScreen(navController = navController, onContinueClicked = { navController.navigate("signup") }) }
                    composable("signup") { SignupScreen(navController = navController)}
                    composable("authentication") { AuthenticationScreen(navController = navController, onContinueClicked = { navController.navigate("profile") }) }
                    composable("profile") { ProfileScreen(navController = navController, onProfileSaved = { /* handle profile saved */ }) }
                }
        }
    }
}

@Preview(showBackground = true, name = "Onboarding", group = "Onboarding")
@Composable
fun OnboardingScreenPreview() {
    OnboardingScreen(onButtonClicked = {})
}

@Composable
fun OnboardingScreen(
    modifier: Modifier = Modifier,
    onButtonClicked: () -> Unit){
    Image(
        painter = painterResource(R.drawable.onboardingbackground),
        contentDescription = "background image",
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.Crop
    )
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ){
        Column(
            horizontalAlignment =Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.crbtlogo),
                contentDescription = "logo",
                modifier = Modifier.size(120.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Welcome to our app!",
                color = Color.White,
                fontSize = 20.sp,
                textAlign = TextAlign.Center
            )
        }
        Button(
            onClick = {onButtonClicked()},
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 32.dp)
        ) {
            Text("Continue")
        }
    }
}

@Composable
fun ChooseLanguageScreen(
        modifier: Modifier = Modifier,
        onContinueClicked: () -> Unit,
        navController: NavController
    )
    {
        var selectedLanguageIndex by remember { mutableStateOf(0) }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Transparent)) {
            Image(
                painter = painterResource(id = R.drawable.onboardingbackground),
                contentDescription = "background image",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .align(Alignment.BottomCenter)
                    .height(250.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Black)
            ) {
                ChooseLanguageCardContent(
                    selectedLanguageIndex = selectedLanguageIndex,
                    onLanguageSelected = { index -> selectedLanguageIndex = index },
                    onNextClicked = {
                        navController.navigate("signup")
                        onContinueClicked()
                    }
                )
            }
        }
    }

@Composable
fun ChooseLanguageCardContent(
        selectedLanguageIndex: Int,
        onLanguageSelected: (Int) -> Unit,
        onNextClicked: () -> Unit
) {
        val items = listOf("English", "Spanish", "French")
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Choose your preferred language",
                color = Color.White,
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.headlineLarge
            )
            Spacer(modifier = Modifier.height(16.dp))
            LanguageDropdown(items, selectedLanguageIndex, onLanguageSelected)

            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onNextClicked) {
                Text("Next")
            }
        }
    }


@Composable
fun LanguageDropdown(
    items: List<String>,
    selectedIndex: Int,
    onItemSelected: (Int) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    Box {
        Text(
            items[selectedIndex],
            modifier = Modifier
                .clickable { expanded = true }
                .border(
                    width = 1.dp,
                    color = Color.White,
                    shape = RoundedCornerShape(4.dp)
                )
                .padding(8.dp),
            color = Color.White
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            items.forEachIndexed { index, item ->
                DropdownMenuItem(
                    text = { Text(item) },
                    onClick = {
                        onItemSelected(index)
                        expanded = false
                    }
                )
            }
        }
    }
}
/**
@Preview(showBackground = true)
@Composable
fun SignupScreenPreview() {
    SignupScreen(onContinueClicked = {})
}

**/

@Composable
fun SignupScreen(
    modifier: Modifier = Modifier,
    navController: NavController // Added back navController parameter

) {
    var phoneNumber by remember { mutableStateOf("") }

    Box(
        modifier = modifier.fillMaxSize().background(Color.Transparent)
    ) {
        Image(
            painter = painterResource(id = R.drawable.onboardingbackground),
            contentDescription = "background image",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .align(Alignment.BottomCenter)
                .height(300.dp),
            colors = CardDefaults.cardColors(containerColor = Color.Black)
        ) {
            SignupCardContent(
                phoneNumber = phoneNumber,
                onPhoneNumberChange = { phoneNumber = it },
                onContinue = {
                    if (phoneNumber.length == 9) {
                        navController.navigate("authentication")
                    }
                }
            )// Call onSignupSuccess from SignupCardContent
        }
    }
}

//private val PHONE_NUMBER_PATTERN = PatternValidator(pattern = "^[\\+]?[(]?[0-9]{3}[)]?[\\s-]?[0-9]{3}[\\s-]?[0-9]{4}$")

/** private fun sendVerificationCode(
    phoneNumber: String,
    context: Context,
    auth: FirebaseAuth,
    scope: CoroutineScope,
    onVerificationCompleted: (String) -> Unit
) {
    val options = PhoneAuthOptions.newBuilder()
        .setPhoneNumber(phoneNumber)
        .setTimeout(60L, TimeUnit.SECONDS)
        .setActivity(context as Activity) // Cast context to Activity for verification callbacks
        .setActivityCodeResultListener { _, result ->
            val verificationId = result.verificationId
            onVerificationCompleted(verificationId ?: "") // Pass verificationId if successful
        }
        .build()

    scope.launch {
        try {
            PhoneAuthProvider.getInstance(auth).verifyPhoneNumber(options)
        } catch (e: Exception) {
            // Handle verification errors (e.g., network issue, invalid number)
            Toast.makeText(context, "Verification failed: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }
}
**/

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignupCardContent( phoneNumber: String,
                       onPhoneNumberChange: (String) -> Unit,
                       onContinue: () -> Unit ) {
    val countries = listOf("Ethiopia", "Kenya", "Uganda")
    var selectedCountryIndex by remember { mutableStateOf(0) }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Welcome to CRBT",
            color = Color.White,
            fontSize = 20.sp,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.headlineLarge
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Enter your details to sign up",
            color = Color.White,
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(16.dp))
        CountryDropdown(countries, selectedCountryIndex) { index ->
            selectedCountryIndex = index
        }
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = phoneNumber,
            onValueChange = {
                if (it.length <= 9) onPhoneNumberChange(it) // Limit to 9 digits
            },
            label = { Text("Phone Number", color = Color.White) },
            textStyle = TextStyle(color = Color.White),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Phone,
                imeAction = ImeAction.Done // Set IME action to Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    if (phoneNumber.length == 9) { // Check if 9 digits entered
                        onContinue() // Call onContinue lambda
                    }
                }
            )
        )
        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "By signing up, you agree to our Privacy Policy and Terms of Service.",
            color = Color.White,
            fontSize = 12.sp,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodySmall
        )
        Button(
            onClick = {
                if (phoneNumber.length == 9) {
                    onContinue() // Call onContinue for signup logic

                }
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Next")
        }
    }
}

@Composable
fun CountryDropdown(
    items: List<String>,
    selectedIndex: Int,
    onItemSelected: (Int) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    Box {
        Text(
            items[selectedIndex],
            modifier = Modifier
                .clickable { expanded = true }
                .border(
                    width = 1.dp,
                    color = Color.White,
                    shape = RoundedCornerShape(4.dp)
                )
                .padding(8.dp),
            color = Color.White
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            items.forEachIndexed { index, item ->
                DropdownMenuItem(
                    text = { Text(item) },
                    onClick = {
                        onItemSelected(index)
                        expanded = false
                    }
                )
            }
        }
    }
}
/**
@Preview(showBackground = true)
@Composable
fun AuthenticationScreenPreview() {
    AuthenticationScreen(onContinueClicked = {})
}
**/
@Composable
fun AuthenticationScreen(modifier: Modifier = Modifier, onContinueClicked: () -> Unit,
                         navController: NavController // Added navController as a parameter
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent)
    ) {
        Image(
            painter = painterResource(id = R.drawable.onboardingbackground),
            contentDescription = "background image",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .align(Alignment.BottomCenter)
                .height(300.dp),
            colors = CardDefaults.cardColors(containerColor = Color.Black)
        ) {
            AuthenticationCardContent(onContinue = {onContinueClicked()
                                        navController.navigate("profile")})
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { onContinueClicked() },
            ) {
                Text("Continue")
            }
        }
    }
}

@Composable
fun AuthenticationCardContent(onContinue: () -> Unit){

    var code by remember { mutableStateOf("") }
    val validCode = "5544" // Replace with desired valid code


    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Enter the code sent to your phone",
            color = Color.White,
            fontSize = 20.sp,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.headlineLarge
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = code,
            onValueChange = { if (it.length <= 4) code = it }, // Limit to 4 digits
            label = { Text("Code", color = Color.White) },
            textStyle = TextStyle(color =Color.White),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number, // Show numeric keyboard
                imeAction = ImeAction.Done // Set IME action to "Done"
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    if (code.length == 4) { // Check if 4 digits entered
                        onContinue() // Call onContinue lambda
                    }
                }
            )
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Request new code",
                color = Color.White,
                fontSize = 12.sp,
                textAlign = TextAlign.Start,
                modifier = Modifier.clickable { /* handle request new code */ }
            )
            Text(
                text = "Help",
                color = Color.White,
                fontSize = 12.sp,
                textAlign = TextAlign.End,
                modifier = Modifier.clickable { /* handle help */ }
            )
            Button(
                onClick = {
                    if (code == validCode) {
                        onContinue()
                    } else {
                        // Handle incorrect code (e.g., show error message)
                    }
                }
            ) {
                Text("Continue")
            }
        }
    }
}
@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    val navController = rememberNavController()
    ProfileScreen(
        onProfileSaved = {},
        navController = navController
    )
}




@Composable
fun ProfileScreen(modifier: Modifier = Modifier, onProfileSaved: (ProfileData) -> Unit,
                  navController: NavController // Added navController parameter
) {
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var receiveEmails by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .align(Alignment.Center)
                .height(450.dp), // Increased height to accommodate email field
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // ... other composables (firstName, lastName)

                Text(
                    text = "Receive updates and recommendations on your playlist",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyMedium
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = receiveEmails,
                        onCheckedChange = { receiveEmails = it }
                    )
                    Text("Receive emails")
                }
                if (receiveEmails) {
                    Spacer(modifier = Modifier.height(8.dp)) // Add some space
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        val profileData = ProfileData(firstName, lastName, email, receiveEmails)
                        onProfileSaved(profileData) // Call onProfileSaved with the data
                        navController.navigate("home") // Navigate to "home" screen
                    },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Save Profile")
                }
            }
        }
    }
    }
}

