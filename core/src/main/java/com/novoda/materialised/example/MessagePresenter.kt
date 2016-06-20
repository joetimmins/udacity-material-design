package com.novoda.materialised.example

class MessagePresenter(toggleMessages: ToggleMessages) {
    private val toggleMessages: ToggleMessages = toggleMessages
    private var isToggled = false

    fun currentMessage(): String {
        return if (shouldShowDefaultMessage()) toggleMessages.defaultMessage else toggleMessages.toggledMessage
    }

    private fun shouldShowDefaultMessage(): Boolean {
        val isCurrentlyToggled = isToggled

        isToggled = !isToggled

        return isCurrentlyToggled
    }
}
