package defecto;

import java.util.Scanner;

/**
 * @author gabriel
 *
 */
public class clsModeloProb {
//	private int n;/*Cantidad de Proceso*/
//	private int duracion;/*Tiempo de Proceso*/
//	private int inicio; /*Tiempo Real*/
//	private float p; /*Porcentaje de Promedio*/
	private Object matrizInformacion[][];
	private float matrizProceso[][];
	
        /*Constructor: Tiene Tabla Informacion y de Procesos*/
	public clsModeloProb(int n){
		this.matrizInformacion=cargaMatriz(n);
		this.matrizProceso=creaMatriz(n);
	}
	
        /*Genera la Tabla Informacion*/
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
				Scanner t=new Scanner(System.in);
				matriz[i][j]=t.next();
			}
			for (int j=4;j<5;j++){
				matriz[i][j]=true;
			}
		}
		return matriz;
	}
	
        /*Genera la Tabla Proceso*/
	protected float[][] creaMatriz(int n){
		float[][] matriz=new float[3][n];
		for(int i=0;i<n;i++){ /*Columna*/
			for(int j=0;j<3;j++){ /*Fila*/
				switch(j){
				case 0:{matriz[j][i]=cpuociosa(i+1);break;}
				case 1:{matriz[j][i]=cpuocupado(matriz[0][i]); break;}
				case 2:{matriz[j][i]=cpuproceso(matriz[1][i],i+1);break;}
				}
			}
		}
		return matriz;
	}
	
        /*Calcula cpu ociosa por x cantidad de proceso*/
	private float cpuociosa(int n) {
		float aux=1;
		/*Calculo de P*/
		for(int i=0; i < n;i++){
			aux*=Float.parseFloat(matrizInformacion[i][3].toString());
		} 
		return aux;
	}
	
        /*Calcula cpu ocupado por x cantidad de proceso*/
	private float cpuocupado(float p) {
		/*Calculo de 1-P*/
		return 1-p;
	}

        /*Calcula cpu proceso por x cantidad de proceso*/
	private float cpuproceso(float p,int i) {
		/*Calculo de 1-P/n*/
		return p/i;
	}
        
        /*Muestra las dos Tablas Infomarcion y Proceso*/
	public void Muestra(){
		for(int i=0; i < matrizInformacion.length;i++){
			for(int j=0; j < 5;j++){
				System.out.print(" "+matrizInformacion[i][j]);
			}
			System.out.println();
		}
		System.out.println();
		System.out.println();
		for(int i=0; i < 3;i++){
			for(int j=0; j < matrizProceso.length;j++){
				System.out.print(" "+matrizProceso[i][j]);
			}
			System.out.println();
		}
	}
	
        /*Devulve el cpu por proceso de una x cantidad de procesos*/
        public float getcpuproceso(int x){
            return matrizProceso[2][x];
        }
        
        /*Devuelve el nombre del proceso x*/
        public String getprocesoname(int x){
            return matrizInformacion[x][0].toString();
        }
        
        /*Devuelce la hora de inicio de todos los procesos*/
        public String gethorainicial(){
            String aux=aux=matrizInformacion[0][1].toString();;
            for(int i=1; i < matrizInformacion.length;i++){
                if(matrizInformacion[i][1].toString().compareTo(aux) < 0){
                    aux=matrizInformacion[i][1].toString();
                }
            }
            return aux;
        }
	
        /*Devuelve apaga la bandera hasta esa hora dada*/
        public void apagabool(String hora){
            int cont=0;
            for(int i=0; i< matrizInformacion.length;i++){
                if(hora.compareTo(matrizInformacion[i][1].toString())<0){
                    cont++;
                    matrizInformacion[i][4]=false;
                }
            }
        }
        
        /*Cantidad de Procesos Activos hasta esa hora*/
        public int getcatidadactivos(String hora){
        	 int cont=0;
             for(int i=0; i< matrizInformacion.length;i++){
                 if(hora.compareTo(matrizInformacion[i][1].toString())<0){
                     cont++;
                 }
             }
             return cont;	
        }
        
        /*Devuelve cuantos min falta hasta la siguiente llamada del proceso
         * y ademas la hora del siguiente proceso*/
        public String[] horafaltantesigproceso(String hora,String[] h1){
            int aux2,aux=240000;/*No tengo mas procesos que se iniciaran*/
            int i=0;
           while(i<matrizInformacion.length){
        	   if((boolean)matrizInformacion[i][4]==true){
        		   aux2=restahoras(matrizInformacion[i][1].toString(),hora);
                   if(aux2>0 && aux2<aux){aux=aux2;h1[1]=matrizInformacion[i][1].toString();}
        	   }
               i++;
           }
           	h1[0]=String.valueOf(aux);
            return h1;
        }
        
        /*Resta de Dos horas*/
        public int restahoras(String h1, String h2){
            String aux;
            /*Supongo que guarde 00:00:00*/
            /*Min de h2 es mas grande que min h1*/
            if(h1.substring(2,4).compareTo(h2.substring(2,4))<0){
                aux="40";
            } 
            else { 
                aux="00";
            }
            /*Comparo Segundos de h1 y h2*/
            if(h1.substring(4,6).compareTo(h2.substring(4,6))<0){
                aux+="40";
            } 
            else { 
                aux+="00";
            }
            return (Integer.parseInt(h1)-Integer.parseInt(h2)-Integer.parseInt(aux));
        }
        
        /*Calcula Timepo Proceso
         * Necesita la hora faltante hasta el sigueinte proceoso*/
        public float calculoTP(String hora,float cpuproceso){
            float TP; /*Tiempo de Proceso en minutos*/
            TP=cpuproceso*(Integer.parseInt(hora.substring(0,2))*60 /*Hora*/
                          + Integer.parseInt(hora.substring(2,4)) /*minuto*/
                          + Integer.parseInt(hora.substring(4,6))/60 /*Segundos*/
                          );
            return TP;
        }

        /*Devuelve Primera fila de la tabla final el resto todo con -1*/
        public Object[] primerafila(String Hora){
        	Object[] t= new Object[matrizInformacion.length+1];
        	t[0]=Hora;
        	for(int i=1;i<t.length;i++){
        		if(Hora.compareTo(matrizInformacion[i][1].toString())==0) t[i]=matrizInformacion[i][2];
        		else t[i]=-1;
        	}
        	return t;
        }
        
        /*Carga nuevos eventos en tabla*/
        public Object[] cargaevento(String Hora){
        	Object[] t= new Object[matrizInformacion.length+1];
        	t[0]=Hora;
        	for(int i=1;i<t.length;i++){
        		if(Hora.compareTo(matrizInformacion[i][1].toString())==0 && 
        		   Integer.parseInt(t[i].toString())==-1) t[i]=matrizInformacion[i][2];
        	}
        	return t;
        }
        
        /*Pseudo Resolucion*/
        public void pseudoResolucion(){
        	String h_ini;
        	String[] hfs=new String[2];
        	Object[] f=new Object[3];
        	clsListaEnlazada lista=new clsListaEnlazada();
        	float cpuproceso;
        	float TP = 0;
        	int cant_proceso;
        	hfs[0]="0";
        	h_ini=gethorainicial();  				/*Agarro la hora de inicio de todos los procesos*/
        	lista.inserta(primerafila(h_ini));		/*Cargo la primera linea de la tabla*/
        	cant_proceso=getcatidadactivos(h_ini);	/*Veo cuantos procesos se prendieron*/
        	/*Empieza el proceso*/
        	while(Integer.parseInt(hfs[0])!=240000){
        		apagabool(h_ini);						/*Apago los que se prendieron*/
	        	hfs=horafaltantesigproceso(h_ini,hfs);	/*Veo cuanto falta para eso y la siguiente hora de llegada*/
	        	cpuproceso=getcpuproceso(cant_proceso);	/*Traigo el valor de cpuproceso segun la cantidad de proceso*/
	        	/*Aqui falta controlar cuando hfs[0] es 240000*/
	        	if(Integer.parseInt(hfs[0].toString())==240000) hfs[0]="-1"; else TP=calculoTP(hfs[0],cpuproceso); /*Calculo la Unidad de Tiempo de Proceso con ese tiempodado*/
	        	lista.inserta(procesos(TP,lista.ultimo(),hfs[0],cant_proceso,cpuproceso,hfs[1],lista));
	        	h_ini=hfs[1];							/*Cargo la nueva hora del proceso*/
	        	cant_proceso=numprocesos(lista.ultimo()); /*Veo cuantos procesos qdaron activos*/
        	}										
        }
        
        /*MaxTiempo Restante*/
        
        /*Es una funcion que me devolvera cuantos proceso termina o no*/
        public int termina(float tp,Object[] ultimatupla){
        	int i,cont=0;
        	for(i=0;i<ultimatupla.length;i++){
        		if(Integer.parseInt(ultimatupla[i].toString())-tp == 0){
        			cont++;
        		}
        	}
        	return cont;
        }
        
        /*Modifica los valore con el tp dado*/
        public Object[] modificavalores(float tp,Object[] ultimatupla){
        	for(int i=1;i<ultimatupla.length;i++){
        		if(Integer.parseInt(ultimatupla[i].toString())!=-1) ultimatupla[i]=Float.parseFloat(ultimatupla[i].toString())-tp;
        	}
        	return ultimatupla;
        }
        
        /*Veo Cuanto Procesos ahora se prendieron*/
        public int numprocesos(Object[] ultimatupla){
        	int cont=0;
        	for(int i=1;i<ultimatupla.length;i++){
        		if(Integer.parseInt(ultimatupla[i].toString())!=-1 && Integer.parseInt(ultimatupla[i].toString())!=0){
        			cont++;
        		}
        	}
        	return cont;
        }
        /*Ve si tengo procesos por terminar y tomo decisiones*/
        public Object[] procesos(float tp, Object[] ultimatupla,String hora,int cant_proceso,float cpuproceso,String h2,clsListaEnlazada list){
        	if(termina(tp,ultimatupla)!=0 && Integer.parseInt(hora)!=-1){
        		ultimatupla=modificavalores(tp,ultimatupla);
        		cant_proceso-=termina(tp,ultimatupla);
        		cpuproceso=getcpuproceso(cant_proceso);
        		tp=calculoTP(hora,cpuproceso);
        	}
        	if(Integer.parseInt(hora)!=-1){
        	ultimatupla=modificavalores(tp,ultimatupla);
        	ultimatupla=cargaevento(h2);
        	}else{
        	todotermina(list,ultimatupla,cant_proceso);
        	}
        	return ultimatupla;
        }
        
       
        /*Termina todo es termina todos los procesos*/
        private void todotermina(clsListaEnlazada list, Object[] ultimatupla,int cpuproceso) {
			int i=1;
                        float TP;
                        String TR="";
			while(i<ultimatupla.length){
				if(Integer.parseInt(ultimatupla[i].toString())==0){
					i++;
				}else{
                                    TP=busca(ultimatupla);
                                    TR=TR(cpuproceso,TP);
                                    list.inserta(modificaultomos(utlimatupla,TP));
                                    TR=sumatiempo(TR,hora); /*Hay q sacar la ultima hora de la actualizacion realizada*/
				}
				
			}
		}
        
        
        /*Calculo teniendo el TP cuanto min en tiempo real necesito*/
        public String TR(int cpuproceso,float TP){
        	/*Preguntar a amor si puedo hacer eso*/
	        switch(String.valueOf(cpuproceso/TP).length()){
	        case 0:{return String.valueOf(cpuproceso/TP).substring(0);}
	        case 1:{return String.valueOf(cpuproceso/TP).substring(0,1);}
	        case 2:{return String.valueOf(cpuproceso/TP).substring(0,2);}
	        case 3:{return String.valueOf(cpuproceso/TP).substring(0,3);}
	        }
	        return "";
        }
        
        /*Busca menor del restante*/
        public float busca(Object[] ultimatupla){
        	float aux=Integer.parseInt(ultimatupla[1].toString());
        	for(int i=2;i<ultimatupla.length;i++ ){
        		if(aux <= Integer.parseInt(ultimatupla[i].toString())){
        			aux=Integer.parseInt(ultimatupla[i].toString());
        		}
        	}
        	return aux;
        }
        
        /*Suma Tiempo*/
        public String SumaTiempo(String h1,String h2){
            /*Donde h1 es hora inicial y h2 son los minutos que tiene q transcurrir*/
            int a,b,c,acarreo;
            String d="";
            acarreo=0;
            /*Primero Segundos*/
            a=Integer.parseInt(h1.substring(4,6));
            b=Integer.parseInt(h2.substring(4,6));
            c=a+b;
            if(c>60){
                c-=60;
                acarreo++;
            }
            d=d+String.valueOf(c);
            /*Los minutos*/
            a=Integer.parseInt(h1.substring(2,4));
            b=Integer.parseInt(h2.substring(2,4));
            c=a+b+acarreo;
            acarreo=0;
            if(c>60){
                c-=60;
                acarreo++;
            }
            d=String.valueOf(c)+d;
            /*Horas*/
            a=Integer.parseInt(h1.substring(0,2));
            b=Integer.parseInt(h2.substring(0,2));
            c=a+b+acarreo;
            d=String.valueOf(c)+d;
            return d;
        }
        
        
		//CUIDADO TENEMOS Q VER TODO CON LA CANTIDAD CUANDO ES 0
        public static void main(String[] args){
                clsModeloProb obj1=new clsModeloProb(3);
                obj1.Muestra();

	}
	
}
