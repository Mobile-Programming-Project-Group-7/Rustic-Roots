package com.example.rusticroots.Backend.User

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Users {


    class AuthHandler {
        private val auth = FirebaseAuth.getInstance()
        private val db = Firebase.firestore

        fun signIn(email: String, password: String, onSuccess: () -> Unit, onFailure: (String?) -> Unit) {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        onSuccess()
                    } else {
                        onFailure(task.exception?.message)
                    }
                }
        }

        fun signUp(email: String, password: String, onSuccess: () -> Unit, onFailure: (String?) -> Unit) {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        onSuccess()
                    } else {
                        onFailure(task.exception?.message)
                    }
                }
        }

        fun signOut() {
            auth.signOut()
        }

        fun getCurrentUser() = auth.currentUser


    }
}