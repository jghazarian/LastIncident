package com.jghazarian.lastincident.navigation

object HomeDestination : NavigationDestination {
    override val route = "home"
    override val titleRes: Int = 0  //TODO: get resource
}

object EntryDestination : NavigationDestination {
    override val route: String = "entry"
    override val titleRes: Int = 1
}

object DetailDestination : NavigationDestination {
    override val route: String = "detail"
    override val titleRes: Int = 2
    const val incidentIdArg = "incidentId"
    val routeWithArgs = "$route/{$incidentIdArg}"
}