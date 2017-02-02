package sources;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

import sources.Commande.Sens;
import sources.Main.Phase;


public class Terrain extends JPanel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int angle = 0;
	private Sens sens = Sens.croissant;
	
	public Terrain(){
		
	}
	
	public void paintComponent(Graphics g){
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());   //On dessine le fond
		g.setColor(Color.CYAN);
		g.fillRect(this.getWidth()/2-100, 0, 200, this.getHeight());
		
		g.setColor(Color.BLUE);    //On dessine la cible
		g.fillOval(this.getWidth()/2-100, 0, 200, 200);
		g.setColor(Color.WHITE);
		g.fillOval(this.getWidth()/2-75, 25, 150, 150);
		g.setColor(Color.RED);
		g.fillOval(this.getWidth()/2-50, 50, 100, 100);
		g.setColor(Color.WHITE);
		g.fillOval(this.getWidth()/2-25, 75, 50, 50);
		
		for (Palet pal : Main.getListePalets()){
			if (pal.getEquipe()=="joueur1"){  //On dessine les palets
				g.setColor(Color.BLACK);
			}
			else{
				g.setColor(Color.YELLOW);
			}
			g.fillOval((int)pal.getPosX(), (int)pal.getPosY(), 50, 50);
		}
		
		if (Main.getPhase() == Phase.angle){   //On dessine la barre de direction
			g.setColor(Color.RED);
			g.drawLine(this.getWidth()/2, 600, this.getWidth()/2 + (int)(Math.cos(angle*3.14/180)*100), 600 - (int)(Math.sin(angle*3.14/180)*100));
		}
	}
	
	public Sens getSens(){
		return sens;
	}
	
	public void setSens(Sens nouveau){
		sens = nouveau;
	}
	
	public int getAngle(){
		return angle;
	}
	
	public void incrementAngle(){
		angle++;
	}
	
	public void decrementAngle(){
		angle--;
	}

}
