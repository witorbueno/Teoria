import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;




public class maquina {
	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		//String path = "C:/Users/Igor Soares/Desktop/teste.txt";
		maquina maq = new maquina();
		System.out.println(maq.roda(args[0]));


	}
	
	ArrayList <Character> fita1 =  new ArrayList<Character>();
	ArrayList <Character> fita2 =  new ArrayList<Character>();
	ArrayList <Character> fita3 =  new ArrayList<Character>();

	//Fun��o que le um arquivo no formato desejado e organiza a entrada nas 3 fitas
	public int  carregar(String path) throws IOException{
		//Criando vari�vel que representa o arquivo a ser lido (e o buffer de leitura)
		File arq = new File(path);
		BufferedReader br = new BufferedReader(new FileReader(arq));
		
		String entrada="";
		
		String saida = "000";
		//Pula primeiro "000"
		entrada = br.readLine();
		int n = 0;
		//Le todo arquivo at� encontrar "000" enquanto copia informa��es para variavel "saida"
		while (entrada != null && (entrada = br.readLine()) != null){
			if (entrada.contains("000") == false){
				saida += entrada;
			}else{
				
				//Caso encontre "000" escreve na fita 3 o cote�do seguinte
				entrada = br.readLine();
				for (char c : entrada.toCharArray()) {
					  fita3.add(c);
				}
				entrada = null;
			}
			n++;
		}
		
		br.close();
		//Copia "sa�da" para fita 1
		for (char c : saida.toCharArray()) {
			  fita1.add(c);
		}
		
		//Adiciona ultimos 3 zeros na fita 1 e "1" na fita 2.s
		fita1.add('0');
		fita1.add('0');
		fita1.add('0');
		fita2.add('1');
		return n;
	}
	
	public String monta(int n){
		String saida ="00";
		int i =0;
		int contador=0;
		
		while(contador!=n){
			
			if(fita3.get(i) == '0'){
				contador++;
			}
			i++;
			
		}
		
		
		for(int j=0;j<fita2.size();j++){
			//System.out.println("AAA" + fita2.get(j));
			saida += fita2.get(j);
		}
		
		saida += '0';
		
		
		//if(n!=0) i++;
		//System.out.println("BBB "+i);

		if(i>=fita3.size()){
			return "false";
		}
		while(fita3.get(i) != '0'){
			saida += fita3.get(i);
			i++;
		}
		
		
		
		
		saida += '0';
		
		
		return saida;
	}
	
	public String getStringRepresentation(ArrayList<Character> list){
		StringBuilder builder = new StringBuilder(list.size());
		for(Character ch: list){
			builder.append(ch);
		}
		return builder.toString();
		
	}
	
	public String buscarCarinha(String delta){
		String saida = "";
		String f1 = getStringRepresentation(fita1);
		int posCarinha = f1.indexOf(delta);
		int i = posCarinha + delta.length(), cont = 0;
		if (posCarinha == -1){
			return "-1";
		}
		while(cont != 2){
			if(fita1.get(i) == '0'){
				cont++;
			}
			else{
				cont = 0;
			}
			saida += fita1.get(i);
			i++;
		}
		
		
		return saida;
	}
	
	public void escreveFita2(String entrada){
		fita2.clear();
		int i = 0;
		while(entrada.toCharArray()[i] != '0'){
			fita2.add(entrada.toCharArray()[i]);
			i++;
		}
		
	}
	
	public boolean verifica(){
		
		//flag come�a verdadeira e se torna falsa caso algum crit�rio falhe
		boolean flag = true;
		
		//Verifica 3 primeiros e 3 ultimos zeros da fita 1
		if(fita1.get(0)!='0' || fita1.get(1) != '0' || fita1.get(2)!='0'){
			flag = false;
		}
		if(fita1.get(fita1.size()-1)!='0' && fita1.get(fita1.size()-2) != '0' && fita1.get(fita1.size()-3)!='0'){
			flag = false;
		}
		
		//Contador para controlar quantos argumentos separados por "0" antes de um "00"
		//Esperam-se 5 argumentos (quintupla).
		int count =0;
		for(int i=2;i<fita1.size();i++){
		
			if (fita1.get(i)==0){
				if (count == 5){
					//Caso seja o pr�ximo "0" ap�s o quinto, dever� ser um "00"
					if(fita1.get(i+1)!=0){
						flag = false;
					}
					count = 0;
					i++;
				}else{
					count++;
				}
			}
			
		}
		return flag;
	}
	
	public void escreveFita3(int pos, String entrada){
		boolean flag = false;
		int cont = 0;
		String entrada2 = "";
		while(cont < entrada.length()-2){
			if(flag){
				entrada2 += entrada.charAt(cont);
				if(entrada.charAt(cont+1)=='0'){
					flag = false;
					break;
				}
			}
			
			if(entrada.charAt(cont)=='0'){
				flag = true;
			}
			
	
			cont++;
		}
		entrada = entrada2;
		//System.out.println(entrada);
		int posReal=0;
		cont=0;
		flag = true;
		
		for(int i=0;i<fita3.size() && flag;i++){
			if(fita3.get(i)=='0'){
				cont++;
			}
			if(cont==pos){
				posReal = i+1;
				flag = false;
			}
			
		}
		if(pos == 0) posReal = 0;
		char elem = fita3.get(posReal);
		
		while(elem!='0'){
			fita3.remove(posReal);
			elem = fita3.get(posReal);
		}
		cont =0;
		while(cont < entrada.length()){
			fita3.add(posReal, entrada.charAt(cont));
			cont++;
		}
		
	}
	
	public String direcao(String entrada){
		int i= 0;
		boolean v = true;
		String saida = "";
		while(v){
		
			if(entrada.toCharArray()[i] == '0'){

				if(entrada.toCharArray()[i+1] == '0'){
					
					i--;
					v=false;
					if(entrada.toCharArray()[i-1] == 1){
						i--;
						i--;
						
					}
					else{
						i--;
					}
				}
			}
			i++;
		}
		
		if(entrada.toCharArray()[i-1]=='1'){
			saida = "11";
		}else{
			saida = "1";
		}
		
		/*while(entrada.toCharArray()[i] == '1'){
			saida += "1";
			i--;
		}*/
		

		return saida;
		
		
				
	}
	
	public boolean roda(String path) throws IOException{
		int heur = carregar(path);
		int cabess = 0;
		System.out.println("Fita 2: "+fita2);
		System.out.println("Fita 3: "+fita3);
		System.out.println("");
		int contHeur = 0;
		while(true){
			if (contHeur == heur * fita3.size()) return false;
			contHeur++;
			
			//System.out.println(cabess);
			String trabalho = monta(cabess);
			
			if(trabalho == "false") return false;
			//System.out.println("Monta retornou: " + trabalho);
			trabalho = buscarCarinha(trabalho);
			//System.out.println("Busca retornou: " + trabalho);
			//System.out.println(trabalho);
			if(trabalho=="-1") return true;
			escreveFita2(trabalho);
			escreveFita3(cabess,trabalho);
			String direc = direcao(trabalho);
			//System.out.println("Direcao: " + direc);
			//System.out.println(direc.length());
			
			if(direc == "11"){
				//System.out.println("AAA");
				cabess--;
			}else{
				cabess++;
			}
			
			System.out.println("Execucao numero: " + contHeur);
			System.out.println("Fita 2: "+fita2);
			System.out.println("Fita 3: "+fita3);
			System.out.println("");
		}
	}
}
