package proyecto;

import java.util.Scanner;
/**
 * @author gabriel-lidia
 *
 */

public class clsModeloProb {
	
	/*Lista Global que puede ser local*/
	clsListaEnlazada list =new clsListaEnlazada();

	/*Variables Globales*/
	private Object matrizInformacion[][];

	/*Constructores*/
	/*Constructor: Tiene Tabla Informacion*/
	public clsModeloProb(int n){
		this.matrizInformacion=cargaMatriz(n);
	}
	
	/*Construcor:  tabla de informacion*/
	public clsModeloProb(Object[][] matrizInformacion,int n) {
		this.matrizInformacion = matrizInformacion;
	}

	/*Devuelve lista*/
	public clsListaEnlazada getList() {
		return list;
	}
	
	/*Carga Lista*/
	public void setList(clsListaEnlazada list) {
		this.list = list;
	}
	
	/*Devuelve tabla de informacion*/
	public Object[][] getMatrizInformacion() {
		return matrizInformacion;
	}
	
	/*Carga Matriz informacion*/
	public void setMatrizInformacion(Object[][] matrizInformacion) {
		this.matrizInformacion = matrizInformacion;
	}

	/*Genera la Tabla Informacion donde estara:
	 * Nombre del Proceso
	 * Hora de llegada
	 * Tiempo de Duracion
	 * Booleano que indicara si fue activado o no*/
	protected Object[][] cargaMatriz(int n){
		Object matriz[][]= new Object[n][5];
		for(int i=0; i < matriz.length;i++){
			for(int j=0; j < 4;j++){
				switch(j){
				case 0: {System.out.println("Ingrese el nombre del proceso");break;}
				case 1: {System.out.println("Ingrese hora de llegada (HH:MM:SS) de "+matriz[i][0]);break;} /*obligare al usuario ingresar numeros enteros como cadenas por medio de la interfaz*/
				case 2: {System.out.println("Ingrese duracion de "+matriz[i][0]);break;}
				case 3: {System.out.println("Ingrese promedio I/O de "+matriz[i][0]);break;}
				}
				matriz[i][j]=new Scanner(System.in).next();
			}
			matriz[i][4]=true;
		}
		return matriz;
	}

	/*Geters de Elementos*/

	/*Devuelve el nombre del proceso x*/
	public String getprocesoname(int x){
		return matrizInformacion[x][0].toString();
	}

	/*Procesos que realizaran el programa*/
	/*Devuelve la hora de inicio*/
	public String gethorainicial(){
		String aux=matrizInformacion[0][1].toString();;
		for(int i=1; i < matrizInformacion.length;i++){
			if(matrizInformacion[i][1].toString().compareTo(aux) < 0) aux=matrizInformacion[i][1].toString();
		}
		return aux;
	}

	/*Devuelve apaga la bandera de la hora inicial*/
	public void apagabool(String hora){
		for(int i=0; i< matrizInformacion.length;i++){
			if(hora.compareTo(matrizInformacion[i][1].toString())==0) matrizInformacion[i][4]=false;
		}
	}

	/*Cantidad de Procesos Activos hasta la hora incial*/
	public int getcatidadactivos(String hora){
		int cont=0;
		for(int i=0; i< matrizInformacion.length;i++){
			if((Boolean)matrizInformacion[i][4]==false) cont++;
		}
		return cont;
	}
	/*Procesos que usara el resto del programa*/

	/*Devuelve cuantos min falta hasta la siguiente llamada del proceso
	 * y ademas la hora del siguiente proceso*/
	public String[] horafaltantesigproceso(String hora){
		int aux2=0,aux=240000;/*No tengo mas procesos que se iniciaran*/
		String[] h1=new String[2];
		for(int i=0;i<matrizInformacion.length;i++){
			if((boolean)matrizInformacion[i][4]){
				aux2=restahoras(matrizInformacion[i][1].toString(),hora);
				if(aux2>0 && aux2<aux){aux=aux2;h1[1]=matrizInformacion[i][1].toString();
				}
			}
		}
		h1[0]=corrigehora(String.valueOf(aux));
		if(aux2<=0){
			h1[1]=hora;
		}
		return h1;
	}

	/*Resta de Dos horas*/
	public int restahoras(String h1, String h2){
		String aux;
		/*Supongo que guarde 00:00:00*/
		/*Minutos*/
		if(h1.substring(2,4).compareTo(h2.substring(2,4))<0){
			aux="40";
		}
		else {
			aux="00";
		}
		/*Segundos*/
		if(h1.substring(4,6).compareTo(h2.substring(4,6))<0){
			aux+="40";
		}
		else {
			aux+="00";
		}
		return (Integer.parseInt(h1)-Integer.parseInt(h2)-Integer.parseInt(aux));
	}

	/*Corrige Hora*/
	public String corrigehora(String h1){
		for(int i=h1.length(); i<6;i++){
			h1="0"+h1;
		}
		return h1;
	}

	/*Calcula Tiempo Proceso
	 * Necesita la hora faltante hasta el sigueinte proceoso*/
	public float calculoTP(String hora,float cpuproceso){
		/*Tiempo de Proceso en minutos*/
		return cpuproceso*(Integer.parseInt(hora.substring(0,2))*60 /*Hora*/
				+ Integer.parseInt(hora.substring(2,4)) /*minuto*/
				+ Integer.parseInt(hora.substring(4,6))/60 /*Segundos*/
				);
	}

	/*Devuelve Primera fila de la tabla final el resto todo con -1*/
	public Object[] primerafila(String Hora){
		Object[] t= new Object[matrizInformacion.length+1];
		t[0]=Hora;
		for(int i=1;i<t.length;i++){
			if(Hora.compareTo(matrizInformacion[i-1][1].toString())==0) t[i]=matrizInformacion[i-1][2];
			else t[i]=-1;
		}
		return t;
	}

	/*Ponelos nuevos elementos para ponerlo en lista*/
	public Object[] cargaevento(String Hora,Object[] t){
		t[0]=Hora;
		for(int i=1;i<t.length;i++){
			if(Hora.compareTo(matrizInformacion[i-1][1].toString())==0 &&
					(int)Float.parseFloat(t[i].toString())==-1){
				t[i]=matrizInformacion[i-1][2];
				matrizInformacion[i-1][4]=false;
			}
		}
		return t;
	}

	/*Es una funcion que me devolvera cuantos proceso terminaron*/
	public int termina(float tp,Object[] ultimatupla){
		int i,cont=0;
		for(i=0;i<ultimatupla.length;i++){
			if((int)Float.parseFloat(ultimatupla[i].toString())!=-1){
				if (Float.parseFloat(ultimatupla[i].toString())-tp == 0.0
						|| Float.parseFloat(ultimatupla[i].toString())-tp< 0.0) cont++;
			}
		}
		return cont;
	}

	/*Veo Cuanto Procesos siguen*/
	public int numprocesos(Object[] ultimatupla){
		int cont=0;
		for(int i=1;i<ultimatupla.length;i++){
			if((int)Float.parseFloat(ultimatupla[i].toString())!=-1 && Float.parseFloat(ultimatupla[i].toString())!=0.0)
				cont++;			
		}
		return cont;
	}

	/*Modifica los valores con el tp dado*/
	public Object[] modificavalores(float tp,Object[] ultimatupla,String h2){
		for(int i=1;i<ultimatupla.length;i++){
			if((int)Float.parseFloat(ultimatupla[i].toString())!=-1 && Float.parseFloat(ultimatupla[i].toString())!=0.0) 
				ultimatupla[i]=Float.parseFloat(ultimatupla[i].toString())-tp;				
		}
		ultimatupla[0]=h2;
		return ultimatupla;
	}

	/*Ve si tengo procesos por terminar y tomo decisiones*/
	public void procesos(float tp, Object[] ultimatupla,int cant_proceso,float cpuproceso,String h2){
		if(termina(tp,ultimatupla)!=0){
			tprocesos(tp,cpuproceso,ultimatupla,h2);
		}else{
			list.add(modificavalores(tp,ultimatupla,h2));
		}
		list.add(cargaevento(h2,list.ultimo()));
	}

	/*Terima todos los proceso antes de la hora del siguiente proceso*/
	public void tprocesos(float tp,float cpuproceso, Object[] ultimatupla, String h2){
		float TPN=0;
		int cant_proceso=0;
		String Hora=ultimatupla[0].toString();
		String TR;
		while (termina(tp,ultimatupla)!=0 && Hora!="0"){ /*Controlar mejor hora*/
			TPN=busca(ultimatupla);	/*Busco el menor de los TP antes del otro proceso*/
			TR=TR(cpuproceso,TPN);
			if(TPN>=tp){
				ultimatupla=modificavalores(tp,ultimatupla,h2);
				list.add(ultimatupla);
				prendebool(ultimatupla);
				Hora="0";
			}else{
				Hora=SumaTiempo(Hora,TR);
				Hora=corrigehora(Hora);
				ultimatupla=modificavalores(TPN,ultimatupla,Hora);
				list.add(ultimatupla);
				prendebool(ultimatupla);
				cant_proceso=numprocesos(ultimatupla);
				cpuproceso=getcpuprocesobool(cant_proceso);
				Hora=corrigehora(String.valueOf(restahoras(h2,Hora)));
				tp=calculoTP(Hora,cpuproceso);
			}        
		}
	}


	/*Proceso Final*/
	/*Busca menor del restante*/
	public float busca(Object[] ultimatupla){
		float aux=240000;
		for(int i=1;i<ultimatupla.length;i++ ){
			if(Float.parseFloat(ultimatupla[i].toString()) < aux && (int) Float.parseFloat(ultimatupla[i].toString()) != -1 && Float.parseFloat(ultimatupla[i].toString()) !=0.0){
				aux=Float.parseFloat(ultimatupla[i].toString());
			}
		}
		return aux;
	}

	/*Suma Tiempo
	 *Donde h1 es hora inicial y h2 son los minutos que tiene q transcurrir*/
	public String SumaTiempo(String h1,String h2){
		int a,b,c,acarreo;
		String d="";
		acarreo=0;
		/*Primero Segundos*/
		a=(int) Float.parseFloat(h1.substring(4,6));
		b=(int) Float.parseFloat(h2.substring(4,6));
		c=a+b;
		if(c>60){
			c-=60;
			acarreo++;
		}
		d=String.valueOf(c);
		if(d.length()==1) d="0"+d;
		/*Los minutos*/
		a=(int) Float.parseFloat(h1.substring(2,4));
		b=(int) Float.parseFloat(h2.substring(2,4));
		c=a+b+acarreo;
		acarreo=0;
		if(c>60){
			c-=60;
			acarreo++;
		}
		d=String.valueOf(c)+d;
		if(d.length()==3) d="0"+d;
		/*Horas*/
		a=(int) Float.parseFloat(h1.substring(0,2));
		b=(int) Float.parseFloat(h2.substring(0,2));
		c=a+b+acarreo;
		d=String.valueOf(c)+d;
		if(d.length()==5) d="0"+d;
		return d;
	}

	/*Calculo teniendo el TP cuanto min en tiempo real necesito falta mejorar a horas exactas*/
	public String TR(float cpuproceso,float TP){
		int hora=0;
		float min=0;
		String falta="",aux="";
		min=TP/cpuproceso;
		if(min<60){
			falta+=String.valueOf((int)min);
		}else{
			hora=(int)(min/60);
			falta+=String.valueOf(hora);
			min=min-hora*60;
			falta+=String.valueOf((int)min);
		}
		/*Cargo Segundos*/
		aux=String.valueOf(min);
		hora=aux.indexOf('.');
		if(aux.substring(hora+1,aux.length()).length()>=2){
			falta+=aux.substring(hora+1,hora+3);
		}else{
			falta+=aux.substring(hora+1,aux.length())+"0";
		}
		return corrigehora(falta);
	}

	/*Termina todo es termina todos los procesos*/
	private void todotermina(Object[] ultimatupla,int cant_proceso,String hora) {
		int i=1;
		float TP,cpuproceso; /*El menor faltante*/
		String TR="";
		while(i<ultimatupla.length && cant_proceso != 0){
			if(Float.parseFloat(ultimatupla[i].toString())==0.0){
				i++;
			}else{
				cpuproceso=getcpuprocesobool(cant_proceso); 
				TP=busca(ultimatupla);
				TR=TR(cpuproceso,TP);
				hora=SumaTiempo(TR,hora);
				ultimatupla=modificavalores(TP,ultimatupla,hora);
				list.add(ultimatupla);
				prendebool(ultimatupla);
				cant_proceso=numprocesos(ultimatupla);
			}
		}
	}

	/*RESOLUCION*/
	public void Resolucion(){
		String h_ini;
		String[] hfs=new String[2];
		float cpuproceso;
		float TP = 0;
		int cant_proceso;
		hfs[0]="0";
		h_ini=gethorainicial(); /*Agarro la hora de inicio de todos los procesos*/
		list.add(primerafila(h_ini)); /*Cargo la primera linea de la tabla*/
		apagabool(h_ini); /*Apago los que se prendieron*/
		cant_proceso=getcatidadactivos(h_ini); /*Veo cuantos procesos se prendieron*/
		/*Empieza el proceso*/
		while(Integer.parseInt(hfs[0])!=240000){
			hfs=horafaltantesigproceso(h_ini);	/*Veo cuanto falta para eso y la siguiente hora de llegada*/
			cpuproceso=getcpuprocesobool(cant_proceso);	/*Traigo el valor de cpuproceso segun la cantidad de proceso*/           
			if(Integer.parseInt(hfs[0].toString())==240000) todotermina(list.ultimo(),cant_proceso,hfs[1]);
			else{
				TP=calculoTP(hfs[0],cpuproceso); /*Calculo la Unidad de Tiempo de Proceso con ese tiempodado*/
				procesos(TP,list.ultimo(),cant_proceso,cpuproceso,hfs[1]);
				h_ini=hfs[1];
				cant_proceso=numprocesos(list.ultimo());/*Cargo la nueva hora del proceso*/
			}
		}
		correccion();
		System.out.println("Lista Final");
		list.muestra();
	}

	/*Corrigue la lista*/
	public void correccion(){
		Object[] t= new Object[matrizInformacion.length];
		for(int i=0;i<matrizInformacion.length;i++){
			t[i]=matrizInformacion[i][2];
		}
		list.corrigelista(t);
	}
	
	/*Prende los bool cuando el proceso termino*/
	public void prendebool(Object[] t){
		for(int i=1; i < t.length;i++){
			if(Float.parseFloat(t[i].toString())==0.0) matrizInformacion[i-1][4]=true;
		}
	}
	
	/*Devuelve el gpuproceso*/
	public float getcpuprocesobool(int n){
		float tp=1;
		for(int i=0;i<matrizInformacion.length;i++){
			if(!(boolean)matrizInformacion[i][4]){
				tp*=Float.parseFloat(matrizInformacion[i][3].toString());
			}
		}
		return ((1-tp)/n);
	}        
	
	/*Main Prueba*/
	public static void main(String[] args){
		clsModeloProb obj1=new clsModeloProb(2);
		obj1.Resolucion();
	}

}
