package sources;
import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;

public class Commande extends JPanel{
	
	public enum Sens{
		croissant, 
		decroissant
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int puissance = 100;
	private Sens sens = Sens.croissant;
	
	public void paintComponent(Graphics g){
		g.setColor(Color.GRAY);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		g.setColor(Color.BLACK);
		g.fillRect(100, 5, 200, 50);
		g.setColor(Color.GREEN);
		g.fillRect(100, 5, puissance, 50);
	}
	
	public int getPuissance(){
		return puissance;
	}
	
	public void incrementPuissance(){
		puissance++;
	}
	
	public void decrementPuissance(){
		puissance--;
	}
	
	public Sens getSens(){
		return sens;
	}
	
	public void setSens(Sens nouveau){
		sens = nouveau;
	}

}
