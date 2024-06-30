package com.example.foodnoteapp

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.outlined.Create
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import java.io.File

class MainActivity : ComponentActivity() {
    private val fileName = "Food-Data.txt"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val context = LocalContext.current
            val foodItemsState = remember { mutableStateOf<List<FoodItem>>(emptyList()) }

            if (!fileExist(context, fileName)) {
                fileCreate(context, fileName)
            }
            val foodItems = remember { mutableStateOf(fileRead(this, fileName)) }
            Tab(foodItems.value, onAddItem = { newItem ->
                val updatedItems = foodItems.value + newItem
                foodItems.value = updatedItems
                fileWrite(this, updatedItems, fileName)
            })

        }
    }
}

private fun fileRead(context: Context, fileName: String): List<FoodItem> {
    val file = File(context.filesDir, fileName)
    val foodItems = mutableListOf<FoodItem>()
    file.bufferedReader().useLines { lines ->
        lines.forEach { line ->
            val parts = line.split(",")
            if (parts.size == 2) {
                val name = parts[0]
                val price = parts[1].toIntOrNull() ?: 0
                foodItems.add(FoodItem(name, price))
            }
        }
    }
    return foodItems
}
data class FoodItem(val name: String, val price: Int, val type: String = "")

private fun fileExist(context: Context, fileName: String): Boolean {
    val file = File(context.filesDir, fileName)
    return file.exists()
}

private fun fileCreate(context: Context, fileName: String) {
    val file = File(context.filesDir, fileName)
    file.createNewFile()
}
private fun fileWrite(context: Context, foodItems: List<FoodItem>, fileName: String) {
    val file = File(context.filesDir, fileName)
    file.bufferedWriter().use { writer ->
        foodItems.forEach { item ->
            writer.write("${item.name},${item.price}\n")
        }
    }
}

@Composable
fun FoodList(item: FoodItem, isChecked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Surface(
        color = Color.White,
        border = BorderStroke(2.dp, Color(0xFF74C931)),
        modifier = Modifier.padding(vertical = 10.dp, horizontal = 20.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = isChecked,
                colors = CheckboxDefaults.colors(checkedColor = Color.Black),
                onCheckedChange = onCheckedChange
            )
            Column(
                modifier = Modifier
                    .padding(horizontal = 10.dp, vertical = 15.dp)
                    .fillMaxWidth()
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(start = 5.dp)) {
                        Text(
                            text = item.name,
                            color = Color.Black,
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontSize = 25.sp
                            )
                        )

                    }
                    Column {
                        Text(
                            modifier = Modifier.padding(end = 10.dp),
                            text = "₹" + item.price.toString(),
                            color = Color.Black,
                            style = MaterialTheme.typography.headlineMedium.copy(
                                 fontSize = 25.sp
                            )
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun RecyclerView(names: List<FoodItem>, selectedItems: Set<FoodItem>, onItemCheckedChange: (FoodItem, Boolean) -> Unit) {
    LazyColumn(modifier = Modifier.padding(vertical = 4.dp), verticalArrangement = Arrangement.Top) {
        items(items = names) { item ->
            FoodList(
                item = item,
                isChecked = selectedItems.contains(item),
                onCheckedChange = { isChecked ->
                    onItemCheckedChange(item, isChecked)
                }
            )
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Tab(foodItems: List<FoodItem>, onAddItem: (FoodItem) -> Unit    ){
    val context= LocalContext.current
    val activity=context as? Activity
    Column (){
        val pagerState = rememberPagerState (
            pageCount = { 2 }
        )
        val coroutineScope = rememberCoroutineScope()
        Box(
            modifier = Modifier
                .background(color = Color(0xFF181818))
                .fillMaxWidth(),
        ){
            Icon(imageVector = Icons.Outlined.Create,
                contentDescription = null,
                modifier = Modifier.padding(start = 15.dp).align(Alignment.CenterStart).size(25.dp), tint = Color.White)
            Text(
                text = "Billify",
                modifier = Modifier.padding(top = 15.dp, bottom = 15.dp, start = 55.dp),
                fontSize = 25.sp,
                color = Color.White
            )
            Icon(imageVector = Icons.Filled.List,
                contentDescription = null,
                modifier = Modifier.padding(end = 15.dp).align(Alignment.CenterEnd).size(30.dp), tint = Color.White)
        }
        TabRow(
            selectedTabIndex = pagerState.currentPage,
            Modifier.background(Color(0xFF74C931)),
            containerColor = Color(0xFF74C931),
            divider={},
            indicator = {tabPositions ->
                TabRowDefaults.Indicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[pagerState.currentPage]),
                    height = 2.dp,
                    color = Color(0xFF74C931)
                )
            }
        ) {
            androidx.compose.material3.Tab(
                modifier= Modifier.background(
                    if (pagerState.currentPage == 0){
                        Color.White
                    }
                    else {
                        Color(0xFF74C931)
                    }
                ),
                selected = pagerState.currentPage ==0,
                text = {
                    Text(text = "Food", fontSize = 25.sp)
                },
                onClick = {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(0)
                    }
                },
                selectedContentColor = Color(0xFF74C931),
                unselectedContentColor = Color.White
            )
            androidx.compose.material3.Tab(
                modifier= Modifier.background(
                    if (pagerState.currentPage == 1){
                        Color.White
                    }
                    else {
                        Color(0xFF74C931)
                    }
                ),
                selected = pagerState.currentPage ==1,
                text = {
                    Text(text = "Fun", fontSize = 25.sp,)
                },
                onClick = {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(1);
                    }
                },
                selectedContentColor = Color(0xFF74C931),
                unselectedContentColor = Color.White
            )
        }
        HorizontalPager(
            state = pagerState,
            userScrollEnabled = false
        ) {
            if (pagerState.currentPage==0){
                FoodDisplay(foodItems,onAddItem)
            }
        }
    }
}

@Composable
fun FoodDisplay(foodItems: List<FoodItem>, onAddItem: (FoodItem) -> Unit){
    val context = LocalContext.current
    val activity=context as? Activity
    var selectedItems by remember { mutableStateOf(setOf<FoodItem>()) }
    val totalPrice by remember { derivedStateOf { selectedItems.sumOf { it.price } } }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start,
    ) {
        Text(
            text = "Total Price: ₹ $totalPrice",
            modifier = Modifier.padding(top = 30.dp, bottom = 10.dp, start = 25.dp),
            fontSize = 20.sp,
            color = Color.Black
        )
        RecyclerView(
            names = foodItems,
            selectedItems = selectedItems,
            onItemCheckedChange = { item, isChecked ->
                selectedItems = if (isChecked) {
                    selectedItems + item
                } else {
                    selectedItems - item
                }
            }
        )
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(14.dp)) {
            FloatingActionButton(
                onClick = {
                    context.startActivity(Intent(context,AddItem::class.java))
                    activity?.finish()
                },
                Modifier
                    .background(Color(0xFF74C931), CircleShape,)
                    .align(Alignment.BottomEnd)
                    .padding(16.dp),
                contentColor = Color(0xFF74C931),
                shape = CircleShape,
                containerColor = Color.White,
            ) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = null)
            }
        }

    }
}


@Preview(showBackground = true,showSystemUi = true)
@Composable
fun GreetingPreview() {
    Tab(foodItems = emptyList(), onAddItem = {})
}