package com.appfolks.mtgedhmatchtracker.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.appfolks.mtgedhmatchtracker.domain.model.CommanderColor
import com.appfolks.mtgedhmatchtracker.domain.model.PlayerSlot

@Composable
fun PlayerInputDialog(
    playerSlot: PlayerSlot,
    playerName: String,
    commanderName: String,
    selectedColor: CommanderColor,
    canConfirm: Boolean,
    onPlayerNameChange: (String) -> Unit,
    onCommanderNameChange: (String) -> Unit,
    onColorSelected: (CommanderColor) -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) = Dialog(onDismissRequest = onDismiss) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            PlayerInputHeader(playerSlot)

            PlayerNameInput(
                playerName = playerName,
                onPlayerNameChange = onPlayerNameChange,
            )

            CommanderNameInput(
                commanderName = commanderName,
                onCommanderNameChange = onCommanderNameChange,
            )

            CommanderColorSelector(
                selectedColor = selectedColor,
                onColorSelected = onColorSelected,
            )

            Buttons(
                onDismiss = onDismiss,
                onConfirm = onConfirm,
                canConfirm = canConfirm,
            )
        }
    }
}

@Composable
private fun PlayerInputHeader(playerSlot: PlayerSlot) = Text(
    text = "Configure ${playerSlot.displayName}",
    fontSize = 24.sp,
    fontWeight = FontWeight.Bold,
    color = MaterialTheme.colorScheme.onSurface,
)

@Composable
private fun PlayerNameInput(
    playerName: String,
    onPlayerNameChange: (String) -> Unit,
) = OutlinedTextField(
    value = playerName,
    onValueChange = onPlayerNameChange,
    label = { Text("Player Name") },
    modifier = Modifier.fillMaxWidth(),
    singleLine = true,
    colors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = MaterialTheme.colorScheme.primary,
        unfocusedBorderColor = MaterialTheme.colorScheme.outline,
    )
)

@Composable
private fun CommanderNameInput(
    commanderName: String,
    onCommanderNameChange: (String) -> Unit,
) = OutlinedTextField(
    value = commanderName,
    onValueChange = onCommanderNameChange,
    label = { Text("Commander Name") },
    modifier = Modifier.fillMaxWidth(),
    singleLine = true,
    colors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = MaterialTheme.colorScheme.primary,
        unfocusedBorderColor = MaterialTheme.colorScheme.outline,
    ),
)

@Composable
private fun CommanderColorSelector(
    selectedColor: CommanderColor,
    onColorSelected: (CommanderColor) -> Unit,
) {
    Column {
        Text(
            text = "Commander Color",
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(8.dp))
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(CommanderColor.entries.toTypedArray()) { color ->
                CommanderColorChip(
                    commanderColor = color,
                    isSelected = color == selectedColor,
                    onClick = { onColorSelected(color) },
                )
            }
        }
    }
}

@Composable
private fun Buttons(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    canConfirm: Boolean,
) = Row(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.End),
) {
    TextButton(
        onClick = onDismiss,
    ) {
        Text("Cancel")
    }

    Button(
        onClick = onConfirm,
        enabled = canConfirm,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            disabledContainerColor = MaterialTheme.colorScheme.outline,
        ),
    ) {
        Text("Confirm")
    }
}

@Composable
private fun CommanderColorChip(
    commanderColor: CommanderColor,
    isSelected: Boolean,
    onClick: () -> Unit,
) = Surface(
    shadowElevation = if (isSelected) 4.dp else 0.dp,
    modifier = Modifier
        .size(60.dp, 40.dp)
        .clip(CircleShape)
        .background(commanderColor.color)
        .border(
            width = if (isSelected) 3.dp else 1.dp,
            color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
            shape = CircleShape,
        )
        .clickable { onClick() },
) {}
