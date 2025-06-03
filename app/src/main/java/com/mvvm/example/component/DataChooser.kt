package com.mvvm.example.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mvvm.example.R
import com.mvvm.example.data.room.model.SmartSuggestionsData
import com.mvvm.example.ui.theme.fonts
import com.mvvm.example.utilities.scaledSp


@Composable
fun ChooseSuggestion (
    modifier: Modifier,
    suggestionList: List<SmartSuggestionsData> = emptyList(),
    isChipSelected: (chip: String)-> Boolean,
    onSelectDeviceData: (data: SmartSuggestionsData?) -> Unit
) {
    ChipVerticalGrid(modifier = modifier, 10.dp) {
        suggestionList.forEach { each->
            ChipLabel(id = each.suggestion1, word = each.suggestion1, isChipSelected = isChipSelected, onSelectChipLabel = { _text->
                onSelectDeviceData (suggestionList.find { suggestion-> suggestion.suggestion1 == _text })
            })
        }
    }
}

@Composable
fun ChipLabel(
    id: String,
    word: String,
    isChipSelected: (chipId: String)-> Boolean,
    onSelectChipLabel: (text: String) -> Unit = {}) {

    /*var selectedId by remember {
        mutableStateOf(0)
    }*/

    val selected = isChipSelected(id)
    val bgColor = if (selected) R.color.CCCC2DC else R.color.white
    val txtColor = if (selected) R.color.white else R.color.CCCC2DC
    val outLineColor = if (selected) R.color.CCCC2DC else R.color.CCCC2DC
    Box(modifier = Modifier
        .clip(shape = RoundedCornerShape(size = 4.dp))
        .clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = null,
            onClick = {
                //selectedId = if (selectedId == id) 0 else id
                onSelectChipLabel(id)
            })
        .border(
            width = 1.dp,
            color = colorResource(id = outLineColor),
            shape = RoundedCornerShape(size = 4.dp)
        )
        .background(color = colorResource(id = bgColor), shape = RoundedCornerShape(size = 4.dp))
        .padding(vertical = 8.dp, horizontal = 12.dp)){
        Text(
            text = word,
            fontSize = 14.scaledSp,
            letterSpacing = 0.sp,
            fontFamily = fonts,
            fontWeight = FontWeight.Bold,
            color = colorResource(id = txtColor),
            lineHeight = 17.sp
        )
    }
}

/**
 * https://stackoverflow.com/questions/68979046/how-to-do-multiline-chip-group-in-jetpack-compose
 */
@Composable
fun ChipVerticalGrid(
    modifier: Modifier = Modifier,
    spacing: Dp,
    content: @Composable () -> Unit
) {
    Layout(
        content = content,
        modifier = modifier
    ) { measurables, constraints ->
        var currentRow = 0
        var currentOrigin = IntOffset.Zero
        val spacingValue = spacing.toPx().toInt()
        val placeables = measurables.map { measurable ->
            val placeable = measurable.measure(constraints)

            if (currentOrigin.x > 0f && currentOrigin.x + placeable.width > constraints.maxWidth) {
                currentRow += 1
                currentOrigin = currentOrigin.copy(x = 0, y = currentOrigin.y + placeable.height + spacingValue)
            }

            placeable to currentOrigin.also {
                currentOrigin = it.copy(x = it.x + placeable.width + spacingValue)
            }
        }

        layout(
            width = constraints.maxWidth,
            height = placeables.lastOrNull()?.run { first.height + second.y } ?: 0
        ) {
            placeables.forEach {
                val (placeable, origin) = it
                placeable.place(origin.x, origin.y)
            }
        }
    }
}