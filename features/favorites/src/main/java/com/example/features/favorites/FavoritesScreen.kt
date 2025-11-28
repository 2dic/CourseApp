package com.example.features.favorites

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.core.designsystem.icon.rememberSortIcon
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.domain.model.Course
import com.example.features.home.CourseCard
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(
    viewModel: FavoritesViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        // Поиск и нефункциональная кнопка сортировки
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Поле поиска (нефункциональное)
            OutlinedTextField(
                value = "",
                onValueChange = {},
                placeholder = { Text("Поиск курсов") },
                modifier = Modifier.weight(1f),
                enabled = false
            )

            Spacer(modifier = Modifier.width(8.dp))

            // Кнопка сортировки с индикатором
            Box {
                IconButton(
                    onClick = { viewModel.toggleSort() }
                ) {
                    Icon(
                        painter = rememberSortIcon(),
                        contentDescription = when (uiState.sortType) {
                            SortType.NONE -> "Без сортировки"
                            SortType.BY_DATE -> "Сортировка по дате"
                            SortType.BY_RATING -> "Сортировка по рейтингу"
                            SortType.BY_PRICE -> "Сортировка по цене"
                        },
                        tint = if (uiState.sortType != SortType.NONE) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.onSurfaceVariant
                        }
                    )
                }

                // Индикатор активной сортировки
                if (uiState.sortType != SortType.NONE) {
                    Badge(
                        modifier = Modifier.align(Alignment.TopEnd)
                    ) {
                        Text("●")
                    }
                }
            }
        }

        // Показываем тип сортировки если активен
        if (uiState.sortType != SortType.NONE) {
            Text(
                text = when (uiState.sortType) {
                    SortType.BY_DATE -> "Сортировка: по дате публикации"
                    SortType.BY_RATING -> "Сортировка: по рейтингу"
                    SortType.BY_PRICE -> "Сортировка: по цене"
                    SortType.NONE -> ""
                },
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }

        if (uiState.isEmpty) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Вы не добавили ни одного курса",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium
                )
            }
        } else {
            AnimatedVisibility(
                visible = true,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(
                        items = uiState.favoriteCourses,
                        key = { it.id }
                    ) { course ->
                        AnimatedFavoriteCard(
                            course = course,
                            onFavoriteClick = { viewModel.toggleFavorite(course) }
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AnimatedFavoriteCard(
    course: Course,
    onFavoriteClick: () -> Unit
) {
    var isVisible by remember { mutableStateOf(true) }
    val coroutineScope = rememberCoroutineScope()

    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn() + expandVertically(),
        exit = slideOutHorizontally(
            animationSpec = tween(durationMillis = 400),
            targetOffsetX = { -it }
        ) + fadeOut(animationSpec = tween(durationMillis = 300))
    ) {
        CourseCard(
            course = course,
            onFavoriteClick = {
                isVisible = false

                coroutineScope.launch {
                    delay(400) // Ждем завершения анимации
                    onFavoriteClick() // Вызываем колбэк удаления
                }
            }
        )
    }
}

