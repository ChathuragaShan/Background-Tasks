package com.chathurangashan.backgroundtasks.ui.screen

import android.os.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.chathurangashan.backgroundtasks.data.moshi.InboxResponse
import com.chathurangashan.backgroundtasks.data.moshi.MessageContent
import com.chathurangashan.backgroundtasks.ui.theme.DescriptionTextColor
import com.chathurangashan.backgroundtasks.ui.theme.DividerColor
import com.chathurangashan.backgroundtasks.ui.theme.TitleTextColor
import com.chathurangashan.backgroundtasks.utilities.readJsonFileFromRawResource
import com.squareup.moshi.Moshi
import com.chathurangashan.backgroundtasks.ui.theme.Typography

private const val INBOX_MESSAGES_BUNDLE_KEY = "MESSAGES_KEY"

@Composable
fun ThreadExampleScreen(navController: NavController = rememberNavController()) {

    var loadingState by remember { mutableStateOf(true) }
    val inboxMessagesState = remember { mutableStateListOf<MessageContent>() }

    val context = LocalContext.current
    val jsonDataString = context.readJsonFileFromRawResource("inbox_data_response")

    val handler = object : Handler(Looper.getMainLooper()) {

        override fun handleMessage(msg: Message) {

            val inboxMessages = if (Build.VERSION.SDK_INT >= 33) {
                msg.data.getParcelableArrayList(
                    INBOX_MESSAGES_BUNDLE_KEY,
                    MessageContent::class.java
                )
            } else {
                msg.data.getParcelableArrayList(INBOX_MESSAGES_BUNDLE_KEY)
            }

            inboxMessages?.let {
                inboxMessagesState.addAll(it)
                loadingState = false
            }
        }

    }

    val prepareMessageRunnable = Runnable {

        val inboxMessages = prepareMessages(jsonDataString)

        val bundle = Bundle()
        bundle.putParcelableArrayList(INBOX_MESSAGES_BUNDLE_KEY, inboxMessages)

        val message = handler.obtainMessage()
        message.data = bundle

        handler.sendMessage(message)
    }

    LaunchedEffect(key1 = inboxMessagesState){
        val prepareMessageThread = Thread(prepareMessageRunnable)
        prepareMessageThread.start()
    }

    Box(
        modifier = Modifier
            .background(Color.White)
            .fillMaxSize(),
    ) {
        if (loadingState) {
            CircularProgressIndicator(Modifier.align(Alignment.Center))
        } else {
            LazyColumn {
                itemsIndexed(inboxMessagesState) { index, item ->
                    InboxListItem(item)
                    if (index < inboxMessagesState.lastIndex){
                        Divider(
                            modifier = Modifier.padding(horizontal = 8.dp),
                            color = DividerColor,
                            thickness = 0.5.dp
                        )
                    }
                }
            }
        }
    }
}

@Composable
@Preview
fun InboxListItem(
    message: MessageContent = MessageContent(
        1,
        "Sample Inbox message description",
        "http://sampleimage.png",
        "Sample Inbox message"
    )
) {
    Row(
        modifier = Modifier
            .background(Color.White)
            .fillMaxWidth()
            .height(100.dp)
            .padding(16.dp, 8.dp)
    )
    {
        AsyncImage(
            modifier = Modifier
                .height(80.dp)
                .width(80.dp),
            model = message.imageLink,
            contentDescription = message.title
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Text(
                text = message.title,
                style = Typography.h3,
                color = TitleTextColor
            )
            Text(
                text = message.description,
                style = Typography.body2,
                color = DescriptionTextColor
            )
        }
    }
}

fun prepareMessages(response: String): ArrayList<MessageContent> {

    val moshi = Moshi.Builder().build()
    val jsonAdapter = moshi.adapter(InboxResponse::class.java)
    val inboxResponse = jsonAdapter.fromJson(response)
    val messages = arrayListOf<MessageContent>()

    inboxResponse?.apply {

        messages.addAll(offers)
        messages.addAll(loyalty)
        return ArrayList(messages.sortedBy { it.title })

    }
    
    return messages
}
