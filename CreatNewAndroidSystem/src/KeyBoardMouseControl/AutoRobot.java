//负责操作键盘与鼠标
package KeyBoardMouseControl;

import java.awt.AWTException;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import CreatNewAndroidSystem.SystemParams;
import FindPictureLocation.FindPictureLocation;
import FindPictureLocation.captureDesktop;

public class AutoRobot {
	// private SystemParams MyParams = new SystemParams();
	public Robot robot;
	public captureDesktop TmpDesktop = new captureDesktop();
	public FindPictureLocation MouseLocation = new FindPictureLocation();

	public AutoRobot() {
		try {
			robot = new Robot();
		} catch (AWTException ex) {
			ex.printStackTrace();
		}
	}

	public int[] GetMouseLocation() {
		int[] Location = new int[2];
		Point mousepoint = MouseInfo.getPointerInfo().getLocation();
		// System.out.println(mousepoint.x + "   " + mousepoint.y);
		Location[0] = mousepoint.x;
		Location[1] = mousepoint.y;
		return Location;
	}

	public boolean MoveMouseToPicture(String FilePath) {
		FilePath=FindPictureFolder(FilePath);
		if(FilePath==null){
			return false;
		}
		int x;
		int y;
		List<int[]> list = new ArrayList<int[]>();
		list = MouseLocation
				.PictureLocation(TmpDesktop.LoadDesktop(), FilePath);
		if (list.isEmpty()) {
			return false;
		}
		x = list.get(0)[0] + MouseLocation.GetPictureWidth(FilePath) / 2;
		y = list.get(0)[1] + MouseLocation.GetPictureHeight(FilePath) / 2;
		robot.mouseMove(x, y);
		return true;
	}

	public String FindPictureFolder(String FilePath) {
		File file = new File(FilePath);
		if (file.isFile()) {
			return file.getAbsolutePath();
		}
		if (file.isDirectory()) {
			File[] array = file.listFiles();
			for (int i = 0; i < array.length; i++) {
				if (array[i].isFile()) {
					// System.out.println(array[i].getAbsolutePath());
					if (FindPicture(array[i].getAbsolutePath())) {
						return array[i].getAbsolutePath();
					}
				}
			}
		}
		return null;
	}

	public Boolean FindPicture(String FilePath) {
		FilePath=FindPictureFolder(FilePath);
		if(FilePath==null){
			return false;
		}
		List<int[]> list = new ArrayList<int[]>();
		list = MouseLocation
				.PictureLocation(TmpDesktop.LoadDesktop(), FilePath);
		if (list.isEmpty()) {
			return false;
		}
		return true;
	}

	public void MoveMouse(int x, int y) {
		robot.mouseMove(x, y);
	}

	// InputEvent.BUTTON1_DOWN_MASK 左键
	// InputEvent.BUTTON2_DOWN_MASK 中键
	// InputEvent.BUTTON3_DOWN_MASK 右键
	public void MousePresss(int buttons) {
		// robot.mousePress(buttons);
		// 使用Dllcall 来调mouse_event API实现模鼠标按下，否则虚机无法响应鼠标动作
		String CMD = SystemParams.LibPath + "dllcall mouse_event,";
		switch (buttons) {
		case InputEvent.BUTTON1_DOWN_MASK:
			CMD = CMD + 0x02;
			break;
		case InputEvent.BUTTON2_DOWN_MASK:
			CMD = CMD + 0x20;
			break;
		case InputEvent.BUTTON3_DOWN_MASK:
			CMD = CMD + 0x08;
			break;
		}
		CMD = CMD + ",0,0,0,0,user32.dll";
		ExecuteCMD(CMD);
	}

	public void MouseRelease(int buttons) {
		// robot.mouseRelease(buttons);
		// // 使用Dllcall 来调mouse_event API实现模鼠标按下，否则虚机无法响应鼠标动作
		String CMD = SystemParams.LibPath + "dllcall mouse_event,";
		switch (buttons) {
		case InputEvent.BUTTON1_DOWN_MASK:
			CMD = CMD + 0x04;
			break;
		case InputEvent.BUTTON2_DOWN_MASK:
			CMD = CMD + 0x40;
			break;
		case InputEvent.BUTTON3_DOWN_MASK:
			CMD = CMD + 0x10;
			break;
		}
		CMD = CMD + ",0,0,0,0,user32.dll";
		ExecuteCMD(CMD);
	}

	public void Mouseclick(int buttons) {
		MousePresss(buttons);
		MouseRelease(buttons);
	}

	public void MouseDoubleclick(int buttons) {
		MousePresss(buttons);
		MouseRelease(buttons);
		MousePresss(buttons);
		MouseRelease(buttons);
	}

	public void MouseWheel(int wheelAmt) {
		robot.mouseWheel(wheelAmt);
	}

	public void KeyPress(int keycode) {
		robot.keyPress(keycode);
	}

	public void KeyRelease(int keycode) {
		robot.keyRelease(keycode);
	}

	public void KeyClick(int keycode) {
		robot.keyPress(keycode);
		robot.keyRelease(keycode);
	}

	public void KeyClick(String Str) {
		char[] bytes = Str.toUpperCase().toCharArray();
		char[] Testbytes = Str.toCharArray();
		int I = 0;
		for (I = 0; I < bytes.length; I++) {
			int keycode = bytes[I];
			switch (bytes[I]) {
			case '~':
				KeyPress(VK.VK_SHIFT);
				KeyClick(VK.VK_BACK_QUOTE);
				KeyRelease(VK.VK_SHIFT);
				break;
			case '!':
				KeyPress(VK.VK_SHIFT);
				KeyClick(VK.VK_1);
				KeyRelease(VK.VK_SHIFT);
				break;
			case '@':
				KeyPress(VK.VK_SHIFT);
				KeyClick(VK.VK_2);
				KeyRelease(VK.VK_SHIFT);
				break;
			case '#':
				KeyPress(VK.VK_SHIFT);
				KeyClick(VK.VK_3);
				KeyRelease(VK.VK_SHIFT);
				break;
			case '$':
				KeyPress(VK.VK_SHIFT);
				KeyClick(VK.VK_4);
				KeyRelease(VK.VK_SHIFT);
				break;
			case '%':
				KeyPress(VK.VK_SHIFT);
				KeyClick(VK.VK_5);
				KeyRelease(VK.VK_SHIFT);
				break;
			case '^':
				KeyPress(VK.VK_SHIFT);
				KeyClick(VK.VK_6);
				KeyRelease(VK.VK_SHIFT);
				break;
			case '&':
				KeyPress(VK.VK_SHIFT);
				KeyClick(VK.VK_7);
				KeyRelease(VK.VK_SHIFT);
				break;
			case '*':
				KeyPress(VK.VK_SHIFT);
				KeyClick(VK.VK_8);
				KeyRelease(VK.VK_SHIFT);
				break;
			case '(':
				KeyPress(VK.VK_SHIFT);
				KeyClick(VK.VK_9);
				KeyRelease(VK.VK_SHIFT);
				break;
			case ')':
				KeyPress(VK.VK_SHIFT);
				KeyClick(VK.VK_0);
				KeyRelease(VK.VK_SHIFT);
				break;
			case '_':
				KeyPress(VK.VK_SHIFT);
				KeyClick(VK.VK_MINUS);
				KeyRelease(VK.VK_SHIFT);
				break;
			case '+':
				KeyPress(VK.VK_SHIFT);
				KeyClick(VK.VK_EQUALS);
				KeyRelease(VK.VK_SHIFT);
				break;
			case '{':
				KeyPress(VK.VK_SHIFT);
				KeyClick(VK.VK_OPEN_BRACKET);
				KeyRelease(VK.VK_SHIFT);
				break;
			case '}':
				KeyPress(VK.VK_SHIFT);
				KeyClick(VK.VK_CLOSE_BRACKET);
				KeyRelease(VK.VK_SHIFT);
				break;
			case '|':
				KeyPress(VK.VK_SHIFT);
				KeyClick(VK.VK_BACK_SLASH);
				KeyRelease(VK.VK_SHIFT);
				break;
			case ':':
				KeyPress(VK.VK_SHIFT);
				KeyClick(VK.VK_SEMICOLON);
				KeyRelease(VK.VK_SHIFT);
				break;
			case '"':
				KeyPress(VK.VK_SHIFT);
				KeyClick(VK.VK_QUOTE);
				KeyRelease(VK.VK_SHIFT);
				break;
			case '<':
				KeyPress(VK.VK_SHIFT);
				KeyClick(VK.VK_COMMA);
				KeyRelease(VK.VK_SHIFT);
				break;
			case '>':
				KeyPress(VK.VK_SHIFT);
				KeyClick(VK.VK_PERIOD);
				KeyRelease(VK.VK_SHIFT);
				break;
			case '?':
				KeyPress(VK.VK_SHIFT);
				KeyClick(VK.VK_SLASH);
				KeyRelease(VK.VK_SHIFT);
				break;
			// case '`':
			// case '1':
			// case '2':
			// case '3':
			// case '4':
			// case '5':
			// case '6':
			// case '7':
			// case '8':
			// case '9':
			// case '0':
			// case '-':
			// case '=':
			// case '[':
			// case ']':
			// case '\\':
			// case ';':
			// case '\'':
			// case ',':
			// case '.':
			// case '/':
			// KeyClick(keycode);
			// break;
			default:
				if (bytes[I] == Testbytes[I] && bytes[I] >= VK.VK_A
						&& bytes[I] <= VK.VK_Z) {
					KeyPress(VK.VK_SHIFT);
					KeyClick(keycode);
					KeyRelease(VK.VK_SHIFT);
				} else {
					KeyClick(keycode);
				}
				break;
			}
		}
	}

	/**
	 * @param args
	 */
	// 执行命令
	public String ExecuteCMD(String CMD) {
		String output = null;
		String error = null;
		StringBuffer outputBuffer = new StringBuffer();
		StringBuffer errorBuffer = new StringBuffer();
		try {
			Process process = Runtime.getRuntime().exec(CMD);
			errorBuffer = GetOutput(process.getErrorStream());
			outputBuffer = GetOutput(process.getInputStream());
			process.waitFor();
			output = outputBuffer.toString();
			error = errorBuffer.toString();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if (output.length() > 0) {
			return output;
		} else {// if (error.length() > 0) {
			return error;
		}
	}

	// 获取命令行输出
	public StringBuffer GetOutput(InputStream ins) {
		InputStream is;
		StringBuffer buffer = new StringBuffer();
		is = ins;
		try {
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			String line = null;
			while ((line = br.readLine()) != null) {
				buffer.append(line);
				buffer.append("\n");
			}
			br.close();

		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		return buffer;
	}

	public static void main(String[] args) {
		AutoRobot Tmp = new AutoRobot();
		// TODO Auto-generated method stub
		try {
			Thread.sleep(5000);
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		}
		// Tmp.MoveMouseToPicture("e:/tt1.png");
		// Tmp.Mouseclick(InputEvent.BUTTON1_MASK);
		// Tmp.KeyPress(VK.VK_A);
		// Tmp.KeyRelease(VK.VK_A);
		// Tmp.robot.mousePress(InputEvent.BUTTON1_MASK);
		// Tmp.KeyClick("~!@#$%^&*()_+{}|:\"<>?");
		// Tmp.KeyClick("HtTp://WwW.21Cn.cOm");
		Tmp.FindPictureFolder("D:/android-x86/RobotData/samsung - GT-S6358/GetAndroidIP");

	}

}
