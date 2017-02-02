package sources;

public class Palet {
	
	private double posX;
	private double posY;
	private String equipe;
	private double distance; // distance par rapport au centre
	
	public Palet(String s){
		this.posX = 684/2-25;
		this.posY = 500;
		this.equipe = s;
	}
	
	public Palet(int x, int y, String s){
		this.posX = x;
		this.posY = y;
		this.equipe = s;
	}
	
	public double getPosX(){
		return posX;
	}
	
	public double getPosY(){
		return posY;
	}
	
	public void addPos(double x, double y){
		posX+=x;
		posY+=y;
	}
	
	public String getEquipe(){
		return equipe;
	}
	
	public double getDistance(){
		return distance;
	}
	
	public void setDistance(double distance_){
		distance = distance_;
	}

}