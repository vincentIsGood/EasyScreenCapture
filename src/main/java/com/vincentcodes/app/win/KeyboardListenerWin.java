package com.vincentcodes.app.win;

import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.*;
import com.vincentcodes.app.KeyListener;

import java.io.Closeable;
import java.io.IOException;
import java.util.Objects;

public class KeyboardListenerWin implements Closeable, Runnable {
    // Handle Hook
    private User32.HHOOK keyboardHook;
    private User32.LowLevelKeyboardProc lowLevelKeyListenerFuncPtr;

    private boolean started = false;
    private boolean shiftDown = false;
    private boolean ctrlDown = false;

    private KeyListener keyListener;

    public KeyboardListenerWin(KeyListener keyListener) {
        Objects.requireNonNull(keyListener);
        this.keyListener = keyListener;
    }

    @Override
    public void run(){
        if(started)
            throw new IllegalStateException("Cannot start the same listener twice.");
        User32.HMODULE currentProcess = Kernel32.INSTANCE.GetModuleHandle(null);
        lowLevelKeyListenerFuncPtr = new WindowsKeyHandler();

        keyboardHook = User32.INSTANCE.SetWindowsHookEx(
                User32.WH_KEYBOARD_LL, lowLevelKeyListenerFuncPtr, currentProcess, 0);
        if(keyboardHook == null) return;

        User32.MSG msg = new User32.MSG();
        while(true){
            User32.INSTANCE.GetMessage(msg, null, 0, 0);
        }
    }

    /**
     * Unhook and set state to not started.
     */
    @Override
    public void close() throws IOException {
        if(keyboardHook != null)
            User32.INSTANCE.UnhookWindowsHookEx(keyboardHook);
        started = false;
    }

    /**
     * https://learn.microsoft.com/en-us/windows/win32/winmsg/lowlevelkeyboardproc
     */
    private class WindowsKeyHandler implements WinUser.LowLevelKeyboardProc{

        @Override
        public User32.LRESULT callback(int nCode, User32.WPARAM wParam, User32.KBDLLHOOKSTRUCT lParam) {
            if(nCode < 0)
                return User32.INSTANCE.CallNextHookEx(keyboardHook, nCode, wParam, new WinDef.LPARAM(Pointer.nativeValue(lParam.getPointer())));

            int keycode = wParam.intValue();
            if(keycode == WinUser.WM_KEYDOWN){
                int vkCode = lParam.vkCode;
                if(vkCode == WinUser.VK_LCONTROL || vkCode == WinUser.VK_RCONTROL) ctrlDown = true;
                if(vkCode == WinUser.VK_LSHIFT || vkCode == WinUser.VK_RSHIFT) shiftDown = true;
                keyListener.handle(lParam.vkCode, ctrlDown, shiftDown);
            }else if(keycode == WinUser.WM_KEYUP){
                int vkCode = lParam.vkCode;
                if(vkCode == WinUser.VK_LCONTROL || vkCode == WinUser.VK_RCONTROL) ctrlDown = false;
                if(vkCode == WinUser.VK_LSHIFT || vkCode == WinUser.VK_RSHIFT) shiftDown = false;
            }
            return new User32.LRESULT(0);
        }
    }
}
