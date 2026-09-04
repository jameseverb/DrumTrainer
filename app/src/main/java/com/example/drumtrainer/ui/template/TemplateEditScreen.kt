package com.example.drumtrainer.ui.template

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.drumtrainer.R
import com.example.drumtrainer.data.local.entity.TrainingProject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TemplateEditScreen(
    viewModel: TemplateEditViewModel,
    onBack: () -> Unit,
) {
    val template by viewModel.template.collectAsStateWithLifecycle()
    val projects by viewModel.projects.collectAsStateWithLifecycle()

    var showEditor by remember { mutableStateOf(false) }
    var editingProject by remember { mutableStateOf<TrainingProject?>(null) }
    var pendingDelete by remember { mutableStateOf<TrainingProject?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(template?.name.orEmpty()) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.back))
                    }
                },
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                editingProject = null
                showEditor = true
            }) {
                Icon(Icons.Filled.Add, contentDescription = stringResource(R.string.add_project))
            }
        },
    ) { innerPadding ->
        if (projects.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(24.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = stringResource(R.string.empty_project_list),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyLarge,
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                items(projects, key = { it.id }) { project ->
                    ProjectRow(
                        project = project,
                        onEdit = {
                            editingProject = project
                            showEditor = true
                        },
                        onDelete = { pendingDelete = project },
                    )
                }
            }
        }
    }

    if (showEditor) {
        ProjectEditorDialog(
            initial = editingProject,
            onConfirm = { title, content, needsBpm ->
                val project = editingProject
                if (project == null) {
                    viewModel.addProject(title, content, needsBpm)
                } else {
                    viewModel.updateProject(project, title, content, needsBpm)
                }
                showEditor = false
            },
            onDismiss = { showEditor = false },
        )
    }

    // 项目删除确认（防误删）
    pendingDelete?.let { project ->
        AlertDialog(
            onDismissRequest = { pendingDelete = null },
            title = { Text(stringResource(R.string.delete)) },
            text = { Text(stringResource(R.string.delete_project_confirm)) },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.deleteProject(project)
                    pendingDelete = null
                }) {
                    Text(stringResource(R.string.delete))
                }
            },
            dismissButton = {
                TextButton(onClick = { pendingDelete = null }) {
                    Text(stringResource(R.string.cancel))
                }
            },
        )
    }
}

@Composable
private fun ProjectRow(
    project: TrainingProject,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = project.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f),
                )
                if (project.needsBpm) {
                    Text(
                        text = stringResource(R.string.bpm_label),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.secondary,
                    )
                }
                IconButton(onClick = onEdit) {
                    Icon(Icons.Filled.Edit, contentDescription = stringResource(R.string.edit))
                }
                IconButton(onClick = onDelete) {
                    Icon(Icons.Filled.Delete, contentDescription = stringResource(R.string.delete))
                }
            }
            Text(
                text = project.content,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun ProjectEditorDialog(
    initial: TrainingProject?,
    onConfirm: (title: String, content: String, needsBpm: Boolean) -> Unit,
    onDismiss: () -> Unit,
) {
    var title by remember { mutableStateOf(initial?.title.orEmpty()) }
    var content by remember { mutableStateOf(initial?.content.orEmpty()) }
    var needsBpm by remember { mutableStateOf(initial?.needsBpm ?: false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(if (initial == null) R.string.add_project else R.string.edit)) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text(stringResource(R.string.project_title_hint)) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                )
                OutlinedTextField(
                    value = content,
                    onValueChange = { content = it },
                    label = { Text(stringResource(R.string.project_content_hint)) },
                    minLines = 3,
                    modifier = Modifier.fillMaxWidth(),
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = needsBpm, onCheckedChange = { needsBpm = it })
                    Text(stringResource(R.string.needs_bpm))
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = { onConfirm(title, content, needsBpm) },
                enabled = title.isNotBlank(),
            ) {
                Text(stringResource(R.string.save))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.cancel))
            }
        },
    )
}
