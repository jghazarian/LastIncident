package com.jghazarian.lastincident

import com.jghazarian.lastincident.viewmodel.IncidentEntryViewModel
import com.jghazarian.lastincident.viewmodel.MainViewModel
import com.jghazarian.lastincident.viewmodel.IncidentDetailViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    viewModelOf(::IncidentEntryViewModel)
    viewModelOf(::MainViewModel)
    viewModelOf(::IncidentDetailViewModel)
}