package main;

import java.awt.BorderLayout;

import main.AppLogic;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class MainGUI extends JFrame {

	private static final long serialVersionUID = 8949873992845980794L;
	private static MainGUI frame;
	private JPanel pnlHtpContent;
	private JPanel pnlEtpContent;
	private JTextField txtHtpSelectedNode;
	private JTextField txtEtpSelectedNode;
	private JTextField txtEtpSignalStrength;

	// Activated if 'add node' was clicked and position of
	// new node is not set yet
	private Boolean flagHtpNewStation = false;
	private Boolean flagEtpNewStation = false;
	private Station tempStation;

	// Activated if right click on node was done recently
	// and new position of node is not set yet
	private Boolean flagHtpMoveMode = false;
	private Boolean flagEtpMoveMode = false;

	// Default values for #Stations and Signal Strength of new stations
	private final int ETP_DEFAULT_NUM_STATIONS = 4;
	private final int ETP_DEFAULT_SIGNAL_STRENGTH = 150;
	private final int HTP_DEFAULT_NUM_STATIONS = 3;
	private final int HTP_DEFAULT_SIGNAL_STRENGTH = 150;
	private JButton packet_btnHtp;
	private JButton packet_btnEtp;

	// Text of status label
	private final static String TEXT_STATUS_READY = "Ready for Transmission.";
	private final static String TEXT_NEW_STATION = "Click on position where center of new Station should be set to.";
	private final static String TEXT_NEW_POSITION = "Click on desired new position of station ";
	private final static String TEXT_NO_SELECTION = "Necessary information for data transmission missing - cannot send any message.";

	// Window title and tab title
	private final static String TITLE_HTP = "Hidden Terminal Problem";
	private final static String TITLE_ETP = "Exposed Terminal Problem";

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					// Instantiate and configure JFrame
					frame = new MainGUI();
					frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					frame.setBounds(200, 200, frame.getWidth(),
							frame.getHeight());
					frame.setMinimumSize(new Dimension(1100, 500));
					frame.setVisible(true);

					// set title to match title of first tab
					frame.setTitle(TITLE_HTP);
					// frame.getContentPane().add(new MyCanvas());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the JFrame and its components
	 */
	public MainGUI() {
		setPreferredSize(new Dimension(825, 530));
		setMinimumSize(new Dimension(825, 530));
		initGui();
	}

	/**
	 * Initialize GUI elements
	 */
	private void initGui() {

		// Panel which contains all components of JFrame
		JPanel contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		// TabbedPane provides environment to change btw HTP and ETP
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setMinimumSize(new Dimension(500, 900));
		contentPane.add(tabbedPane, BorderLayout.CENTER);

		initHTP(tabbedPane);
		initETP(tabbedPane);

		// after tabs have been added in initHTP / initETP, add ChangeListener
		tabbedPane.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent arg0) {
				frame.setTitle(String.valueOf(tabbedPane.getTitleAt(tabbedPane
						.getSelectedIndex())));
			}

		});

	}

	/**
	 * Initialize GUI elements of the Exposed Terminal Tab
	 * 
	 * @param tabbedPane The tabbedPane where the new JPanel of ETP should be added to
	 */
	@SuppressWarnings("unchecked")
	private void initETP(JTabbedPane tabbedPane) {
		// Main panel of ETP Tab
		JPanel pnlEtpMain = new JPanel();
		tabbedPane.addTab(TITLE_ETP, null, pnlEtpMain, null);
		pnlEtpMain.setLayout(new BorderLayout(0, 0));

		// Create n default stations based on ETP_DEFAULT_NUM_STATIONS value
		// with ETP_DEFAULT_SIGNAL_STRENGTH
		ArrayList<Station> stations = new ArrayList<Station>();
		Station s;
		int[][] defaultPositions = { { 363, 154 }, { 477, 154 }, { 591, 154 },
				{ 705, 154 } };
		for (int i = 0; i < ETP_DEFAULT_NUM_STATIONS; i++) {
			s = new Station(ETP_DEFAULT_SIGNAL_STRENGTH);
			s.setX(defaultPositions[i][0]);
			s.setY(defaultPositions[i][1]);
			stations.add(s);
		}

		JPanel pnlEtpControl = new JPanel();
		pnlEtpMain.add(pnlEtpControl, BorderLayout.SOUTH);
		pnlEtpControl.setLayout(new BorderLayout(0, 0));

		JPanel pnlEtpStatusMsg = new JPanel();
		pnlEtpStatusMsg.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "STATUS",
				TitledBorder.LEADING, TitledBorder.TOP, null, Color.GRAY));
		pnlEtpControl.add(pnlEtpStatusMsg, BorderLayout.NORTH);
		pnlEtpStatusMsg.setLayout(new GridLayout(0, 1, 0, 0));

		JLabel lblEtpStatus = new JLabel("Ready.", SwingConstants.CENTER);
		lblEtpStatus.setForeground(Color.BLUE);
		lblEtpStatus.setBorder(new EmptyBorder(3, 0, 10, 0));
		pnlEtpStatusMsg.add(lblEtpStatus);

		// Panel containing the lower configuration area
		JPanel pnlEtpConfigArea = new JPanel();
		pnlEtpControl.add(pnlEtpConfigArea, BorderLayout.SOUTH);
		pnlEtpConfigArea.setBorder(new TitledBorder(null, "CONFIGURATION",
				TitledBorder.LEADING, TitledBorder.TOP, null, Color.GRAY));

		// Section 1 "Node config" of configuration area
		JPanel pnlEtpNodeCfg = new JPanel();
		pnlEtpNodeCfg
				.setBorder(BorderFactory.createTitledBorder("Node config"));
		pnlEtpConfigArea.add(pnlEtpNodeCfg);
		JButton btnEtpAddNode = new JButton("Add node");
		btnEtpAddNode.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				lblEtpStatus.setForeground(Color.blue);
				lblEtpStatus.setText(TEXT_NEW_STATION);
				pnlEtpContent.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
				flagEtpNewStation = true;
				tempStation = new Station(ETP_DEFAULT_SIGNAL_STRENGTH);
				stations.add(tempStation);
			}
		});
		pnlEtpNodeCfg.add(btnEtpAddNode);

		// Section 2 "Signal range info"
		JPanel pnlEtpSignalCfg = new JPanel();
		pnlEtpSignalCfg.setFont(new Font("Tahoma", Font.PLAIN, 10));
		pnlEtpSignalCfg.setBorder(BorderFactory
				.createTitledBorder("Signal range info"));
		pnlEtpConfigArea.add(pnlEtpSignalCfg);
		pnlEtpSignalCfg.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		JLabel lblEtpSelectedNode = new JLabel("Selected Node:");
		lblEtpSelectedNode.setBorder(new EmptyBorder(0, 5, 0, 3));
		pnlEtpSignalCfg.add(lblEtpSelectedNode);

		txtEtpSelectedNode = new JTextField();
		txtEtpSelectedNode.setEditable(false);
		txtEtpSelectedNode.setColumns(2);
		pnlEtpSignalCfg.add(txtEtpSelectedNode);

		JLabel lblEtpSignalStrength = new JLabel("Signal strength:");
		lblEtpSignalStrength.setBorder(new EmptyBorder(0, 5, 0, 3));
		pnlEtpSignalCfg.add(lblEtpSignalStrength);

		txtEtpSignalStrength = new JTextField();
		txtEtpSignalStrength.setEditable(false);
		txtEtpSignalStrength.setColumns(2);
		pnlEtpSignalCfg.add(txtEtpSignalStrength);

		JLabel lblEtpDbm = new JLabel("dBm");
		lblEtpDbm.setBorder(new EmptyBorder(0, 0, 0, 5));
		pnlEtpSignalCfg.add(lblEtpDbm);

		// Section 3 "Send messages" of configuration area
		JPanel pnlEtpSendMsg = new JPanel();
		pnlEtpSendMsg.setBorder(BorderFactory
				.createTitledBorder("Send messages"));
		pnlEtpConfigArea.add(pnlEtpSendMsg);
		JLabel lblEtpFrom = new JLabel("From");
		pnlEtpSendMsg.add(lblEtpFrom);
		JComboBox<Station> cbEtpFrom1 = new JComboBox<Station>();
		pnlEtpSendMsg.add(cbEtpFrom1);
		JLabel lblEtpTo1 = new JLabel("to");
		pnlEtpSendMsg.add(lblEtpTo1);
		JComboBox<Station> cbEtpTo1 = new JComboBox<Station>();
		pnlEtpSendMsg.add(cbEtpTo1);
		JLabel lblEtpFrom2 = new JLabel("and from");
		pnlEtpSendMsg.add(lblEtpFrom2);
		JComboBox<Station> cbEtpFrom2 = new JComboBox<Station>();
		pnlEtpSendMsg.add(cbEtpFrom2);
		JLabel lblEtpTo2 = new JLabel("to");
		pnlEtpSendMsg.add(lblEtpTo2);
		JComboBox<Station> cbEtpTo2 = new JComboBox<Station>();
		pnlEtpSendMsg.add(cbEtpTo2);

		// Initial filling of combo boxes
		fillAndClearCombos(new JComboBox[] {}, new JComboBox[] { cbEtpFrom1,
				cbEtpTo1, cbEtpFrom2, cbEtpTo2 }, stations);

		// Define itemChangeListener for combo boxes
		cbEtpFrom1.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				// itemChange is only valid if selection index is in valid
				// range
				if (cbEtpFrom1.getSelectedIndex() > -1) {
					fillAndClearCombos(new JComboBox[] { cbEtpFrom1 },
							new JComboBox[] { cbEtpTo1, cbEtpFrom2, cbEtpTo2 },
							stations);
				}
			}
		});

		cbEtpTo1.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				// itemChange is only valid if selection index is in valid
				// range
				if (cbEtpTo1.getSelectedIndex() > -1) {
					fillAndClearCombos(
							new JComboBox[] { cbEtpFrom1, cbEtpTo1 },
							new JComboBox[] { cbEtpFrom2, cbEtpTo2 }, stations);
				}
			}
		});

		cbEtpFrom2.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				// itemChange is only valid if selection index is in valid
				// range
				if (cbEtpTo1.getSelectedIndex() > -1) {
					fillAndClearCombos(new JComboBox[] { cbEtpFrom1, cbEtpTo1,
							cbEtpFrom2 }, new JComboBox[] { cbEtpTo2 },
							stations);
				}
			}
		});

		// Function of ETP 'Send' button
		JButton btnEtpSend = new JButton("Send");
		btnEtpSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (cbEtpFrom1.getSelectedIndex() == -1
						|| cbEtpFrom2.getSelectedIndex() == -1
						|| cbEtpTo1.getSelectedIndex() == -1
						|| cbEtpTo2.getSelectedIndex() == -1) {
					lblEtpStatus.setForeground(Color.BLUE);
					lblEtpStatus.setText(TEXT_NO_SELECTION);
					return;
				}

				Station StCbFrom1 = Station.getStationByName(stations,
						cbEtpFrom1.getSelectedItem().toString());
				Station StCbFrom2 = Station.getStationByName(stations,
						cbEtpFrom2.getSelectedItem().toString());
				Station StCbTo1 = Station.getStationByName(stations, cbEtpTo1
						.getSelectedItem().toString());
				Station StCbTo2 = Station.getStationByName(stations, cbEtpTo2
						.getSelectedItem().toString());

				AppLogic a = new AppLogic();

				int result = a.IsIntersecting(StCbFrom1.xPosSignal,
						StCbFrom1.yPosSignal, StCbFrom2.xPosSignal,
						StCbFrom2.yPosSignal, StCbTo1.xPosSignal,
						StCbTo1.yPosSignal, StCbTo2.xPosSignal,
						StCbTo2.yPosSignal,
						(StCbFrom1.getSignalStrength() - 46),
						(StCbFrom2.getSignalStrength() - 46),
						(StCbTo1.getSignalStrength() - 46),
						(StCbTo2.getSignalStrength() - 46));

				if (result == 1) {
					packet_btnEtp.setVisible(true);
					a.runAnimationThread(StCbFrom1.getX(), StCbFrom1.getY(),
							StCbTo1.getX(), StCbTo1.getY(), packet_btnEtp);
					lblEtpStatus.setForeground(Color.RED);
					lblEtpStatus.setText(StCbFrom2.getName()
							+ " is exposed to " + StCbFrom1.getName());

				}

				else if (result == 2) {
					lblEtpStatus.setForeground(Color.GREEN);
					packet_btnEtp.setVisible(true);
					a.runAnimationThread(StCbFrom1.getX(), StCbFrom1.getY(),
							StCbTo1.getX(), StCbTo1.getY(), packet_btnEtp);
					a.runAnimationThread(StCbFrom2.getX(), StCbFrom2.getY(),
							StCbTo2.getX(), StCbTo2.getY(), packet_btnEtp);
					lblEtpStatus.setText("Communicating successfuly");
				}

				else {
					lblEtpStatus.setForeground(Color.ORANGE);
					lblEtpStatus
							.setText("Destination(s) out of range - no communication possible.");
				}

			}
		});
		pnlEtpSendMsg.add(btnEtpSend);

		// Initial filling of combo boxes
		fillAndClearCombos(new JComboBox[] {}, new JComboBox[] { cbEtpFrom1,
				cbEtpTo1, cbEtpFrom2, cbEtpTo2 }, stations);

		// Draw stations on EtpContent panel
		pnlEtpContent = new JPanel() {
			private static final long serialVersionUID = 1L;

			public void paintComponent(Graphics g2) {
				super.paintComponent(g2);
				drawStations(g2, stations);
			}
		};

		pnlEtpContent.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				lblEtpStatus.setForeground(Color.blue);
				int clickedX = e.getX();
				int clickedY = e.getY();
				if (flagEtpNewStation) {
					// "add node" button was just pressed
					fillAndClearCombos(new JComboBox[] {}, new JComboBox[] {
							cbEtpFrom1, cbEtpFrom2, cbEtpTo1, cbEtpTo2 },
							stations);
					lblEtpStatus.setForeground(Color.BLUE);
					lblEtpStatus.setText(TEXT_STATUS_READY);
					flagEtpNewStation = false;
					pnlEtpContent.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
					pnlEtpContent.repaint();

				} else if (flagEtpMoveMode) {
					// right-click done - existing node should be moved
					Station n = Station.getStationByName(stations,
							txtEtpSelectedNode.getText());
					if (n != null) {
						n.setX(clickedX);
						n.setY(clickedY);
						flagEtpMoveMode = false;
						lblEtpStatus.setForeground(Color.BLUE);
						lblEtpStatus.setText(TEXT_STATUS_READY);
						pnlEtpContent.setCursor(new java.awt.Cursor(
								Cursor.DEFAULT_CURSOR));
						pnlEtpContent.repaint();
					}
				} else {
					// normal selection-mode of existing nodes
					Station n = getStation(stations, clickedX, clickedY);
					if (n != null) {
						txtEtpSelectedNode.setText(n.getName());
						txtEtpSignalStrength.setText(String.valueOf(n
								.getSignalStrength()));
						// if right-button was clicked - move mode activated
						if (e.isMetaDown()) {
							lblEtpStatus.setForeground(Color.BLUE);
							lblEtpStatus.setText(TEXT_NEW_POSITION
									+ txtEtpSelectedNode.getText());
							pnlEtpContent.setCursor(new java.awt.Cursor(
									Cursor.CROSSHAIR_CURSOR));
							flagEtpMoveMode = true;
						}
					} else {
						txtEtpSelectedNode.setText("");
						txtEtpSignalStrength.setText("");
					}
				}
			}
		});
		
		// MouseListener for "visual move" mode
		pnlEtpContent.addMouseMotionListener(new MouseMotionListener() {
			@Override
			public void mouseMoved(MouseEvent e) {
				if (flagEtpNewStation) {
					tempStation.setX(e.getX());
					tempStation.setY(e.getY());
					pnlEtpContent.repaint();
				}
				else if (flagEtpMoveMode) {
					Station n = Station.getStationByName(stations,
							txtEtpSelectedNode.getText());
					if (n != null) {
						n.setX(e.getX());
						n.setY(e.getY());
						pnlEtpContent.repaint();
					}
				}
			}

			@Override
			public void mouseDragged(MouseEvent e) {
			}

		});
		pnlEtpMain.add(pnlEtpContent, BorderLayout.CENTER);

		packet_btnEtp = new JButton("Message");
		packet_btnEtp.setBackground(Color.ORANGE);
		packet_btnEtp.setText("Msg");
		packet_btnEtp.setVisible(false);
		pnlEtpContent.add(packet_btnEtp);
	}

	/**
	 * Draws the stations on the given Graphics object
	 * @param g2 The graphics object where the stations in stations should be painted on
	 * @param stations The ArrayList with the stations objects
	 */
	protected void drawStations(Graphics g2, ArrayList<Station> stations) {

		// Calculate positions
		Dimension panelDim = pnlHtpContent.getSize();
		double areaDelimiters = (panelDim.getWidth() / ((double) stations
				.size() + 1));
		double offset = areaDelimiters / 2;
		int offsetCnt = 2;
		int x = (int) (offset * offsetCnt);
		int y = (int) (panelDim.getHeight() / 2);

		// Create stations and draw them
		for (Station s : stations) {
			x = s.getX();
			y = s.getY();
			s.draw(g2, x, y);
		}
	}

	
	/**
	 * Initializes the GUI elements of the Hidden Terminal Tab
	 * @param tabbedPane
	 */
	@SuppressWarnings("unchecked")
	private void initHTP(JTabbedPane tabbedPane) {

		// Main panel of HTP Tab
		JPanel pnlHtpMain = new JPanel();
		tabbedPane.addTab(TITLE_HTP, null, pnlHtpMain, null);
		pnlHtpMain.setLayout(new BorderLayout(0, 0));

		// Create n default stations based on HTP_DEFAULT_NUM_STATIONS value
		// with HTP_DEFAULT_SIGNAL_STRENGTH
		ArrayList<Station> stations = new ArrayList<Station>();
		Station s;
		int[][] defaultPositions = { { 437, 154 }, { 534, 154 }, { 631, 154 } };
		for (int i = 0; i < HTP_DEFAULT_NUM_STATIONS; i++) {
			s = new Station(HTP_DEFAULT_SIGNAL_STRENGTH);
			s.setX(defaultPositions[i][0]);
			s.setY(defaultPositions[i][1]);
			stations.add(s);
		}

		JPanel pnlHtpControl = new JPanel();
		pnlHtpMain.add(pnlHtpControl, BorderLayout.SOUTH);
		pnlHtpControl.setLayout(new BorderLayout(0, 0));

		JPanel pnlHtpStatusMsg = new JPanel();
		pnlHtpControl.add(pnlHtpStatusMsg, BorderLayout.NORTH);
		pnlHtpStatusMsg.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "STATUS",
				TitledBorder.LEADING, TitledBorder.TOP, null, Color.GRAY));
		pnlHtpStatusMsg.setLayout(new GridLayout(0, 1, 0, 0));
		JLabel lblHtpStatus = new JLabel(TEXT_STATUS_READY,
				SwingConstants.CENTER);
		lblHtpStatus.setForeground(Color.BLUE);

		lblHtpStatus.setBorder(new EmptyBorder(3, 0, 10, 0));
		pnlHtpStatusMsg.add(lblHtpStatus);

		// Panel containing the lower configuration area
		JPanel pnlHtpConfigArea = new JPanel();
		pnlHtpControl.add(pnlHtpConfigArea, BorderLayout.SOUTH);
		pnlHtpConfigArea.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "CONFIGURATION",
				TitledBorder.LEADING, TitledBorder.TOP, null, Color.GRAY));
		pnlHtpConfigArea.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		// Section 1 "Node config" of configuration area
		JPanel pnlHtpTitleNodeCfg = new JPanel();
		pnlHtpTitleNodeCfg.setBorder(BorderFactory
				.createTitledBorder("Node config"));
		pnlHtpConfigArea.add(pnlHtpTitleNodeCfg);
		JButton btnHtpAddNode = new JButton("Add node");
		btnHtpAddNode.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				lblHtpStatus.setForeground(Color.blue);
				lblHtpStatus.setText(TEXT_NEW_STATION);
				pnlHtpContent.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
				flagHtpNewStation = true;
				tempStation = new Station(HTP_DEFAULT_SIGNAL_STRENGTH);
				stations.add(tempStation);
			}
		});

		pnlHtpTitleNodeCfg.add(btnHtpAddNode);

		// Section 2 "Signal range info" of configuration area
		JPanel pnlHtpTitleSignalCfg = new JPanel();
		pnlHtpTitleSignalCfg.setFont(new Font("Tahoma", Font.PLAIN, 10));
		pnlHtpTitleSignalCfg.setBorder(BorderFactory
				.createTitledBorder("Signal range info"));
		pnlHtpConfigArea.add(pnlHtpTitleSignalCfg);
		pnlHtpTitleSignalCfg.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		JLabel lblHtpSelectedNode = new JLabel("Selected Node:");
		lblHtpSelectedNode.setBorder(new EmptyBorder(0, 5, 0, 3));
		pnlHtpTitleSignalCfg.add(lblHtpSelectedNode);

		txtHtpSelectedNode = new JTextField();
		txtHtpSelectedNode.setEditable(false);
		pnlHtpTitleSignalCfg.add(txtHtpSelectedNode);
		txtHtpSelectedNode.setColumns(2);
		txtHtpSelectedNode.setHorizontalAlignment(JTextField.CENTER);

		JLabel lblHtpSignalStrength = new JLabel("Signal strength:");
		lblHtpSignalStrength.setBorder(new EmptyBorder(0, 5, 0, 3));
		pnlHtpTitleSignalCfg.add(lblHtpSignalStrength);
		JTextField txtHtpSignalStrength = new JTextField();
		txtHtpSignalStrength.setEditable(false);
		pnlHtpTitleSignalCfg.add(txtHtpSignalStrength);
		txtHtpSignalStrength.setColumns(2);
		JLabel lblHtpDbm = new JLabel("dBm");
		lblHtpDbm.setBorder(new EmptyBorder(0, 0, 0, 5));
		pnlHtpTitleSignalCfg.add(lblHtpDbm);

		// Section 3 "Send messages" of configuration area
		JPanel pnlHtpTitleSendMsg = new JPanel();
		pnlHtpTitleSendMsg.setBorder(BorderFactory
				.createTitledBorder("Send messages"));
		pnlHtpConfigArea.add(pnlHtpTitleSendMsg);
		JLabel lblHtpFrom = new JLabel("From");
		pnlHtpTitleSendMsg.add(lblHtpFrom);
		JComboBox<Station> cbHtpFrom1 = new JComboBox<Station>();
		cbHtpFrom1.setName("cbHtpFrom1");

		pnlHtpTitleSendMsg.add(cbHtpFrom1);
		JComboBox<Station> cbHtpFrom2 = new JComboBox<Station>();
		cbHtpFrom2.setName("cbHtpFrom2");
		pnlHtpTitleSendMsg.add(cbHtpFrom2);
		JLabel lblHtpTo = new JLabel("to");
		pnlHtpTitleSendMsg.add(lblHtpTo);
		JComboBox<Station> cbHtpTo1 = new JComboBox<Station>();
		cbHtpTo1.setName("cbHtpTo1");
		pnlHtpTitleSendMsg.add(cbHtpTo1);
		// Function of HTP 'Send' button
		JButton btnHtpSend = new JButton("Send");
		btnHtpSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (cbHtpFrom1.getSelectedIndex() == -1
						|| cbHtpFrom2.getSelectedIndex() == -1
						|| cbHtpTo1.getSelectedIndex() == -1) {
					lblHtpStatus.setForeground(Color.BLUE);
					lblHtpStatus.setText(TEXT_NO_SELECTION);
					return;
				}
				Station StCbFrom1 = Station.getStationByName(stations,
						cbHtpFrom1.getSelectedItem().toString());
				Station StCbFrom2 = Station.getStationByName(stations,
						cbHtpFrom2.getSelectedItem().toString());
				Station StCbTo1 = Station.getStationByName(stations, cbHtpTo1
						.getSelectedItem().toString());

				AppLogic a = new AppLogic();
				int result = a.IsIntersecting(StCbFrom1.xPosSignal,
						StCbFrom1.yPosSignal, StCbFrom2.xPosSignal,
						StCbFrom2.yPosSignal, StCbTo1.xPosSignal,
						StCbTo1.yPosSignal,
						(StCbFrom1.getSignalStrength() - 46),
						(StCbFrom2.getSignalStrength() - 46),
						(StCbTo1.getSignalStrength() - 46));

				if (result == 1) {
					packet_btnHtp.setVisible(true);
					a.runAnimationThread(StCbFrom1.getX(), StCbFrom1.getY(),
							StCbTo1.getX(), StCbTo1.getY(), packet_btnHtp);
					a.runAnimationThread(StCbFrom2.getX(), StCbFrom2.getY(),
							StCbTo1.getX(), StCbTo1.getY(), packet_btnHtp);
					lblHtpStatus.setForeground(Color.RED);
					lblHtpStatus.setText("Collision occurs!");

				}

				else if (result == 2) {
					packet_btnHtp.setVisible(true);
					a.runAnimationThread(StCbFrom1.getX(), StCbFrom1.getY(),
							StCbTo1.getX(), StCbTo1.getY(), packet_btnHtp);
					a.runAnimationThread(StCbFrom2.getX(), StCbFrom2.getY(),
							StCbTo1.getX(), StCbTo1.getY(), packet_btnHtp);
					lblHtpStatus.setForeground(Color.GREEN);
					lblHtpStatus.setText("Communication successfully");
				}

				else {
					lblHtpStatus.setForeground(Color.ORANGE);
					lblHtpStatus
							.setText("Destination out of range - no communication possible.");
				}
			}
		});

		pnlHtpTitleSendMsg.add(btnHtpSend);

		// Initial filling of combo boxes
		fillAndClearCombos(new JComboBox[] {}, new JComboBox[] { cbHtpFrom1,
				cbHtpFrom2, cbHtpTo1 }, stations);

		// Define itemChangeListener for combo boxes
		cbHtpFrom1.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				// itemChange is only valid if selection index is in valid range
				if (cbHtpFrom1.getSelectedIndex() > -1) {
					fillAndClearCombos(new JComboBox[] { cbHtpFrom1 },
							new JComboBox[] { cbHtpFrom2, cbHtpTo1 }, stations);
				}
			}
		});
		cbHtpFrom2.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (cbHtpFrom2.getSelectedIndex() > -1) {
					fillAndClearCombos(
							new JComboBox[] { cbHtpFrom1, cbHtpFrom2 },
							new JComboBox[] { cbHtpTo1 }, stations);
				}
			}
		});

		cbHtpFrom1.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				// itemChange is only valid if selection index is in valid range
				if (cbHtpFrom1.getSelectedIndex() > -1) {
					fillAndClearCombos(new JComboBox[] { cbHtpFrom1 },
							new JComboBox[] { cbHtpFrom2, cbHtpTo1 }, stations);
				}
			}
		});
		cbHtpFrom2.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (cbHtpFrom2.getSelectedIndex() > -1) {
					fillAndClearCombos(
							new JComboBox[] { cbHtpFrom1, cbHtpFrom2 },
							new JComboBox[] { cbHtpTo1 }, stations);
				}
			}
		});

		// Draw stations on EtpContent panel
		pnlHtpContent = new JPanel() {
			private static final long serialVersionUID = 1L;
			public void paintComponent(Graphics g2) {
				super.paintComponent(g2);
				drawStations(g2, stations);
			}
		};

		pnlHtpContent.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int clickedX = e.getX();
				int clickedY = e.getY();
				if (flagHtpNewStation) {
					// "add node" was just pressed
					fillAndClearCombos(new JComboBox[] {}, new JComboBox[] {
							cbHtpFrom1, cbHtpFrom2, cbHtpTo1 }, stations);
					lblHtpStatus.setForeground(Color.BLUE);
					lblHtpStatus.setText(TEXT_STATUS_READY);
					flagHtpNewStation = false;
					pnlHtpContent.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
					pnlHtpContent.repaint();

				} else if (flagHtpMoveMode) {
					// right-click was done on existing node - set now new position
					Station n = Station.getStationByName(stations,
							txtHtpSelectedNode.getText());
					if (n != null) {
						n.setX(clickedX);
						n.setY(clickedY);
						flagHtpMoveMode = false;
						lblHtpStatus.setForeground(Color.BLUE);
						lblHtpStatus.setText(TEXT_STATUS_READY);
						pnlHtpContent.setCursor(new java.awt.Cursor(
								Cursor.DEFAULT_CURSOR));
						pnlHtpContent.repaint();
					}
				} else {
					// normal selection-mode of existing nodes
					Station n = getStation(stations, clickedX, clickedY);
					if (n != null) {
						txtHtpSelectedNode.setText(n.getName());
						txtHtpSignalStrength.setText(String.valueOf(n
								.getSignalStrength()));
						// if right-button was clicked - move mode activated
						if (e.isMetaDown()) {
							lblHtpStatus.setForeground(Color.BLUE);
							lblHtpStatus.setText(TEXT_NEW_POSITION
									+ txtHtpSelectedNode.getText());
							pnlHtpContent.setCursor(new java.awt.Cursor(
									Cursor.CROSSHAIR_CURSOR));
							flagHtpMoveMode = true;
						}
					} else {
						txtHtpSelectedNode.setText("");
						txtHtpSignalStrength.setText("");
					}
				}
			}
		});
		// listeners for "visual move" mode
		pnlHtpContent.addMouseMotionListener(new MouseMotionListener() {
			@Override
			public void mouseMoved(MouseEvent e) {
				if (flagHtpNewStation) {
					tempStation.setX(e.getX());
					tempStation.setY(e.getY());
					pnlHtpContent.repaint();
				}
				else if (flagHtpMoveMode) {
					Station n = Station.getStationByName(stations,
							txtHtpSelectedNode.getText());
					if (n != null) {
						n.setX(e.getX());
						n.setY(e.getY());
						pnlHtpContent.repaint();
					}
				}
			}

			@Override
			public void mouseDragged(MouseEvent e) {
			}

		});
		pnlHtpMain.add(pnlHtpContent, BorderLayout.CENTER);
		pnlHtpContent.setLayout(null);

		packet_btnHtp = new JButton("Message");
		packet_btnHtp.setBackground(Color.ORANGE);
		packet_btnHtp.setText("Msg");
		packet_btnHtp.setVisible(false);
		pnlHtpContent.add(packet_btnHtp);
	}

	/**
	 * Receives an Array of combo boxes 'keep' (will not be cleared) and 'fill' (will be cleared at beginning). 
	 * After removing items of 'fill' combo boxes, all stations will be added to all 'fill' combo boxes. 
	 * Then there will be a iteration over all keep combo boxes, which executes a deletion of all elements which
	 * are selected in one of keep combo boxes plus the preceding combo boxes (-> order of given array 'fill' is important!)
	 * @param keep The combo boxes which will not be modified, only the selectedItem will be retrieved
	 * @param fill The combo boxes which should be filled with all stations except those selected in keep combo boxes
	 * or preceding fill combo boxes (-> order is important!)
	 * @param stations The ArrayList of station objects to add
	 */
	private void fillAndClearCombos(JComboBox<Station>[] keep,
			JComboBox<Station>[] fill, ArrayList<Station> stations) {

		// clear all 'fill' combo boxes
		for (int i = 0; i < fill.length; i++) {
			fill[i].removeAllItems();
		}

		// add to all 'fill' combo boxes all stations
		for (int i = 0; i < fill.length; i++) {
			for (Station s : stations) {
				fill[i].addItem(s);
			}
		}

		// remove from 'fill' combo boxes selection of 'keep' combo boxes
		// accumulates selection of previous combo boxes
		ArrayList<Station> previousSelections = new ArrayList<Station>();
		for (int j = 0; j < fill.length; j++) {
			for (Station prev : previousSelections) {
				fill[j].removeItem(prev);
			}
			for (int i = 0; i < keep.length; i++) {
				fill[j].removeItem(keep[i].getSelectedItem());
			}
			previousSelections.add((Station) (fill[j].getSelectedItem()));
		}

		// set default value of all fill combo boxes to "" 
		// -> avoids non-setting of combo boxes
		for (int i = 0; i < fill.length; i++) {
			fill[i].setSelectedItem(null);
		}
	}

	/**
	 * Retrieves a station from an ArryList s, based on the given coordinates pointX, pointY
	 * @param s The list of stations which should be used to search for
	 * @param pointX The x-coordinate of the station to find
	 * @param pointY The y-coordinate of the station to find
	 * @return null if no station was found, otherwise the station object with coordinates pointX, pointY
	 */
	private Station getStation(ArrayList<Station> s, int pointX, int pointY) {
		for (Station n : s) {
			int stationX = n.getX();
			int stationY = n.getY();
			double distance = Math.sqrt((pointX - stationX)
					* (pointX - stationX) + (pointY - stationY)
					* (pointY - stationY));
			if (distance <= n.getStationRadius()) {
				return n;
			}
		}
		return null;
	}


}