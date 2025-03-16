package com.loc.newsapp.presentation.nvgraph

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.loc.newsapp.data.user.UserViewModel
import com.loc.newsapp.presentation.bookmark.BookmarkScreen
import com.loc.newsapp.presentation.bookmark.BookmarkViewModel
import com.loc.newsapp.presentation.login.LoginScreen
import com.loc.newsapp.presentation.login.RegisterScreen
import com.loc.newsapp.presentation.news_navigator.NewsNavigator
import com.loc.newsapp.presentation.onboarding.OnBoardingScreen
import com.loc.newsapp.presentation.onboarding.OnBoardingViewModel

@Composable
fun NavGraph(
    startDestination: String
) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = startDestination) {
        navigation(
            route = Route.AppStartNavigation.route,
            startDestination = Route.LoginScreen.route
        ) {
            composable(route = Route.OnBoardingScreen.route) {
                val viewModel: OnBoardingViewModel = hiltViewModel()
                OnBoardingScreen(
                    event = viewModel::onEvent,
                    onNavigateToLogin = {
                        navController.navigate(Route.LoginScreen.route) {
                            popUpTo(Route.OnBoardingScreen.route) { inclusive = true }
                        }
                    }
                )
            }
            composable(route = Route.LoginScreen.route) {
                val userViewModel: UserViewModel = hiltViewModel()
                LoginScreen(
                    userViewModel = userViewModel,
                    onLoginSuccess = {
                        navController.navigate(Route.NewsNavigation.route) {
                            popUpTo(Route.AppStartNavigation.route) { inclusive = true }
                        }
                    },
                    onNavigateToRegister = {
                        navController.navigate("register")
                    }
                )
            }
            composable(route = "register") {
                val userViewModel: UserViewModel = hiltViewModel()
                RegisterScreen(
                    userViewModel = userViewModel,
                    onRegisterSuccess = {
                        navController.navigate(Route.LoginScreen.route) {
                            popUpTo("register") { inclusive = true }
                        }
                    },
                    onNavigateToLogin = {
                        navController.navigateUp()
                    }
                )
            }
        }

        navigation(
            route = Route.NewsNavigation.route,
            startDestination = Route.NewsNavigatorScreen.route
        ) {
            composable(route = Route.NewsNavigatorScreen.route) {
                NewsNavigator()
            }

            composable(route = Route.BookmarkScreen.route) {
                val bookmarkViewModel: BookmarkViewModel = hiltViewModel()
                BookmarkScreen(
                    state = bookmarkViewModel.state.value,
                    navigateToDetails = { article ->
                        navController.navigate(Route.DetailsScreen.route.plus("/${article.url}"))
                    },
                    viewModel = bookmarkViewModel
                )
            }
        }
    }
}