package com.android.applicationcode.presentation.vehicledetail

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.rounded.Call
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.android.applicationcode.util.rememberPhoneCallHandler
import com.android.applicationcode.R
import com.android.domain.model.Vehicle

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun VehicleDetailScreen(
    navController: NavController,
    viewModel: VehicleDetailViewModel
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { _ ->
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            when (val state = uiState) {
                is VehicleDetailUiState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                is VehicleDetailUiState.Success -> {
                    VehicleDetailContent(
                        vehicle = state.vehicle,
                        context = LocalContext.current,
                        onBackClick = { navController.popBackStack() }
                    )
                }
                is VehicleDetailUiState.NotFound -> {
                    ErrorScreen(message = stringResource(R.string.vehicle_not_found), onRetry = { navController.popBackStack() })
                }
                is VehicleDetailUiState.Error -> {
                    ErrorScreen(message = state.message, onRetry = { navController.popBackStack() })
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VehicleDetailContent(
    vehicle: Vehicle,
    context: Context,
    onBackClick: () -> Unit
) {
    val makePhoneCall = rememberPhoneCallHandler(context)

    Box(modifier = Modifier.fillMaxSize()) {

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentPadding = PaddingValues(bottom = 70.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            item {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(vehicle.photo.takeIf { it.isNotEmpty() })
                        .crossfade(true)
                        .build(),
                    contentDescription = vehicle.make,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(16f / 10f),
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(android.R.drawable.ic_menu_gallery),
                    error = painterResource(android.R.drawable.ic_menu_report_image)
                )
            }

            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = stringResource(
                            R.string.year_make_model_trim,
                            vehicle.year, vehicle.make, vehicle.model, vehicle.trim
                        ),
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = stringResource(R.string.price_mileage, vehicle.price, vehicle.mileage),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            item {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    shape = MaterialTheme.shapes.medium,
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.vehicle_info),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                        TwoColumnSpecGrid(
                            specs = listOf(
                                stringResource(R.string.location) to "${vehicle.city}, ${vehicle.state}",
                                stringResource(R.string.exterior_color) to vehicle.exteriorColor,
                                stringResource(R.string.interior_color) to vehicle.interiorColor,
                                stringResource(R.string.drive_type) to vehicle.driveType,
                                stringResource(R.string.transmission) to vehicle.transmission,
                                stringResource(R.string.body_style) to vehicle.bodyType,
                                stringResource(R.string.engine) to vehicle.engine,
                                stringResource(R.string.fuel) to vehicle.fuel
                            )
                        )
                    }
                }
            }

            item {
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .height(52.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    shape = RoundedCornerShape(12.dp),
                    onClick = { makePhoneCall(vehicle.phone) }
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Call,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stringResource(R.string.call_dealer),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

           
        }

       TopAppBar(
            modifier = Modifier.statusBarsPadding(),
            title = { Text(text = "") },
            navigationIcon = {
                Box(
                    modifier = Modifier
                        .padding(start = 16.dp, top = 8.dp)
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color.Black.copy(alpha = 0.4f)),
                    contentAlignment = Alignment.Center
                ) {
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back),
                            tint = Color.White
                        )
                    }
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent,
                scrolledContainerColor = Color.Transparent
            )
        )
    }
}
@Composable
fun TwoColumnSpecGrid(specs: List<Pair<String, String>>) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        specs.forEach { (label, value) ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = value,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1.2f),
                    textAlign = TextAlign.End
                )
            }
        }
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