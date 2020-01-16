import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class Client {

	public static void main(String[] args) {

		List<Integer> chemin = new ArrayList<Integer> ();
		boolean restartPathing = true;
		int nextX, nextY;
		int comptX = -1, comptY = -1;

		if (args.length != 3) {
			System.out.println("Il faut 3 arguments : l'adresse ip du serveur, le port et le nom d'équipe.");
			System.exit(0);
		}
		try {
			Socket s = new Socket(args[0], Integer.parseInt(args[1]));
			boolean fin = false;

			// ecriture
			OutputStream os = s.getOutputStream();
			PrintWriter pw = new PrintWriter(os);
			//lecture
			InputStream is = s.getInputStream();
			BufferedReader bf = new BufferedReader(
				new InputStreamReader(is));

			pw.println(args[2]);
			pw.flush();

			String numJoueur = bf.readLine();

			System.out.println("Numero de joueur : " + numJoueur);

			while (!fin) {
				String msg = bf.readLine();

				//System.out.println("Message recu : "+msg);
				//System.out.println();
				fin = msg.equals("FIN");

				if (!fin) {
					//Creation du labyrinthe en fonction des informations recues
					Labyrinthe laby = new Labyrinthe(msg);

					// Informations sur le joueur
					//System.out.println("Je me trouve en : ("+laby.getJoueur(Integer.parseInt(numJoueur)).getPosX()+","+laby.getJoueur(Integer.parseInt(numJoueur)).getPosY()+")");
					
					ArrayList<Integer> infosMoule = new ArrayList<Integer> ();
					//Parcours du plateau pour trouver toutes les moules et leur valeur
					for (int j = 0; j<laby.getTailleY(); j++)
						for (int i = 0; i<laby.getTailleX(); i++)
							if (laby.getXY(i, j).getType() == Case.MOULE) {
								infosMoule.add(i);
								infosMoule.add(j);
								infosMoule.add(laby.getXY(i, j).getPointRapporte());
							}

					// Affichage des informations sur les moules du plateau
					//for(int i=0;i<infosMoule.size()/3;i++)
					//System.out.println("Moule en ("+infosMoule.get(i*3)+","+infosMoule.get(i*3+1)+") pour "+infosMoule.get(i*3+2)+" points");

					int currX = laby.getJoueur(Integer.parseInt(numJoueur)).getPosX();
					int currY = laby.getJoueur(Integer.parseInt(numJoueur)).getPosY();

					restartPathing = true;

					// la destination c'est la premiere moule trouvé dans le labyrinthe
					// marche bien dans un labyrinthe parfait ou fermé
					// on parcours beaucoup de cases = on prends plein de bonus
					int destX = infosMoule.get(0);
					int destY = infosMoule.get(1);
					chemin = new ArrayList<Integer> ();

					Client.searchPath(laby, currX, currY, destX, destY, chemin);

					// Affichage du chemin
					// for (Integer element: chemin) {
					// 	System.out.print(element + ",");
					// }
					// System.out.println();

					if (restartPathing) {
						comptY = chemin.size() - 1;
						comptX = comptY - 1;
					}

					comptX -= 2;
					comptY -= 2;

					nextX = chemin.get(comptX);
					nextY = chemin.get(comptY);

					// System.out.println("(comptX,comtY)=(" + comptX + "," + comptY + ")");
					// System.out.println("(currX,currY)=(" + currX + "," + currY + ")");
					// System.out.println("(nextX,nextY)=(" + nextX + "," + nextY + ")");

					if (currX > nextX) {
						msg = "O";
					} else if (currX<nextX) {
						msg = "E";
					} else if (currY > nextY) {
						msg = "N";
					} else {
						msg = "S";
					}

					restartPathing = false;

					// System.out.println(msg);

					//Envoi du message au serveur.
					pw.println(msg);
					pw.flush();
				}

			}
			s.close();

		} catch (Exception e) {
			System.err.println(e);
			e.printStackTrace();
		}
	}
	
	public static boolean searchPath(Labyrinthe laby, int x, int y, int fx, int fy, List<Integer> chemin){

		//System.out.println("TEST CHEMIN:"+x+" "+y+" -> "+fx+" "+fy);

        if(x==fx && y==fy){ // cas d'arrêt
            chemin.add(x);
            chemin.add(y);
            return true;
        }

		if(laby.getXY(x, y).visited == 0){ // cas récursif
			laby.getXY(x,y).setVisited(1);

			int dx=-1;
			int dy=0;
			if(searchPath(laby, x+dx, y+dy, fx, fy, chemin)) {
				chemin.add(x);
				chemin.add(y);
				return true;
			}

			dx=1;
			dy=0;
			if(searchPath(laby, x+dx, y+dy, fx, fy, chemin)){
				chemin.add(x);
				chemin.add(y);
				return true;
			}

			dx = 0;
            dy = -1;
            if (searchPath(laby, x + dx, y + dy, fx, fy, chemin)) {
                chemin.add(x);
                chemin.add(y);
                return true;
            }

            dx = 0;
            dy = 1;
            if (searchPath(laby, x + dx, y + dy, fx, fy, chemin)) {
                chemin.add(x);
                chemin.add(y);
                return true;
            }
		}
		return false;
	}
}