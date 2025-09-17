package com.appfolks.mtgedhmatchtracker.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.appfolks.mtgedhmatchtracker.domain.model.PlayerSlot
import com.appfolks.mtgedhmatchtracker.store.MatchScreenStore
import com.appfolks.mtgedhmatchtracker.store.MatchScreenStore.Intent.ClearPlayerSlot
import com.appfolks.mtgedhmatchtracker.store.MatchScreenStore.Intent.OpenPlayerDialog
import com.appfolks.mtgedhmatchtracker.ui.components.PlayerInputDialog
import com.appfolks.mtgedhmatchtracker.ui.components.PlayerSlotCard
import com.arkivanov.mvikotlin.core.rx.observer
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import kotlinx.coroutines.ExperimentalCoroutinesApi

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun MatchScreen(
    store: MatchScreenStore,
    modifier: Modifier = Modifier
) {
    val state by store.stateFlow.collectAsStateWithLifecycle()
    
    DisposableEffect(store) {
        val labelDisposable = store.labels(observer { label ->
                when (label) {
                    is MatchScreenStore.Label.ShowMessage -> {
                        //TODO show composable toast
                        println("Message: ${label.message}")
                    }
                }
            }
        )
        onDispose { labelDisposable.dispose() }
    }

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        PlayersSlots(
            state = state,
            store = store,
        )

        PlayerInputDialog(
            state = state,
            store = store,
        )
    }
}

@Composable
private fun PlayerInputDialog(
    state: MatchScreenStore.State,
    store: MatchScreenStore,
) = state.selectedSlot?.let { selectedSlot ->
    if (state.isDialogOpen) {
        PlayerInputDialog(
            playerSlot = selectedSlot,
            playerName = state.tempPlayerName,
            commanderName = state.tempCommanderName,
            selectedColor = state.tempCommanderColor,
            canConfirm = state.canConfirm,
            onPlayerNameChange = { name ->
                store.accept(MatchScreenStore.Intent.UpdatePlayerName(name))
            },
            onCommanderNameChange = { name ->
                store.accept(MatchScreenStore.Intent.UpdateCommanderName(name))
            },
            onColorSelected = { color ->
                store.accept(MatchScreenStore.Intent.UpdateCommanderColor(color))
            },
            onConfirm = {
                store.accept(MatchScreenStore.Intent.ConfirmPlayerDetails)
            },
            onDismiss = {
                store.accept(MatchScreenStore.Intent.ClosePlayerDialog)
            },
        )
    }
}

@Composable
private fun PlayersSlots(
    state: MatchScreenStore.State,
    store: MatchScreenStore,
) = Column(
    modifier = Modifier
        .fillMaxSize()
        .padding(16.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp),
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .weight(1f),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        PlayerSlotCard(
            player = state.players[PlayerSlot.TOP_LEFT],
            playerSlot = PlayerSlot.TOP_LEFT,
            onSlotClick = {
                store.accept(OpenPlayerDialog(PlayerSlot.TOP_LEFT))
            },
            onLongClick = if (state.players[PlayerSlot.TOP_LEFT] != null) {
                { store.accept(ClearPlayerSlot(PlayerSlot.TOP_LEFT)) }
            } else null,
            modifier = Modifier.weight(1f),
        )

        PlayerSlotCard(
            player = state.players[PlayerSlot.TOP_RIGHT],
            playerSlot = PlayerSlot.TOP_RIGHT,
            onSlotClick = {
                store.accept(OpenPlayerDialog(PlayerSlot.TOP_RIGHT))
            },
            onLongClick = if (state.players[PlayerSlot.TOP_RIGHT] != null) {
                { store.accept(ClearPlayerSlot(PlayerSlot.TOP_RIGHT)) }
            } else null,
            modifier = Modifier.weight(1f),
        )
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .weight(1f),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        PlayerSlotCard(
            player = state.players[PlayerSlot.BOTTOM_LEFT],
            playerSlot = PlayerSlot.BOTTOM_LEFT,
            onSlotClick = {
                store.accept(OpenPlayerDialog(PlayerSlot.BOTTOM_LEFT))
            },
            onLongClick = if (state.players[PlayerSlot.BOTTOM_LEFT] != null) {
                { store.accept(ClearPlayerSlot(PlayerSlot.BOTTOM_LEFT)) }
            } else null,
            modifier = Modifier.weight(1f),
        )

        PlayerSlotCard(
            player = state.players[PlayerSlot.BOTTOM_RIGHT],
            playerSlot = PlayerSlot.BOTTOM_RIGHT,
            onSlotClick = {
                store.accept(OpenPlayerDialog(PlayerSlot.BOTTOM_RIGHT))
            },
            onLongClick = if (state.players[PlayerSlot.BOTTOM_RIGHT] != null) {
                { store.accept(ClearPlayerSlot(PlayerSlot.BOTTOM_RIGHT)) }
            } else null,
            modifier = Modifier.weight(1f),
        )
    }
}
