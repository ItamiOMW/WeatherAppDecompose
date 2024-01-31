package com.itami.weather_app.presentation.favourite

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.itami.weather_app.R
import com.itami.weather_app.presentation.ui.theme.CardGradient
import com.itami.weather_app.presentation.ui.theme.CardGradients
import com.itami.weather_app.utils.formatTempC

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FavouriteContent(component: FavouriteComponent) {

    val state by component.state.collectAsState()

    LazyVerticalGrid(
        modifier = Modifier.fillMaxSize(),
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item(span = { GridItemSpan(2) }) {
            SearchCard(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    component.onSearchClick()
                }
            )
        }
        itemsIndexed(
            items = state.cityItems,
            key = { _, item -> item.city.id }
        ) { index, cityItem ->
            CityCard(
                modifier = Modifier
                    .fillMaxSize()
                    .animateItemPlacement(),
                cityItem = cityItem,
                cardGradient = CardGradients.cardGradients[index % CardGradients.cardGradients.size],
                onClick = {
                    component.onCityItemClick(city = cityItem.city)
                }
            )
        }
        item {
            AddFavouriteCityCard(
                modifier = Modifier.fillMaxSize(),
                onClick = {
                    component.onAddFavouriteClick()
                }
            )
        }
    }
}

@Composable
private fun SearchCard(
    modifier: Modifier = Modifier,
    shape: Shape = MaterialTheme.shapes.extraLarge,
    onClick: () -> Unit,
) {
    Card(
        modifier = modifier,
        shape = shape,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        ),
        onClick = onClick,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon(
                imageVector = Icons.Rounded.Search,
                contentDescription = stringResource(R.string.desc_search_icon),
                modifier = Modifier.size(24.dp),
                tint = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Text(
                text = stringResource(R.string.hint_search),
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Composable
private fun AddFavouriteCityCard(
    modifier: Modifier = Modifier,
    shape: Shape = MaterialTheme.shapes.extraLarge,
    border: BorderStroke = BorderStroke(
        width = 2.dp,
        color = MaterialTheme.colorScheme.primary
    ),
    minHeight: Dp = 196.dp,
    onClick: () -> Unit,
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.primary
        ),
        shape = shape,
        border = border,
        onClick = onClick,
    ) {
        Column(
            modifier = Modifier
                .sizeIn(minHeight = minHeight)
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                modifier = Modifier
                    .padding(16.dp)
                    .size(42.dp),
                imageVector = Icons.Rounded.Edit,
                contentDescription = stringResource(R.string.desc_edit_icon),
                tint = MaterialTheme.colorScheme.primary
            )
            Text(
                text = stringResource(R.string.button_add_favourite),
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun CityCard(
    cityItem: FavouriteStore.State.CityItem,
    modifier: Modifier = Modifier,
    shape: Shape = MaterialTheme.shapes.extraLarge,
    cardGradient: CardGradient = CardGradients.cardGradients.first(),
    minHeight: Dp = 196.dp,
    onClick: () -> Unit,
) {
    Card(
        modifier = modifier
            .shadow(
                elevation = 16.dp,
                spotColor = cardGradient.shadowColor,
                shape = shape
            ),
        colors = CardDefaults.cardColors(contentColor = cardGradient.onGradients),
        shape = shape,
        onClick = onClick,
    ) {
        Box(
            modifier = Modifier
                .background(brush = cardGradient.primaryGradient)
                .fillMaxSize()
                .sizeIn(minHeight = minHeight)
                .drawBehind {
                    drawCircle(
                        brush = cardGradient.secondaryGradient,
                        radius = size.maxDimension / 1.8f,
                        center = Offset(x = size.width / 3f, y = size.height)
                    )
                }
                .padding(24.dp),
        ) {
            when (val weatherState = cityItem.weatherState) {
                is FavouriteStore.State.WeatherState.Loaded -> {
                    AsyncImage(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .size(56.dp),
                        model = weatherState.iconUrl,
                        contentDescription = stringResource(R.string.desc_weather_condition_icon)
                    )
                    Column(
                        modifier = Modifier.align(Alignment.BottomStart),
                        horizontalAlignment = Alignment.Start
                    ) {
                        Text(
                            text = weatherState.tempC.formatTempC(),
                            style = MaterialTheme.typography.displayMedium,
                            textAlign = TextAlign.Start,
                        )
                        Text(
                            text = cityItem.city.name,
                            style = MaterialTheme.typography.labelLarge,
                            textAlign = TextAlign.Start,
                        )
                    }
                }

                is FavouriteStore.State.WeatherState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = cardGradient.onGradients
                    )
                    Text(
                        modifier = Modifier.align(Alignment.BottomStart),
                        text = cityItem.city.name,
                        style = MaterialTheme.typography.labelLarge,
                        textAlign = TextAlign.Start,
                    )
                }

                else -> {
                    Text(
                        modifier = Modifier.align(Alignment.BottomStart),
                        text = cityItem.city.name,
                        style = MaterialTheme.typography.labelLarge,
                        textAlign = TextAlign.Start,
                    )
                }
            }
        }
    }
}