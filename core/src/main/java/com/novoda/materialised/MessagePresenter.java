package com.novoda.materialised;

public class MessagePresenter {
    private final ToggleMessages toggleMessages;
    private boolean isToggled = false;

    public MessagePresenter(ToggleMessages toggleMessages) {
        this.toggleMessages = toggleMessages;
    }

    private boolean shouldShowDefaultMessage() {
        Boolean isCurrentlyToggled = isToggled;

        isToggled = !isToggled;

        return isCurrentlyToggled;
    }

    public String currentMessage() {
        return shouldShowDefaultMessage() ? toggleMessages.getDefaultMessage() : toggleMessages.getToggledMessage();
    }
}
