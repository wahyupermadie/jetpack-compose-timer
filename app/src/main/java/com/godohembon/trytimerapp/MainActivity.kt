package com.godohembon.trytimerapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ExperimentalComposeApi
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.godohembon.trytimerapp.ui.theme.TryTimerAppTheme
import com.godohembon.trytimerapp.utils.Helper
import com.godohembon.trytimerapp.utils.Helper.getCountDownHint
import com.godohembon.trytimerapp.utils.TimeCount

class MainActivity : AppCompatActivity() {
    private val timerViewModel by viewModels<CountTimeViewModel>()

    @ExperimentalComposeApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TryTimerAppTheme {
                // A surface container using the 'background' color from the theme
                TimerApp(timerViewModel)
            }
        }
    }
}

@Composable
@ExperimentalComposeApi
fun TimerApp(countTimeViewModel: CountTimeViewModel) {
    val progress = countTimeViewModel.progress.observeAsState()
    val curTime = countTimeViewModel.curTime.observeAsState()
    val dataChips = countTimeViewModel.timeChips.observeAsState()
    val isFinished = countTimeViewModel.isFinish.observeAsState()
    var timeCountClicked: Int? by rememberSaveable { mutableStateOf(0) }
    countTimeViewModel.setTime(300L)
    val context = LocalContext.current
    Surface(color = MaterialTheme.colors.background) {
        val typography = MaterialTheme.typography
        if (isFinished.value == true) {
            Toast.makeText(context,"Your timer already finished", Toast.LENGTH_SHORT).show()
        }
        Column(
            modifier = Modifier
                .padding()
                .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 40.dp, end = 40.dp, top = 10.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Countdown Timer App",
                    fontSize = 24.sp,
                    style = typography.h4,
                    color = Color.Blue,
                    fontStyle = FontStyle.Italic
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 2.dp, end = 2.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .height(160.dp)
                            .width(160.dp),
                        color = Color.Blue,
                        progress = progress.value!!,
                        strokeWidth = 12.dp
                    )
                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp), contentAlignment = Alignment.Center) {
                        ReusableHeaderText(
                            text = curTime.value.toString(),
                            color = Color.Blue
                        )
                    }
                }
            }
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.padding(start = 24.dp,end = 24.dp)
            ) {
                itemsIndexed(
                    items = dataChips.value!!,
                    itemContent = { index, item ->
                        BuildChip(
                            timeCount = item,
                            timeCountClicked == index,
                            index = index,
                            updated = { i ->
                                timeCountClicked = i
                                countTimeViewModel.stopCountDown()
                                countTimeViewModel.setTime(item.time.toLong())
                            }
                        )
                    }
                )
            }
            
            Button(onClick = {
                countTimeViewModel.startCountDown()
            },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 24.dp, end = 24.dp, top = 16.dp)
            ) {
                Text(text = "Start Timer")
            }
        }
    }
}

@Composable
fun BuildChip(
    timeCount: TimeCount,
    isSelected: Boolean,
    index: Int,
    updated: (Int) -> Unit
) {

    Surface(
        modifier = Modifier
            .padding(end = 8.dp, bottom = 8.dp)
            .clickable {
                updated(index)
            }
        ,
        shape = RoundedCornerShape(8.dp),
        color = when {
            isSelected -> Color.Blue
            else -> Color.White
        },
        border = BorderStroke(
            width = 1.dp,
            color = when {
                isSelected -> Color.Blue
                else -> colorResource(R.color.purple_500)
            }
        )
    ) {
        Text(
            text = timeCount.label,
            fontSize = 16.sp,
            modifier = Modifier.padding(8.dp),
            color = when {
                isSelected -> Color.White
                else -> Color.Black
            }
        )
    }
}
@Composable
fun ReusableHeaderText(text: String, color: Color) {
    Text(
        text = text,
        fontSize= 24.sp,
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.h1,
        color = color
    )
}