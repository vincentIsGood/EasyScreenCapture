package com.vincentcodes.app.consts;

import com.sun.jna.platform.win32.Win32VK;
import com.vincentcodes.app.utils.TriPredicate;

public class KeyMap {
    /**
     * Kill this capturing program with ":" key
     */
    public static TriPredicate IS_KILL_PROGRAM = (key, ctrl, shift) -> ctrl && shift && key == Win32VK.VK_OEM_1.code;

    /**
     * Toggle program "`" key
     */
    public static TriPredicate IS_TOGGLE_PROGRAM = (key, ctrl, shift) -> ctrl && shift && key == Win32VK.VK_OEM_3.code;

    // ---------------- START ffmpeg ---------------- //

    /**
     * From number 1 on your keyboard
     */
    public static TriPredicate CAP_SCREEN_WITH_AUDIO = (key, ctrl, shift) -> ctrl && shift && key == Win32VK.VK_1.code;
    public static TriPredicate CAP_SCREEN_ONLY = (key, ctrl, shift) -> ctrl && shift && key == Win32VK.VK_2.code;
    public static TriPredicate CAP_SCREEN_WITH_MIC = (key, ctrl, shift) -> ctrl && shift && key == Win32VK.VK_3.code;
    public static TriPredicate CAP_AUDIO_ONLY = (key, ctrl, shift) -> ctrl && shift && key == Win32VK.VK_4.code;
    public static TriPredicate CAP_MIC_ONLY = (key, ctrl, shift) -> ctrl && shift && key == Win32VK.VK_5.code;

    // ---------------- STOP ffmpeg ---------------- //

    /**
     * Term ffmpeg is the way to stop ffmpeg gracefully (ie. sending 'q' to ffmpeg).
     */
    public static TriPredicate IS_TERM_FFMPEG = (key, ctrl, shift) -> ctrl && shift && key == Win32VK.VK_Q.code;

    /**
     * Kill ffmpeg means to stop ffmpeg without ending the program correctly.
     */
    public static TriPredicate IS_KILL_FFMPEG = (key, ctrl, shift) -> ctrl && shift && key == Win32VK.VK_K.code;
}
