package com.github.sagar2093.bmicompose.ui

import androidx.compose.*
import androidx.ui.core.Alignment
import androidx.ui.core.Modifier
import androidx.ui.foundation.Text
import androidx.ui.graphics.Color
import androidx.ui.layout.*
import androidx.ui.layout.ColumnScope.gravity
import androidx.ui.material.Scaffold
import androidx.ui.material.ScaffoldState
import androidx.ui.material.Slider
import androidx.ui.material.icons.Icons
import androidx.ui.material.icons.filled.Add
import androidx.ui.material.icons.filled.Remove
import androidx.ui.material.icons.outlined.Notifications
import androidx.ui.material.icons.outlined.Person
import androidx.ui.res.stringResource
import androidx.ui.text.SpanStyle
import androidx.ui.text.TextStyle
import androidx.ui.text.annotatedString
import androidx.ui.text.withStyle
import androidx.ui.tooling.preview.Preview
import androidx.ui.unit.TextUnit
import androidx.ui.unit.dp
import com.github.sagar2093.bmicompose.ui.widgets.RoundIconButton
import com.github.sagar2093.bmicalculator.ui.widgets.Toolbar
import com.github.sagar2093.bmicompose.R
import com.github.sagar2093.bmicompose.Screen
import com.github.sagar2093.bmicompose.navigateTo
import com.github.sagar2093.bmicompose.theme.AppTheme
import com.github.sagar2093.bmicompose.theme.accentColor
import com.github.sagar2093.bmicompose.ui.widgets.RoundedButton
import com.github.sagar2093.bmicompose.ui.widgets.RoundedCard
import com.github.sagar2093.bmicompose.ui.widgets.RoundedToggleButton
import com.github.sagar2093.bmicompose.util.BmiCalculator

@Composable
fun HomeScreen(
    scaffoldState: ScaffoldState = remember { ScaffoldState() }
) {
    Scaffold(
        scaffoldState = scaffoldState,
        topAppBar = {
            Toolbar(
                title = stringResource(R.string.app_name),
                navigationIcon = {
                    RoundIconButton(
                        vectorAsset = Icons.Outlined.Notifications,
                        onClick = { navigateTo(Screen.Tips) }
                    )
                },
                actions = {
                    RoundIconButton(vectorAsset = Icons.Outlined.Person, onClick = { })
                }
            )
        },
        bodyContent = {
            Content()
        }
    )
}

@Composable
private fun Content() {
    Column(
        modifier = Modifier.padding(16.dp).fillMaxSize(),
        verticalArrangement = Arrangement.SpaceAround
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            val maleState = mutableStateOf(true)
            val femaleState = mutableStateOf(false)
            RoundedToggleButton(
                state = maleState,
                text = "Male",
                onClick = {
                    maleState.value = true
                    femaleState.value = false
                },
                modifier = Modifier.padding(end = 8.dp).weight(1f)
            )
            RoundedToggleButton(
                state = femaleState,
                text = "Female",
                onClick = {
                    femaleState.value = true
                    maleState.value = false
                },
                modifier = Modifier.padding(start = 8.dp).weight(1f)
            )
        }
        val heightState: MutableState<Int> = state { 170 }
        val weightState: MutableState<Int> = state { 62 }
        val ageState: MutableState<Int> = state { 20 }
        PickerView(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(top = 16.dp),
            heightState = heightState,
            weightState = weightState,
            ageState = ageState
        )
        RoundedButton(
            text = "Lets Begin",
            onClick = {
                val bmi = BmiCalculator(
                    heightState.value,
                    weightState.value
                )
                navigateTo(Screen.Result(bmi))
            },
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
        )
    }
}

@Composable
private fun PickerView(
    modifier: Modifier = Modifier,
    heightState: MutableState<Int>,
    weightState: MutableState<Int>,
    ageState: MutableState<Int>
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        HeightSelector(
            modifier = Modifier
                .weight(1f)
                .gravity(align = Alignment.CenterHorizontally)
                .padding(bottom = 8.dp)
                .fillMaxHeight(),
            heightState = heightState
        )
        Row(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            NumberPicker(
                label = "Weight",
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp)
                    .fillMaxHeight(),
                pickerState = weightState
            )
            NumberPicker(
                label = "Age",
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp)
                    .fillMaxHeight(),
                pickerState = ageState
            )
        }
    }
}

@Composable
private fun HeightSelector(
    modifier: Modifier = Modifier,
    heightState: MutableState<Int>
) {
    val height = annotatedString {
        withStyle(
            style = SpanStyle(fontSize = TextUnit.Sp(32))
        ) { append(heightState.value.toString()) }
        append(" cm")
    }
    RoundedCard(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .gravity(Alignment.CenterHorizontally),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Height",
                modifier = ColumnChildModifier,
                style = LabelStyle
            )
            Slider(
                value = heightState.value.toFloat(),
                onValueChange = { heightState.value = it.toInt() },
                modifier = Modifier.fillMaxWidth().padding(8.dp),
                valueRange = (1f..272f),
                color = accentColor
            )
            Text(
                text = height,
                modifier = ColumnChildModifier,
                style = textStyle
            )
        }
    }
}

@Composable
private fun NumberPicker(
    label: String,
    modifier: Modifier = Modifier,
    pickerState: MutableState<Int>,
    range: IntRange = 1..100
) {
    RoundedCard(modifier = modifier) {
        Column(
            modifier = Modifier,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = label,
                style = LabelStyle,
                modifier = ColumnChildModifier
            )
            Text(
                text = pickerState.value.toString(),
                style = ValueStyle,
                modifier = ColumnChildModifier
            )
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = ColumnChildModifier
            ) {
                RoundIconButton(vectorAsset = Icons.Default.Add, onClick = {
                    if (pickerState.value < range.last) {
                        pickerState.value = pickerState.value + 1
                    }
                })
                RoundIconButton(vectorAsset = Icons.Default.Remove, onClick = {
                    if (pickerState.value > range.first) {
                        pickerState.value = pickerState.value - 1
                    }
                })
            }
        }
    }
}

@Preview
@Composable
private fun ScreenPreview() {
    AppTheme {
        HomeScreen()
    }
}

private val LabelStyle = TextStyle(
    color = Color.Black.copy(alpha = 0.6f),
    fontSize = TextUnit.Sp(18)
)

private val ValueStyle = TextStyle(
    color = Color.Black.copy(alpha = 0.9f),
    fontSize = TextUnit.Sp(32)
)

private val ColumnChildModifier = Modifier.gravity(Alignment.CenterHorizontally).padding(8.dp)
