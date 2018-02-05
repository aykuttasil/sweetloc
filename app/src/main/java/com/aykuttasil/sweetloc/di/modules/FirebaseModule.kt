package com.aykuttasil.sweetloc.di.modules

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class FirebaseModule {

    @Provides
    @Singleton
    fun databaseReference(): DatabaseReference = FirebaseDatabase.getInstance().reference

    @Provides
    @Singleton
    fun firebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    /*
    @Singleton
    @Provides
    internal fun firebaseAuthService(firebaseAuth: FirebaseAuth): FirebaseAuthService = FirebaseAuthService(firebaseAuth)

    @Singleton
    @Provides
    internal fun firebaseDbService(database: DatabaseReference): FirebaseDbService = FirebaseDbService(database)
    */
}
