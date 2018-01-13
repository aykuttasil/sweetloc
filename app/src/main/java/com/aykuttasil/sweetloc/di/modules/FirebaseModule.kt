package com.aykuttasil.sweetloc.di.modules

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import dagger.Module
import dagger.Provides

@Module
class FirebaseModule {

    @Provides
    internal fun databaseReference(): DatabaseReference = FirebaseDatabase.getInstance().reference

    @Provides
    internal fun firebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    /*
    @Singleton
    @Provides
    internal fun firebaseAuthService(firebaseAuth: FirebaseAuth): FirebaseAuthService = FirebaseAuthService(firebaseAuth)

    @Singleton
    @Provides
    internal fun firebaseDbService(database: DatabaseReference): FirebaseDbService = FirebaseDbService(database)
    */
}
