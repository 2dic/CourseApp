package com.example.features.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.core.common.Result
import com.example.core.designsystem.icon.rememberArrowRight
import com.example.core.designsystem.icon.rememberMarkFilledIcon
import com.example.core.designsystem.icon.rememberMarkIcon
import com.example.core.designsystem.icon.rememberSortIcon
import com.example.domain.model.Course

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.refreshCourses()
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // Поиск и сортировка
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

            // Кнопка сортировки
            IconButton(onClick = { viewModel.toggleSort() }) {
                Icon(
                    painter = rememberSortIcon(),
                    contentDescription = "Сортировка",
                    tint = if (uiState.isSorted) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    }
                )
            }
        }

        when (val state = uiState.loadingState) {
            is Result.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            is Result.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Проблемы с интернетом")
                }
            }
            is Result.Success -> {
                if (uiState.filteredCourses.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Курсы не найдены")
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(uiState.filteredCourses) { course ->
                            CourseCard(
                                course = course,
                                onFavoriteClick = { viewModel.toggleFavorite(course) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourseCard(
    course: Course,
    onFavoriteClick: () -> Unit
) {
    Card(
        onClick = { /* Можно добавить детали курса */ },
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Заголовок и лайк
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = course.title,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                IconButton(
                    onClick = onFavoriteClick,
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        painter = if (course.hasLike) {
                            rememberMarkFilledIcon()
                        } else {
                            rememberMarkIcon()
                        },
                        contentDescription = "Избранное",
                        tint = if (course.hasLike) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ){
                Text(
                    text = "★ ${course.rate}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xAF7CFC00)
                )

                Text(
                    text = "Начало: ${course.startDate}",
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Описание (максимум 2 строки)
            Text(
                text = course.text,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Цена, рейтинг и дата
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${course.price} ₽",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.primary
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Подробнее",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xAF7CFC00)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        painter = rememberArrowRight(),
                        contentDescription = "Перейти",
                        tint = Color(0xAF7CFC00)
                    )
                }
            }
        }
    }
}