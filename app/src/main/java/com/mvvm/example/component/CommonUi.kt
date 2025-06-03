package com.mvvm.example.component

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.ContentAlpha
import com.mvvm.example.R
import com.mvvm.example.utilities.scaledSp
import com.mvvm.example.ui.theme.fonts



@Composable
fun GoBackNavigation (
    modifier: Modifier,
    screenTitle: String,
    showClearAllLabel: Boolean = false,
    showBackCrossIcon: Boolean = false,
    showFilterIcon: Boolean = false,
    showEditIcon: Boolean = false,
    showThreeDotIcon: Boolean = false,
    showDoneIconOnRight: Boolean = false,
    isBottomPaddingZero: Boolean = false,
    onBackClicked: () -> Unit,
    elevation: Dp = 0.dp,
    onRemoveAllClicked: () -> Unit = {},
    onDoneClicked: () -> Unit = {},
    onFilterClicked: () -> Unit = {},
    onNavigateToEditScreen: () -> Unit = {},
    onThreeDotClicked: () -> Unit = {}
) {

    Surface(
        tonalElevation = elevation,
        color = Color.White
    ) {

        Box(modifier = modifier
            .fillMaxWidth()
            .padding(top = 16.dp, start = 16.dp, bottom = if (isBottomPaddingZero) 0.dp else 16.dp)
            .background(color = colorResource(id = R.color.white))) {

            Row(modifier = Modifier.align(alignment = Alignment.CenterStart)) {

                IconButton(
                    onClick = onBackClicked,
                    modifier = Modifier
                        .align(alignment = Alignment.CenterVertically)
                        .size(24.dp)
                        .wrapContentWidth()
                        .wrapContentHeight()
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.ArrowBack,

                        contentDescription = stringResource(R.string.navigation_action_back_cd),
                        tint = MaterialTheme.colorScheme.primary,
                    )
                }
//                BackArrowImage (onBackClicked = onBackClicked, showBackCrossIcon = showBackCrossIcon)

                Box(modifier = Modifier,
                    contentAlignment = Alignment.Center
                ) {

                    Text(
                        text = screenTitle,
                        fontSize = 20.scaledSp,
                        fontFamily = fonts,
                        letterSpacing = 0.sp,
                        fontWeight = FontWeight.Bold,
                        color = colorResource(id = R.color.black),
                        lineHeight = 26.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(start = 12.dp)
                    )
                }
            }

            if (showClearAllLabel) {
                Text(
                    text = stringResource(id = R.string.app_name),
                    fontSize = 16.scaledSp,
                    fontFamily = fonts,
                    letterSpacing = 0.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(id = R.color.black),
                    lineHeight = 20.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .padding(end = 24.dp)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = onRemoveAllClicked
                        )
                        .align(alignment = Alignment.CenterEnd)
                )
            }

            if (showFilterIcon) {
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher_foreground),
                    contentDescription = "filter",
                    modifier = Modifier
                        //.size(18.dp)
                        .padding(end = 24.dp)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = onFilterClicked
                        )
                        .align(alignment = Alignment.CenterEnd)
                )
            }

            if (showEditIcon) {
                Image(
                    imageVector = Icons.Default.Edit,
                    //painter = painterResource(id = R.drawable.ic_launcher_foreground),
                    contentDescription = "add",
                    modifier = Modifier
                        //.size(18.dp)
                        .padding(end = 14.dp)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = onNavigateToEditScreen
                        )
                        .align(alignment = Alignment.CenterEnd),
                    colorFilter = androidx.compose.ui.graphics.ColorFilter.tint(MaterialTheme.colorScheme.primary)
                )
            }

            if (showThreeDotIcon) {
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher_foreground),
                    contentDescription = stringResource(id = R.string.app_name),
                    modifier = Modifier
                        .wrapContentWidth()
                        .wrapContentHeight()
                        .padding(end = 14.dp)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = onThreeDotClicked
                        )
                        .align(alignment = Alignment.CenterEnd)
                )
            }

            if (showDoneIconOnRight) {
                Image(
                    painter = painterResource(id = R.drawable.ic_done_24),
                    contentDescription = stringResource(id = R.string.app_name),
                    modifier = Modifier
                        .wrapContentWidth()
                        .wrapContentHeight()
                        .padding(end = 16.dp)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = onDoneClicked
                        )
                        .align(alignment = Alignment.CenterEnd)
                )
            }
        }
    }
}

@Composable
fun InputFieldState(
    modifier: Modifier,
    tag: String = "",
    label: String,
    mask: String = "+45 __ __ __ __",
    maskNumber: Char = '_',
    changedText: String = "",
    enabled: Boolean = true,
    isPhoneNumber: Boolean = false,
    showVisibleIcon: Boolean = true,
    placeHolder: String = "",
    keyboardCapitalization : KeyboardCapitalization = KeyboardCapitalization.Sentences,
    keyboardType: KeyboardType,
    maxCharacters: Int = Int.MAX_VALUE,
    visualPrefix: VisualTransformation = VisualTransformation.None,
    backgroundColor: Color = colorResource(id = R.color.white),
    disabledTextColor: Color = LocalContentColor.current.copy(ContentAlpha.disabled),
    onChangeValue: (c: String) -> Unit = {}) {

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }
    InputFieldState (
        modifier = modifier,
        tag = tag,
        keyboardController = keyboardController,
        focusRequester = focusRequester,
        label = label, mask = mask, maskNumber = maskNumber, changedText = changedText,
        enabled = enabled, isPhoneNumber = isPhoneNumber, showVisibleIcon = showVisibleIcon,
        placeHolder = placeHolder, keyboardType = keyboardType,keyboardCapitalization = keyboardCapitalization,
        maxCharacters = maxCharacters,
        visualPrefix = visualPrefix,
        backgroundColor = backgroundColor,
        disabledTextColor = disabledTextColor,
        onChangeValue = onChangeValue
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputFieldState(
    modifier: Modifier,
    tag: String = "",
    label: String,
    mask: String = "+45 __ __ __ __",
    maskNumber: Char = '_',
    changedText: String = "",
    enabled: Boolean = true,
    isPhoneNumber: Boolean = false,
    showVisibleIcon: Boolean = true,
    placeHolder: String = "",
    keyboardType: KeyboardType,
    keyboardCapitalization: KeyboardCapitalization = KeyboardCapitalization.Sentences,
    keyboardController: SoftwareKeyboardController?,
    focusRequester: androidx.compose.ui.focus.FocusRequester,
    maxCharacters: Int = Int.MAX_VALUE,
    visualPrefix: VisualTransformation = VisualTransformation.None,
    backgroundColor: Color = colorResource(id = R.color.white),
    disabledTextColor: Color,
    onChangeValue: (c: String) -> Unit = {}) {

    val interactionSource = remember { MutableInteractionSource() }
    val isFocused = interactionSource.collectIsFocusedAsState()

    Column (modifier = modifier) {

        Text(
            text = label,
            fontSize = 16.scaledSp,
            fontFamily = fonts,
            letterSpacing = 0.sp,
            fontWeight = FontWeight.Bold,
            color = colorResource(id = R.color.black),
            lineHeight = 20.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(start = 0.dp, end = 0.dp)
        )
        Spacer(modifier = Modifier.padding(top = 6.dp))
        Surface(modifier = Modifier
            .height(59.dp)
            .padding(start = 0.dp, end = 0.dp)
            .border(
                width = 1.dp,
                color = colorResource(id = if (isFocused.value) R.color.black else R.color.CE2E2E2),
                shape = RoundedCornerShape(size = 4.dp)
            )) {



            val revealPassword: MutableState<Boolean> = remember {
                mutableStateOf(true)
            }

            val visualTransformation =
                if (keyboardType == KeyboardType.Password) {
                    if (revealPassword.value)
                        visualPrefix
                    else
                        VisualTransformation.None
                } else {
                    visualPrefix
                }

            TextField(
                value = changedText,
                singleLine = true,
                enabled = enabled,
                interactionSource = interactionSource,
                textStyle = TextStyle(
                    fontSize = 18.scaledSp,
                    letterSpacing = 0.sp,
                    fontFamily = fonts,
                    fontWeight = FontWeight.Bold
                ) ,
                onValueChange = { value ->
                    if (isPhoneNumber) {
                        onChangeValue(value.take(mask.count { it == maskNumber }))
                    }
                    else if (value.length <= maxCharacters) {
                        //changedText = value
                        //text.value
                        onChangeValue(value)
                    }
                },
                visualTransformation = visualTransformation,
                trailingIcon = {
                    if (showVisibleIcon && keyboardType == KeyboardType.Password) {
                        if (revealPassword.value) {
                            IconButton(onClick = {
                                revealPassword.value = false
                            }) {
                                Icon(
                                    painter = painterResource(id = androidx.core.R.drawable.ic_call_answer),
                                    contentDescription = null,
                                    tint = colorResource(id = R.color.black)
                                )
                            }
                        } else {
                            IconButton(onClick = { revealPassword.value = true }) {
                                Icon(
                                    //imageVector = Icons.Filled.Visibility,
                                    painter = painterResource(id = androidx.core.R.drawable.ic_call_decline_low),
                                    contentDescription = null,
                                    tint = colorResource(id = R.color.black)
                                )
                            }
                        }
                    }
                },
                colors = TextFieldDefaults.textFieldColors(
                    //textColor = Color.Black,
                    disabledTextColor = disabledTextColor,
                    //backgroundColor = backgroundColor /*MaterialTheme.colors.surface*/
                    //,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                ),

                placeholder = {
                    Text(text = placeHolder,
                        fontSize = 14.scaledSp,
                        lineHeight = 14.scaledSp,
                        fontFamily = fonts,
                        letterSpacing = 0.sp,
                        fontWeight = FontWeight.Normal)
                },
                modifier = Modifier
                    .focusRequester(focusRequester = focusRequester)
                    .testTag(tag)
                    .fillMaxWidth()
                    .fillMaxHeight(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = keyboardType,
                    imeAction = ImeAction.Done,
                    capitalization = keyboardCapitalization
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        keyboardController?.hide()
                        //onKeyboardDone(changedText)
                    }
                )
            )
        }
    }
}

@Composable
fun TextAction (
    modifier: Modifier,
    tag: String = "",
    textValue: String,
    @ColorRes bgColor: Int = R.color.C6650a4,
    @ColorRes outLineColor: Int = R.color.white,
    @ColorRes textColor: Int = R.color.black,
    textVerticalPadding: Dp = 13.dp,
    textHorizontalPadding: Dp = 16.dp,
    roundedCornerSize: Dp = 4.dp,
    @DrawableRes drawableResource: Int = R.drawable.ic_launcher_foreground,
    borderStrockWidth: Dp = 1.5.dp,
    fontSize : TextUnit = 18.scaledSp,
    contentAlignment: Alignment = Alignment.Center,
    onTextClick: () -> Unit
) {
    Box(
        modifier = modifier
            .testTag(tag = tag)
            .border(
                width = borderStrockWidth,
                color = colorResource(id = if (outLineColor == R.color.white) bgColor else outLineColor),
                shape = RoundedCornerShape(size = roundedCornerSize)
            )
            .background(
                color = colorResource(id = bgColor),
                shape = RoundedCornerShape(size = roundedCornerSize)
            )
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onTextClick
            ),
        contentAlignment = contentAlignment
    ) {

        val isLeftIconGone = drawableResource == R.drawable.ic_launcher_foreground
        if (!isLeftIconGone) {
            Row {
                Image(
                    painter = painterResource(id = drawableResource),
                    contentDescription = "startDrawable",
                    modifier = Modifier.align(alignment = Alignment.CenterVertically)
                )
                Text(
                    text = textValue,
                    style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false)),
                    fontSize = fontSize,
                    fontFamily = fonts,
                    letterSpacing = 0.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(id = textColor),
                    lineHeight = 22.sp,
                    modifier = Modifier
                        .padding(
                            top = textVerticalPadding,
                            start = textHorizontalPadding,
                            end = textHorizontalPadding,
                            bottom = textVerticalPadding
                        )
                )
            }

        } else {
            Text(
                text = textValue,
                style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false)),
                fontSize = fontSize,
                fontFamily = fonts,
                letterSpacing = 0.sp,
                fontWeight = FontWeight.Bold,
                color = colorResource(id = textColor),
                lineHeight = 22.sp,
                modifier = Modifier
                    .padding(
                        top = textVerticalPadding,
                        start = textHorizontalPadding,
                        end = textHorizontalPadding,
                        bottom = textVerticalPadding
                    )
            )
        }
    }
}