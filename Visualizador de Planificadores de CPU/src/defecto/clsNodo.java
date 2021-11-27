/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyecto;


public class clsNodo {
	private clsNodo Sig;
	private String NodoInfo;
	
	clsNodo(String Info){
		this(Info,null);
	}
	
	clsNodo(String Info, clsNodo sig){
		this.NodoInfo=Info;
		this.Sig=sig;
	}
	
	public void setNodoInfo(String info){
		this.NodoInfo=info;
	}
	
	public void setNextNodo(clsNodo sig){
		this.Sig=sig;
	}

	public String getNodoInfo(){
		return this.NodoInfo;
	}

	public clsNodo getSigNodo(){
		return this.Sig;
	}
}