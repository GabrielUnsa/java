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
        public String[] horafaltantesigproceso(String hora){
            int aux2,aux=240000;/*No tengo mas procesos que se iniciaran*/
            int i=0;
            String[] h1=new String[2];
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

        
        /*Pseudo Resolucion*/
        public void pseudoResolucion(){
        	String h_ini;
        	String[] hfs=new String[2];
        	float cpuproceso;
        	float TP;
        	int cant_proceso;
        	
        	h_ini=gethorainicial();
        	apagabool(h_ini);
        	cant_proceso=getcatidadactivos(h_ini);
        	hfs=horafaltantesigproceso(h_ini);
        	cpuproceso=getcpuproceso(cant_proceso);
        	TP=calculoTP(hfs[1],cpuproceso);
        	/*Preguntamos si nuevoTP es mayor algun TPviejo de la tabla
        	 * de procesos luego de eso calculamos el nuevoto de con el tp faltante (modulo q falta)
        	 * y repetimos cambiamos cant_proceso y repetimos lo mismo
        	 * a mejorar el tema de la hora y el booleano*/
        	
        	
        	
        	
        }
        public static void main(String[] args){
        		String sig="";
                clsModeloProb obj1=new clsModeloProb(3);
                obj1.Muestra();

	}
	
}
