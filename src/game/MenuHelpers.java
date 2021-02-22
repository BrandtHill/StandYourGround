package game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

public class MenuHelpers {

	private MenuHelpers() {}
	private static final Font ARIAL_48 = new Font("Arial", 1, 48);
	private static final Font ARIAL_42 = new Font("Arial", 1, 42);
	private static final Font ARIAL_36 = new Font("Arial", 1, 36);
	private static final Font MONOSPACED_20 = new Font("Monospaced", 1, 20);
	
	public static void renderStartMenu(Graphics g) {
		g.setColor(new Color(150,48,30));
		g.draw3DRect(100, 90, Main.WIDTH-200, Main.HEIGHT-200, true);
		g.setColor(Color.WHITE);
		g.setFont(ARIAL_42);
		g.drawString("STAND YOUR GROUND", 160, 200);
		renderCommand(g, 225, 375, "Space", "Start New Game");
		renderCommand(g, 225, 400, "1", "Load from Save 1");
		renderCommand(g, 225, 425, "2", "Load from Save 2");
		renderCommand(g, 225, 450, "3", "Load from Save 3");
	}
	
	public static void renderPauseMenu(Graphics g) {
		g.setColor(new Color(115,48,168));
		g.draw3DRect(100, 90, Main.WIDTH-200, Main.HEIGHT-200, true);
		g.setColor(Color.WHITE);
		g.setFont(ARIAL_48);
		g.drawString("GAME PAUSED", 210, 200);
		g.setFont(ARIAL_36);
		g.drawString("PRESS 'ESC' TO RESUME", 170, 400);
	}
	
	public static void renderGameOverMenu(Graphics g) {
		g.setColor(new Color(30,60,30));
		g.draw3DRect(100, 90, Main.WIDTH-200, Main.HEIGHT-200, true);
		g.setColor(Color.WHITE);
		g.setFont(ARIAL_42);
		g.drawString("YOU DIED", 300, 200);
		renderCommand(g, 225, 350, "R", "Retry from Autosave");
		renderCommand(g, 225, 375, "N", "Main Menu (New Game)");
		renderCommand(g, 225, 400, "1", "Save current state to Save 1");
		renderCommand(g, 225, 425, "2", "Save current state to Save 2");
		renderCommand(g, 225, 450, "3", "Save current state to Save 3");
	}
	
	public static void renderCommand(Graphics g, int x, int y, String cmd, String desc) {
		g.setFont(MONOSPACED_20);
		g.setColor(Color.LIGHT_GRAY);
		g.drawString(cmd, x, y);
		g.drawString(":", x + 65, y);
		g.drawString(desc, x + 95, y);
	}
}
