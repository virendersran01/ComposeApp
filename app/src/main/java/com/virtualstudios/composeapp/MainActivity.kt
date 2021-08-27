package com.virtualstudios.composeapp

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.virtualstudios.composeapp.ui.theme.ComposeAppTheme
import com.virtualstudios.composeapp.ui.theme.Purple200

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
//            ComposeAppTheme() {
//                Column() {
//                    MessageCard(Message("JetBrains", "Created awesome kotlin language"))
//                    Conversations(messages = sampleData())
//                }
//            }
            MyApp {
                //MyScreenContentList()
                //MyScreenContent()
                MyScreenContentWithScroll()
            }
        }
    }
}

@Composable
fun MyApp(content: @Composable () -> Unit){
    ComposeAppTheme() {
        androidx.compose.material.Surface(color = Color.LightGray) {
            content()
        }
    }
}

@Preview
@Composable
fun PreviewMyApp(){
    MyApp {
        MyScreenContentWithScroll()
    }
}

@Composable
fun DisplayTextWithClick(text: String){
    var isSelected by remember {
        mutableStateOf(false)
    }
    val backgroundColor by animateColorAsState(targetValue = if (isSelected) Color.Red else Color.Transparent)
    
    Text(text = text,
    modifier = Modifier
        .padding(24.dp)
        .background(color = backgroundColor)
        .clickable(onClick = { isSelected = !isSelected})
    )
}



@Composable
fun MyScreenContentWithScroll(){
    val names = List(1000){"Android $it"}
    val counterState = remember {
        mutableStateOf(0)
    }
    Column(Modifier.fillMaxHeight()) {
        NamesListScrolling(names = names, modifier = Modifier.weight(1f))
        Counter(count = counterState.value, updateCount = { newValue ->
            counterState.value = newValue
        })
    }
}

@Composable
fun NamesListScrolling(names: List<String>, modifier: Modifier = Modifier){
    LazyColumn(modifier){
        items(items = names){ name ->
            DisplayTextWithClick(text = name)
            Divider(color = Color.Black)
        }
    }
}

@Composable
fun NamesList(names: List<String>, modifier: Modifier = Modifier){
    Column(modifier = modifier) {
        for (name in names){
            DisplayText(text = name)
            Divider(color = Color.Black)
        }
    }
}

@Composable
fun DisplayText(text: String, textColor: Color = Color.Black){
    Text(text = text,
    modifier = Modifier.padding(8.dp),
    color = textColor)
}

@Composable()
fun MyScreenContent(){
    val names: List<String> = List(1000){"Android $it"}
    Column {
        DisplayText(text = "Android")
        Divider(color = Color.Black)
        DisplayText(text = "Android")
//        Spacer(modifier = Modifier.height(16.dp))
//        NamesList(names = names)

    }
}

@Composable
fun MyScreenContentList(
    names: List<String> = listOf("Android", "Kotlin", "Java")){
    val counterState = remember {
        mutableStateOf(0)
    }
    androidx.compose.material.Surface(color = Purple200) {
        Column (Modifier.fillMaxHeight()){
            Column(Modifier.weight(1f)) {
                for (name in names){
                    DisplayText(text = name, Color.White)
                    Divider(color = Color.Cyan)
                }
            }
            
            Counter(count = counterState.value,
                updateCount = { newValue ->
                    counterState.value = newValue
                    Log.d("TAG", "MyScreenContentList: value = $newValue")
                }
            )
            
            Spacer(modifier = Modifier.height(8.dp))
        }

    }
}

@Composable
fun Counter(count: Int, updateCount: (Int) -> Unit){
    Button(modifier = Modifier.fillMaxWidth(),
        onClick = { updateCount(count+1) },
    colors = ButtonDefaults.buttonColors(
        backgroundColor = if (count > 5) Color.Green else Color.White
    )) {
        DisplayText(text = "I' ve been clicked $count times")
    }
}

//@Composable
//fun Counter() {
//
//    val count = remember { mutableStateOf(0) }
//
//    Button(onClick = { count.value++ }) {
//        Text("I've been clicked ${count.value} times")
//    }
//}

@Composable
fun MessageCard(msg: Message){
    Row (Modifier.padding(all = 8.dp)){
        Image(painter = painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = "Profile Picture",
            Modifier
                .size(40.dp)
                .clip(CircleShape)
                .border(1.5.dp, MaterialTheme.colors.secondary, CircleShape)
        )
        
        Spacer(modifier = Modifier.width(8.dp))

        var isExpanded by remember { mutableStateOf(false) }

        val surfaceColor: Color by animateColorAsState(
            if (isExpanded) MaterialTheme.colors.primary else MaterialTheme.colors.surface,
        )

        Column(modifier = Modifier.clickable{isExpanded = !isExpanded}) {
            Text(text = msg.author,
                color = MaterialTheme.colors.secondaryVariant,
                style = MaterialTheme.typography.subtitle2
            )
            Spacer(modifier = Modifier.height(4.dp))
            androidx.compose.material.Surface(shape = MaterialTheme.shapes.medium, elevation = 1.dp,
            color = surfaceColor, modifier = Modifier
                    .animateContentSize()
                    .padding(1.dp)) {
                Text(text = msg.body,
                    maxLines = if (isExpanded) Int.MAX_VALUE else 1,
                    style = MaterialTheme.typography.body2,
                    modifier = Modifier.padding(all = 4.dp)
                )

            }

        }
    }

}

//@Preview(name = "Light Mode")
//@Preview(
//    uiMode = Configuration.UI_MODE_NIGHT_YES,
//    showBackground = true,
//    name = "Dark Mode"
//)
@Composable
fun PreviewMessageCard(){
    ComposeAppTheme() {
        MessageCard(Message("Android", "Best Mobile Operating System"))
    }

}

data class Message(val author: String, val body: String)

@Composable
fun Conversations(messages: List<Message>){
    LazyColumn{
        items(messages) { message ->
           MessageCard(msg = message)
        }
    }
}

//@Preview
@Composable
fun PreviewConversations(){
    ComposeAppTheme() {
        Conversations(messages = sampleData())
    }
}

fun sampleData() = listOf(
    Message("Android", "Best OS for mobile Devices"),
    Message("Kotlin", "Awesome language created by jetbrains, Kotlin provides better code optimizations than java as decrease code with scope functions avaiable"),
    Message("Java", "First Language used for android app development"),
    Message("Android Studio", "IDE for android app development")
)