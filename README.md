# Foody

Food Recipe application. Browse a list of dishes with detailed cooking instructions and videos. Also, you can add your favorites to the quick access menu! Based on Mealdb API.

## Preview: 

https://user-images.githubusercontent.com/48939805/186488950-b5711fce-b4ac-462c-8c31-38d8367fe9c5.mp4


## Case Study:
* [Room](https://developer.android.com/jetpack/androidx/releases/room)
   - How to create Entity
   - How to create Database
   - How to create DAO
* [Navigation](https://developer.android.com/guide/navigation/navigation-getting-started) 
  - Navigation in the Application
* [MVVM & LiveData](https://developer.android.com/jetpack/docs/guide)
  - MVVM architecture facilitates a separation of development
     - Model: Represents the logic of working with data 
     - View: User Interface
     - ViewModel:
        A class that allows Activities and Fragments to keep the objects they need alive when the screen is rotated with LiveData.
        - LiveData: A class that stores data and implements the Observable pattern.
        
        ![image](https://user-images.githubusercontent.com/48939805/185736696-06f88094-8327-480e-ac60-d3dca87de545.png)
 

# Libraries and technologies used.
- [Retrofit](https://square.github.io/retrofit/) - Making HTTP connection with the rest API and convert reponse json file to Kotlin/Java object.
- [Room](https://developer.android.com/jetpack/androidx/releases/room) - Save meals in local database.
- [MVVM & LiveData](https://developer.android.com/jetpack/docs/guide) - Saperate logic code from views and save the state in case the screen configuration changes.
- [Coroutines](https://kotlinlang.org/docs/coroutines-overview.html) - Do some code in the background.
- [ViewBinding](https://developer.android.com/topic/libraries/view-binding) - Allows you to more easily write code that interacts with views. 
- [Glide](https://github.com/bumptech/glide) - Load and cache images by URL.
- [Circle Image](https://github.com/hdodenhof/CircleImageView) - A fast circular ImageView perfect for profile images.
- [Android-Gif-Drawable](https://github.com/koral--/android-gif-drawable) - Views and Drawable for displaying animated GIFs on Android
- [Intuit](https://github.com/intuit/sdp) - An android lib that provides a new size unit - sdp (scalable dp).
- [Navigation](https://developer.android.com/guide/navigation/navigation-getting-started) - Handle everything related for in-app navigation.
