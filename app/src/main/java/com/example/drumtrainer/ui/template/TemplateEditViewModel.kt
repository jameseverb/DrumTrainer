package com.example.drumtrainer.ui.template

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.drumtrainer.DrumTrainerApp
import com.example.drumtrainer.data.local.entity.TrainingProject
import com.example.drumtrainer.data.local.entity.TrainingTemplate
import com.example.drumtrainer.data.repository.TrainingRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TemplateEditViewModel(
    private val repository: TrainingRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val templateId: Long = checkNotNull(savedStateHandle.get<Long>("templateId")) {
        "缺少导航参数 templateId"
    }

    val template: StateFlow<TrainingTemplate?> = repository.observeTemplate(templateId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    val projects: StateFlow<List<TrainingProject>> = repository.observeProjects(templateId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun addProject(title: String, content: String, needsBpm: Boolean) {
        viewModelScope.launch {
            repository.addProject(
                TrainingProject(
                    templateId = templateId,
                    title = title.trim(),
                    content = content.trim(),
                    needsBpm = needsBpm,
                    sortOrder = projects.value.size,
                )
            )
        }
    }

    fun updateProject(project: TrainingProject, title: String, content: String, needsBpm: Boolean) {
        viewModelScope.launch {
            repository.updateProject(
                project.copy(title = title.trim(), content = content.trim(), needsBpm = needsBpm)
            )
        }
    }

    fun deleteProject(project: TrainingProject) {
        viewModelScope.launch { repository.deleteProject(project) }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = this[APPLICATION_KEY] as DrumTrainerApp
                TemplateEditViewModel(
                    repository = app.container.trainingRepository,
                    savedStateHandle = createSavedStateHandle(),
                )
            }
        }
    }
}
