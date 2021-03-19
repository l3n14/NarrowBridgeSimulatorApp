import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/* 
 *  Symulacja problemu przejazdu przez wąski most
 *  
 *
 *  Autor: Leanid Paulouski
 *   Data: styczeń 2020 r.
 */

enum AutoDirection {
	EAST, WEST;

	@Override
	public String toString() {
		switch (this) {
		case EAST:
			return "E";
		case WEST:
			return "W";
		}
		return "";
	}
}

enum BridgeType{
	oneWay("One way(max 1 auto)"), twoWay("Two way (max 3 cars)"), oneWay1("One way(max 3 autos)"),noLimits("No limits");
	
	String type;
	
	private BridgeType(String Type) {
		type = Type;
	}
	
	@Override
	public String toString() {
		return type;
	}
}

public class BridgeApp extends JFrame implements ActionListener {
	
	private static final Font font = new Font("MonoSpaced", Font.BOLD, 14);
    
	public static void main(String[] args) {
		Bridge bridge = new Bridge();
		new BridgeApp();
		while (true) {
			Auto auto = new Auto(bridge);
			new Thread(auto).start();

			try {
				Thread.sleep(5500 - getTrafficValue());
			} catch (InterruptedException e) {
			}
		}
	}
	
	public BridgeApp() {
		createUI();
	}
	
	JPanel panel=new JPanel(null);
	static JComboBox<BridgeType> box=new JComboBox<BridgeType>(BridgeType.values());
	static JLabel waitingAutoInfo=new JLabel();
	static JLabel onBridgeAutoInfo=new JLabel();
	static JLabel currentTraffic1=new JLabel();
	static JLabel trafficLimitsLabel=new JLabel("Traffic limits:");
	static JLabel directionLabel=new JLabel("Direction:");
	static JLabel trafficLabel=new JLabel("Traffic:");
	static JTextArea textArea = new JTextArea();
	static JSlider directionSlider=new JSlider(0,100);
	static JSlider trafficSlider=new JSlider(500,5000);
	static JTextField waitingAutosNumbers=new JTextField();
	static JTextField bridgeAutosNumbers=new JTextField();
	JPanel line=new JPanel(null);
	JScrollPane textPane=new JScrollPane(textArea);
	
    public static void addStringInfo(String info) {
    	textArea.append(info+"\n");
    	textArea.setCaretPosition(textArea.getDocument().getLength());
    }
    
    public static void setLabels(int waiting, int onBridge) {
    	waitingAutoInfo.setText("Number of waiting cars : "+waiting);
    	onBridgeAutoInfo.setText("Number of cars on the Bridge: "+onBridge);
    }
    
    public static void setWaitingText(int id) {
    	waitingAutosNumbers.setText(""+id);
    }
    
    public static void setBridgeText(int id) {
    	bridgeAutosNumbers.setText(""+id);
    	
    }
    
    public static int getDirectionValue() {
    	return directionSlider.getValue();
    }
    
    public static int getTrafficValue() {
    	return trafficSlider.getValue();
    }
    
	public void createUI(){
		
		setSize(550,600);
		setDefaultCloseOperation(3);
		setLayout(new BorderLayout());
		setResizable(false);
		setTitle("Bridge App");
		
		line.setBackground(Color.BLACK);
		line.setBounds(290,10,2,200);
		panel.add(line);
		
		waitingAutoInfo.setText("Number of waiting cars : "+Bridge.getNumberWaiting());
    	onBridgeAutoInfo.setText("Number of cars on the Bridge: "+Bridge.getNumberOnBridge());
		
		waitingAutoInfo.setFont(font);
		waitingAutoInfo.setBounds(4, 24, 280, 20);
		panel.add(waitingAutoInfo);
		
		onBridgeAutoInfo.setFont(font);
		onBridgeAutoInfo.setBounds(4, 114, 300, 20);
		panel.add(onBridgeAutoInfo);
		
		waitingAutosNumbers.setBounds(4, 64, 270, 20);
		panel.add(waitingAutosNumbers);
		
		bridgeAutosNumbers.setBounds(4, 159, 270, 20);
		panel.add(bridgeAutosNumbers);
		
		Hashtable labelTable = new Hashtable();
		labelTable.put( new Integer(  50), new JLabel("Medium") );
		labelTable.put( new Integer(  0), new JLabel("East") );
		labelTable.put( new Integer( 100 ), new JLabel("West") );
		directionSlider.setLabelTable( labelTable );
		directionSlider.setPaintLabels(true);
		
		directionSlider.setFont(font);
		directionSlider.setBounds(314, 159, 200, 30);
		directionSlider.setBackground(Color.LIGHT_GRAY);
		panel.add(directionSlider);
		
		Hashtable labelTable2 = new Hashtable();
		labelTable2.put( new Integer(  2500), new JLabel("Medium") );
		labelTable2.put( new Integer(  500), new JLabel("Slow") );
		labelTable2.put( new Integer( 5000 ), new JLabel("High") );
		trafficSlider.setLabelTable( labelTable2 );
		trafficSlider.setPaintLabels(true);
		
		trafficSlider.setFont(font);
		trafficSlider.setBounds(314, 84, 200, 30);
		trafficSlider.setBackground(Color.LIGHT_GRAY);
		panel.add(trafficSlider);
		
		directionLabel.setFont(font);
		directionLabel.setBounds(374, 129, 210, 20);
		panel.add(directionLabel);
		
		trafficLabel.setFont(font);
		trafficLabel.setBounds(384, 59, 210, 20);
		panel.add(trafficLabel);
		
		textArea.setFont(font);
		textArea.setEditable(false);
		
		trafficLimitsLabel.setFont(font);
		trafficLimitsLabel.setBounds(354, 4, 150, 20);
		panel.add(trafficLimitsLabel);
		
		box.setBounds(304, 24, 225, 20);
		box.setFont(font);
		panel.add(box);
		
		textPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		textPane.setBounds(4, 220, 527, 340);
		panel.add(textPane);
		
		panel.setBackground(Color.LIGHT_GRAY);
		add(panel);
		setContentPane(panel);
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		
		
	}
}
