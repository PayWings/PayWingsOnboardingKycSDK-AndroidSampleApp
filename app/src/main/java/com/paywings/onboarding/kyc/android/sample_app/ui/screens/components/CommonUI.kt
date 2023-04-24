package com.paywings.onboarding.kyc.android.sample_app.ui.screens.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.paywings.onboarding.kyc.android.sample_app.ui.theme.*


@Composable
fun ShowErrorText(modifier: Modifier = Modifier, @StringRes errorResId: Int?) {
    errorResId?.let {
        Text(
            modifier = modifier.padding(start = 4.dp, end = 4.dp),
            text = stringResource(id = it),
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
fun ScreenTitle(modifier: Modifier = Modifier, @StringRes stringResId: Int) {
    ScreenTitle(title = stringResource(stringResId), modifier = modifier)
}

@Composable
fun ScreenTitle(modifier: Modifier = Modifier, title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.headlineSmall,
        modifier = modifier
    )
}

@Composable
fun ScreenTitleWithBackButtonIcon(
    modifier: Modifier = Modifier,
    title: String,
    onClose: () -> Unit,
    enabled: Boolean = true
) {
    Box(
        modifier = Modifier
            .wrapContentHeight()
            .height(IntrinsicSize.Max)
    ) {
        IconButton(
            modifier = modifier
                .then(Modifier.size(24.dp))
                .wrapContentSize(Alignment.Center)
                .align(
                    Alignment.TopStart
                ),
            onClick = onClose,
            enabled = enabled
        ) {
            Icon(
                modifier = modifier,
                imageVector = Icons.Filled.ArrowBack,
                contentDescription = Icons.Filled.ArrowBack.name,
                tint = MaterialTheme.colorScheme.primary
            )
        }
        Column(
            modifier = modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = modifier.padding(start = 28.dp, end = 28.dp)
            )
        }
    }
}

@Composable
fun SpacerDialogTitleBody() {
    Spacer(Modifier.height(MaterialTheme.shapes.dialogTitleBodyDefaultPadding))
}

@Composable
fun ProcessingButton(
    modifier: Modifier = Modifier,
    @StringRes textResId: Int,
    onClick: () -> Unit,
    isLoading: Boolean = false,
    isEnabled: Boolean = true
) {
    Button(
        modifier = modifier.fillMaxWidth(),
        enabled = isEnabled,
        onClick = { if (!isLoading) onClick() }) {
        when (isLoading) {
            true -> CircularProgressIndicator(
                modifier = Modifier.size(24.dp),
                color = MaterialTheme.colorScheme.onPrimary,
                strokeWidth = 2.dp
            )
            false -> Text(text = stringResource(textResId))
        }
    }
}

@Composable
fun ScreenTitleWithCloseButtonIcon(
    modifier: Modifier = Modifier,
    @StringRes titleResId: Int,
    onClose: () -> Unit,
    enabled: Boolean = true
) {
    Box(
        modifier = Modifier
            .wrapContentHeight()
            .height(IntrinsicSize.Max)
    ) {
        IconButton(
            modifier = modifier
                .then(Modifier.size(24.dp))
                .wrapContentSize(Alignment.Center)
                .align(
                    Alignment.TopStart
                ),
            onClick = onClose,
            enabled = enabled
        ) {
            Icon(
                modifier = modifier,
                imageVector = Icons.Filled.Close,
                contentDescription = Icons.Filled.Close.name
            )
        }
        Column(
            modifier = modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(titleResId),
                style = MaterialTheme.typography.titleLarge,
                modifier = modifier.padding(start = 28.dp, end = 28.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenTopAppBarWithNavigation(title: String, navigationIcon: ImageVector, navigationEnabled: Boolean = true, onNavigation:() -> Unit) {
    TopAppBar(
        title = { Text(text = title) },
        navigationIcon = {
            IconButton(enabled = navigationEnabled, onClick = onNavigation) {
                Icon(
                    imageVector = navigationIcon,
                    contentDescription = navigationIcon.name
                )
            }
        },
        colors = TopAppBarDefaults.smallTopAppBarColors(titleContentColor = MaterialTheme.colorScheme.primary, navigationIconContentColor = MaterialTheme.colorScheme.primary, actionIconContentColor = MaterialTheme.colorScheme.primary)
    )
}

@Composable
fun ScreenTopAppBarWithBackNavigation(@StringRes title: Int, backNavigationEnabled: Boolean = true, onBackNavigation:() -> Unit) {
    ScreenTopAppBarWithBackNavigation(title = stringResource(id = title), backNavigationEnabled = backNavigationEnabled, onBackNavigation = onBackNavigation)
}

@Composable
fun ScreenTopAppBarWithBackNavigation(title: String, backNavigationEnabled: Boolean = true, onBackNavigation:() -> Unit) {
    ScreenTopAppBarWithNavigation(title = title, navigationIcon = Icons.Filled.ArrowBack, navigationEnabled = backNavigationEnabled, onNavigation = onBackNavigation)
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenTopAppBarWithCloseNavigation(@StringRes title: Int, closeNavigationEnabled: Boolean = true, onCloseNavigation:() -> Unit) {
    TopAppBar(
        title = { Text(text = stringResource(id = title)) },
        navigationIcon = {
            IconButton(enabled = closeNavigationEnabled, onClick = onCloseNavigation) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = Icons.Filled.Close.name
                )
            }
        },
        colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Color.Transparent, titleContentColor = MaterialTheme.colorScheme.primary, navigationIconContentColor = MaterialTheme.colorScheme.primary, actionIconContentColor = MaterialTheme.colorScheme.primary)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenTopAppBar(@StringRes title: Int) {
    TopAppBar(
        title = { Text(text = stringResource(id = title)) },
        colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Color.Transparent, titleContentColor = MaterialTheme.colorScheme.primary, navigationIconContentColor = MaterialTheme.colorScheme.primary, actionIconContentColor = MaterialTheme.colorScheme.primary)
    )
}


