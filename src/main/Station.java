package main;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

public class Station {

	private static int nameCnt = 0;
	private String name;
	private int signalStrength;
	private int stationRadius = 60;
	private int x = -1;
	private int y = -1;
	public int xPosSignal;
	public int yPosSignal;
	public int signalWidth;
	public int signalHeight;
	
	public Station(int signalStrength) {
		setName((char)nameCnt);
		nameCnt++;
		this.setSignalStrength(signalStrength);
	}

	public String getName() {
		return name;
	}

	public void setName(char nameCnt2) {
		this.name = String.valueOf((char) ('A' + nameCnt2));
	}

	public int getSignalStrength() {
		return signalStrength;
	}

	public void setSignalStrength(int signalStrength) {
		this.signalStrength = signalStrength;
	}
	
	public int getStationRadius() {
		return this.stationRadius;
	}
	
	public void draw(Graphics g2, int centerX, int centerY) {
		Graphics2D g = (Graphics2D)g2;  
        		
        // Draw station
        int xPos = centerX-stationRadius/2;
        int yPos = centerY-stationRadius/2;
        g.setColor(Color.blue);
        g.fillOval(xPos, yPos, stationRadius, stationRadius);
        
        // Draw signal range around station
        g.setColor(Color.black);
        xPosSignal = centerX-(stationRadius+this.signalStrength)/2;
        yPosSignal = centerY-(stationRadius+this.signalStrength)/2;
        signalWidth = stationRadius+this.signalStrength;
        signalHeight = stationRadius+this.signalStrength;
        g.drawOval(xPosSignal, yPosSignal,
        		signalWidth, signalHeight);

        // Draw centered text
        FontMetrics fm = g.getFontMetrics();
        g.setFont(new Font(g.getFont().getName(), Font.BOLD, 18));
        g.setColor(Color.WHITE);
        double textWidth = fm.getStringBounds(this.getName(), g).getWidth();     
        g.drawString(this.getName(), (int) (centerX - textWidth/2), (int) (centerY + fm.getMaxAscent() / 2));

        
	}
	
	public static Station getStationByName(ArrayList<Station> s, String name) {
		for (Station n : s) {
			if (n.getName().equals(name)) {
				return n;
			}
		}
		return null;
	}

	public void setY(int yPos) {
		this.y = yPos;
	}

	public void setX(int xPos) {
		this.x = xPos;
	}
	
	public int getY() {
		return this.y;
	}
	
	public int getX() {
		return this.x;
	}
	
	
	@Override
	public String toString() {
		return this.getName();
	}
	
}
