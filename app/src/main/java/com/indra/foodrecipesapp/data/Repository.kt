package com.indra.foodrecipesapp.data

import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

@ActivityRetainedScoped
class Repository @Inject constructor(
    remoteDataStore: RemoteDataStore
) {

    val remote = remoteDataStore
}