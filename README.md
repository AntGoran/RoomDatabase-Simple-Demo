# RoomDatabase-Simple-Demo
This Room Database App was created as part of 'The Complete Android 12 & Kotlin Development Masterclass' course by Dennis Panjuta. It is a simple app that utilizes Room, a SQLite object mapping library, to perform CRUD (Create, Read, Update, Delete) operations.

The main screen of the app features two input fields for entering a name and an email, a button, and a RecyclerView. Upon entering a name and email and pressing the button, the data is stored in the Room database. The RecyclerView below the button displays the entered name and email as items.

Each item in the RecyclerView includes the name and email, along with two buttons for updating and deleting the record. Clicking the 'Update' button triggers a dialog that allows you to modify the record. The dialog can only be dismissed by clicking the 'Cancel' button within the dialog.

The 'Delete' button presents a simple dialog with options to confirm or cancel the deletion of the record.

This app provides a basic implementation of a Room Database with CRUD functionality, allowing users to store and manage their records. It serves as a learning exercise for understanding how to work with Room in an Android app.
