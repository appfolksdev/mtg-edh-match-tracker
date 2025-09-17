package com.appfolks.mtgedhmatchtracker.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.appfolks.mtgedhmatchtracker.domain.model.Player
import com.appfolks.mtgedhmatchtracker.domain.model.PlayerSlot

@Composable
fun PlayerSlotCard(
    player: Player?,
    playerSlot: PlayerSlot,
    onSlotClick: () -> Unit,
    onLongClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
) {
    val backgroundColor = player?.commanderColor?.color ?: Color(0xFFE0E0E0)
    val textColor = player?.commanderColor?.textColor ?: Color.Black
    
    Box(
        modifier = modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(12.dp))
            .background(backgroundColor)
            .border(
                width = 2.dp,
                color = Color.Gray,
                shape = RoundedCornerShape(12.dp),
            )
            .combinedClickable(
                onClick = onSlotClick,
                onLongClickLabel = "Clear slot",
                onLongClick = onLongClick,
            ),
        contentAlignment = Alignment.Center,
    ) {
        if (player != null && player.isAdded) {
            AddedPlayerSlotCard(
                player = player,
                textColor = textColor,
            )
        } else EmptyPlayerSlotCard(playerSlot)
    }
}

@Composable
private fun AddedPlayerSlotCard(
    player: Player,
    textColor: Color,
) = Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center,
    modifier = Modifier.padding(16.dp),
) {
    Text(
        text = player.commanderName,
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        color = textColor,
        textAlign = TextAlign.Center,
        maxLines = 2,
    )

    Spacer(modifier = Modifier.height(8.dp))

    Text(
        text = "controlled by ${player.playerName}",
        fontSize = 14.sp,
        fontWeight = FontWeight.Normal,
        color = textColor.copy(alpha = 0.8f),
        textAlign = TextAlign.Center,
        maxLines = 2,
    )
}

@Composable
private fun EmptyPlayerSlotCard(playerSlot: PlayerSlot) = Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center,
) {
    Text(
        text = "Tap to add\n${playerSlot.displayName}",
        fontSize = 16.sp,
        fontWeight = FontWeight.Medium,
        color = Color.Gray,
        textAlign = TextAlign.Center,
    )
}
