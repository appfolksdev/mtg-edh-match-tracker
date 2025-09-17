package com.appfolks.mtgedhmatchtracker.domain.model

data class Player(
    val playerName: String,
    val commanderName: String,
    val commanderColor: CommanderColor,
) {
    val isAdded: Boolean
        get() = playerName.isNotBlank() && commanderName.isNotBlank()
}
