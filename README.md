

# FeedForward Courier App

## Introduction

The FeedForward Courier App is an Android application designed to manage donation from restaurants to associations.
The app allowes the user to update order status and make a route with google maps

## Features
- Viewing and tracking past and activte orders.
- Integration with google places API in order to find the best suitlbe resurents in your area
- Making route with google mapa from current location -> restaurant location -> association location
-  Presistent data retention both on client side and server side.
- Multi-language support and localization.
- Updating and change the orders on the go.

## Tech Stack
-   **Java based application using the MVVM desgin patten**
-   **Server communication based on the Retrofit framework**
-   **Integration with Google maps**
-   **Usage of navigation UI and bottom navigation view**

## Future improvements
-   Add notifications for adding or changing orders
-  Support in pagination
## Setup

1.  **Clone the repository**:
    ```bash
    git clone https://github.com/GalRadia/FeedForward_Courier
    ```
    
2.  **Install Dependencies**:  
    Ensure that Gradle is installed and your environment is set up for Android development. Run:
```bash
 ./gradlew build
 ```
3. **Configure Retrofit**:
    Change the BaseURL to the path of the server with yourIP.
    for example ```http://10.0.2.2:8084/```
   
    ```10.0.2.2``` is the ip for the emulator in android studio.

## Application Flow
1.  **Registration/Login**: Association begin by registering for an account using an email.
2.  **Home Page**: Upon successful login, the home page displays the orders in radius of 5,15,30 KM from the association
3.  **Updating Donations status**: After picking an order , The order changes its status and notifies all the relevent sources
4.  **Dashboard**: Courier can see all Active and Finished orders. by clicking each order it will pop up google maps with the route to the destination and waypoint

## Pick Order
<img src="https://github.com/user-attachments/assets/7d7672d2-3937-4dc4-b8a7-fafe1e09ccb2" alt="Pick Order" width="300" height="600">

## Dashboard
<img src="https://github.com/user-attachments/assets/28e6bcf9-8858-41a2-b2ef-74ee9a39cfb7" alt="Dashboard" width="300" height="600">



