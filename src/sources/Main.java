package sources;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import sources.Commande.Sens;

public class Main extends JFrame implements ActionListener{
	
	public enum Phase {
		  puissance,
		  angle,
		  lancer,
		  fin;  
	}
	
	private static final long serialVersionUID = 1L;
	private Boolean next = false; // true indique qu'une action a �t� effectu�e (appui sur le bouton, le palet est arret�, etc...)
	private JButton go = new JButton("GO");  //bouton 
	private Terrain ter = new Terrain();   //fond
	private Commande cmd = new Commande();   //panneau en gris
	private static int actuel = 0;   //numero du palet actuellement en mouvement
	private static LinkedList<Palet> listePalets = new LinkedList<Palet>();  //liste des palets d�j� jou�s + palet en attente
	private int parcours; //lors du lancer, chemin qui reste � parcourir au palet
	private int angle;    //angle du palet actuellement lanc�
	private int c = 0; //compte le nombre de palet d�j� lanc�s
	private static Phase phase = Phase.puissance;
	private int score1 = 0;
	private int score2 = 0;
	private JLabel equipe1 = new JLabel("Noirs : 0 point");
	private JLabel equipe2 = new JLabel("Jaunes : 0 point");
	private int largeur = 684;  //largeur du panneau de fond
	
	public Main(){
		this.setTitle("Curling");
		this.setSize(700, 700);
		this.setLocationRelativeTo(null);               
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		ter.setLayout(new BorderLayout());
		ter.add(cmd, BorderLayout.SOUTH);
		
		go.setPreferredSize(new Dimension(100,50));
		go.addActionListener(this);
		cmd.add(go);
		
		ter.add(equipe1, BorderLayout.WEST);
		equipe1.setVerticalAlignment(JLabel.NORTH);
		equipe1.setFont(new Font("Tahoma", Font.BOLD, 25));
		ter.add(equipe2, BorderLayout.EAST);
		equipe2.setVerticalAlignment(JLabel.NORTH);
		equipe2.setFont(new Font("Tahoma", Font.BOLD, 25));
		
		this.setContentPane(ter);
		this.setVisible(true);
		
		while (true){
			jouer();
		}
	}
	
	public static LinkedList<Palet> getListePalets(){
		return listePalets;
	}
	
	public static Phase getPhase(){
		return phase;
	}
	
	public void jouer(){    //Programme traitant la partie en g�n�ral
		listePalets.add(new Palet("joueur1"));
		c=0;
		actuel = 0;
		while(c!=6){  //On continue tant que 6 palets n'ont pas �t� lanc�s
			while (next==false){   //On attend que le joueur appuie sur go
				MAJbarre();
			}
			next = false;
			while (next==false){   //On attend que le joueur appuie sur go
				MAJangle();
			}
			next = false;
			while (next==false){   //On attend que le palet s'arr�te
				lancerPalet();
			}
			c++;
			next=false;
			actualiserScore();
			if (c!=6){
				if (listePalets.get(listePalets.size()-1).getEquipe() == "joueur1"){
					listePalets.add(new Palet("joueur2"));
				}
				else{
					listePalets.add(new Palet("joueur1"));
				}
				ter.repaint();
				phase = Phase.puissance;
				actuel = listePalets.size()-1;
			}
			
		}
		phase = Phase.fin;
		next = false;
		go.setText("Continuer");
		while(!next){   //On attend que le joueur clique sur continuer
			try{
				  Thread.sleep(5);
			}catch(InterruptedException e) {
				  e.printStackTrace();
			}
		} 
		go.setText("GO");
		listePalets = new LinkedList<Palet>();
		ter.repaint();
		next = false;
		phase = Phase.puissance;
	}
	
	public void MAJbarre(){   //Gestion de la barre de puissance
		if (cmd.getPuissance() == 0){
			cmd.setSens(Sens.croissant);
			cmd.incrementPuissance();
		}
		else if (cmd.getPuissance() == 200){
			cmd.setSens(Sens.decroissant);
			cmd.decrementPuissance();
		}
		else if (cmd.getSens() == Sens.croissant){
			cmd.incrementPuissance();
		}
		else if (cmd.getSens() == Sens.decroissant){
			cmd.decrementPuissance();
		}
		cmd.repaint();
		try{
			  Thread.sleep(5);
		}catch(InterruptedException e) {
			  e.printStackTrace();
		}
	}
	
	public void MAJangle(){   //Gestion de l'aiguille angulaire
		if (ter.getAngle() == 0){
			ter.setSens(Sens.croissant);
			ter.incrementAngle();
		}
		else if (ter.getAngle() == 180){
			ter.setSens(Sens.decroissant);
			ter.decrementAngle();
		}
		else if (ter.getSens() == Sens.croissant){
			ter.incrementAngle();
		}
		else if (ter.getSens() == Sens.decroissant){
			ter.decrementAngle();
		}
		ter.repaint();
		try{
			  Thread.sleep(5);
		}catch(InterruptedException e) {
			  e.printStackTrace();
		}
	}
	
	public void lancerPalet(){  // Gestion du mouvement du palet
		while(parcours!=0){
			testChoc();
			double x = Math.cos(angle*3.14/180);
			double y = Math.sin(angle*3.14/180);
			if (x<0)
				x=x*x;
			else
				x=-x*x;
			y=y*y;
			parcours-=1;
			listePalets.get(actuel).addPos(-x, -y);
			ter.repaint();
			try{
				  Thread.sleep(5);
			}catch(InterruptedException e) {
				  e.printStackTrace();
			}
		}
		next=true;
	}
	
	public void testChoc(){   // On regarde si un choc a eu lieu et si c'est le cas on change le numero du palet en mouvement
		boolean choc = false;
		for (int i = 0; i<listePalets.size(); i++){
			if (distance(listePalets.get(actuel), listePalets.get(i))<50   //Il y a contact
					&& distance(listePalets.get(actuel), listePalets.get(i))>1   //Il s'agit de 2 palets distincts
					&& !choc   //Un seul choc par test (toutes les 5ms)
					&& listePalets.get(i).getPosY()<=listePalets.get(actuel).getPosY()){   //empeche un bug ou le palet en d�placement change ind�finiment
				choc = true;
				parcours = parcours/2;  // simulation de la perte d'�nergie due au choc
				Palet next = listePalets.get(i);
				double x = next.getPosX() - listePalets.get(actuel).getPosX();
				double y = next.getPosY() - listePalets.get(actuel).getPosY();
				if (x<0){   //Calcul du nouvel angle en fonction de la position des 2 palets
					angle = 180 - (int)(Math.atan(y/x)*180/3.14);
				}
				else if(x == 0){
					angle = 90;
				}
				else{
					angle = (int)(Math.atan(y/x)*180/3.14);
				}
				actuel = i;
			}
		}
	}
	
	public void actualiserScore(){
		Palet[] tab = new Palet[listePalets.size()];
		for (int i = 0; i<listePalets.size(); i++){   //Calcul des distances au centre
			int x = (int)listePalets.get(i).getPosX() + 10; //posX/Y correspondent au coin en haut � gauche du palet, ici on prend plutot le centre 
			int y = (int)listePalets.get(i).getPosY() + 10;
			listePalets.get(i).setDistance(Math.sqrt((Math.pow(x-largeur/2, 2)+Math.pow(y-100, 2))));
			tab[i] = listePalets.get(i);
		}
		Boolean fini = false;
		while (fini == false){   //Tri des palets par distance au centre
			fini = true;
			for (int i = 0; i<tab.length-1; i++){
				if (tab[i].getDistance()>tab[i+1].getDistance()){
					fini = false;
					Palet temp = tab[i];
					tab[i] = tab[i+1];
					tab[i+1] = temp;
				}
			}
		}
		if (tab[0].getDistance()<=100){   //Calcul et affichage des scores
			int score = 0;
			String gagnant = tab[0].getEquipe();
			String actuel = gagnant;
			int i=0;
			while ((actuel == gagnant) && (i != tab.length)){
				actuel = tab[i].getEquipe();
				if (actuel == gagnant){
					score++;
				}
				i++;
			}
			if (gagnant == "joueur1"){
				int scoreTemp = score+score1;
				equipe1.setText("Noirs : " + scoreTemp + " points");
				equipe2.setText("Jaunes : " + score2 + " points");
			}
			else{
				int scoreTemp = score+score2;
				equipe2.setText("Jaunes : " + scoreTemp + " points");
				equipe1.setText("Noirs : " + score1 + " points");
			}
		}
	}
	
	public double distance(Palet p1, Palet p2){
		double x1 = p1.getPosX();
		double x2 = p2.getPosX();
		double y1 = p1.getPosY();
		double y2 = p2.getPosY();
		return Math.sqrt((Math.pow(x2-x1, 2)+Math.pow(y2-y1, 2)));
	}

	public static void main(String[] args) {
		@SuppressWarnings("unused")
		Main main = new Main();
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (phase == Phase.puissance){
			parcours = cmd.getPuissance()*3+100;
			next = true;
			phase = Phase.angle;
		}
		else if (phase == Phase.angle){
			angle = ter.getAngle();
			next = true;
			phase = Phase.lancer;
		}
		else{
			next = true;
		}
	}
}
