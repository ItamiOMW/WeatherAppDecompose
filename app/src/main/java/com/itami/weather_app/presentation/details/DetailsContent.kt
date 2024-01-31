package com.itami.weather_app.presentation.details

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.itami.weather_app.R
import com.itami.weather_app.domain.model.Forecast
import com.itami.weather_app.domain.model.Weather
import com.itami.weather_app.presentation.ui.common.ErrorText
import com.itami.weather_app.presentation.ui.theme.gradients
import com.itami.weather_app.utils.formatTempC
import com.itami.weather_app.utils.formattedFullDate
import com.itami.weather_app.utils.formattedShortDayOfWeek

@Composable
fun DetailsContent(component: DetailsComponent) {

    val state by component.state.collectAsState()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = MaterialTheme.gradients.purple.primaryGradient),
        contentColor = MaterialTheme.gradients.purple.onGradients,
        containerColor = Color.Transparent,
        topBar = {
            TopBar(
                cityName = state.city.name,
                isFavourite = state.isFavourite,
                onNavigateBackClick = component::onNavigateBackClick,
                onChangeFavouriteClick = component::onChangeFavouriteClick
            )
        }
    ) {
        Box(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
        ) {
            when (val forecastState = state.forecastState) {
                is DetailsStore.State.ForecastState.Error -> {
                    ForecastError()
                }

                is DetailsStore.State.ForecastState.Initial -> {
                    Unit
                }

                is DetailsStore.State.ForecastState.Loaded -> {
                    val forecast = forecastState.forecast
                    Forecast(forecast = forecast)
                }

                is DetailsStore.State.ForecastState.Loading -> {
                    ForecastLoading()
                }
            }
        }
    }
}

@Composable
private fun Forecast(
    forecast: Forecast,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = forecast.currentWeather.condition,
            style = MaterialTheme.typography.titleLarge,
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = forecast.currentWeather.tempC.formatTempC(),
                style = MaterialTheme.typography.displayLarge,
            )
            AsyncImage(
                modifier = Modifier
                    .size(70.dp),
                model = forecast.currentWeather.conditionIconUrl,
                contentDescription = stringResource(R.string.desc_weather_condition_icon)
            )
        }
        Text(
            text = forecast.currentWeather.date.formattedFullDate(),
            style = MaterialTheme.typography.titleLarge,
        )
        Spacer(modifier = Modifier.weight(1f))
        AnimatedUpcomingWeather(upcoming = forecast.upcomingWeather)
        Spacer(modifier = Modifier.weight(0.5f))
    }
}

@Composable
fun AnimatedUpcomingWeather(
    upcoming: List<Weather>,
) {
    val visibleState = remember {
        MutableTransitionState(false).apply {
            targetState = true
        }
    }
    AnimatedVisibility(
        visibleState = visibleState,
        enter = fadeIn(animationSpec = tween(500)) + slideIn(
            animationSpec = tween(500),
            initialOffset = { intSize -> IntOffset(x = 0, y = intSize.height) }
        )
    ) {
        UpcomingWeather(upcoming = upcoming)
    }
}

@Composable
private fun UpcomingWeather(
    upcoming: List<Weather>,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        shape = MaterialTheme.shapes.extraLarge,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background.copy(alpha = 0.25f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = stringResource(R.string.upcoming),
                style = MaterialTheme.typography.headlineLarge,
            )
            Spacer(modifier = Modifier.height(24.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                upcoming.forEach { weather ->
                    SmallWeatherCard(
                        weather = weather,
                        modifier = Modifier
                            .height(128.dp)
                            .weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
private fun SmallWeatherCard(
    weather: Weather,
    modifier: Modifier = Modifier,
    shape: Shape = MaterialTheme.shapes.extraLarge,
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.onBackground
        ),
        shape = shape
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = weather.tempC.formatTempC(),
                style = MaterialTheme.typography.bodyLarge,
            )
            AsyncImage(
                modifier = Modifier
                    .size(48.dp),
                model = weather.conditionIconUrl,
                contentDescription = stringResource(R.string.desc_weather_condition_icon)
            )
            Text(
                text = weather.date.formattedShortDayOfWeek(),
                style = MaterialTheme.typography.bodyLarge,
            )
        }
    }
}

@Composable
private fun ForecastError() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        ErrorText(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(id = R.string.error_failed_to_load_current_weather),
            color = MaterialTheme.gradients.purple.onGradients
        )
    }
}

@Composable
private fun ForecastLoading() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(color = MaterialTheme.gradients.purple.onGradients)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(
    cityName: String,
    isFavourite: Boolean,
    onNavigateBackClick: () -> Unit,
    onChangeFavouriteClick: () -> Unit,
) {
    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent,
            titleContentColor = MaterialTheme.gradients.purple.onGradients,
            actionIconContentColor = MaterialTheme.gradients.purple.onGradients,
            navigationIconContentColor = MaterialTheme.gradients.purple.onGradients
        ),
        title = {
            Text(
                text = cityName,
                style = MaterialTheme.typography.titleLarge,
            )
        },
        navigationIcon = {
            IconButton(
                onClick = onNavigateBackClick
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.desc_arrow_back_icon),
                    modifier = Modifier.size(24.dp),
                )
            }
        },
        actions = {
            IconButton(
                onClick = onChangeFavouriteClick
            ) {
                Icon(
                    imageVector = if (isFavourite) Icons.Default.Star
                    else Icons.Default.StarBorder,
                    contentDescription = stringResource(R.string.desc_is_favourite_icon),
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    )
}