package com.appfolks.mtgedhmatchtracker.store

import com.appfolks.mtgedhmatchtracker.domain.model.CommanderColor
import com.appfolks.mtgedhmatchtracker.domain.model.Player
import com.appfolks.mtgedhmatchtracker.domain.model.PlayerSlot
import com.appfolks.mtgedhmatchtracker.store.MatchScreenStore.Intent
import com.appfolks.mtgedhmatchtracker.store.MatchScreenStore.Label
import com.appfolks.mtgedhmatchtracker.store.MatchScreenStore.State
import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class MatchScreenStoreFactory(
    private val storeFactory: StoreFactory,
    private val mainDispatcher: CoroutineDispatcher = Dispatchers.Main,
) {

    fun create(): MatchScreenStore =
        object : MatchScreenStore, Store<Intent, State, Label> by storeFactory.create(
            name = MatchScreenStore::class.simpleName,
            initialState = State(),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl,
        ) {}

    private sealed interface Message {
        data class PlayerDialogOpened(val slot: PlayerSlot) : Message
        object PlayerDialogClosed : Message
        data class PlayerNameUpdated(val name: String) : Message
        data class CommanderNameUpdated(val name: String) : Message
        data class CommanderColorUpdated(val color: CommanderColor) : Message
        data class PlayerDetailsConfirmed(val slot: PlayerSlot, val player: Player) : Message
        data class PlayerSlotCleared(val slot: PlayerSlot) : Message
    }

    private inner class ExecutorImpl : CoroutineExecutor<Intent, Nothing, State, Message, Label>(
        mainContext = mainDispatcher
    ) {
        override fun executeIntent(intent: Intent) {
            when (intent) {
                is Intent.OpenPlayerDialog -> executeOpenPlayerDialog(intent)
                is Intent.ClosePlayerDialog -> executeClosePlayerDialog()
                is Intent.UpdatePlayerName -> executeUpdatePlayerName(intent)
                is Intent.UpdateCommanderName -> executeUpdateCommanderName(intent)
                is Intent.UpdateCommanderColor -> executeUpdateCommanderColor(intent)
                is Intent.ConfirmPlayerDetails -> executeConfirmPlayerDetails()
                is Intent.ClearPlayerSlot -> executeClearPlayerSlot(intent)
            }
        }
        
        private fun executeOpenPlayerDialog(intent: Intent.OpenPlayerDialog) {
            val existingPlayer = state().players[intent.slot]
            dispatch(Message.PlayerDialogOpened(intent.slot))
            existingPlayer?.let { player ->
                dispatch(Message.PlayerNameUpdated(player.playerName))
                dispatch(Message.CommanderNameUpdated(player.commanderName))
                dispatch(Message.CommanderColorUpdated(player.commanderColor))
            }
        }
        
        private fun executeClosePlayerDialog() {
            dispatch(Message.PlayerDialogClosed)
        }
        
        private fun executeUpdatePlayerName(intent: Intent.UpdatePlayerName) {
            dispatch(Message.PlayerNameUpdated(intent.name))
        }
        
        private fun executeUpdateCommanderName(intent: Intent.UpdateCommanderName) {
            dispatch(Message.CommanderNameUpdated(intent.name))
        }
        
        private fun executeUpdateCommanderColor(intent: Intent.UpdateCommanderColor) {
            dispatch(Message.CommanderColorUpdated(intent.color))
        }
        
        private fun executeConfirmPlayerDetails() {
            val currentState = state()
            val slot = currentState.selectedSlot
            
            if (slot != null && currentState.canConfirm) {
                val player = Player(
                    playerName = currentState.tempPlayerName.trim(),
                    commanderName = currentState.tempCommanderName.trim(),
                    commanderColor = currentState.tempCommanderColor,
                )
                dispatch(Message.PlayerDetailsConfirmed(slot, player))
                dispatch(Message.PlayerDialogClosed)
                publish(
                    Label.ShowMessage(
                        message = "Player ${player.playerName} with commander " +
                                "${player.commanderName} added successfully!"
                    )
                )
            } else {
                publish(Label.ShowMessage("Please fill in all required fields"))
            }
        }
        
        private fun executeClearPlayerSlot(intent: Intent.ClearPlayerSlot) {
            dispatch(Message.PlayerSlotCleared(intent.slot))
            publish(Label.ShowMessage("Player slot cleared"))
        }
    }

    private object ReducerImpl : Reducer<State, Message> {
        override fun State.reduce(msg: Message): State =
            when (msg) {
                is Message.PlayerDialogOpened -> reducePlayerDialogOpened(msg)
                is Message.PlayerDialogClosed -> reducePlayerDialogClosed()
                is Message.PlayerNameUpdated -> reducePlayerNameUpdated(msg)
                is Message.CommanderNameUpdated -> reduceCommanderNameUpdated(msg)
                is Message.CommanderColorUpdated -> reduceCommanderColorUpdated(msg)
                is Message.PlayerDetailsConfirmed -> reducePlayerDetailsConfirmed(msg)
                is Message.PlayerSlotCleared -> reducePlayerSlotCleared(msg)
            }
        
        private fun State.reducePlayerDialogOpened(
            msg: Message.PlayerDialogOpened,
        ): State = copy(
            selectedSlot = msg.slot,
            isDialogOpen = true,
            tempPlayerName = "",
            tempCommanderName = "",
            tempCommanderColor = CommanderColor.COLORLESS,
        )
        
        private fun State.reducePlayerDialogClosed(): State = copy(
            selectedSlot = null,
            isDialogOpen = false,
            tempPlayerName = "",
            tempCommanderName = "",
            tempCommanderColor = CommanderColor.COLORLESS,
        )
        
        private fun State.reducePlayerNameUpdated(
            msg: Message.PlayerNameUpdated,
        ): State = copy(
            tempPlayerName = msg.name,
        )
        
        private fun State.reduceCommanderNameUpdated(
            msg: Message.CommanderNameUpdated,
        ): State = copy(
            tempCommanderName = msg.name,
        )
        
        private fun State.reduceCommanderColorUpdated(
            msg: Message.CommanderColorUpdated
        ): State = copy(
            tempCommanderColor = msg.color,
        )
        
        private fun State.reducePlayerDetailsConfirmed(
            msg: Message.PlayerDetailsConfirmed
        ): State = copy(
            players = players + (msg.slot to msg.player),
        )
        
        private fun State.reducePlayerSlotCleared(
            msg: Message.PlayerSlotCleared
        ): State = copy(
            players = players - msg.slot,
        )
    }
}
