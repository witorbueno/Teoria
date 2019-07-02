import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class maquina {
	
	/**
		Função main, onde a MTU será rodada.
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		//String path = "C:/Users/Igor Soares/Desktop/teste.txt";
		maquina maq = new maquina();
		System.out.println(maq.roda(args[0]));
	}
	
	ArrayList <Character> fita1 =  new ArrayList<Character>();
	ArrayList <Character> fita2 =  new ArrayList<Character>();
	ArrayList <Character> fita3 =  new ArrayList<Character>();

	/**
		Função que recebe um caminho de um arquivo texto, 
		lê linha por linha e transforma na estrutura esperada pela máquina de turing universal. 
		Onde,
		Fita 1: entrada
		Fita 2: estado atual
		Fita 3: palavra w
		Também retorna a quantidade de linhas (transições), que depois será usada na heurística.
	 */
	public int  carregar(String path) throws IOException{
		// Criando variavel que representa o arquivo a ser lido (e o buffer de leitura)
		File arq = new File(path);
		BufferedReader br = new BufferedReader(new FileReader(arq));
		
		String entrada="";
		
		String saida = "000";
		// Pula primeiro "000"
		entrada = br.readLine();
		int n = 0;
		// Le todo arquivo ate encontrar "000" enquanto copia informacoes para variavel "saida"
		while (entrada != null && (entrada = br.readLine()) != null){
			if (entrada.contains("000") == false){
				saida += entrada;
			}else{
				
				// Caso encontre "000" escreve na fita 3 o conteudo seguinte
				entrada = br.readLine();
				for (char c : entrada.toCharArray()) {
					  fita3.add(c);
				}
				entrada = null;
			}
			n++;
		}
		
		br.close();
		// Copia "saida" para fita 1
		for (char c : saida.toCharArray()) {
			  fita1.add(c);
		}
		
		// Adiciona ultimos 3 zeros na fita 1 e "1" na fita 2.
		fita1.add('0');
		fita1.add('0');
		fita1.add('0');
		fita2.add('1');
		return n;
	}
	
	/**
		Função que busca na Fita2 o estado atual e na Fita3 o símbolo sendo lido e 
		concatena as duas, como resultado retorna “00+estadoatual+0+simbolo+0”;
	 */
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
		
		// Variavel saida recebe conteudo da fita2
		for(int j=0;j<fita2.size();j++){
			saida += fita2.get(j);
		}
		
		// Concatena com 0
		saida += '0';


		if(i>=fita3.size()){
			return "false";
		}

		// Variavel saida recebe o conteudo da fita3 
		// e concatena com o que ja existe em "saida"
		while(fita3.get(i) != '0'){
			saida += fita3.get(i);
			i++;
		}

		// Concatena com 0
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

	/**
		Função que recebe o resultado da função monta() e 
		retorna a transição adequada, (recebe o estado atual + símbolo lido 
		e devolve o próximo estado, o símbolo escrito e a direção do movimento);
		Caso não encontre a transição, ele retorna -1;
	 */
	public String buscarCarinha(String delta){
		String saida = "";
		String f1 = getStringRepresentation(fita1);
		int posCarinha = f1.indexOf(delta);
		int i = posCarinha + delta.length(), cont = 0;

		//verifica se a posiçao é -1
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
	
	/**
		Função que recebe a transição vindo de buscaCarinha() 
		e escreve o novo estado na fita2;
	 */
	public void escreveFita2(String entrada){
		fita2.clear();
		int i = 0;
		//adiciona estado atual na fita2
		while(entrada.toCharArray()[i] != '0'){
			fita2.add(entrada.toCharArray()[i]);
			i++;
		}
		
	}
	
	public boolean verifica(){
		//flag comeca verdadeira e se torna falsa caso algum criterio falhe
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
					//Caso seja o proximo "0" apos o quinto, devera ser um "00"
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
	

	/**
		Função que recebe de buscaCarinha() a transição e uma posição da cabeça de leitura e escrita, 
		e escreve na posição correta da fita3 o símbolo;
	 */
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

	/**
		Função que Recebe de buscaCarinha() a transição atual e retorna a direção,
		 1 para direita e 11 para esquerda;
	 */
	public String direcao(String entrada){
		int i= 0;
		boolean v = true;
		String saida = "";
		while(v){
			// Vai até o final da fita e volta para ultimos 1's antes dos 0's finais
			// se for "1" indica que a direção é direita, se for "11" indica a direção esquerda.
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

		return saida;
				
	}

	/**
		Função principal que roda a MTU e onde são aplicadas as heurísticas
	 */
	public boolean roda(String path) throws IOException{
		int heur = carregar(path);
		int cabess = 0;
		System.out.println("Fita 2: "+fita2);
		System.out.println("Fita 3: "+fita3);
		System.out.println("");
		int contHeur = 0;
		
		//maquina irá rodar até que algo específico retorne false e a faça parar
		while(true){
			if (contHeur == heur * fita3.size()) return false;
			contHeur++;
			String trabalho = monta(cabess);
			
			// Se a entrada nao estiver no formato esperado pela MTU
			// a funçao monta retorna false e a maquina para aqui.
			if(trabalho == "false") return false;

			//System.out.println("Monta retornou: " + trabalho);
			// Chama-se a buscarCarinha() para fazer a computaçao com a saída da funçao monta()
			trabalho = buscarCarinha(trabalho);
			
			//System.out.println("Busca retornou: " + trabalho);
			//System.out.println(trabalho);
			
			// Se buscarCarinha() nao encontrar a transição retorna -1
			// mas a maquina continua rodando em busca de uma resposta
			if(trabalho=="-1") return true;

			//escrevemos o estado atual na fita2
			escreveFita2(trabalho);

			//escrevemos a palavra na fita3
			escreveFita3(cabess,trabalho);

			//verificamos a direçao da transição se é esquerda ou direita
			String direc = direcao(trabalho);
			//System.out.println("Direcao: " + direc);
			//System.out.println(direc.length());
			
			// se for esquerda a cabeça de leitura/escrita é decrementada
			// se for direita a cabeça de leitura/escrita é incrementada
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
