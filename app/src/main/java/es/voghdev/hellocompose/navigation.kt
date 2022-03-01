import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import es.voghdev.hellocompose.ChipsScreen
import es.voghdev.hellocompose.DraggableScreen
import es.voghdev.hellocompose.ItemsScreen
import es.voghdev.hellocompose.MainScreen

@Composable
fun HelloAppNavigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "main") {
        composable(NavItem.Main.route) {
            MainScreen(
                onOpenGridClick = {
                    navController.navigate(NavItem.Chips.route)
                },
                onOpenItemsClick = {
                    navController.navigate(NavItem.ItemsList.route)
                },
                onOpenDraggableClick = {
                    navController.navigate(NavItem.Draggable.route)
                }
            )
        }
        composable(NavItem.Chips.route) {
            ChipsScreen()
        }
        composable(NavItem.ItemsList.route) {
            ItemsScreen()
        }
        composable(NavItem.Draggable.route) {
            DraggableScreen()
        }
    }
}

sealed class NavItem(
    val baseRoute: String,
    val navArgs: List<Args> = emptyList()
) {
    val route = run {
        val argKeys = navArgs.map { "{${it.key}}" }
        listOf(baseRoute).plus(argKeys).joinToString("/")
    }

//    val args = navArgs.map {
//        navArgument(it.key) { type = it.navType }
//    }

    object Main : NavItem("main")
    object Chips : NavItem("chips")
    object ItemsList : NavItem("items")
    object Draggable : NavItem("draggable")
}

enum class Args(val key: String, val navType: NavType<*>) {
    InitialId("id", NavType.IntType)
    // TODO: try ReferenceType
    // TODO: try IntArrayType
}