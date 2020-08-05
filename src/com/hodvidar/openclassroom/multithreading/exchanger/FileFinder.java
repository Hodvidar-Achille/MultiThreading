package com.hodvidar.openclassroom.multithreading.exchanger;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Exchanger;

public class FileFinder implements Runnable {
	private List<String> listDocument = new ArrayList<>();
	private List<String> listDocumentInitial = new ArrayList<>();
	Exchanger exchanger;

	public FileFinder(Exchanger ex){
		exchanger = ex;
		listDocumentInitial.add("fichier 1");
		listDocumentInitial.add("fichier 2");
		listDocumentInitial.add("fichier 3");
		listDocumentInitial.add("fichier 4");
		listDocumentInitial.add("fichier 5");
	}

	public void run() {
		int numEchange = 1;
		int count = 0;
		while(count < 5){
			count += 1;
			System.out.println("---------------------------------------");
			System.out.println("Contenu de la liste côté trouveur : ");
			System.out.println(listDocument);
			System.out.println("---------------------------------------");
			Iterator<String> it = listDocumentInitial.iterator();

			while(it.hasNext()){
				//On traite avec notre objet
				String nom = numEchange + "-" + it.next();
				listDocument.add(nom);
				System.out.println("[+] Ajout de " + nom + " dans la collection");
				try {
					Thread.sleep(2500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			//Lorsque la liste est vide, on demande à récupérer une liste pleine
			try {
				System.err.println("\t -> Liste remplie du côte du trouveur de fichier !");
				listDocument = (List<String>)exchanger.exchange(listDocument);
				numEchange++;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
