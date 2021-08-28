package com.virtualstudios.composeapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.google.android.material.chip.Chip
import com.virtualstudios.composeapp.ui.theme.ComposeAppTheme
import kotlinx.coroutines.launch
import kotlin.math.max


class MainActivity2 : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            App {
                ScaffoldApp()
            }
        }
    }
}

@Composable
fun App(content: @Composable () -> Unit) {
    ComposeAppTheme() {
        content()
    }
}

@Preview
@Composable
fun PreviewApp() {
    App {
        ScaffoldApp()
    }
}

@OptIn(ExperimentalCoilApi::class)
@Composable
fun ScaffoldApp() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Compose")
                },
                actions = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(Icons.Filled.Favorite, contentDescription = "Fav")
                    }
                }
            )
        }
    ) { innerPadding ->
     AppContent(
         Modifier
             .padding(innerPadding)
             .padding(8.dp))
    }
}

@ExperimentalCoilApi
@Composable
fun AppContent(modifier: Modifier = Modifier) {
//    Column(modifier = modifier.padding(16.dp)) {
//        DisplayText(text = "Android")
//        DisplayText(text = "Kotlin")
//        DisplayText(text = "Compose UI is Awesome")
//    }
    //SimpleList()
    //LazyList()
   // ScrollingList()
//    MyOwnColumn(modifier = modifier.padding(8.dp)) {
//        Text(text = "Android")
//        Text(text = "Kotlin")
//        Text(text = "Compose is awesome")
//    }

    StaggeredGridViewScrollable(modifier = modifier)
}

@Composable
fun DisplayText(text: String) {
    Text(
        text = text
    )
}

@Composable
fun SimpleList() {
    val scrollState = rememberScrollState()
    Column(Modifier.verticalScroll(scrollState)) {
        repeat(100){
            DisplayText(text = "Android $it")
        }
    }
}

@ExperimentalCoilApi
@Composable
fun LazyList() {
    val scrollState = rememberLazyListState()
    LazyColumn(state = scrollState){
        items(100){
          //  DisplayText(text = "Android $it")
            ImageListItem(index = it)
        }
    }
}

@ExperimentalCoilApi
@Composable
fun ImageListItem(index: Int) {
    Row(verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Image(
            painter = rememberImagePainter(
                data = "https://developer.android.com/images/brand/Android_Robot.png"
        ),
            contentDescription = "Android Logo",
            modifier = Modifier.size(50.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(text = "Item #$index", style = MaterialTheme.typography.subtitle1)
    }
}

@Composable
fun ScrollingList() {
    val listSize = 100
    val scrollState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    Column() {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(8.dp)) {
            Button(onClick = {
                coroutineScope.launch {
                    scrollState.animateScrollToItem(0)
                }
            },
                modifier = Modifier.weight(1f)
            ) {
                Text(text = "Scroll To Top")
            }

            Spacer(modifier = Modifier.width(8.dp))
            
            Button(onClick = { 
                coroutineScope.launch { 
                    scrollState.animateScrollToItem(listSize -1)
                }
            },
                modifier = Modifier.weight(1f)
            ) {
                Text(text = "Scroll To Bottom")
            }

        }

        LazyColumn(state = scrollState){
            items(100){
                ImageListItem(index = it)
            }
        }

    }

}

@Composable
fun MyOwnColumn(
    modifier: Modifier,
    content: @Composable () -> Unit
) {
    Layout(
        modifier = modifier,
        content = content
    ){ measurables, constraints ->
        val placeables = measurables.map { measurable ->
            measurable.measure(constraints)
        }

        var yPosition = 0

        layout(constraints.maxWidth, constraints.maxHeight){
            placeables.forEach{ placeable ->  
                placeable.placeRelative(x=0, y= yPosition)
                yPosition += placeable.height
            }
        }
    }
}

@Composable
fun StaggeredGrid(
    modifier: Modifier = Modifier,
    rows: Int = 3,
    content: @Composable () -> Unit
) {
    
    Layout(
        modifier = modifier,
        content = content
    ){ measurables, constraints ->

        val rowWidths = IntArray(rows){ 0 }
        val rowHeights = IntArray(rows){ 0 }

        val placeables = measurables.mapIndexed{index, measurable ->
            val placeable = measurable.measure(constraints = constraints)

            val row = index % rows
            rowWidths[row] += placeable.width
            rowHeights[row] = max(rowHeights[row], placeable.height)

            placeable
        }

        val width = rowWidths.maxOrNull()
            ?.coerceIn(constraints.minWidth.rangeTo(constraints.maxWidth)) ?: constraints.minWidth

        val height = rowHeights.sumOf { it }
            .coerceIn(constraints.minHeight.rangeTo(constraints.maxHeight))

        val rowY = IntArray(rows){ 0 }
        for (i in 1 until rows){
            rowY[i] = rowY[i-1] + rowHeights[i-1]
        }

        layout(width = width, height = height){
            val rowX = IntArray(rows){ 0 }
            placeables.forEachIndexed{index, placeable ->
                val row = index % rows
                placeable.placeRelative(
                    x = rowX[row],
                    y = rowY[row]
                )
                rowX[row] += placeable.width
            }
        }
    }
}

@Composable
fun Chip(modifier: Modifier = Modifier, text: String) {
    Card(
        modifier = modifier,
        border = BorderStroke(color = Color.Black, width = Dp.Hairline),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier.padding(start = 8.dp, top = 4.dp, end = 8.dp, bottom = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier
                .size(16.dp, 16.dp)
                .background(color = MaterialTheme.colors.secondary))
            Spacer(Modifier.width(4.dp))
            Text(text = text)
        }
    }
}

@Preview
@Composable
fun PreviewChip() {
    ComposeAppTheme() {
        Chip(text = "hi there")
    }

}

val topics = listOf(
    "Arts & Crafts", "Beauty", "Books", "Business", "Comics", "Culinary",
    "Design", "Fashion", "Film", "History", "Maths", "Music", "People", "Philosophy",
    "Religion", "Social sciences", "Technology", "TV", "Writing"
)

@Composable
fun StaggeredGridView(modifier: Modifier = Modifier) {
    StaggeredGrid(modifier = modifier, rows = 5) {
        for (topic in topics) {
            Chip(modifier = Modifier.padding(8.dp), text = topic)
        }
    }
}

@Composable
fun StaggeredGridViewScrollable(modifier: Modifier = Modifier) {
    Row(modifier = modifier.horizontalScroll(rememberScrollState())) {
        StaggeredGrid{
            for (topic in topics) {
                Chip(modifier = Modifier.padding(8.dp), text = topic)
            }
        }
    }

}

@Preview
@Composable
fun StaggeredGridViewPreview() {
  ComposeAppTheme() {
      StaggeredGridView()
    }
}