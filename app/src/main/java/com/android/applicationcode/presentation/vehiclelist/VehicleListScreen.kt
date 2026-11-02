package com.android.applicationcode.presentation.vehiclelist

import android.content.Context
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Call
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.android.applicationcode.util.rememberPhoneCallHandler
import com.android.applicationcode.R
import com.android.applicationcode.presentation.navigation.NavRoutes
import com.android.domain.model.Vehicle

@Composable
fun VehicleListScreen(navController: NavController, viewModel: VehiclesListViewModel) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    Scaffold(
        topBar = { CarfaxAppBar() },
        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
    ) { padding ->
        AnimatedContent(
            targetState = uiState,
            transitionSpec = {
                fadeIn(animationSpec = tween(300)) togetherWith fadeOut(animationSpec = tween(300))
            },
            label = "ScreenStateTransition"
        ) { state ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                when (state) {
                    is VehicleListUiState.Loading -> LoadingScreen()
                    is VehicleListUiState.Empty -> {
                        ErrorScreen(message = stringResource(R.string.no_vehicle_found)) { viewModel.refresh() }
                    }

                    is VehicleListUiState.Error -> {
                        ErrorScreen(message = state.message) { viewModel.refresh() }
                    }

                    is VehicleListUiState.Success -> {
                        VehicleList(
                            vehicles = state.vehicles,
                            navController = navController,
                            context = context
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun LoadingScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(48.dp),
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
        )
    }
}

@Composable
fun ErrorScreen(message: String, onRetry: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth(0.8f)
        ) {
            Icon(
                imageVector = Icons.Rounded.Info,
                contentDescription = null,
                modifier = Modifier.size(48.dp),
                tint = MaterialTheme.colorScheme.error
            )
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = onRetry,
                shape = ButtonDefaults.shape
            ) {
                Text(text = stringResource(R.string.retry))
            }
        }
    }
}

@Composable
fun VehicleList(
    vehicles: List<Vehicle>, navController: NavController, context: Context
) {
    val makePhoneCall = rememberPhoneCallHandler(context)
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(items = vehicles, key = { vehicle -> vehicle.vin }) { vehicle ->
            VehicleCard(
                vehicle = vehicle,
                onClick = {
                    navController.navigate("${NavRoutes.DETAIL_SCREEN}/${vehicle.vin}") {
                        // Prevents launching a duplicate screen if it's already running on top
                        launchSingleTop = true
                    }
                },
                onCallClick = { makePhoneCall(vehicle.phone) }
            )
        }
    }
}

@Composable
fun VehicleCard(vehicle: Vehicle, onClick: () -> Unit, onCallClick: () -> Unit) {
    Card(
        onClick = onClick,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = CardDefaults.shape
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(vehicle.photo.takeIf { it.isNotBlank() })
                    .crossfade(true)
                    .build(),
                contentDescription = "${vehicle.year} ${vehicle.make} ${vehicle.model}",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .aspectRatio(16f / 10f),
                contentScale = ContentScale.Crop,
                placeholder = painterResource(android.R.drawable.ic_menu_gallery),
                error = painterResource(android.R.drawable.ic_menu_report_image)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = stringResource(
                        R.string.year_make_model_trim,
                        vehicle.year, vehicle.make, vehicle.model, vehicle.trim
                    ),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = stringResource(R.string.price_mileage, vehicle.price, vehicle.mileage),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = stringResource(R.string.location_format, vehicle.city, vehicle.state),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Button(
                    onClick = onCallClick,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Call,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stringResource(R.string.call_dealer),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarfaxAppBar() {
    TopAppBar(
        title = {
            Text(
                stringResource(R.string.app_name),
                fontWeight = FontWeight.Bold
            )
        }, colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary
        )
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LoaderPreview() {
    LoadingScreen()
}
