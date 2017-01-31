package Clases;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;

/**
 *
 * @author AlphaGo
 */
public class GestionArchivo {

    BaseConocimientos base_conocimientos;
    Scanner lector;

    /**
     * Constante para indicar el nombre del archivo maestro
     */
    public static final String ARCHIVO_MAESTRO = "archivo_maestro";

    /**
     * Método usado para guardar los datos en el archivo binario maestro Solo se
     * utiliza cuando el archivo de texto se modifica
     *
     * @return boolean Indica si la sintáxis de las reglas es correcta
     * @throws java.io.IOException Para en caso de error de lectura
     */
    public boolean escribir() throws IOException {

        ArrayList<BaseConocimientos> tmpListaReglas = leerReglas();
        if (tmpListaReglas == null) { //sintáxis incorrecta
            return false;
        }

        try (ObjectOutputStream archivo = new ObjectOutputStream(new FileOutputStream(ARCHIVO_MAESTRO))) {
            for (int i = 0; i < tmpListaReglas.size(); i++) {
                archivo.writeObject(tmpListaReglas.get(i));
            }
        }
        return true;
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
                System.out.println("Base de conocimientos:");
                for (int i = 0; i < 9; i++) {
                    tmpListaReglas.add((BaseConocimientos) archivo.readObject());
                    System.out.println(tmpListaReglas.get(i).getAntecedentes() + " - " + tmpListaReglas.get(i).getConsecuente());
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
            Scanner verificador = new Scanner(new FileReader(new File("reglas.txt")));
            lector = new Scanner(new FileReader(new File("reglas.txt")));

            if (!verificaReglas(verificador)) { //verifica que la sintáxis de las reglas sea la correcta
                return null;
            }

            while (lector.hasNext()) {
                dato = lector.next();

                if (dato.equals("-")) {
                    flag = true;
                } else {
                    if (!flag) { //en arreglo antecedente
                        tmpAntecedentes.add(dato.toLowerCase());
                    } else { //consecuente
                        tmpListaReglas.add(new BaseConocimientos(tmpAntecedentes, dato.toLowerCase()));
                        flag = false;
                        tmpAntecedentes = new ArrayList<>(0);
                    }
                }
            }
            lector.close();

            return tmpListaReglas;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Verifica que la sintáxis escrita en el archivo reglas.txt sea la correcta
     *
     * @param verificador Scanner relacionado con reglas.txt para poder obtener
     * dichas reglas del archivo
     * @return boolean false = sintáxis incorrecta
     */
    public boolean verificaReglas(Scanner verificador) {
        String regla;
        StringTokenizer st;
        String[] elementos;
        int count;

        while (verificador.hasNext()) {
            regla = verificador.nextLine(); //obtiene la regla

            count = 0;
            for (int i = 0; i < regla.length(); i++) { //checa que solo haya un - en la regla
                if (regla.charAt(i) == '-') {
                    ++count;
                }
            }
            if (count != 1) {
                return false;
            }

            elementos = regla.split("-"); //separa la regla por -
            //hay más de un guión y éste está seguido de más elementos
            //o no hay -
            if (elementos.length != 2) {
                return false;
            }

            st = new StringTokenizer(elementos[1]); //elementos debe tener 2 casillas: antecedentes y consecuentes 
            if (st.countTokens() != 1) { //solo debe haber un consecuente
                return false;
            }
            //si llega a este punto, la sentencia es correcta
        }
        return true;
    }

    /**
     * @param args
     * @throws IOException
     * @deprecated Únicamente para pruebas
     */
    public static void main(String[] args) throws IOException {
        new GestionArchivo().leerMaestro();
    }

    /**
     * Añade las reglas contenidas en un ArrayList<BaseConocimientos> al archivo
     * reglas.txt
     *
     * @param p_listaReglas ArrayList<BaseConocimientos>
     */
    public void escrbirReglas(ArrayList<BaseConocimientos> p_listaReglas, boolean pReescribir) {
        try {
            FileWriter fw = new FileWriter("reglas.txt", pReescribir);
            PrintWriter pw = new PrintWriter(fw);

            for (int j = 0; j < p_listaReglas.size(); j++) {
                int num_antecedentes = p_listaReglas.get(j).antecedentes.size();

                if (!pReescribir && j == 0) {

                } else {
                    pw.println();
                }

                for (int i = 0; i < num_antecedentes; i++) {
                    pw.print(p_listaReglas.get(j).antecedentes.get(i) + " ");
                }
                pw.print("- ");
                pw.print(p_listaReglas.get(j).consecuente);
            }
            fw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
