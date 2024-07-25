package com.example.messageapp

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.messageapp.domain.models.MessageModel
import com.example.messageapp.domain.models.UserModel
import com.example.messageapp.ui.theme.MessageAppTheme
import com.example.messageapp.ui.viewmodel.MessageViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
class MainActivity : ComponentActivity() {

    private val exampleViewModel: MessageViewModel by viewModels()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MessageAppTheme {
                val navController = rememberNavController()
                NavHost(navController, startDestination = "usersList") {
                    composable("usersList") {
                        Scaffold(
                            modifier = Modifier.fillMaxSize(),
                            topBar = {
                                TopAppBar(
                                    title = { Text(text = "Users List") },
                                )
                            },
                        ) {
                            val usersList by remember { exampleViewModel.usersList }
                            UserList(
                                users = usersList.toMutableList().sortedBy { it.name },
                                navController = navController
                            )
                        }
                    }
                    composable("messages/{userId}") { backStackEntry ->
                        Scaffold(
                            modifier = Modifier.fillMaxSize(),
                            topBar = {
                                TopAppBar(
                                    title = { Text(text = "Users List") },
                                    navigationIcon = {
                                        IconButton(onClick = { navController.popBackStack() }) {
                                            Icon(
                                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                                contentDescription = "Back"
                                            )
                                        }
                                    }
                                )
                            },
                        ) {
                            val messagesList by remember { exampleViewModel.messagesList }
                            val usersList by remember { exampleViewModel.usersList }
                            MessageList(
                                modifier = Modifier.padding(it),
                                messages = messagesList,
                                users = usersList,
                                selectedUser = backStackEntry.arguments?.getString("userId")
                                    ?.toInt()
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MessageList(
    modifier: Modifier,
    messages: List<MessageModel>,
    selectedUser: Int?,
    users: List<UserModel>
) {
    LazyColumn(modifier = modifier) {
        items(messages) { message ->
            users.find { it.id == message.userId }?.name?.let {
                MessageCard(
                    message = message,
                    userName = it,
                    userId = selectedUser
                )
            }
        }
    }
}

@Composable
fun MessageCard(message: MessageModel, userId: Int?, userName: String) {
    val isSelectedUser = message.userId == userId
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalAlignment =
        if (isSelectedUser) {
            Alignment.End
        } else {
            Alignment.Start
        },
    ) {
        Card(
            modifier = Modifier.widthIn(max = 340.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (isSelectedUser) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.secondaryContainer
            ),
            shape = cardShapeFor(isSelectedUser),
        ) {
            Column {
                Text(
                    modifier = Modifier.padding(8.dp),
                    text = message.content,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    modifier = Modifier.padding(8.dp),
                    text = "Number of attachments - ${message.attachments?.size ?: 0}",
                    fontSize = 12.sp,
                    color = Color.Red
                )
                message.attachments?.let { attachments ->
                    attachments.forEach { attachment ->
                        Thumbnail(path = attachment.thumbnailUrl)
                    }
                }
            }
        }
        if (!isSelectedUser) {
            Text(
                text = userName,
                fontSize = 12.sp,
            )
        }
    }
}

@Composable
fun cardShapeFor(isSelectedUser: Boolean): Shape {
    val roundedCorners = RoundedCornerShape(16.dp)
    return if (isSelectedUser) {
        roundedCorners.copy(bottomStart = CornerSize(0))
    } else {
        roundedCorners.copy(bottomEnd = CornerSize(0))
    }
}

@Composable
fun UserList(users: List<UserModel>, navController: NavHostController) {
    LazyColumn(contentPadding = PaddingValues(16.dp)) {
        items(users) { user ->
            UserCard(
                user = user,
                onClick = { navController.navigate("messages/${user.id}") }
            )
        }
    }
}

@Composable
fun UserCard(user: UserModel, onClick: (user: UserModel) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(user) }
            .padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalAlignment = when {
            else -> Alignment.Start
        },
    ) {
        Card(
            modifier = Modifier.widthIn(max = 340.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            ),
            shape = RoundedCornerShape(16.dp)
        ) {
            Row {
                Thumbnail(path = user.avatarId)
                Text(
                    modifier = Modifier.padding(8.dp),
                    text = user.name,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun Thumbnail(path: String) {
    GlideImage(
        model = path,
        contentDescription = "Image",
        modifier = Modifier.size(40.dp)
    ) {
        it.error(Icons.Filled.Clear)
            .placeholder(R.drawable.ic_launcher_foreground)
            .load(path)
    }
}