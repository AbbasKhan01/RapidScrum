package main;

import java.awt.Point;

import javax.swing.JButton;

/**
 * Class for representing the application logic. This class calculates:
 * <ul>
 * 	<li>If intersection of signal ranges of two stations occurs</li>
 *  <li>If Intersection of signal ranges of three stations occurs</li>
 *  <li>Which route (coordinates) messages are moved during simulation</li>
 * </ul>
 *
 */
public class AppLogic {
	/**
	 * IsIntersecting method for HTP problem to find whether the circles
	 * intersect each other or not
	 * @param xCbFrom1 x-point of the first sending station
	 * @param yCbFrom1 y-point of the first sending station
	 * @param xCbFrom2 x-point of the second sending station
	 * @param yCbFrom2 y-point of the second sending station
	 * @param xCbTo1 x-point of the receiving station
	 * @param yCbTo1 y-point of the receiving station
	 * @param rCbFrom1 radius of the first sending station
	 * @param rCbFrom2 radius of the second sending station
	 * @param rCbTo1 radius of the receiving station
	 * @return whether circles intersecting or not
	 */
	public int IsIntersecting(int xCbFrom1, int yCbFrom1, int xCbFrom2,
			int yCbFrom2, int xCbTo1, int yCbTo1, int rCbFrom1, int rCbFrom2,
			int rCbTo1) {
		int result = 0;
		int r1 = rCbFrom1 + rCbFrom2;
		int r2 = rCbFrom2 + rCbTo1;
		int r3 = rCbFrom1 + rCbTo1;
		int DAB, DBC, DAC;
		int x1 = xCbFrom1 - xCbFrom2;
		int y1 = yCbFrom1 - yCbFrom2;
		int x2 = xCbFrom2 - xCbTo1;
		int y2 = yCbFrom2 - yCbTo1;
		int x3 = xCbTo1 - xCbFrom1;
		int y3 = yCbTo1 - yCbFrom1;

		DAB = (int) Math.sqrt((x1 * x1) + (y1 * y1));
		DBC = (int) Math.sqrt((x2 * x2) + (y2 * y2));
		DAC = (int) Math.sqrt((x3 * x3) + (y3 * y3));

		if (DAB <= r1 && DBC <= r2 && DAC <= r3)
			result = 1;
		else if (DAB > r1 && DBC <= r2 && DAC <= r3) {
			result = 2;
		} else
			result = 3;

		return result;
	}

	// overloaded version of IsIntersecting method for ETP problem to find
	// weather the circles intersect each other or not
	/**
	 * IsIntersecting method for HTP problem to find whether the circles
	 * intersect each other or not
	 * @param xCbFrom1 x-point of the first sending station
	 * @param yCbFrom1 y-point of the first sending station
	 * @param xCbFrom2 x-point of the second sending station
	 * @param yCbFrom2 y-point of the second sending station
	 * @param xCbTo1 x-point of the first receiving station
	 * @param yCbTo1 y-point of the first receiving station
	 * @param xCbTo2 x-point of the second receiving station
	 * @param yCbTo2 y-point of the second receiving station
	 * @param rCbFrom1 radius of the first sending station
	 * @param rCbFrom2 radius of the second sending station
	 * @param rCbTo1 radius of the first receiving station
	 * @param rCbTo2 radius of the second receiving station
	 * @return result whether circles intersecting or not
	 */
	public int IsIntersecting(int xCbFrom1, int yCbFrom1, int xCbFrom2,
			int yCbFrom2, int xCbTo1, int yCbTo1, int xCbTo2, int yCbTo2,
			int rCbFrom1, int rCbFrom2, int rCbTo1, int rCbTo2) {
		int result = 0;
		int r1 = rCbFrom1 + rCbTo1;
		int r2 = rCbFrom2 + rCbTo2;
		int r3 = rCbFrom1 + rCbFrom2;
		int DF1T1, DF2T2, DF1F2;
		int x1 = xCbFrom1 - xCbTo1;
		int y1 = yCbFrom1 - yCbTo1;
		int x2 = xCbFrom2 - xCbTo2;
		int y2 = yCbFrom2 - yCbTo2;
		int x3 = xCbFrom1 - xCbFrom2;
		int y3 = yCbFrom1 - yCbFrom2;

		DF1T1 = (int) Math.sqrt((x1 * x1) + (y1 * y1));
		DF2T2 = (int) Math.sqrt((x2 * x2) + (y2 * y2));
		DF1F2 = (int) Math.sqrt((x3 * x3) + (y3 * y3));

		if (DF1T1 <= r1 && DF2T2 <= r2 && DF1F2 <= r3)
			result = 1;
		else if (DF1T1 <= r1 && DF2T2 <= r2 && DF1F2 > r3) {
			result = 2;
		} else
			result = 3;

		return result;
	}

	/**
	 * Thread to animate the message packet from initial position to destination
	 * @param startX starting x-position of the message packet 
	 * @param startY starting y-position of the message packet 
	 * @param finalX ending x-position of the message packet 
	 * @param finalY ending y-position of the message packet 
	 * @param button works as a message packet to be animated
	 */
	public void runAnimationThread(int startX, int startY, int finalX,
			int finalY, JButton button) {
		try {
			Thread t = new Thread() {
				public void run() {
					animateButton(startX, startY, finalX, finalY, button);
				}
			};
			t.start();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
	
	/**
	 * Function that is passed to runAnimationThread
	 * @param startX starting x-position of the message packet 
	 * @param startY starting y-position of the message packet 
	 * @param finalX ending x-position of the message packet 
	 * @param finalY ending y-position of the message packet 
	 * @param button works as a message packet to be animated
	 */
	public void animateButton(int startX, int startY, int finalX, int finalY,
			JButton button) {
		boolean reach_destination = false;
		Point c = new Point(startX, startY);

		while (!reach_destination) {
			try {
				Thread.sleep(50);
			} catch (Exception e) {
				e.printStackTrace();
			}

			button.setBounds(c.x, c.y, 30, 20);
			c = get_pattern(c, finalX, finalY);
			if (c.x == finalX && c.y == finalY) {
				button.setBounds(finalX, finalY, 30, 20);
				reach_destination = true;
				try {
					Thread.sleep(700);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		button.setVisible(false);
	}

	/**
	 * Function to calculate pattern of animation
	 * @param c to get initial x-point and y-point in order to compare them with finalX and finalY
	 * @param finalX
	 * @param finalY
	 * @return next position of the message packet
	 */
	public Point get_pattern(Point c, int finalX, int finalY) {
		if (c.x > finalX) {
			if (c.x > (finalX - 1))
				c.x = c.x - 1;
			else
				c.x = finalX;
		} else if (c.x < finalX) {
			if (c.x < (finalX + 1))
				c.x = c.x + 1;
			else
				c.x = finalX;
		} else
			c.x = finalX;

		if (c.y > finalY) {
			if (c.y > (finalY - 1))
				c.y = c.y - 1;
			else
				c.y = finalY;
		} else if (c.y < finalY) {
			if (c.y < (finalY + 1))
				c.y = c.y + 1;
			else
				c.y = finalY;
		} else
			c.y = finalY;

		return c;
	}

}
