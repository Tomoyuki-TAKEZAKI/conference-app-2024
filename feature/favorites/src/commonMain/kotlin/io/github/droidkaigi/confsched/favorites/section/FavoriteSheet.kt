package io.github.droidkaigi.confsched.favorites.section

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import conference_app_2024.feature.favorites.generated.resources.empty_description
import conference_app_2024.feature.favorites.generated.resources.empty_guide
import io.github.droidkaigi.confsched.designsystem.theme.KaigiTheme
import io.github.droidkaigi.confsched.favorites.FavoritesRes
import io.github.droidkaigi.confsched.favorites.component.FavoriteFilters
import io.github.droidkaigi.confsched.model.DroidKaigi2024Day
import io.github.droidkaigi.confsched.model.DroidKaigi2024Day.ConferenceDay1
import io.github.droidkaigi.confsched.model.DroidKaigi2024Day.ConferenceDay2
import io.github.droidkaigi.confsched.model.Timetable
import io.github.droidkaigi.confsched.model.TimetableItem
import io.github.droidkaigi.confsched.model.fake
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

const val FavoritesScreenEmptyViewTestTag = "FavoritesScreenEmptyViewTestTag"

sealed interface FavoritesSheetUiState {
    val currentDayFilter: PersistentList<DroidKaigi2024Day>
    val allFilterSelected: Boolean
    val isAllFilterSelected: Boolean
        get() = allFilterSelected
    val isDay1FilterSelected: Boolean
        get() = allFilterSelected.not() && currentDayFilter.contains(ConferenceDay1)
    val isDay2FilterSelected: Boolean
        get() = allFilterSelected.not() && currentDayFilter.contains(ConferenceDay2)

    data class FavoriteListUiState(
        override val currentDayFilter: PersistentList<DroidKaigi2024Day>,
        override val allFilterSelected: Boolean,
        val timeTable: Timetable,
    ) : FavoritesSheetUiState

    data class Empty(
        override val currentDayFilter: PersistentList<DroidKaigi2024Day>,
        override val allFilterSelected: Boolean,
    ) : FavoritesSheetUiState
}

@Composable
fun FavoriteSheet(
    uiState: FavoritesSheetUiState,
    onTimetableItemClick: (TimetableItem) -> Unit,
    onAllFilterChipClick: () -> Unit,
    onDay1FilterChipClick: () -> Unit,
    onDay2FilterChipClick: () -> Unit,
    onBookmarkClick: (TimetableItem) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxSize()) {
        FavoriteFilters(
            allFilterSelected = uiState.isAllFilterSelected,
            day1FilterSelected = uiState.isDay1FilterSelected,
            day2FilterSelected = uiState.isDay2FilterSelected,
            onAllFilterChipClick = onAllFilterChipClick,
            onDay1FilterChipClick = onDay1FilterChipClick,
            onDay2FilterChipClick = onDay2FilterChipClick,
        )

        when (uiState) {
            is FavoritesSheetUiState.Empty -> {
                EmptyView()
            }

            is FavoritesSheetUiState.FavoriteListUiState -> {
                FavoriteList(
                    timetable = uiState.timeTable,
                    onBookmarkClick = onBookmarkClick,
                    onTimetableItemClick = onTimetableItemClick,
                )
            }
        }
    }
}

@Composable
private fun EmptyView(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.testTag(FavoritesScreenEmptyViewTestTag).fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Box(
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.onPrimary,
                    shape = RoundedCornerShape(24.dp),
                )
                .size(84.dp),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                modifier = Modifier.size(36.dp),
                imageVector = Icons.Filled.Favorite,
                contentDescription = null,
                tint = Color.Green,
            )
        }
        Spacer(Modifier.height(12.dp))
        Text(
            text = stringResource(FavoritesRes.string.empty_description),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(6.dp))
        Text(
            text = stringResource(FavoritesRes.string.empty_guide),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
@Preview
fun FavoriteSheetPreview() {
    KaigiTheme {
        Surface {
            FavoriteSheet(
                uiState = FavoritesSheetUiState.FavoriteListUiState(
                    allFilterSelected = true,
                    currentDayFilter = persistentListOf(ConferenceDay1, ConferenceDay2),
                    timeTable = Timetable.fake(),
                ),
                onAllFilterChipClick = {},
                onDay1FilterChipClick = {},
                onDay2FilterChipClick = {},
                onBookmarkClick = {},
                onTimetableItemClick = {},
            )
        }
    }
}

@Composable
@Preview
fun FavoriteSheetNoFavoritesPreview() {
    KaigiTheme {
        Surface {
            FavoriteSheet(
                uiState = FavoritesSheetUiState.Empty(
                    allFilterSelected = false,
                    currentDayFilter = persistentListOf(ConferenceDay1, ConferenceDay2),
                ),
                onAllFilterChipClick = {},
                onDay1FilterChipClick = {},
                onDay2FilterChipClick = {},
                onBookmarkClick = {},
                onTimetableItemClick = {},
            )
        }
    }
}
