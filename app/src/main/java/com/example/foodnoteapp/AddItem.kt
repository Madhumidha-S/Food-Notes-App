package com.example.foodnoteapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment

class AddItem : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent() {
            TextField()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextField() {
    var name by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var isExpanded by remember { mutableStateOf(false) }
    val list = listOf("Select the Category","Breakfast", "Lunch", "Dinner", "Snacks", "Drinks", "Others")
    var type by remember { mutableStateOf(list[0]) }

    val context = LocalContext.current
    val activity = context as? Activity

    Text(text = "Add Food Items",
        style = MaterialTheme.typography.headlineLarge,
        fontWeight = FontWeight.ExtraBold,
        color = Color(0xFF74C931),
        textAlign = TextAlign.Center,
        fontSize = 30.sp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp)
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
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color(0xFF74C931),
                    unfocusedBorderColor = Color(0xFF74C931),
                )
            )
            OutlinedTextField(
                modifier = Modifier.padding( bottom = 10.dp),
                value = price,
                onValueChange = { price = it },
                label = { Text("Enter Price", color = Color(0xFF74C931)) },
                colors = TextFieldDefaults.outlinedTextFieldColors(
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
                    value = type,
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded)
                    },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Color(0xFF74C931),
                        unfocusedBorderColor = Color(0xFF74C931),
                    )
                )
                ExposedDropdownMenu(
                    expanded = isExpanded,
                    onDismissRequest = { isExpanded = false },
                ) {
                    list.forEach { selectionOption ->
                        DropdownMenuItem(
                            text = { Text(selectionOption) },
                            onClick = { type = selectionOption
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
                    if (name.isNotBlank() && price.isNotBlank()) {
                        val foodItem = Food(
                            name =name,
                            price = price,
                            type = type,
                        )
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

@Composable
fun AddPreview(){
    TextField()
}


class Food(name: String, price: String, type: String) {

}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun TextFieldPreview() {
    TextField()
}