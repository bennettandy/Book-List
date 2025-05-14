# QuilterDemo
QuilterDemo is an Android application that allows users to browse books from the Open Library API.

## Screenshots
![](screenshots/details_view.png)

### Installation
1. **Clone the Repository**:
   ```bash
   git clone https://github.com/your-repo/quilterdemo.git
   cd quilterdemo
   ```

2**Build and Run**:
   - Select the `debug` build variant.
   - Run the app (`./gradlew assembleDebug` or Run in Android Studio).
   - The app will fetch books from `https://openlibrary.org/` and display them.

## Architecture
QuilterDemo follows **MVVM** with **MVI-inspired** state management:
- **Model**: `BookRepository` fetches data from `OpenLibraryApiService` (Retrofit).
- **View**: Jetpack Compose UI (`BookScreen`, `BookItem`, `BottomSheetContent`) in `MainActivity`.
- **ViewModel**: `BookViewModel` manages state (`booksList`, `showBottomSheet`, `selectedBook`) using `StateFlow`.
- **Use Cases**: `GetWantToReadBooksUseCase` encapsulates business logic, bridging repository and ViewModel.
- **Dependency Injection**: Hilt provides dependencies (`ImageLoader`, `OkHttpClient`, `Retrofit`).

### Key Components
- **BookScreen**: Displays a `LazyColumn` of books with loading/error states.
- **BookItem**: Shows book title, author, and cover image (clickable to open bottom sheet).
- **BottomSheetContent**: Displays selected book’s title, large cover image, and author in a `ModalBottomSheet`.
- **BookViewModel**: Handles data fetching, sheet visibility, and book selection.
- **OpenLibraryApiService**: Fetches book data via Retrofit/RxJava.

## Dependencies
- **Jetpack Compose**: `2024.09.00` (UI, Material 3)
- **Coil**: `2.7.0` (image loading with caching)
- **Hilt**: `2.56.1` (dependency injection)
- **Retrofit**: `2.11.0` (API requests)
- **RxJava**: `2.1.9` (reactive programming)
- **OkHttp**: `4.12.0` (networking, logging)
- **Kotlin**: `2.0.21`
- **Timber**: `5.0.1` (logging)
- Full list in `app/libs.versions.toml`

## Usage
1. Launch the app to see the book list (“Want to Read”).
2. Tap a book to view details in a bottom sheet (title, large cover, author).
3. Use the “Refresh” button in the `Scaffold`’s bottom bar to reload books.
4. Swipe down or tap “Cancel” to dismiss the bottom sheet.

## Acknowledgments
- Open Library API for book data.
- Jetpack Compose for modern Android UI.
- Coil for efficient image loading.