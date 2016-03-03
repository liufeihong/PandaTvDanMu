package com.neucrack.GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

import com.neucrack.protocol.Bamboo;
import com.neucrack.protocol.ConnectDanMuServer;
import com.neucrack.protocol.Danmu;
import com.neucrack.protocol.Gift;
import com.neucrack.protocol.Platform;
import com.neucrack.protocol.User;
import com.neucrack.protocol.Visitors;
import com.sun.awt.AWTUtilities;
import com.sun.corba.se.impl.oa.poa.ActiveObjectMap.Key;

import java.awt.GridLayout;

import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.ListModel;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Font;

import javax.swing.border.TitledBorder;
import javax.swing.border.BevelBorder;
import javax.swing.plaf.ListUI;

import java.awt.Toolkit;

import javax.swing.ImageIcon;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Vector;
import java.awt.FlowLayout;

import javax.swing.SwingConstants;

import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;

public class PandaTVDanmu extends JFrame {

	private JPanel contentPane;
	static Point origin = new Point();
	private JTextField mRoomID;
	private JList<ListItemDanMu> mMessageList;
	final JFrame parentPanel=this;
	private boolean mLock=false;
	private boolean mIsConnectionAlive=false;
	private DefaultListModel<ListItemDanMu> mListItem;
	//	DefaultListModel listModel;
	int mMessagelastIndex=0;
	private ConnectDanMuServer mDanMuConnection;
	
	static PandaTVDanmu frame;
	private JLabel mVisitorNum;
	private JPanel panel_header_1;
	private JPanel panel_header_2;
	private JLabel label;
	private JLabel mLockHint;
	private JLabel mCloseWindow;
	private JPanel panel_1_left;
	private JPanel panel_2_right;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					String lookAndFeel = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
					UIManager.setLookAndFeel(lookAndFeel);
					frame = new PandaTVDanmu();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public PandaTVDanmu() {
		setIconImage(Toolkit.getDefaultToolkit().getImage("./resources/pic/icon.png"));
		setBackground(Color.BLACK);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(1100, 250, 272, 323);
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setOpaque(false);
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JPanel panelHeader = new JPanel();
		panelHeader.setBackground(Color.DARK_GRAY);
		contentPane.add(panelHeader, BorderLayout.NORTH);
		panelHeader.setOpaque(false);
		GridBagLayout gbl_panelHeader = new GridBagLayout();
		gbl_panelHeader.columnWidths = new int[]{0, 0};
		gbl_panelHeader.rowHeights = new int[]{0, 0, 0, 0, 0};
		gbl_panelHeader.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_panelHeader.rowWeights = new double[]{0.0, 0.0, 1.0, 1.0, Double.MIN_VALUE};
		panelHeader.setLayout(gbl_panelHeader);
		
		panel_header_1 = new JPanel();
		panel_header_1.setOpaque(false);
		GridBagConstraints gbc_panel_header_1 = new GridBagConstraints();
		gbc_panel_header_1.anchor = GridBagConstraints.EAST;
		gbc_panel_header_1.insets = new Insets(0, 0, 5, 0);
		gbc_panel_header_1.fill = GridBagConstraints.VERTICAL;
		gbc_panel_header_1.gridx = 0;
		gbc_panel_header_1.gridy = 0;
		panelHeader.add(panel_header_1, gbc_panel_header_1);
		panel_header_1.setLayout(new BorderLayout(0, 0));
		
		panel_1_left = new JPanel();
		panel_header_1.add(panel_1_left, BorderLayout.WEST);
		panel_1_left.setOpaque(false);
		
		mVisitorNum = new JLabel("0");
		panel_1_left.add(mVisitorNum);
		mVisitorNum.setHorizontalAlignment(SwingConstants.LEFT);
		mVisitorNum.setIcon(new ImageIcon("./resources/pic/audience.png"));
		mVisitorNum.setBackground(Color.DARK_GRAY);
		mVisitorNum.setForeground(Color.WHITE);
		
		panel_2_right = new JPanel();
		panel_header_1.add(panel_2_right, BorderLayout.EAST);
		panel_2_right.setOpaque(false);
		
		mLockHint = new JLabel("F10锁定");
		panel_2_right.add(mLockHint);
		mLockHint.setForeground(Color.WHITE);
		
		mCloseWindow = new JLabel("");
		panel_2_right.add(mCloseWindow);
		mCloseWindow.setIcon(new ImageIcon("./resources/pic/close.png"));
		mCloseWindow.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				System.exit(0);
			}
			@Override
			public void mouseEntered(MouseEvent e) {
				mCloseWindow.setIcon(new ImageIcon("./resources/pic/close_hover.png"));
			}
			@Override
			public void mousePressed(MouseEvent e) {
				mCloseWindow.setIcon(new ImageIcon("./resources/pic/close_pressed.png"));
			}
			@Override
			public void mouseExited(MouseEvent e) {
				mCloseWindow.setIcon(new ImageIcon("./resources/pic/close.png"));
			}
		});
		
		panel_header_2 = new JPanel();
		panel_header_2.setOpaque(false);
		GridBagConstraints gbc_panel_header_2 = new GridBagConstraints();
		gbc_panel_header_2.insets = new Insets(0, 0, 5, 0);
		gbc_panel_header_2.fill = GridBagConstraints.BOTH;
		gbc_panel_header_2.gridx = 0;
		gbc_panel_header_2.gridy = 1;
		panelHeader.add(panel_header_2, gbc_panel_header_2);
		GridBagLayout gbl_panel_header_2 = new GridBagLayout();
		gbl_panel_header_2.columnWidths = new int[]{0, 0, 0};
		gbl_panel_header_2.rowHeights = new int[]{0, 0};
		gbl_panel_header_2.columnWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		gbl_panel_header_2.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		panel_header_2.setLayout(gbl_panel_header_2);
		
		label = new JLabel("房间");
		label.setForeground(Color.WHITE);
		GridBagConstraints gbc_label = new GridBagConstraints();
		gbc_label.insets = new Insets(0, 0, 0, 5);
		gbc_label.gridx = 0;
		gbc_label.gridy = 0;
		panel_header_2.add(label, gbc_label);
		
		mRoomID = new JTextField();
		mRoomID.setBorder(new EmptyBorder(0,0,0,0));
		GridBagConstraints gbc_mRoomID = new GridBagConstraints();
		gbc_mRoomID.gridx = 1;
		gbc_mRoomID.gridy = 0;
		panel_header_2.add(mRoomID, gbc_mRoomID);
		mRoomID.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if(e.getKeyCode()==KeyEvent.VK_ENTER){
					if(mIsConnectionAlive)
						CloseConnection();
					StartConnection();
				}
			}
		});
		mRoomID.setBackground(Color.DARK_GRAY);
		mRoomID.setForeground(Color.WHITE);
		mRoomID.setColumns(10);
		mRoomID.setText("313180");
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.getHorizontalScrollBar().setPreferredSize(new Dimension(0,0));
		scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(0,0));
		scrollPane.setOpaque(false);//设置透明
		scrollPane.getViewport().setOpaque(false);
		contentPane.add(scrollPane, BorderLayout.CENTER);
		
		mListItem = new DefaultListModel<ListItemDanMu>();
		mMessageList = new JList<ListItemDanMu>(mListItem);
		mMessageList.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				// 当鼠标按下的时候获得窗口当前的位置
				if(!mLock){
					origin.x = e.getX();
					origin.y = e.getY();
				}
			}
		});
		mMessageList.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				// 当鼠标拖动时获取窗口当前位置
				if(!mLock){
					Point p =parentPanel.getLocation();
					// 设置窗口的位置
					// 窗口当前的位置 + 鼠标当前在窗口的位置 - 鼠标按下的时候在窗口的位置
					parentPanel.setLocation(p.x + e.getX() - origin.x, p.y + e.getY()- origin.y);
				}
			}
		});
		mMessageList.setCellRenderer(new ListRenderer());
		mMessageList.setBorder(null);
		mMessageList.setBackground(new Color(0, 0, 0, 0));
		mMessageList.setOpaque(false);//设置透明
		mMessageList.setBorder(new EmptyBorder(0,0,0,0));
		
		//((JLabel)mMessageList.getCellRenderer()).setOpaque(false);//设置jlist条目透明，不是自己构造listrenderer时使用
		scrollPane.setViewportView(mMessageList);
		
		
		this.setAlwaysOnTop(true);//窗口置顶
		this.setTitle("PandaTVDanMu");
		this.setUndecorated(true);
		//AWTUtilities.setWindowOpacity(this, 1f);//设置透明度
		this.setOpacity(0.6f);
		this.validate();
		
		
		
		this.addMouseListener(new MouseAdapter() {
			// 按下（mousePressed 不是点击，而是鼠标被按下没有抬起）
			public void mousePressed(MouseEvent e) {
				// 当鼠标按下的时候获得窗口当前的位置
				if(!mLock){
					origin.x = e.getX();
					origin.y = e.getY();
				}
			}
		});
		this.addMouseMotionListener(new MouseMotionAdapter() {
			// 拖动（mouseDragged 指的不是鼠标在窗口中移动，而是用鼠标拖动）
			public void mouseDragged(MouseEvent e) {
				// 当鼠标拖动时获取窗口当前位置
				if(!mLock){
					Point p =parentPanel.getLocation();
					// 设置窗口的位置
					// 窗口当前的位置 + 鼠标当前在窗口的位置 - 鼠标按下的时候在窗口的位置
					parentPanel.setLocation(p.x + e.getX() - origin.x, p.y + e.getY()- origin.y);
				}
			}
		});
		
		
	}
	private void StartConnection(){
		UpdateDanMu(new ListItemDanMu(false, false, null, "", "", "连接中。。。", null, null, null));
		mDanMuConnection = new ConnectDanMuServer(frame);
		if(mDanMuConnection.ConnectToDanMuServer(mRoomID.getText().trim())){//连接成功
			mIsConnectionAlive=true;
			UpdateDanMu(new ListItemDanMu(false, false, null, "", "", "连接弹幕服务器成功", null, null, null));
		}
		else{
			mIsConnectionAlive=false;
			UpdateDanMu(new ListItemDanMu(false, false, null, "", "", "连接弹幕服务器失败！！", null, null, null));
		}
	}
	private void CloseConnection(){
		UpdateDanMu(new ListItemDanMu(false, false, null, "", "", "断开连接中。。。", null, null, null));
		if(mDanMuConnection!=null)
			mDanMuConnection.Close();
		mIsConnectionAlive=false;
		UpdateDanMu(new ListItemDanMu(false, false, null, "", "", "与弹幕服务器断开连接成功", null, null, null));
	}
	public void UpdateDanMu(ListItemDanMu message){
		mListItem.addElement(message);
		if(mListItem.getSize()>250){//数据过多，避免占用内存，清理掉
			mListItem.removeRange(0, mListItem.getSize()-50);
		}
		mMessagelastIndex = mMessageList.getModel().getSize() - 1;
		if (mMessagelastIndex >= 0) {
			mMessageList.ensureIndexIsVisible(mMessagelastIndex);
		}
	}
	
	public void Lock(){
		mRoomID.setEnabled(false);
		mMessageList.setEnabled(false);
		mLockHint.setText("F10解锁");
	}
	public void UnLock(){
		mRoomID.setEnabled(true);
		mMessageList.setEnabled(true);
		mLockHint.setText("F10锁定");
	}
	//显示数据
	public void UpdateDanMu(Object message){
		ListItemDanMu danMuMessage=new ListItemDanMu();
		if(message.getClass().equals(Danmu.class)){//弹幕
			Danmu danmu = (Danmu) message;
			danMuMessage.setGift(false);
			if(danmu.mPlatform.equals(Platform.PLATFORM_Android)||danmu.mPlatform.equals(Platform.PLATFORM_Ios)){
				danMuMessage.setPhone(true);
				danMuMessage.setPhoneIcon(new ImageIcon("./resources/pic/mobile.png"));
			}
			String userName=danmu.mNickName;
			if(Integer.parseInt(danmu.mIdentity)>=60){
				if(danmu.mIdentity.equals(User.ROLE_MANAGER)){//管理员
					userName+="(管理)";
				}
				else if(danmu.mIdentity.equals(User.ROLE_HOSTER)){//主播
					userName+="(主播)";
				}
				else if(danmu.mIdentity.equals(User.ROLE_SUPER_MANAGER)){//超管
					userName+="(超管)";
				}
			}
			danMuMessage.setUserName(userName);
			danMuMessage.setSymbolAfterUserName(":");
			danMuMessage.setMessage(danmu.mContent);
		}
		else if(message.getClass().equals(Bamboo.class)){//竹子
			Bamboo bamboo = (Bamboo) message;
			danMuMessage.setPhone(false);
			danMuMessage.setGift(true);
			danMuMessage.setUserName(bamboo.mNickName);
			danMuMessage.setSymbolAfterUserName("");
			danMuMessage.setMessage("送给主播");
			danMuMessage.setGiftNumber(bamboo.mContent);
			danMuMessage.setGiftUnit("个");
			danMuMessage.setGiftName("竹子");
		}
		else if(message.getClass().equals(Visitors.class)){//访客数量
			Visitors visitor = (Visitors) message;
			mVisitorNum.setText(visitor.mContent);
			return;
		}
		else if(message.getClass().equals(Gift.class)){//礼物
			Gift gift = (Gift) message;
			danMuMessage.setPhone(false);
			danMuMessage.setGift(true);
			danMuMessage.setUserName(gift.mNickName);
			danMuMessage.setSymbolAfterUserName("");
			danMuMessage.setMessage("送给主播");
			danMuMessage.setGiftNumber(gift.mContentCombo);
			danMuMessage.setGiftUnit("个");
			danMuMessage.setGiftName(gift.mContentName);
		}
		UpdateDanMu(danMuMessage);
	}

}
