# Rustic-Roots App
The primary objective of this project is to create a restaurant table booking  mobile application.'

## Tables of contents
* [App Features](#app-features)
* [In-app technology](#in-app-technology)
* [App Demo Video](#app-demo-video)
* [App UI](#app-ui)

## App Features
1. Browse parties and members information. 
1. View member's reviews and their star rating based on the listed reviews.
1. Add, Update and Delete reviews for each members.
1. Send emails to Parliament members.
1. Add members to favorite list and view favorite list.
1. View statistic of party in Finnish parliament.

## In-app technology
This Android app is built by Kotlin, targetting min SDK 24 and following MVVM software architectural pattern. 

Components used in App: 
* FrontEnd: jetpack compose, Bottom navigation, 
* BackEnd:
1. Database: firebase
1. Network: Retrofit
1. WorkManager: Update data from network periodically once a day in background through PeriodicWorkRequest
1. Unit Testing: JUnit 4 (for app main functions)
1. Implicit intent to send email


## App Demo Video: 
