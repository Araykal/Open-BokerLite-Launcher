package cn.Launcher;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.google.gson.Gson;
import com.sun.jna.platform.win32.Kernel32;

import pi.yalan.packet.PacketRegistry;
import pi.yalan.packet.client.CAuthPacket;
import pi.yalan.packet.server.SAuthResultPacket;
import cn.Launcher.Injection.Minecraft;
import cn.BokerLite.Auth;

public class GuiLauncher extends JFrame {
	private static final long serialVersionUID = -111075541442518967L;
	private JTextField username;
	private JPasswordField password;
	private JComboBox<Minecraft> pid;
	static int flag = 1;//用来判
	private String sessionToken = null;
	private boolean skipLogin = false;
	private static final File mdk = new File("C:/Program Files/BokerLite");

	private static final File dll = new File("C:/Program Files/BokerLite/BokerLite.dll");

	private static final Gson gson = new Gson();


	
	public static void main(String[] args) throws IOException, InterruptedException {

		FlatDarculaLaf.setup();
		PacketRegistry.register();
		Auth.Instance = new Auth();

		Auth.Instance.connect();
		Auth.Instance.openReadPacketThread();
//		if (!new File(System.getProperty("user.home"), "vapu_font.ttf").exists()) {
//			try {
//				InputStream is = GuiLauncher.class.getResourceAsStream("/assets/font.ttf");
//				File f = new File(System.getProperty("user.home"), "vapu_font.ttf");
//				File fp = new File(f.getParent());
//				if (!fp.exists()) {
//					fp.mkdirs();
//				}
//				if (!f.exists()) {
//					f.createNewFile();
//				}
//				OutputStream os = new FileOutputStream(f);
//				int index = 0;
//				byte[] bytes = new byte[1024];
//				while ((index = is.read(bytes)) != -1) {
//					os.write(bytes, 0, index);
//				}
//				os.flush();
//				os.close();
//				is.close();
//			} catch (Exception e) {
//				new File(System.getProperty("user.home"), "vapu_font.ttf").delete();
//				JOptionPane.showMessageDialog(null, "Failed to extra font.", "L", JOptionPane.ERROR_MESSAGE);
//				e.printStackTrace();
//				System.exit(-1);
//			}
//		}

//        System.out.println("Injector. . .\n┌─────────────────────────────────┐");
//        for(int i=0;i<35;i++) {
//            long time = System.currentTimeMillis();
//            while(System.currentTimeMillis()-time<=500);
//            System.out.print("■");
//        }
//        System.out.println("\n└─────────────────────────────────┘");
		GuiLauncher frame = new GuiLauncher();
		frame.setVisible(true);
	}
	
	void findMinecraft() {
		pid.removeAllItems();
		for(Minecraft mc : Injection.findMinecraft()) {
			pid.addItem(mc);
		}
	}
	public static void deleteFile(File file){
		//判断文件不为null或文件目录存在
		if (file == null || !file.exists()){
			flag = 0;
			System.out.println("文件删除失败,请检查文件路径是否正确");
			return;
		}
		//取得这个目录下的所有子文件对象
		File[] files = file.listFiles();
		//遍历该目录下的文件对象
		for (File f: files){
			//打印文件名
			String name = file.getName();
			System.out.println(name);
			//判断子目录是否存在子目录,如果是文件则删除
			if (f.isDirectory()){
				deleteFile(f);
			}else {
				f.delete();
			}
		}
		//删除空文件夹  for循环已经把上一层节点的目录清空。
		file.delete();
	}


	public static String download(String dowUrl, String dowPath){
		try {

			URL url = new URL(dowUrl);

			URLConnection urlConnection = url.openConnection();

			HttpURLConnection httpURLConnection = (HttpURLConnection) urlConnection;// http的连接类

			//String contentType = httpURLConnection.getContentType();//请求类型,可用来过滤请求，

			httpURLConnection.setConnectTimeout(1000*5);//设置超时

			httpURLConnection.setRequestMethod("POST");//设置请求方式，默认是GET

			httpURLConnection.setRequestProperty("Charset", "UTF-8");// 设置字符编码

			httpURLConnection.connect();// 打开连接

			BufferedInputStream bin = new BufferedInputStream(httpURLConnection.getInputStream());

			String path = dowPath;// 指定存放位置
			File filed = new File(path);

			OutputStream out = new FileOutputStream(filed);
			int size = 0;

			byte[] b = new byte[2048];
			//把输入流的文件读取到字节数据b中，然后输出到指定目录的文件
			while ((size = bin.read(b)) != -1) {
				out.write(b, 0, size);
			}
			// 关闭资源
			bin.close();
			out.close();
			return "200";
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "500";
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "400";
		}
	}
	public GuiLauncher() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(733, 450);
		setResizable(false);
		setLocationByPlatform(true);
		JPanel contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		JPanel titlePanel = new JPanel();
		JLabel ver = new JLabel("BokerLite Injector");

		JPanel injectPanel = new JPanel();

		JPanel panel = new JPanel();

		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING).addGroup(gl_contentPane
				.createSequentialGroup()
				.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup().addContainerGap()
								.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
										.addComponent(titlePanel, GroupLayout.PREFERRED_SIZE, 698,
												GroupLayout.PREFERRED_SIZE)
										.addComponent(ver, GroupLayout.PREFERRED_SIZE, 283,
												GroupLayout.PREFERRED_SIZE)))
						.addGroup(gl_contentPane.createSequentialGroup().addGap(9).addComponent(panel,
								GroupLayout.PREFERRED_SIZE, 699, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_contentPane.createSequentialGroup().addGap(8).addComponent(injectPanel,
								GroupLayout.PREFERRED_SIZE, 699, GroupLayout.PREFERRED_SIZE)))
				.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
		gl_contentPane.setVerticalGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_contentPane.createSequentialGroup().addContainerGap()
						.addComponent(titlePanel, GroupLayout.PREFERRED_SIZE, 84, GroupLayout.PREFERRED_SIZE).addGap(5)
						.addComponent(panel, GroupLayout.PREFERRED_SIZE, 37, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(injectPanel, GroupLayout.PREFERRED_SIZE, 238, GroupLayout.PREFERRED_SIZE)
						.addGap(9).addComponent(ver)));

		username = new JTextField();
		username.setToolTipText("Username");
		username.setColumns(10);

		JButton loginBtn = new JButton("LOGIN");

		password = new JPasswordField();
		password.setToolTipText("Password");

		pid = new JComboBox<>();
		pid.setPreferredSize(new Dimension(240, 8));
		
		//findMinecraft();
		
		JLabel tips = new JLabel();

		JButton injectBtn = new JButton("Inject");
		injectBtn.addActionListener(e -> {
			if(pid.getSelectedItem() == null) {
				JOptionPane.showMessageDialog(this, "Please choose a game to inject.", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
			injectBtn.setVisible(false);
			pid.setVisible(false);
			tips.setText("Injecting, Please wait...");
			String result = Injection.inject(((Minecraft) pid.getSelectedItem()).pid, dll);
			if (result.equals("Inject Succeed")) {
				new Thread(() -> {
					tips.setText("Waiting game...");
					if(Kernel32.INSTANCE.WaitNamedPipe("\\\\.\\pipe\\vapu", 10000)) {
						try {
							sessionToken = sessionToken == null ? "" : sessionToken;
							RandomAccessFile file = new RandomAccessFile("\\\\.\\pipe\\vapu", "rw");
							FileChannel channel = file.getChannel();
							ByteBuffer buf = ByteBuffer.allocate(8).order(ByteOrder.nativeOrder());
							buf.putInt(0x01); // 傻逼struct补齐机制害得我debug了半天，结果才想起来struct需要补齐
							byte[] bytes = sessionToken.getBytes(StandardCharsets.UTF_8);
							buf.putInt(bytes.length);
							((Buffer)buf).flip(); // L Java 9
							channel.write(buf);
							file.write(bytes);
							((Buffer)buf).clear();
							while(channel.isOpen() && channel.read(buf) > 0) {
								((Buffer)buf).flip();
								switch(buf.getInt()) {
								case 0x02:
									// TODO: 下载资源文件
									break;
								case 0xFF:
									break;
								case 0x03:
									tips.setText("Stage " + buf.getInt());
									break;
								}
								((Buffer)buf).clear();
							}
							file.close();
						} catch (IOException e1) {
							e1.printStackTrace();
							JOptionPane.showMessageDialog(GuiLauncher.this, e1, "L", JOptionPane.ERROR_MESSAGE);
						}
					} else {
						JOptionPane.showMessageDialog(GuiLauncher.this, "Inject Succeed.", "WARNING", JOptionPane.WARNING_MESSAGE);
					}
					System.exit(0);
				}).start();
			} else {
				tips.setText("Select a game of Minecraft 1.8.9 for inject.");
				injectBtn.setVisible(true);
				pid.setVisible(true);
				JOptionPane.showMessageDialog(this, result + "\nPlease upload your bug to forum or our discord. \nBUG Upload:https://getvapu.today/forum/viewforum.php?id=4", "Error", JOptionPane.ERROR_MESSAGE);
			}
		});

		loginBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(skipLogin || !(sessionToken == null || sessionToken.isEmpty())) {
					username.setVisible(false);
					password.setVisible(false);
					loginBtn.setVisible(false);
					tips.setText("Select a game of Minecraft 1.8.9 for inject.");
					injectBtn.setVisible(true);
					pid.setVisible(true);
					Thread t = new Thread(() -> {
						try {
							while(GuiLauncher.this.isVisible()) {
								if(GuiLauncher.this.isActive())
									GuiLauncher.this.findMinecraft();
								Thread.sleep(5000L);
							}
						} catch (InterruptedException ex) {
							;
						}
					});
					t.setDaemon(true);
					t.start();
				} else {
					if(username.getText().isEmpty() || password.getPassword().length == 0) {
						JOptionPane.showMessageDialog(GuiLauncher.this, "Please enter your account and your password.", "Error", JOptionPane.ERROR_MESSAGE);
						return;
					}
					try {
						verify();




						loginBtn.doClick();
					} catch (Throwable e1) {
						e1.printStackTrace();
						JOptionPane.showMessageDialog(GuiLauncher.this, e1, "L", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});

		tips.setHorizontalAlignment(SwingConstants.CENTER);
		GroupLayout gl_injectPanel = new GroupLayout(injectPanel);
		gl_injectPanel.setHorizontalGroup(gl_injectPanel.createParallelGroup(Alignment.LEADING).addGroup(gl_injectPanel
				.createSequentialGroup().addGap(227)
				.addGroup(gl_injectPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_injectPanel.createSequentialGroup().addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(loginBtn, GroupLayout.PREFERRED_SIZE, 245, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_injectPanel.createParallelGroup(Alignment.TRAILING, false)
								.addComponent(pid, Alignment.CENTER).addComponent(password, Alignment.LEADING)
								.addComponent(username, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 244,
										Short.MAX_VALUE)))
				.addContainerGap(227, Short.MAX_VALUE))
				.addGroup(Alignment.TRAILING,
						gl_injectPanel.createSequentialGroup().addContainerGap(302, Short.MAX_VALUE)
								.addComponent(injectBtn, GroupLayout.PREFERRED_SIZE, 97, GroupLayout.PREFERRED_SIZE)
								.addGap(300))
				.addGroup(Alignment.TRAILING, gl_injectPanel.createSequentialGroup().addContainerGap()
						.addComponent(tips, GroupLayout.DEFAULT_SIZE, 679, Short.MAX_VALUE).addContainerGap()));
		gl_injectPanel.setVerticalGroup(gl_injectPanel.createParallelGroup(Alignment.LEADING).addGroup(gl_injectPanel
				.createSequentialGroup().addGap(6).addComponent(tips).addPreferredGap(ComponentPlacement.RELATED)
				.addComponent(pid, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
				.addPreferredGap(ComponentPlacement.RELATED).addComponent(injectBtn).addGap(16)
				.addComponent(username, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE).addGap(4)
				.addComponent(password, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
						GroupLayout.PREFERRED_SIZE)
				.addPreferredGap(ComponentPlacement.UNRELATED)
				.addComponent(loginBtn, GroupLayout.PREFERRED_SIZE, 31, GroupLayout.PREFERRED_SIZE)
				.addContainerGap(54, Short.MAX_VALUE)));
		injectPanel.setLayout(gl_injectPanel);

		JProgressBar progressBar = new JProgressBar();
		progressBar.setStringPainted(true);
		panel.add(progressBar);
		JLabel title = new JLabel("BokerLite");
		title.setFont(new Font("Agency FB", Font.BOLD, 62));
		titlePanel.add(title);
		contentPane.setLayout(gl_contentPane);

		tips.setText("Login to BokerLite");
		progressBar.setVisible(false);
		pid.setVisible(false);
		injectBtn.setVisible(false);


//		try {
//			if (!InetAddress.getByName("getvapu.today").isReachable(5000)) {
//				skipLogin = true;
//				loginBtn.doClick();
//			}
//		} catch (IOException e1) {
//			e1.printStackTrace();
//			skipLogin = true;
//			loginBtn.doClick();
//		}

	}
	public static String readTxt(String txtPath) {
		File file = new File(txtPath);
		if(file.isFile() && file.exists()){
			try {
				FileInputStream fileInputStream = new FileInputStream(file);
				InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
				BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
				StringBuffer sb = new StringBuffer();
				String text = null;
				while((text = bufferedReader.readLine()) != null){
					sb.append(text);
				}
				return sb.toString();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}


	public static void writeTxt(String txtPath,String content){
		FileOutputStream fileOutputStream = null;
		File file = new File(txtPath);
		try {
			if(file.exists()){
				//判断文件是否存在，如果不存在就新建一个txt
				file.createNewFile();
			}
			fileOutputStream = new FileOutputStream(file);
			fileOutputStream.write(content.getBytes());
			fileOutputStream.flush();
			fileOutputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void verify() throws Exception {
		Auth.Instance.sendPacket(new CAuthPacket(username.getText(),password.getText(),Addon.QaQ()));

		while (Auth.Instance.memory.get(Auth.ADDRESS_AUTH_RESULT) == null) {
			Thread.sleep(1);
		}

		switch ((SAuthResultPacket.Result) Auth.Instance.memory.get(Auth.ADDRESS_AUTH_RESULT)) {
			case SUCCEED:
				if  (!mdk.exists()  && !mdk .isDirectory())
				{
					if(mdk.mkdirs()){

					}else {
						JOptionPane.showMessageDialog(GuiLauncher.this, "e1", "ERROR Please run as Administrator", JOptionPane.ERROR_MESSAGE);
					}
				} else
				{

				}

				skipLogin = true;
				JOptionPane.showMessageDialog(GuiLauncher.this, "SUCCEED Download DLL", "L", JOptionPane.WARNING_MESSAGE);
				download("http://115.126.43.15:28151/down/uKcjaCVBfrqO","C:/Program Files/BokerLite/BokerLite.dll");
				writeTxt("C:/Program Files/BokerLite/username.txt",username.getText());
				 writeTxt("C:/Program Files/BokerLite/password.txt",password.getText());


				break;
			case FAILED:

				System.exit(0);
				break;
			default:
				throw new IllegalStateException("Auth result");
		}

		Auth.Instance.memory.remove(Auth.ADDRESS_AUTH_RESULT);
	}

	public void addNotify() {
		super.addNotify();
	}
	static class LoginResponse {
		public boolean success;
		public String reason, result;
	}
}
