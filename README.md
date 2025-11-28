Приложение "Курсы"

Основные особенности:
- Экран входа с валидацией email в реальном времени
- Главный экран с списком курсов из API
- Система избранного с локальным хранением
- Сортировка курсов по дате публикации
- Анимированные переходы при удалении из избранного
- Кастомные UI компоненты и иконки
- Bottom Navigation с плавной навигацией

Язык & Фреймворки:
- Kotlin + Jetpack Compose (UI)
- Clean Architecture + MVVM
- Многомодульность (8 модулей)

Архитектура & Навигация:
- :app - точка входа
- :core - common, network, database, designsystem
- :data - репозитории, API, БД
- :domain - модели, use cases
- :features - auth, home, favorites, account
- Navigation Compose + Hilt Navigation

Сетевое взаимодействие:
- Retrofit + Gson
- Coroutines & Flow
- JSON API с курсами

Локальное хранилище:
- Room Database для избранного
- Flow для реактивных обновлений

UI & Анимации:
- Material Design 3
- Кастомные иконки
- Compose Animation API
- Анимация удаления карточек

DI & Управление состоянием:
- Dagger Hilt для dependency injection
- ViewModel + StateFlow
- CoroutineScope для асинхронных операций

Ключевые экраны:
- Login - вход с валидацией
- Home - список курсов + сортировка
- Favorites - избранное с анимациями
- Account - заглушка профиля

Особенности реализации:
- Реалтайм валидация форм
- Синхронизация состояний между экранами
- Плавные анимации переходов
- Обработка состояний загрузки/ошибок
- Кастомные кнопки соцсетей
