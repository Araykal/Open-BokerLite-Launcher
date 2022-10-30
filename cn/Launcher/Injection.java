package cn.Launcher;

import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.BaseTSD;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;
import com.sun.jna.win32.W32APIOptions;
import cn.Launcher.interfaces.Kernel32;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import static com.sun.jna.platform.win32.User32.*;

public class Injection {
	static final Kernel32 kernel32 = Native.load("kernel32.dll", Kernel32.class, W32APIOptions.ASCII_OPTIONS);
	static final User32 user32 = User32.INSTANCE;

	public static String inject(int processID, File dll) {
		String dllName = dll.getAbsolutePath();

		BaseTSD.DWORD_PTR processAccess = new BaseTSD.DWORD_PTR(0x43A);

		WinNT.HANDLE hProcess = kernel32.OpenProcess(processAccess, new WinDef.BOOL(false),
				new BaseTSD.DWORD_PTR(processID));
		if (hProcess == null) {
			return "Handle or PID is null. Error: " + formatMessage(kernel32.GetLastError());
		}

		BaseTSD.DWORD_PTR loadLibraryAddress = kernel32.GetProcAddress(kernel32.GetModuleHandle("KERNEL32"),
				"LoadLibraryA");
		if (loadLibraryAddress.intValue() == 0) {
			return "Could not find library. Error: " + formatMessage(kernel32.GetLastError());
		}
		
		Pointer m;
		byte[] arr;
		try {
			arr = dllName.getBytes(Native.getDefaultStringEncoding());
			m = new Memory(arr.length+1);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
		m.setString(0, dllName);

		WinDef.LPVOID dllNameAddress = kernel32.VirtualAllocEx(hProcess, null, arr.length+1,
				new BaseTSD.DWORD_PTR(0x2000 | 0x1000), new BaseTSD.DWORD_PTR(0x4));
		if (dllNameAddress == null) {
			return "Address is null. Error: " + formatMessage(kernel32.GetLastError());
		}


		boolean wpmSuccess = kernel32.WriteProcessMemory(hProcess, dllNameAddress, m, dllName.length(), null)
				.booleanValue();
		if (!wpmSuccess) {
			return "WriteProcessMemory failed. Error: " + formatMessage(kernel32.GetLastError());
		}

		BaseTSD.DWORD_PTR threadHandle = kernel32.CreateRemoteThread(hProcess, 0, 0, loadLibraryAddress, dllNameAddress,
				0, 0);
		if (threadHandle.intValue() == 0) {
			return "threadHandle was invalid. Error: " + formatMessage(kernel32.GetLastError());
		}
		kernel32.CloseHandle(hProcess);
		return "Inject Succeed";
	}

	public static String formatMessage(int GLE) {
		PointerByReference ptr = new PointerByReference();
		kernel32.FormatMessage(FORMAT_MESSAGE_ALLOCATE_BUFFER | FORMAT_MESSAGE_FROM_SYSTEM, null, GLE, 0, ptr, 0, null);
		Pointer resultPtr = ptr.getPointer().getPointer(0);
		String result = resultPtr.getString(0, "GBK");
		kernel32.LocalFree(resultPtr);
		return result;
	}

	public static Minecraft[] findMinecraft() {
		ArrayList<Minecraft> mc = new ArrayList<>();
		HWND hwnd = user32.GetWindow(user32.GetDesktopWindow(), new DWORD(GW_CHILD));
		IntByReference ptr = new IntByReference();
		do {
			hwnd = user32.GetWindow(hwnd, new DWORD(GW_HWNDNEXT));
			char[] charArray = new char[1024];
			int length = user32.GetClassName(hwnd, charArray, charArray.length);
			if (length != charArray.length) {
				char[] arr = new char[length];
				System.arraycopy(charArray, 0, arr, 0, length);
				charArray = arr;
			}
			String className = new String(charArray);
			if (!className.equals("LWJGL"))
				continue;
			charArray = new char[user32.GetWindowTextLength(hwnd) + 1];
			user32.GetWindowText(hwnd, charArray, charArray.length);
			String title = new String(charArray);
			user32.GetWindowThreadProcessId(hwnd, ptr);
			mc.add(new Minecraft(ptr.getValue(), title));

		} while (hwnd != null);
		return mc.toArray(new Minecraft[0]);
	}

	public static void main(String[] args) {
		Minecraft[] mc = findMinecraft();
		System.out.println("pid\ttitle");
		System.out.println("===============================");
		for (int i = 0; i < mc.length; i++) {
			Minecraft minecraft = mc[i];
			System.out.printf("%d\t%s", minecraft.pid, minecraft.title);
		}
	}

	static class Minecraft {
		@Override
		public String toString() {
			return title;
		}

		public final int pid;
		public final String title;

		public Minecraft(int pid, String title) {
			this.pid = pid;
			this.title = title;
		}
	}
}
