# Digitalflake-Hackathon
Desk and Meeting Room Booking System
This project provides a system for users to book desks, workstations, and meeting rooms based on available time slots and dates. It uses Kotlin with the MVVM architecture and Retrofit API to communicate with the backend for checking availability, booking, and confirming reservations.

Features
Desk Booking: Allows users to book a desk for a specified date and time slot.
Workstation Booking: Users can reserve workstations based on their preferred time slots.
Meeting Room Booking: Users can book meeting rooms for their meetings.
Availability Checking: Ensures that the selected desk, workstation, or meeting room is available at the chosen date and time.
Booking Confirmation: Once a booking is made, users receive a confirmation with the booking details.
Architecture
This app is built using the MVVM (Model-View-ViewModel) architecture, which promotes separation of concerns and easier testing. It consists of:

Model: Represents the data structure and business logic (e.g., Desk, Workstation, Meeting Room).
View: The UI layer responsible for rendering data to the user.
ViewModel: Holds and manages the UI-related data, making network calls and updating the UI accordingly.
Technologies Used
Kotlin: Programming language.
MVVM Architecture: Ensures clear separation of concerns.
Retrofit: Used for making network calls and interacting with the API.
Room Database: Local database for storing booking information.
LiveData: Provides data in a lifecycle-conscious way.
Coroutines: Handles asynchronous tasks.
Getting Started
Prerequisites
Make sure you have the following tools installed:

Android Studio (for Android development)
Java JDK
Android Emulator / Real Device
Installation
Clone this repository:

bash
Copy
git clone https://github.com/PratikJogdande/Digitalflake-Hackathon.git
cd desk-meeting-room-booking
Open the project in Android Studio.

Sync the project with Gradle files by clicking on File > Sync Project with Gradle Files.

Ensure you have the required permissions (e.g., Internet access for network requests).

Run the project either on an Android Emulator or a physical device.
