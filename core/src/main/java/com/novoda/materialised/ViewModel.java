package com.novoda.materialised;

public class ViewModel {
    private boolean isToggled = false;

    public boolean isToggled() {
        Boolean isCurrentlyToggled = isToggled;

        isToggled = !isToggled;

        return isCurrentlyToggled;
    }
}
