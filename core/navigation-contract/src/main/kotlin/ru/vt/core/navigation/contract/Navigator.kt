package ru.vt.core.navigation.contract

interface Navigator {
    fun navigate(args: RouteArgs)
    fun onBackPressed()
}