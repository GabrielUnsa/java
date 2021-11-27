package proyecto;

import java.awt.*;
import java.io.*;
public class principal {
	public static void main(String[] args) throws IOException{
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					clsVentana ven = new clsVentana(); //ventana nueva
					ven.setVisible(true); // ventana visible
					ven.setResizable(false);  // no se redimensiona
					} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
		});
	}
}