# Book list Demo
BookListDemo is an Android application that allows users to browse books from the Open Library API.

## Screenshots
![](screenshots/details_view.png)

### Installation
1. **Clone the Repository**:
   ```bash
   git clone https://github.com/your-repo/quilterdemo.git
   cd quilterdemo
   ```

2. **Build and Run**:
   - Select the `debug` build variant.
   - Run the app (`./gradlew assembleDebug` or Run in Android Studio).
   - The app will fetch books from `https://openlibrary.org/` and display them.

## Architecture
Using **Android Modules**:
- **:app**: Main Application Module has dependencies on :app:data and :app:domain.
- **:app:domain**: Domain Module, defines Model classes, Interfaces and Usecases that depend only on Domain objects.
- **:app:data**: Data Module, implements the domain repository interface using the retrofit API client.

QuilterDemo follows **MVVM**:
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
1. Launch the app to see the book list.
2. Tap a book to view details in a bottom sheet.
