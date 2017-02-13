
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Lematyzacja {
	public static LinkedList<LinkedList<String>> slownik;
	private static final int MYTHREADS = 10;
	
	public static void main(String args[]) {
		slownik = slownik("data\\odm.txt");
		ExecutorService executor = Executors.newFixedThreadPool(MYTHREADS);
		
		for(int i=1;i<=MYTHREADS;i++) {
			
			executor.execute((new Zmieniacz(slownik,"data\\text"+i+".txt","data\\lematyzacja"+i+".txt")));
			System.out.println("data\\text"+i+".txt     data\\lematyzacja"+i+".txt");
		}
				
	}
	
	public static LinkedList<LinkedList<String>> slownik(String title) {
		String strLine;
		LinkedList<LinkedList<String>> res = new LinkedList<LinkedList<String>>();
		try{
			FileInputStream fstream = new FileInputStream(title);
			BufferedReader br = new BufferedReader(new InputStreamReader(fstream, "UTF8"));			
			while ((strLine = br.readLine()) != null)   {
				 res.add(new LinkedList<String>(Arrays.asList(strLine.split(", "))));
				}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return res;
	}
	

}


class Zmieniacz implements Runnable {
	public static LinkedList<LinkedList<String>> slownik;
	public String sciezka;
	public String wynik_dst="";
	
	public Zmieniacz(LinkedList<LinkedList<String>> slownik,String path,String wynik) {
		this.slownik = slownik;
		this.sciezka = path;
		this.wynik_dst = wynik;
	}
    
    public void run() {
        przetworz(sciezka,wynik_dst);
    }
	
    public static void przetworz(String title,String wynik_dst) {
		PrintWriter writer = null;
		Scanner sc2 = null;
		String slowo="";
	    int licz=0;
		try {
	    	writer = new PrintWriter(wynik_dst, "UTF-8");
	    	sc2 = new Scanner(new File(title), "UTF-8");
	    } 
	    catch (Exception e) {
	        e.printStackTrace();  
	    }
	    while (sc2.hasNextLine()) {
	            Scanner s2 = new Scanner(sc2.nextLine());
	        while (s2.hasNext()) {
	            slowo = s2.next();
	            slowo = zamienSlowo(slowo);
	            writer.print(slowo+" ");
	        }
	        writer.println();
	        licz++;
	        if(licz % 10 == 0)
	        	break;
	    }
	    writer.close();
	}
	
	public static String zamienSlowo(String slowo) {
	    boolean kropka = false;
	    boolean przecinek = false;
	    boolean wykrzyknik = false;
	    boolean pytajnik = false;
	    boolean œrednik = false;
	    boolean pauza = false;
	    
		int licz = 0;
		if(slowo.contains(".")) {
			slowo = slowo.replace(".", "");
			kropka = true;
		}
		if(slowo.contains(",")) {
			slowo = slowo.replace(",", "");
			przecinek = true;
		}
		if(slowo.contains("!")) {
			slowo = slowo.replace("!", "");
			wykrzyknik = true;
		}
		if(slowo.contains("?")) {
			slowo = slowo.replace("?", "");
			pytajnik = true;
		}
		if(slowo.contains(";")) {
			slowo = slowo.replace(";", "");
			œrednik = true;
		}
		if(slowo.contains("-")) {
			slowo = slowo.replace("-", "");
			pauza = true;
		}
			
		for(LinkedList<String> s : slownik) {
			for(String sl : s) {			
				if(sl.equals(slowo)){
					if(licz == 0)
						return przywrocZnaki(sl, kropka, przecinek, wykrzyknik, pytajnik, œrednik, pauza);
					else
						return przywrocZnaki(s.getFirst(), kropka, przecinek, wykrzyknik, pytajnik, œrednik, pauza);
				}
				licz++;
			}
			licz =0;
		}		
		return przywrocZnaki(slowo, kropka, przecinek, wykrzyknik, pytajnik, œrednik, pauza);
				
	}
	
	public static String przywrocZnaki(String slowo2, boolean kropka,boolean przecinek,boolean wykrzyknik,boolean pytajnik,boolean œrednik,boolean pauza) {
		String slowo = new String(slowo2);
		if(kropka)
			slowo += ".";
		if(przecinek)
			slowo += ",";
		if(wykrzyknik)
			slowo += "!";
		if(pytajnik)
			slowo += "?";
		if(œrednik)
			slowo += ";";
		if(pauza)
			slowo += "-";
		return slowo;
	}
    
}
