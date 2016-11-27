package Clases;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author Radogan
 */
public class GestionArchivo {

    BaseConocimientos base_conocimientos;
    Scanner escritor;
    public static final String ARCHIVO_MAESTRO = "archivo_maestro";

    /**
     * Método usado para guardar los datos en el archivo binario Solo se utiliza
     * cuando el archivo de texto se modifica
     *
     * @exception En caso de error de lectura
     */
    public void escribir() throws IOException {
        ArrayList<BaseConocimientos> tmpListaReglas = leerReglas();
        try (ObjectOutputStream archivo = new ObjectOutputStream(new FileOutputStream(ARCHIVO_MAESTRO))) {
            for (int i = 0; i < tmpListaReglas.size(); i++) {
                archivo.writeObject(tmpListaReglas.get(i));
            }
        }
    }

    /**
     * Obtiene la lista de reglas del archivo maestro
     *
     * @return Lista de reglas en ArrayList
     */
    public ArrayList<BaseConocimientos> leerMaestro() {
        ArrayList<BaseConocimientos> tmpListaReglas = new ArrayList<>(9);

        try {
            try (ObjectInputStream archivo = new ObjectInputStream(new FileInputStream(ARCHIVO_MAESTRO))) {
                for (int i = 0; i < 9; i++) {
                    tmpListaReglas.add((BaseConocimientos) archivo.readObject());
                }
            }
        } catch (IOException | ClassNotFoundException e) {
        }

        return tmpListaReglas;
    }

    /**
     * Método usado para leer las reglas del archivo de texto y guardarlas en un
     * ArrayList
     *
     * @return Lista de reglas en arraylist
     */
    public ArrayList<BaseConocimientos> leerReglas() {
        String dato;
        boolean flag = false;
        ArrayList<BaseConocimientos> tmpListaReglas = new ArrayList<>(0);
        ArrayList<String> tmpAntecedentes = new ArrayList<>(0);
        try {
            escritor = new Scanner(new FileReader(new File("reglas.txt")));

            while (escritor.hasNext()) {
                dato = escritor.next();

                if (dato.equals("-")) {
                    flag = true;
                } else {
                    if (!flag) { //en arreglo antecedente
                        tmpAntecedentes.add(dato);
                    } else { //consecuente
                        tmpListaReglas.add(new BaseConocimientos(tmpAntecedentes, dato));
                        flag = false;
                        tmpAntecedentes = new ArrayList<>(0);
                    }
                }
            }
            escritor.close();

            return tmpListaReglas;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) throws IOException {
        new GestionArchivo().leerMaestro();
    }
}
