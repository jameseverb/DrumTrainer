package com.example.drumtrainer.ui.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.drumtrainer.DrumTrainerApp
import com.example.drumtrainer.data.local.entity.TrainingSession
import com.example.drumtrainer.data.local.relation.SessionWithRecords
import com.example.drumtrainer.data.repository.TrainingRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HistoryViewModel(
    private val repository: TrainingRepository,
) : ViewModel() {

    val sessions: StateFlow<List<SessionWithRecords>> =
        repository.observeSessionsWithRecords()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    /** 删除一条训练记录（Room Flow 会自动刷新列表） */
    fun deleteSession(session: TrainingSession) {
        viewModelScope.launch { repository.deleteSession(session) }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = this[APPLICATION_KEY] as DrumTrainerApp
                HistoryViewModel(app.container.trainingRepository)
            }
        }
    }
}
