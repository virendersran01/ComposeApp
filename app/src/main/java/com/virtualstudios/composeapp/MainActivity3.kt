package com.virtualstudios.composeapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import com.virtualstudios.composeapp.ui.theme.ComposeAppTheme

class MainActivity3 : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                  
                }
            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ComposeAppTheme {
       // ConstraintLayoutContent()
//        LargeConstraintLayoutContent()
//        DecoupledConstrainLayout()
        Surface() {
            TwoTextWithDivider(text1 = "Android", text2 = "Kotlin")
        }
    }
}

@Composable
fun ConstraintLayoutContent() {
   ConstraintLayout {
       val (button, button2, text) = createRefs()

       Button(onClick = { /*TODO*/ },
           modifier = Modifier.constrainAs(button){
               top.linkTo(parent.top, margin = 16.dp)
           }
       ) {
           Text(text = "Android")
       }

       Text(text = "Hello", modifier = Modifier.constrainAs(text) {
           top.linkTo(button.bottom, margin = 16.dp)
           bottom.linkTo(parent.bottom, margin = 16.dp)
           centerAround(button.end)
       })

       val barrier = createEndBarrier(button, text)

       Button(onClick = { /*TODO*/ },
       modifier = Modifier.constrainAs(button2){
           top.linkTo(button.top)
           start.linkTo(barrier)
       }) {
           Text(text = "Kotlin")
       }
   }
}

@Composable
fun LargeConstraintLayoutContent() {
    ConstraintLayout {
        val text = createRef()

        val guideline = createGuidelineFromStart(fraction = 0.5f)

        Text(text = "This is very very much long text in side constrain layout",
        modifier = Modifier.constrainAs(text){
           linkTo(start = guideline, end = parent.end)
            width = Dimension.preferredWrapContent
        })
    }
}


@Composable
fun DecoupledConstrainLayout() {
    BoxWithConstraints() {
        val constraints = if (maxWidth < maxHeight){
            decoupledConstraints(16.dp)
        }else{
            decoupledConstraints(32.dp)
        }
        
        ConstraintLayout(constraints) {
            Button(onClick = { /*TODO*/ },
            modifier = Modifier.layoutId("button")) {
                Text(text = "Button")
            }

            Text(text = "Text", modifier = Modifier.layoutId("text"))
        }
    }
}

private fun decoupledConstraints(margin: Dp): ConstraintSet{
    return ConstraintSet {
        val button = createRefFor("button")
        val text = createRefFor("text")

        constrain(button){
            top.linkTo(parent.top, margin = margin)
        }

        constrain(text){
            top.linkTo(button.bottom, margin = margin)
        }
    }
}

@Composable
fun TwoTextWithDivider(modifier: Modifier = Modifier, text1: String, text2: String) {
    Row(modifier = modifier.height(IntrinsicSize.Min)) {
        Text(text = text1,
            modifier = Modifier
                .weight(1f)
                .wrapContentWidth(Alignment.Start)
                .padding(start = 4.dp)

        )

        Divider(color = Color.Black, modifier = Modifier
            .width(1.dp)
            .fillMaxHeight())

        Text(text = text2,
            modifier = Modifier
                .weight(1f)
                .wrapContentWidth(Alignment.End)
                .padding(end = 8.dp)

        )

    }
}