package com.jghazarian.lastincident

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

//class AppContainer(
//    private val factory: Factory
//) {
//    //TODO: This is scaffolding to add api, db, datastore, and scope
//    val dataRepository: DataRepository by lazy {
//        DataRepository(
//            database = factory.createRoomDatabase(),
//            incidentDataStore = factory.createIncidentDataStore(),
//            scope = CoroutineScope(Dispatchers.Default + SupervisorJob())
//        )
//    }
//}