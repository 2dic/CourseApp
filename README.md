Приложение "Курсы"

<img width="361" height="803" alt="image" src="https://github.com/user-attachments/assets/90890f27-f6de-4992-8926-b175bf1b797f" />
<img width="361" height="803" alt="image" src="https://github.com/user-attachments/assets/805ca621-17d8-4ed8-9f26-19adea8f8e76" />
<img width="361" height="803" alt="image" src="https://github.com/user-attachments/assets/21dba31e-62f7-4242-bc3c-e4c32c6c731b" />

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
