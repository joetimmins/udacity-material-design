package com.novoda.materialised.example

class MessagePresenter(toggleMessages: ToggleMessages) {
    private val toggleMessages: ToggleMessages = toggleMessages
    private var isToggled = false

    private fun shouldShowDefaultMessage(): Boolean {
        val isCurrentlyToggled = isToggled

        isToggled = !isToggled

        return isCurrentlyToggled
    }

    fun currentMessage(): String {
        return if (shouldShowDefaultMessage()) toggleMessages.defaultMessage else toggleMessages.toggledMessage
    }
}
