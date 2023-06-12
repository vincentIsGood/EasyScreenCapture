package com.vincentcodes.app;

public interface KeyListener {
    /**
     * @param keyCode for windows, use Win32VK class to check keycode.
     */
    void handle(int keyCode, boolean isCtrlDown, boolean isShiftDown);
}
