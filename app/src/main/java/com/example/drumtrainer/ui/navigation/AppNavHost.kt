package com.example.drumtrainer.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.drumtrainer.ui.history.HistoryScreen
import com.example.drumtrainer.ui.history.HistoryViewModel
import com.example.drumtrainer.ui.template.TemplateEditScreen
import com.example.drumtrainer.ui.template.TemplateEditViewModel
import com.example.drumtrainer.ui.template.TemplateListScreen
import com.example.drumtrainer.ui.template.TemplateViewModel
import com.example.drumtrainer.ui.training.TrainingScreen
import com.example.drumtrainer.ui.training.TrainingViewModel

/** 集中定义所有路由 */
object Routes {
    const val TEMPLATE_LIST = "template_list"
    const val TRAINING = "training/{templateId}"
    const val TEMPLATE_EDIT = "template_edit/{templateId}"
    const val HISTORY = "history"

    fun training(templateId: Long) = "training/$templateId"
    fun templateEdit(templateId: Long) = "template_edit/$templateId"
}

@Composable
fun AppNavHost() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Routes.TEMPLATE_LIST) {

        composable(Routes.TEMPLATE_LIST) {
            val vm: TemplateViewModel = viewModel(factory = TemplateViewModel.Factory)
            TemplateListScreen(
                viewModel = vm,
                onOpenTraining = { id -> navController.navigate(Routes.training(id)) },
                onEditTemplate = { id -> navController.navigate(Routes.templateEdit(id)) },
                onOpenHistory = { navController.navigate(Routes.HISTORY) },
            )
        }

        composable(
            route = Routes.TRAINING,
            arguments = listOf(navArgument("templateId") { type = NavType.LongType }),
        ) {
            val vm: TrainingViewModel = viewModel(factory = TrainingViewModel.Factory)
            TrainingScreen(
                viewModel = vm,
                onBack = { navController.popBackStack() },
            )
        }

        composable(
            route = Routes.TEMPLATE_EDIT,
            arguments = listOf(navArgument("templateId") { type = NavType.LongType }),
        ) {
            val vm: TemplateEditViewModel = viewModel(factory = TemplateEditViewModel.Factory)
            TemplateEditScreen(
                viewModel = vm,
                onBack = { navController.popBackStack() },
            )
        }

        composable(Routes.HISTORY) {
            val vm: HistoryViewModel = viewModel(factory = HistoryViewModel.Factory)
            HistoryScreen(
                viewModel = vm,
                onBack = { navController.popBackStack() },
            )
        }
    }
}
