package com.example.myapplication.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.viewmodel.ProfileViewModel

@Composable
fun ProfileScreen(viewModel: ProfileViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsState()

    // State lokal untuk menampung input sebelum tombol Save ditekan
    var tempName by remember { mutableStateOf(uiState.name) }
    var tempBio by remember { mutableStateOf(uiState.bio) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(text = "Edit Profile", style = MaterialTheme.typography.h5, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(16.dp))

        // Toggle Dark Mode [cite: 572]
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Dark Mode")
            Switch(
                checked = uiState.isDarkMode,
                onCheckedChange = { viewModel.toggleDarkMode(it) }
            )
        }

        // Implementasi State Hoisting [cite: 570]
        LabeledTextField(label = "Nama", value = tempName, onValueChange = { tempName = it })
        LabeledTextField(label = "Bio", value = tempBio, onValueChange = { tempBio = it })

        Button(
            onClick = { viewModel.updateProfile(tempName, tempBio) },
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
        ) {
            Text("Save Profile")
        }

        Divider(modifier = Modifier.padding(vertical = 24.dp))

        // Preview Card
        Card(elevation = 4.dp, modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Preview Data:", style = MaterialTheme.typography.subtitle2)
                Text("Nama: ${uiState.name}", style = MaterialTheme.typography.body1)
                Text("Bio: ${uiState.bio}", style = MaterialTheme.typography.body2)
            }
        }
    }
}