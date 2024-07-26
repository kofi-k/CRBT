package com.example.crbtjetcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.crbtjetcompose.ui.theme.CRBTJetComposeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CRBTJetComposeTheme {

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



@Preview(showBackground = true, name = "Language Selection", group = "Onboarding")
@Composable
fun ChooseLanguageScreenPreview() {
    CRBTJetComposeTheme {
        ChooseLanguageScreen()
    }
}
@Composable
fun ChooseLanguageScreen(modifier: Modifier = Modifier) {
    var selectedLanguageIndex by remember { mutableStateOf(0) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
        ) {
            ChooseLanguageCardContent(selectedLanguageIndex) { index ->
                selectedLanguageIndex = index
            }
        }
    }
}@Composable
fun ChooseLanguageCardContent(
    selectedLanguageIndex: Int,
    onLanguageSelected: (Int) -> Unit
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
            text = "Choose your language",
            color = Color.White,
            fontSize = 20.sp,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.headlineLarge
        )
        Spacer(modifier = Modifier.height(16.dp))
        LanguageDropdown(items, selectedLanguageIndex, onLanguageSelected)
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
        Text(items[selectedIndex],
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
            items.forEachIndexed {
                index, item ->
                DropdownMenuItem(
                    text = { Text(item) },
                    onClick = {
                        onItemSelected(index)
            })
        }
    }
    }
}





