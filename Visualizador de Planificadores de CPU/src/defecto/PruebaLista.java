package defecto;
public class PruebaLista {

public static void MuestraUltim(Object[] t){
    for(int i=0; i<t.length;i++){
        System.out.println("INFO: "+t[i]+" i:"+i);
    }
}

public static void main(String[] args) {
    clsListaEnlazada list =new clsListaEnlazada();
    Object[] t=new Object[10];
    int p=0;
    for(int j=0;j<10;j++){
        p+=20;
        for(int i=0;i<10;i++){
            t[i]=p;
        }
        list.add(t);
    }
    System.out.println("Muestra Principal");
    list.muestra();
}
	
}
