package ocr_bot.win_api

import com.sun.jna.Native
import com.sun.jna.Pointer
import com.sun.jna.platform.win32.WinDef
import com.sun.jna.ptr.PointerByReference

/**
 *  Api związane z oknami windowsa
 */
class Window {

    internal object Psapi {
        external fun GetModuleBaseNameW(hProcess: Pointer?, hmodule: Pointer?, lpBaseName: CharArray?, size: Int): Int

        init {
            Native.register("psapi")
        }
    }

    internal object Kernel32 {
        var PROCESS_QUERY_INFORMATION = 0x0400
        var PROCESS_VM_READ = 0x0010
        external fun GetLastError(): Int
        external fun OpenProcess(dwDesiredAccess: Int, bInheritHandle: Boolean, pointer: Pointer?): Pointer?

        init {
            Native.register("kernel32")
        }
    }

    internal object User32DLL {
        external fun GetWindowThreadProcessId(hWnd: WinDef.HWND?, pref: PointerByReference?): Int
        external fun GetForegroundWindow(): WinDef.HWND?
        external fun GetWindowTextW(hWnd: WinDef.HWND?, lpString: CharArray?, nMaxCount: Int): Int

        init {
            Native.register("user32")
        }
    }

    companion object {
        private const val MAX_TITLE_LENGTH = 1024

        /**
         *  Sprawdzenie czy okno z grą jest na wierzchu
         */
        @JvmStatic
        fun isTibiaWindowOnForeground(): Boolean {
            val buffer = CharArray(MAX_TITLE_LENGTH * 2)
            User32DLL.GetWindowTextW(User32DLL.GetForegroundWindow(), buffer, MAX_TITLE_LENGTH)

            val windowTitle = String(buffer)

            return windowTitle.startsWith("Tibia -")
        }

        /**
         *  Sprawdzenie czy okno z grą jest ukryte
         */
        @JvmStatic
        fun isTibiaWindowOnBackground(): Boolean {
            return !isTibiaWindowOnForeground()
        }
    }
}