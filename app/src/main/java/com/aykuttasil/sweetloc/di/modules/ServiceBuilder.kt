/* Author - Aykut Asil(aykuttasil) */
package com.aykuttasil.sweetloc.di.modules

import com.aykuttasil.sweetloc.service.SingleLocationRequestService
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ServiceBuilder {

    @ContributesAndroidInjector
    abstract fun bindSingleLocationRequestService(): SingleLocationRequestService
}