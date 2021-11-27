package proyecto;
import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;


public class clsVentana extends JFrame{
	protected Focus foco= new Focus();
	protected JComboBox cantidadprocesos;
	protected DefaultTableModel dtm;
	protected JTable tablaproceso;
	protected JButton cargar;
	protected JButton limpiar;
	protected JTable tablaresultados;
	protected DefaultTableModel dtmr;
	protected JScrollPane scrollPane2;
	private clsModeloProb modelo;
	protected JCheckBox mismop;
	protected JFormattedTextField t;
	protected JTextField p;
	protected JButton guardar;
	private boolean ban;
	protected boolean b; //Booleano de error

	public clsVentana(){
		creaVentana();
	}

	private class Focus implements FocusListener {

		@Override
		public void focusGained(FocusEvent fe) {
		}

		@Override
		public void focusLost(FocusEvent fe) {
			if(p.getText().length()!=0)control();  /*Falta Pulir*/
		}
	}

	public void creaVentana(){
		setTitle("Modelo Probabilistico");
		setForeground(new Color(255, 255, 204));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//setBounds(50, 100, 700, 600);
		setSize(600, 450);
		getContentPane().setLayout(null);

		JLabel lb1= new JLabel("Cantidad de Procesos");
		lb1.setBounds(50, 10, 250, 30);
		getContentPane().add(lb1);

		cantidadprocesos = new JComboBox(cargaCombo());
		cantidadprocesos.setBounds(230, 10, 120, 30); //x,y, ancho,largo
		getContentPane().add(cantidadprocesos);

		JLabel lb2 =new JLabel("P unico");
		lb2.setBounds(380, 10, 80, 30);
		getContentPane().add(lb2);

		mismop = new JCheckBox();
		mismop.setBounds(430,10,30,30);
		getContentPane().add(mismop);
		
		p = new JTextField(3);
		p.setBounds(470, 10, 40, 30);p.setEnabled(false);
		getContentPane().add(p);
		p.setEnabled(false);
		p.addFocusListener(foco);
		
		cargar = new JButton("Calcular");
		cargar.setBounds(400, 50, 120, 30);
		getContentPane().add(cargar);

		limpiar =new JButton("Limpiar");
		limpiar.setBounds(400, 100, 120, 30);
		getContentPane().add(limpiar);

		guardar = new JButton("Guardar");
		guardar.setBounds(400, 200, 120, 30);
		getContentPane().add(guardar);

		creaTablaProcesos();
		accionTabla();
		accionCargar();
		accionLimpiar();
		accionCheck();
		accionGuardar();
		controlaTextField();
	}
	public void creaTablaProcesos(){
		tablaproceso =new JTable(){public boolean isCellEditable(int rowIndex, int vColIndex) {return true;}};
		dtm=(DefaultTableModel) tablaproceso.getModel();
		tablaproceso.setModel(dtm);
		tablaproceso.getTableHeader().setReorderingAllowed(false);
		dtm.addColumn("Proceso");
		dtm.addColumn("Hora Inicio");
		dtm.addColumn("Tiempo CPU");dtm.addColumn("% I/O");dtm.setColumnCount(4);
		tablaproceso.setRowHeight(20);
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(30, 50, 350,230 );
		scrollPane.setViewportView(tablaproceso);
		getContentPane().add(scrollPane);
		getContentPane().setLayout(null);
	}
	public void creaTablaResultados(){
		tablaresultados = new JTable() {public boolean isCellEditable(int rowIndex,int vColIndex){return false;}};
		dtmr=(DefaultTableModel) tablaresultados.getModel();
		tablaresultados.setModel(dtmr);
		tablaresultados.getTableHeader().setReorderingAllowed(false);
		dtmr.addColumn("Hora");

		for (int i=0;i<seleccionIndice();i++){
			dtmr.addColumn(dtm.getValueAt(i, 0));
		}
		tablaresultados.setRowHeight(20);
		scrollPane2 = new JScrollPane();
		tablaresultados.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
		scrollPane2.setBounds(10, 300, 500,100 );
		scrollPane2.setViewportView(tablaresultados);
		getContentPane().add(scrollPane2);
		getContentPane().setLayout(null);ban=true;
		//scrollPane2.setVisible(true);

		setSize(600, 450);
	}
	public void accionTabla(){
		cantidadprocesos.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent evento) {
				if(seleccionIndice()!= 0){
					Object object[] = new Object[2];
					cantidadprocesos.setEnabled(false);
					dtm.addRow(object);
					dtm.setNumRows(seleccionIndice());
					if(!pVacio()){
						for (int i=0;i<seleccionIndice();i++)dtm.setValueAt(p.getText(), i, 3);

					}
				}
			}
		});
	}

	//metodos de control de entrada
	public void accionCargar(){
		cargar.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				controltable();
				if(b){
					if(seleccionIndice()!=-1 && !hayCeldasVacias()){
						creaTablaResultados();
						cargaDatos();
						modelo.Resolucion();
						cargaResultados();


					}else
						JOptionPane.showMessageDialog(null, "Hay celdas Vacias ","ERROR",JOptionPane.ERROR_MESSAGE);
				}
			}
		});
	}
	public void accionLimpiar(){
		limpiar.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				limpiaTabla();habilitaCombo();
				limpiaTexto();limpiaCheck();
				if (ban==true){
					limpiaTablaR();ban=false;
					tablaresultados.setVisible(false);
					scrollPane2.setVisible(false);
				}
				setSize(600, 400);
			}});
	}

	public void accionCheck(){
		mismop.addItemListener(new ItemListener(){
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED){
					p.setEnabled(true);
				}else{
					p.setEnabled(false);
				}
			}
		});
	}

	public void controlaTextField(){
		p.addKeyListener(new KeyAdapter(){
			public void keyTyped(KeyEvent e){
				try{
					if(p.getText().length()==5){
						e.consume();
						if(!pValido(p.getText())){
							JOptionPane.showMessageDialog(null,"El P ingresado no es valido","Error",JOptionPane.ERROR_MESSAGE);
							getToolkit().beep();
							p.requestFocus();
						}
					}
				}catch(Exception pk){
					JOptionPane.showMessageDialog(null,"No se admiten Letras","Error",JOptionPane.ERROR_MESSAGE);
					p.setText("");
					p.requestFocus();
				}

			}
			public void keyReleased(KeyEvent e) {
				for (int i=0;i<dtm.getRowCount();i++)dtm.setValueAt(p.getText(), i, 3);

			}

		});
	}

	public void control(){
		try{
			if(!pValido(p.getText())){
				JOptionPane.showMessageDialog(null,"El P ingresado no es valido","Error",JOptionPane.ERROR_MESSAGE);
				getToolkit().beep();
				p.requestFocus();
			}
		}catch(Exception pk){
			JOptionPane.showMessageDialog(null,"No se admiten Letras","Error",JOptionPane.ERROR_MESSAGE);
			getToolkit().beep();
			p.setText("");
			p.requestFocus();
		}
	}

	public boolean pValido(String p){
		float p1=Float.parseFloat(p);
		if(p1<1.0 && p1>=0.0) return true;
		else return false;
	}

	public boolean pVacio(){
		if(p.isEnabled()){
			return p.getText()==null;
		}else return true;
	}
	public void accionGuardar(){
		guardar.addMouseListener(new java.awt.event.MouseAdapter(){
			public void mouseClicked(MouseEvent e){
				if(dtmr.getRowCount()>1){
					try{
						String nom="";
						JFileChooser file =new JFileChooser();
						file.showSaveDialog(null);
						File guarda = file.getSelectedFile();
						if(guarda!=null){
							FileWriter save = new FileWriter(guarda+".txt");
							BufferedWriter b= new BufferedWriter(save);
							b.write("RESULTADOS");b.newLine();
							b.write("Se han ingresado"+seleccionIndice()+" procesos.Los datos ingresados son:");
							b.newLine();
							b.write("   Proceso\tHora Inicio \t Tiempo CPU \tp");
							b.newLine();
							for (int i=0;i<dtm.getRowCount();i++){
								for(int j=0;j<dtm.getColumnCount();j++){
									nom=String.valueOf(dtm.getValueAt(i, j));
									if(j==2 || j==3)nom="\t"+nom;
									b.write("\t"+nom);
								}b.newLine();
							}
							b.write("Se obtuvo como resultado:");b.newLine();
							for(int i=0;i<dtmr.getColumnCount();i++){//nombres de comlumnas
								b.write("\t\t"+dtmr.getColumnName(i));
							}b.newLine();
							for (int i=0;i<dtmr.getRowCount();i++){
								for(int j=0;j<dtmr.getColumnCount();j++){
									nom=String.valueOf(dtmr.getValueAt(i, j));
									b.write("\t\t"+nom);
								}// fi for j
								b.newLine();
							}//fin for i 
							b.close();
							JOptionPane.showMessageDialog(null, "El archivo se guardo","Informacion",JOptionPane.INFORMATION_MESSAGE);
						}
					}catch (IOException e1) {
						// TODO Auto-generated catch block
						JOptionPane.showMessageDialog(null, "No se guardo", "Advertencia", JOptionPane.WARNING_MESSAGE);
						e1.printStackTrace();
					}	
				}else JOptionPane.showMessageDialog(null,"Debe calcular primero","ERROR",JOptionPane.ERROR_MESSAGE);
				
			}
		});
	}

	public void cargaResultados(){
		Object[] obj;
		String cad;
		clsNodo tmp=modelo.getList().getInicio();
		for (int i=0;i<=2;i++){//filas 3
			for(int j=0;j<=seleccionIndice();j++){
				if(tmp!=null){
					obj=modelo.getList().convObject(tmp.getNodoInfo(),tmp.getNodoInfo().length());
					cad=String.valueOf(obj[0]);
					obj[0]=cad.substring(0,2)+":"+cad.substring(2,4)+":"+cad.substring(4,6) ;
					dtmr.addRow(obj);
					tmp=tmp.getSigNodo();
				} //fin if
			}// fi for j
		}//fin for i
	}
	public String[] cargaCombo(){
		String combo[]= new String[11];combo[0]="Seleccione";
		for (int i=1;i<=10;i++){
			combo[i]=Integer.toString(i);
		}
		return combo;
	}
	public boolean hayCeldasVacias(){
		boolean resp=false;
		for (int f=0;f<dtm.getRowCount();f++){
			for(int c=0;c<=2;c++){
				if (esCeldaVacia(f,c))resp=true;
			}
		}
		return resp;
	}
	public boolean esCeldaVacia(int f,int c){
		return tablaproceso.getValueAt(f, c)==null;
	}
	public void habilitaCombo(){
		cantidadprocesos.setEnabled(true);
	}
	public void limpiaTexto(){
		p.setText(null);
	}
	public void limpiaCheck(){
		mismop.setSelected(false);
	}
	public void limpiaTabla(){
		if(dtm.getRowCount()!=0){
			for(int i=dtm.getRowCount()-1;i>=0;i--){
				dtm.removeRow(i);
			}
		}
	}
	public void limpiaTablaR(){
		if(dtmr.getRowCount()!=0){
			for(int i=dtmr.getRowCount()-1;i>=0;i--){
				dtmr.removeRow(i);
			}
		}
	}
	public int seleccionIndice(){
		return cantidadprocesos.getSelectedIndex();
	}
	public void cargaDatos(){
		int filas=seleccionIndice();
		int j;
		Object obj[][]=new Object[filas][5];
		System.out.println(filas);
		for (int i=0;i<filas;i++){
			j=0;
			while(j<4){
				if(j!=1){
					obj[i][j]=dtm.getValueAt(i, j);
				}
				j++;
			}
			obj[i][1]=dtm.getValueAt(i,1).toString().replaceAll(":","");//quita :: de la hora
			obj[i][4]=true;
		}
		modelo = new clsModeloProb(obj,filas);
	}

	public boolean controla(String h){
		if(h.charAt(2)==':' && h.charAt(5)==':' ){
			return true;
		} else return false;
	}

	public boolean esHora(String h){
		if(h.compareTo("00")>=0 && h.compareTo("23")<=0){
			return true;
		}
		else return false;
	}
	public boolean esMinSeg(String min){
		if(min.compareTo("00")>=0 && min.compareTo("59")<=0){
			return true;
		}
		else return false;
	}

	public void controlTP(String tp, int fila){
		try{
			if(Float.parseFloat(tp)<0.0){
				JOptionPane.showMessageDialog(null,"El Tiempo CPU no es valido, por favor ingrese un tp mayor a cero en Fila: "+fila+"Columna: 3","Error",JOptionPane.ERROR_MESSAGE);
				b=false;
			}
		}catch(Exception e){
			JOptionPane.showMessageDialog(null,"No ingrese letras en Fila: "+fila+"Columna: 3","Error",JOptionPane.ERROR_MESSAGE);
			b=false;
		}
	}
	public void controlp(String p,int i){
		try{
			if(!pValido(p)){
				JOptionPane.showMessageDialog(null,"El P ingresado no es valido en Fila: "+i+" Columna: 4","Error",JOptionPane.ERROR_MESSAGE);
				getToolkit().beep();
				b=false;
			}
		}catch(Exception pk){
			JOptionPane.showMessageDialog(null,"No se admiten Letras en Fila: "+i+" Columna: 4","Error",JOptionPane.ERROR_MESSAGE);
			b=false;
		}
	}

	//b es un booleano q se desactivara cuando haya algun tipo de error asi al dejar de procesar los while
	//evitara q las ventanas salgan salvajemente
	public void controltable(){
		int p,k;
		p=0;
		b=true;
		while(b && p<dtm.getRowCount()){
			k=1;
			while(b && k < dtm.getColumnCount()){
				switch(k){
				case 1:{
					if(dtm.getValueAt(p,k).toString().length()==8)controlaHora(dtm.getValueAt(p, k).toString(),p+1);
					else JOptionPane.showMessageDialog(null,"El formato de hora debe ser HH:MM:SS","Error",JOptionPane.ERROR_MESSAGE);break;}
				case 2:{controlTP(dtm.getValueAt(p, k).toString(),p+1);break;}
				case 3:{
					if(!mismop.isSelected()) 
						controlp(dtm.getValueAt(p, k).toString(), p+1);break;}
				}
				k++;
			}
			p++;
		}

	}

	public void controlaHora(String h, int i){
		if(controla(h)){ //contola que sea de formato HH:MM:SS
			String cad[]=h.split(":"); 
			if(esHora(cad[0])){ //controla que 00<=HH<=23
				if(esMinSeg(cad[1])){ //controla que 00<=MM<=59
					if(!esMinSeg(cad[2])){  //controla que 00<=SS<=59
						JOptionPane.showMessageDialog(null,"No es valido segundos en fila: "+i,"Error",JOptionPane.ERROR_MESSAGE);
						b=false;
					} 
				}else{ JOptionPane.showMessageDialog(null,"No es valido minutos en fila: "+i,"Error",JOptionPane.ERROR_MESSAGE); b=false;}
			}else{ JOptionPane.showMessageDialog(null,"No es valido horas en fila: "+i,"Error",JOptionPane.ERROR_MESSAGE); b=false;}
		}else{ JOptionPane.showMessageDialog(null,"Datos Incorrectos en fila: "+i,"Error",JOptionPane.ERROR_MESSAGE);b=false;}
	}
}



