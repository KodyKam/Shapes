//package com.shapes.app
//
//import android.os.Bundle
//import androidx.activity.ComponentActivity
//import androidx.activity.compose.setContent
//import androidx.activity.enableEdgeToEdge
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.padding
//import androidx.compose.material3.Scaffold
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.tooling.preview.Preview
//import com.shapes.app.ui.theme.ShapesTheme
//
//class MainActivity : ComponentActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
//        setContent {
//            ShapesTheme {
//                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
//                    Greeting(
//                        name = "Android",
//                        modifier = Modifier.padding(innerPadding)
//                    )
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun Greeting(name: String, modifier: Modifier = Modifier) {
//    Text(
//        text = "Hello $name!",
//        modifier = modifier
//    )
//}
//
//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    ShapesTheme {
//        Greeting("Android")
//    }
//}
package com.shapes.app

import android.graphics.Bitmap
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.shapes.app.drawing.DrawingViewAndroid
import com.shapes.app.ml.ShapeClassifier
import com.shapes.app.ui.theme.ShapesTheme

class MainActivity : ComponentActivity() {

    private lateinit var classifier: ShapeClassifier

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        classifier = ShapeClassifier(this)

        setContent {
            ShapesTheme {
                ShapeScreen(classifier)
            }
        }
    }
}

@Composable
fun ShapeScreen(classifier: ShapeClassifier) {
    var shapeResult by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // DrawingViewAndroid is a Compose wrapper for your existing DrawingView
        val drawingView = remember { DrawingViewAndroid() }
        drawingView.Render(modifier = Modifier.weight(1f))

        Text(text = "Recognized Shape: $shapeResult")

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = {
                // Step 1 – Wire it together
                val bitmap: Bitmap = drawingView.getBitmap()
                val result: String = classifier.classify(bitmap)
                // Step 2 – Pass result to variable
                shapeResult = result
            }) {
                Text("Recognize Shape")
            }

            Button(onClick = {
                drawingView.clear()
                shapeResult = ""
            }) {
                Text("Clear Drawing")
            }
        }
    }
}