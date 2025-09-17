package com.appfolks.mtgedhmatchtracker.store

import com.appfolks.mtgedhmatchtracker.domain.model.CommanderColor
import com.appfolks.mtgedhmatchtracker.domain.model.Player
import com.appfolks.mtgedhmatchtracker.domain.model.PlayerSlot
import com.arkivanov.mvikotlin.core.store.Store

interface MatchScreenStore : Store<
        MatchScreenStore.Intent,
        MatchScreenStore.State,
        MatchScreenStore.Label,
    > {

    sealed interface Intent {
        data class OpenPlayerDialog(val slot: PlayerSlot) : Intent
        object ClosePlayerDialog : Intent
        data class UpdatePlayerName(val name: String) : Intent
        data class UpdateCommanderName(val name: String) : Intent
        data class UpdateCommanderColor(val color: CommanderColor) : Intent
        object ConfirmPlayerDetails : Intent
        data class ClearPlayerSlot(val slot: PlayerSlot) : Intent
    }

    data class State(
        val players: Map<PlayerSlot, Player> = emptyMap(),
        val selectedSlot: PlayerSlot? = null,
        val isDialogOpen: Boolean = false,
        val tempPlayerName: String = "",
        val tempCommanderName: String = "",
        val tempCommanderColor: CommanderColor = CommanderColor.COLORLESS,
    ) {
        val canConfirm: Boolean
            get() = tempPlayerName.isNotBlank() && tempCommanderName.isNotBlank()
    }

    sealed interface Label {
        data class ShowMessage(val message: String) : Label
        //TODO refactor dialog opening to a Label navigation
    }
}
