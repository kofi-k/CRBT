package com.example.crbtjetcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.crbtjetcompose.ui.theme.CRBTJetComposeTheme

class HomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CRBTJetComposeTheme {

            }
        }
    }
}

@Preview(showBackground = true, name = "home", group = "home")
@Composable
fun HomeScreenPreview() {
    HomeScreen(onButtonClicked = {})
}

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onButtonClicked: () -> Unit
) {
    Image(
        painter = painterResource(R.drawable.jorge),
        contentDescription = "background image",
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.Crop
    )
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "SEE WHAT'S TRENDING",
                color = Color.White,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Recommendation",
                color = Color.White,
                fontSize = 20.sp,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Playlist",
                color = Color.White,
                fontSize = 20.sp,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Caller Tunes",
                color = Color.White,
                fontSize = 20.sp,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(32.dp))  // Added space to move button closer to the center
            Button(
                onClick = { onButtonClicked() },
               // colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
            ) {
                Text(
                    text = "Explore Now",
                    color = Color.Black  // Changed text color to black for contrast
                )
            }
        }
    }
}



@Composable
fun BottomMenu(
    onHomeClicked: () -> Unit,
    onTransactionClicked: () -> Unit,
    onMoneyClicked: () -> Unit,
    onProfileClicked: () -> Unit,
    selectedMenu: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(Color(0xFFF5F5F5), shape = RoundedCornerShape(24.dp)),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onHomeClicked) {
            Icon(
                painter = painterResource(id = R.drawable.ic_home), // Replace with your home icon
                contentDescription = "Home",
                tint = if (selectedMenu == "home") Color.Black else Color.Gray
            )
        }
        IconButton(onClick = onTransactionClicked) {
            Icon(
                painter = painterResource(id = R.drawable.ic_transfer), // Replace with your transaction icon
                contentDescription = "Transactions",
                tint = if (selectedMenu == "transaction") Color.Black else Color.Gray
            )
        }
        IconButton(onClick = onMoneyClicked) {
            Icon(
                painter = painterResource(id = R.drawable.ic_subscriptions), // Replace with your money icon
                contentDescription = "Money",
                tint = if (selectedMenu == "money") Color.Black else Color.Gray
            )
        }
        IconButton(onClick = onProfileClicked) {
            Icon(
                painter = painterResource(id = R.drawable.ic_profile), // Replace with your profile icon
                contentDescription = "Profile",
                tint = if (selectedMenu == "profile") Color.Black else Color.Gray
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun HomeScreen1Preview() {
    HomeScreen1(
        profileName = "John Doe",
        onHistoryClicked = { /*TODO*/ },
        onNotificationClicked = { /*TODO*/ },
        onRefreshClicked = { /*TODO*/ },
        onPopularTodayClicked = { /*TODO*/ },
        onRecentSubscriptionClicked = { /*TODO*/ },
        onHomeClicked = { /*TODO*/ },
        onTransactionClicked = { /*TODO*/ },
        onMoneyClicked = { /*TODO*/ },
        onProfileClicked = { /*TODO*/ }
    )
}

@Composable
fun HomeScreen1(
    profileName: String,
    estBalance: String = "$0.7",
    onHistoryClicked: () -> Unit,
    onNotificationClicked: () -> Unit,
    onRefreshClicked: () -> Unit,
    onPopularTodayClicked: (String) -> Unit, // Assuming you have a function to handle clicking on a popular song
    onRecentSubscriptionClicked: (String) -> Unit, // Assuming you have a function to handle clicking on a recent subscription
    onHomeClicked: () -> Unit,
    onTransactionClicked: () -> Unit,
    onMoneyClicked: () -> Unit,
    onProfileClicked: () -> Unit
) {
    Image(
        painter = painterResource(R.drawable.jorge),
        contentDescription = "background image",
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.Crop
    )
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
                .padding(16.dp)
        ) {
            // Top bar
            Row(
                modifier = Modifier
                    .fillMaxWidth(), // Removed extra comma
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) { // Added missing curly brace
                Column {
                    Text(
                        text = "Welcome",
                        color = Color.White,
                        fontSize = 20.sp
                    )
                    Text(
                        text = profileName,
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Row {
                    IconButton(onClick = onHistoryClicked) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_history),
                            contentDescription = "History",
                            tint = Color.White
                        )
                    }
                    IconButton(onClick = onNotificationClicked) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_notification),
                            contentDescription = "Notification",
                            tint = Color.White)
                    }
                }
            } // Closing curly brace for Row

            // Estimated balance
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .background(Color.Blue, shape = RoundedCornerShape(8.dp))
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Est Balance: $estBalance",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Button(
                    onClick = onRefreshClicked,
                    modifier = Modifier
                        .background(Color.White, shape = RoundedCornerShape(8.dp))
                ) {
                    Text(
                        text = "Refresh",
                        color = Color.Blue
                    )
                }
            }

            // PAP Album
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
                    .background(Color.Gray, shape = RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                Column {
                    Text(
                        text = "PAP Album",
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                    // Include an image or album art if needed
                }
            }

            // Popular Today
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                Text(
                    text = "Popular Today",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                LazyRow(
                    modifier = Modifier.padding(top = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(listOf("Song1", "Song2", "Song3")) { song ->
                        Box(
                            modifier = Modifier
                                .size(100.dp)
                                .background(Color.DarkGray, shape = RoundedCornerShape(8.dp))
                                .clickable { onPopularTodayClicked(song) }
                        ) {
                            Text(
                                text = song,
                                color = Color.White,
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }
                    }
                }
            }

            // Recent Subscriptions
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                Text(
                    text = "Recent Subscriptions",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                LazyColumn(
                    modifier = Modifier.padding(top = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(listOf("Subscription1", "Subscription2", "Subscription3")) { subscription ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(80.dp)
                                .background(Color.DarkGray, shape = RoundedCornerShape(8.dp))
                                .clickable { onRecentSubscriptionClicked(subscription) }
                        ) {
                            Text(
                                text = subscription,
                                color = Color.White,
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }
                    }
                }
            }
        }

    }
    // Bottom Menu
}

@Preview(showBackground = true)
@Composable
fun HistoryScreenPreview() {
    HistoryScreen(
        onBackClicked ={ /*TODO*/ },
        onNotificationClicked = { /*TODO*/ },
        onPaymentsClicked = { /*TODO*/ },
        onSubscriptionsClicked = { /*TODO*/ },
        onHomeClicked = { /*TODO*/ },
        onTransactionClicked = { /*TODO*/ },
        onMoneyClicked = { /*TODO*/ },
        onProfileClicked = { /*TODO*/ }
    )
}

@Composable
fun HistoryScreen(
    onBackClicked: () -> Unit,
    onNotificationClicked: () -> Unit,
    onPaymentsClicked: () -> Unit,
    onSubscriptionsClicked: () -> Unit,
    isPaymentsSelected: Boolean = true,
    onHomeClicked: () -> Unit,
    onTransactionClicked: () -> Unit,
    onMoneyClicked: () -> Unit,
    onProfileClicked: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Top bar with back arrow, heading, and notification icon
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClicked) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_back), // Replace with your back arrow icon
                    contentDescription = "Back",
                    tint = Color.Black
                )
            }
            Text(
                text = "Account History",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            IconButton(onClick = onNotificationClicked) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_notification), // Replace with your notification icon
                    contentDescription = "Notification",
                    tint = Color.Black
                )
            }
        }

        // Tabs for Payments and Subscriptions
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(
                text = "Payments",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = if (isPaymentsSelected) Color.Black else Color.Gray,
                modifier = Modifier.clickable { onPaymentsClicked() }
            )
            Text(
                text = "Subscriptions",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = if (!isPaymentsSelected) Color.Black else Color.Gray,
                modifier = Modifier.clickable { onSubscriptionsClicked() }
            )
        }

        // Main section content
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.LightGray, shape = RoundedCornerShape(8.dp))
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "No payments made yet",
                fontSize = 16.sp,
                color = Color.DarkGray
            )
        }

        // Bottom navigation
        BottomMenu(
            onHomeClicked = onHomeClicked,
            onTransactionClicked = onTransactionClicked,
            onMoneyClicked = onMoneyClicked,
            onProfileClicked = onProfileClicked,
            selectedMenu = "home"
        )
    }
}




