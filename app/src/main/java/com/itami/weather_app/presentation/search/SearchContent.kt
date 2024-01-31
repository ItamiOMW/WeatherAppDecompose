package com.itami.weather_app.presentation.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.itami.weather_app.R
import com.itami.weather_app.domain.model.City
import com.itami.weather_app.presentation.ui.common.ErrorText

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchContent(component: SearchComponent) {

    val state by component.state.collectAsState()

    SearchBar(
        query = state.searchQuery,
        onQueryChange = { query ->
            component.onChangeSearchQuery(query)
        },
        onSearch = { _ ->
            component.onSearchClick()
        },
        active = true,
        onActiveChange = {},
        leadingIcon = {
            IconButton(
                onClick = {
                    component.onNavigateBackClick()
                }
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.desc_arrow_back_icon),
                    modifier = Modifier.size(24.dp),
                )
            }
        },
        trailingIcon = {
            IconButton(
                onClick = {
                    component.onSearchClick()
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = stringResource(R.string.desc_search_icon),
                    modifier = Modifier.size(24.dp),
                )
            }
        }
    ) {
        when (val searchState = state.searchState) {
            is SearchStore.State.SearchState.Error -> {
                SearchError()
            }

            is SearchStore.State.SearchState.Found -> {
                Cities(
                    cities = searchState.cities,
                    onCityClick = component::onCityClick
                )
            }

            is SearchStore.State.SearchState.Initial -> {
                Unit
            }

            is SearchStore.State.SearchState.Loading -> {
                SearchLoading()
            }

            is SearchStore.State.SearchState.NotFound -> {
                SearchEmpty()
            }
        }
    }
}

@Composable
private fun Cities(
    cities: List<City>,
    onCityClick: (City) -> Unit,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        items(items = cities, key = { it.id }) { city ->
            CityCard(
                modifier = Modifier.fillMaxWidth(),
                city = city,
                onClick = {
                    onCityClick(city)
                }
            )
        }
    }
}

@Composable
private fun CityCard(
    city: City,
    modifier: Modifier = Modifier,
    shape: Shape = MaterialTheme.shapes.extraLarge,
    colors: CardColors = CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
    ),
    onClick: () -> Unit,
) {
    Card(
        modifier = modifier,
        shape = shape,
        colors = colors,
        onClick = onClick
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = city.name,
                style = MaterialTheme.typography.bodyLarge,
            )
            Text(
                text = city.country,
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}

@Composable
private fun SearchLoading() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
    }
}

@Composable
private fun SearchEmpty() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        ErrorText(text = stringResource(R.string.error_cities_were_not_found))
    }
}

@Composable
private fun SearchError() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        ErrorText(text = stringResource(id = R.string.error_failed_to_load_cities))
    }
}