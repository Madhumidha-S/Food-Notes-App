package com.example.foodnoteapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FoodNote()
            TextField()
        }
    }
}

@Composable
fun FAB() {

    Box(modifier = Modifier
        .fillMaxSize()
        .padding(14.dp)) {
        FloatingActionButton(
            onClick = {/*TODO*/},
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Tab(){
    Column (modifier = Modifier.padding(PaddingValues(10.dp))){
        val pagerState = rememberPagerState (
            pageCount = { 2 }
        )
        val coroutineScope = rememberCoroutineScope()
        TabRow(
            selectedTabIndex = pagerState.currentPage,
            Modifier.background(Color(0xFF74C931)),
            containerColor = Color(0xFF74C931),
            divider={},
            indicator = {tabPositions ->
                TabRowDefaults.Indicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[pagerState.currentPage]),
                    height = 2.dp,
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
                page ->
            Column (
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ){
                Text(text = "Page: $page")
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GreetingPreview() {
        FoodNote()
}

@Composable
fun FoodNote(){
    FAB()
    Tab()
}