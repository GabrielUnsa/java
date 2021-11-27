/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyecto;

public class clsListaEnlazada {
	/*En este caso nos conviene tener un fin
	 * pues siempre es el que nos dara la info si el
	 * proceso terminara antes de la interrupcion*/
	protected clsNodo inicio;
	private clsNodo fin;

	public clsListaEnlazada(){
		limpialista();
	}

	public void limpialista(){
		this.inicio=this.fin=null;
	}

	public boolean vacia(){
		return (this.inicio==null);
	}
	public clsNodo getInicio() {
		return inicio;
	}

	public String getUltimo(){
		return this.fin.getNodoInfo();
	}
	public int length(){
		int tam=0;
		clsNodo tmp=this.inicio;
		while(tmp!=null){
			tam++;
			tmp=tmp.getSigNodo();
		}
		return tam;
	}
	public void add(Object[] t){
		/*Tenemos que hacer una funciob que convierta de
                Object[] a String*/
		String Info;
		Info=convCadena(t);
		if (vacia()){
			this.inicio=this.fin=new clsNodo(Info);
		}else{
			this.fin.setNextNodo(new clsNodo (Info));
			this.fin=fin.getSigNodo();

		}
	}

	public Object[] ultimo(){
		/*Tenemos que hacer una funciob que convierta de String
                a Object[]*/
		return convObject(this.fin.getNodoInfo(),tam(this.fin.getNodoInfo()));
	}

	public void muestra(){
		clsNodo tmp=this.inicio;
		while(tmp!=null){
			System.out.println(tmp.getNodoInfo());
			tmp=tmp.getSigNodo();
		}
	}

	public String convCadena(Object[] t){
		String c="";
		for(int i=0; i<t.length-1;i++){
			c+=t[i].toString()+"|";
		}
		c+=t[t.length-1].toString();
		return c;
	}

	public Object[] convObject(String c,int n){
		Object[] t=new Object[n];
		int i=0,ini=0,fin;
		fin=c.indexOf('|');
		while(fin!=-1){
			fin=c.indexOf('|');
			if(fin!=-1)t[i]=c.substring(ini,fin);else t[i]=c.substring(ini,c.length());
			c=c.substring(fin+1,c.length());
			i++;

		}
		return t;
	}

	public int tam(String c){
		int i=0,fin=0;
		while(fin!=-1){
			fin=c.indexOf('|');
			c=c.substring(fin+1,c.length());
			i++;
		}
		return i;
	}

	/*Elimina un nodo*/
	public void elimina(clsNodo nodo){
		clsNodo aux=nodo;
		aux.setNextNodo(aux.getSigNodo().getSigNodo());
	}

	/*Este modulo reemplazara los -1 por los valores
	 * tambien se eliminaran los repetidos*/
	public void corrigelista(Object[] Info){
		clsNodo aux=this.inicio;
		int tam=tam(inicio.getNodoInfo());
		Object[] InfoList=new Object[tam];
		while(aux!=null){
			InfoList=convObject(aux.getNodoInfo(),tam);
			for(int i=1;i<tam;i++){
				if((int)Float.parseFloat(InfoList[i].toString())==-1){
					InfoList[i]=Info[i-1];
				}
			}
			aux.setNodoInfo(convCadena(InfoList));
			aux=aux.getSigNodo();
		}
		aux=this.inicio;
		while(aux!=null && aux.getSigNodo()!=null){
			if(aux.getNodoInfo().compareTo(aux.getSigNodo().getNodoInfo())==0) elimina(aux);
			else aux=aux.getSigNodo();
		}
	}
}
