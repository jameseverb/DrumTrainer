package com.example.drumtrainer.ui.template

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.drumtrainer.DrumTrainerApp
import com.example.drumtrainer.data.local.entity.TrainingTemplate
import com.example.drumtrainer.data.repository.TrainingRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TemplateViewModel(
    private val repository: TrainingRepository,
) : ViewModel() {

    val templates: StateFlow<List<TrainingTemplate>> =
        repository.observeTemplates()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun createTemplate(name: String) {
        val trimmed = name.trim()
        if (trimmed.isEmpty()) return
        viewModelScope.launch { repository.addTemplate(trimmed) }
    }

    fun deleteTemplate(template: TrainingTemplate) {
        viewModelScope.launch { repository.deleteTemplate(template) }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = this[APPLICATION_KEY] as DrumTrainerApp
                TemplateViewModel(app.container.trainingRepository)
            }
        }
    }
}
