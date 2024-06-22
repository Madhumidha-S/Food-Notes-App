package com.example.foodnoteapp

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.io.File
import java.io.FileOutputStream

class AddItem : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = Color.White
            ) {
                TextField(onSave = { food ->
                }
                )
            }
        }
    }
}
fun writeFoodItemToFile(context: Context, food: Food, fileName: String) {
    val file = File(context.filesDir, fileName)
    val foodItemString = "${food.name},${food.price.toString()}\n"
    FileOutputStream(file, true).bufferedWriter().use { writer ->
        writer.write(foodItemString)
    }
    println(file.canonicalPath)
}

enum class FoodType {
    Breakfast,
    Lunch,
    Dinner,
    Snacks,
    Drinks,
    Others
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextField( onSave: (Food) -> Unit) {
    val context = LocalContext.current
    val activity= context as? Activity
    var isExpanded by remember { mutableStateOf(false) }
    var name by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var type by remember { mutableStateOf(FoodType.Others) }

    Text(text = "Add Food Items",
        style = MaterialTheme.typography.headlineLarge,
        fontWeight = FontWeight.ExtraBold,
        color = Color(0xFF74C931),
        textAlign = TextAlign.Center,
        fontSize = 30.sp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 40.dp)
    )
    Box(){

        Column(
            modifier = Modifier
                .padding(PaddingValues(10.dp))
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            OutlinedTextField(
                modifier = Modifier.padding(top = 10.dp, bottom = 10.dp),
                value = name,
                onValueChange = { name = it },
                label = { Text("Enter Food Item", color = Color(0xFF74C931)) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF74C931),
                    unfocusedBorderColor = Color(0xFF74C931),
                )
            )
            OutlinedTextField(
                modifier = Modifier.padding( bottom = 10.dp),
                value = price,
                onValueChange = { price = it },
                label = { Text("Enter Price", color = Color(0xFF74C931)) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF74C931),
                    unfocusedBorderColor = Color(0xFF74C931),
                )
            )
            ExposedDropdownMenuBox(
                modifier = Modifier.padding(top = 10.dp, bottom = 10.dp),
                expanded = isExpanded,
                onExpandedChange = {newValue ->
                    isExpanded = newValue
                }
            ) {
                OutlinedTextField(
                    modifier = Modifier.menuAnchor(),
                    value = type.name,
                    onValueChange = {},
                    readOnly = true,
                    label = {Text(text = "Category", color = Color(0xFF74C931))},
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded)
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF74C931),
                        unfocusedBorderColor = Color(0xFF74C931),
                    )
                )
                ExposedDropdownMenu(
                    expanded = isExpanded,
                    onDismissRequest = { isExpanded = false }
                ) {
                    FoodType.values().forEach { selectedOption ->
                        DropdownMenuItem(
                            text = { Text(selectedOption.name) },
                            onClick = { type = selectedOption
                                isExpanded = false
                            },
                            contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                        )
                    }
                }
            }
            Button(
                colors = ButtonDefaults.buttonColors(Color(0xFF74C931)),
                modifier = Modifier.padding(top = 10.dp),
                onClick = {
                    if (name.isNotBlank() && price.isNotBlank() && type.name.isNotBlank()) {
                        val foodItem = Food(
                            name =name,
                            price = price,
                            type = type,
                        )
                        writeFoodItemToFile(context, foodItem, "Food.txt")
                        onSave(foodItem)
                    }

                    context.startActivity(Intent(context,MainActivity::class.java))
                    activity?.finish()
                }
            ) {
                Text(text = "Save", color = Color.White, fontSize = 20.sp)
            }
        }
    }
}

data class Food(
    val name:String,
    val price :String,
    val type : FoodType
)


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun TextFieldPreview() {
    TextField { food ->
    }
}