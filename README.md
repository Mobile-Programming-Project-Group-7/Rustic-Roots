# Rustic-Roots App
The primary objective of this project is to create a restaurant table booking application along with pre ordering the foods.

## Tables of contents
* [Tools](#tools)
* [App Features](#app-features)
* [Technologies Used](#technologies-used)
* [Getting Started](#getting-started)
* [App Demo Video](#app-demo-video)
* [App UI](#app-ui)
* [Contributors](#contributors)

## Tools
- ### UI design tool: 
[**Figma**](https://www.figma.com/)  is a cloud-based design tool that allows you to create and collaborate on user interface designs. With Figma, you can create wireframes, prototypes, and high-fidelity designs for web and mobile applications. Figma is known for its intuitive interface, powerful collaboration features, and ability to integrate with other tools in your workflow.
- ### Code editor:
[**Android Studio**](https://developer.android.com/studio/) is an integrated development environment (IDE) for developing Android applications. Android Studio provides a range of features to help you write, debug, and test your code. These features include code highlighting, autocompletion, debugging tools, and more.
- ### Database design tool:
[**Firebase**](https://console.firebase.google.com/u/0/) is a cloud-based platform that provides a range of services for building and scaling mobile and web applications. Firebase includes a real-time database that allows you to store and sync data in real-time, as well as an authentication service, cloud messaging, and more. With Firebase, you can quickly create and deploy a fully-functional backend for your application.
- ### Version control:
[**Git**](https://git-scm.com/) is a version control system that allows you to track changes to your code over time. Git allows you to create branches, merge changes, and collaborate with other developers.

[**Github**](https://github.com/) is a web-based platform that provides hosting for Git repositories. GitHub provides a range of collaboration features, including pull requests, code reviews, and more. Together, Git and GitHub provide a powerful toolset for managing your code and collaborating with others.

## App Features
* ### Pre-Ordering:
Customers have the ability to order food in advance before they book a table. This feature helps customers to ensure their food is ready when they arrive at the restaurant and saves them time.

* ### Table Reservation:
The app features a booking system that allows users to reserve tables for a specific date and time. This ensures that the restaurant can prepare for the incoming customers and provide a better dining experience.

* ### Payment Integration:
The app is integrated with popular payment systems such as Googlepay to ensure a seamless and secure payment experience for customers. This feature allows customers to pay for their meals easily and efficiently without having to worry about carrying cash or credit cards.

## Technologies Used
This Android app is built by Kotlin, targetting SDK 33 , min SDK 28 and following  Firebase, Jetpack Compose, MVVM software architectural pattern. 

### Components used in App: 
### FrontEnd
* #### Jetpack Compose:
Jetpack Compose is an Android UI toolkit that makes it easier and faster to build native Android apps. It is based on the idea of writing code in a declarative way, where you describe how the user interface should look and behave. With Jetpack Compose, you can create responsive and dynamic UIs with less code and in a more intuitive way.

* #### Bottom Navigation:
Bottom Navigation is a UI pattern used in mobile apps to provide easy navigation between different sections of the app. It typically consists of a row of icons or labels at the bottom of the screen, and tapping on one of them takes you to the corresponding section. It is a commonly used design pattern in Android apps, and can help users quickly navigate between different parts of the app.

### BackEnd
* #### Database: Firebase
Firebase is a cloud-based platform provided by Google that offers various services for building and running mobile and web apps. Firebase offers a real-time database that is a cloud-hosted NoSQL database that stores data in JSON format. It allows you to store and sync data in real-time across all connected clients, including web and mobile apps.

* #### Network: Retrofit
The network component of the app is responsible for communicating with the server to fetch and send data. This can include making HTTP requests, parsing responses, and handling errors. In the app, this might be achieved using libraries such as Retrofit or Volley.

* #### WorkManager:
WorkManager is a library that allows you to schedule background tasks in your app, such as fetching data from the network or performing periodic updates. It is part of the Android Jetpack library, and offers a simple and efficient way to schedule work that needs to be done even when the app is not in the foreground.

* #### Unit Testing: JUnit 4
Unit testing is a software testing method in which individual units or components of a software application are tested in isolation. In the context of the app, this might include testing individual functions or classes to ensure that they are working correctly. Unit tests can help catch bugs early in the development process, and ensure that the app is functioning as expected. There are many testing frameworks available for Android development, including JUnit and Espresso.

## Getting Started
To run the app, you will need to clone the repository and configure Firebase in your project.
* ### Clone the repo:
```
git clone https://github.com/Mobile-Programming-Project-Group-7/Rustic-Roots.git
```
* ### Set up Firebase:
Follow the [**Firebase**](https://console.firebase.google.com/u/0/) documentation to create a new Firebase project and add the necessary Firebase services to your app.
* ### Build and run the app in Android Studio.
## App Demo Video: 
## App UI:
<img width="151" alt="1" src="https://user-images.githubusercontent.com/90723058/235725824-e508a6f7-6786-4cd6-82cc-b8a907540a79.png"><img width="142" alt="2" src="https://user-images.githubusercontent.com/90723058/235725853-238cb802-9eb8-4cbd-bd82-8d59875eba55.png"><img width="140" alt="3" src="https://user-images.githubusercontent.com/90723058/235725870-62a8dedf-65b4-4ab1-874c-96994a2c7b73.png"><img width="134" alt="4" src="https://user-images.githubusercontent.com/90723058/235725897-371ebdfe-85f0-483a-9d38-6b7485d55748.png"><img width="134" alt="5" src="https://user-images.githubusercontent.com/90723058/235725967-ef016adc-9f5b-4586-bdaa-ed3267f573fb.png"><img width="134" alt="6" src="https://user-images.githubusercontent.com/90723058/235779410-0d9db301-f3e6-4f7b-b834-d5b7f4fc66da.png"><img width="138" alt="7" src="https://user-images.githubusercontent.com/90723058/235725990-1f312a8a-f574-4719-b62f-f216966440f3.png">







## Contributors:
This project is developed by :
- **Christina Vargka**, [Github account](https://github.com/ChrisVar95) ( Database + Table Booking Management Functionality +Payment gateway integration)
- **Sujata Shrestha**, [Github account](https://github.com/sujata054)( Design + MainScreens Navigation +Routing)
- **Esther Fatoyinbo**, [Github account](https://github.com/bebesf)( Database + User Authentication)

If you would like to contribute to the project, please submit a pull request with your changes.
We welcome contributions from everyone!
