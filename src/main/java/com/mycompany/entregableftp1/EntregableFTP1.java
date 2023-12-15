/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.mycompany.entregableftp1;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

/**
 *
 * @author Alex
 */
public class EntregableFTP1 {

    private static FTPClient cliente = new FTPClient();

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        String servFTP = "";
        String usuario = "";
        String clave = "";

        menuComandos();
        String respuesta;
        String[] trozos;
        do {
            System.out.println("Introduce el comando con el que quiera trabajar.");
            respuesta = sc.nextLine();
            trozos = respuesta.split(" ");
            switch (trozos[0]) {
                case "Connect":
                    servFTP = trozos[1];
                    String[] partes = trozos[2].split("/");
                    usuario = partes[0];
                    clave = partes[1];
                    conectar(servFTP, usuario, clave);
                    break;
                case "List":
                    if (cliente.isConnected()) {
                        listar();
                    } else {
                        System.out.println("No estás conectado. Usa el comando 'Connect' primero.");
                    }
                    break;
                case "changePath":
                    String nuevoPath = trozos[1];
                    cambiarDirectorio(nuevoPath);
                    break;
                case "down":
                    descargar(trozos[1]);
                    break;
                case "up":
                    subir(trozos[1]);
                    break;
                case "back":
                    cambiarDirectorio("/");
                    break;
                case "menu":
                    menuComandos();
                    break;
            }

        } while (!respuesta.equalsIgnoreCase("Disconnect"));
        desconectar(servFTP, usuario, clave);
    }

    public static void listar() {
        try {
            if (cliente.isConnected()) {
                System.out.println("Directorio actual: "
                        + cliente.printWorkingDirectory());

                FTPFile[] files = cliente.listFiles();
                System.out.println("Ficheros en el directorio actual:"
                        + files.length);

                String tipos[] = {"Fichero", "Directorio", "Enlace simb."};

                for (int i = 0; i < files.length; i++) {
                    System.out.println("\t" + files[i].getName() + " => "
                            + tipos[files[i].getType()]);
                }
            } else {
                System.out.println("No estás conectado. Usa el comando 'Connect' primero.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void menuComandos() {
        System.out.println("------MENU DE COMANDOS-------");
        System.out.println("->Connect NombreServidor User/Pass = Para conectarte con el server.");
        System.out.println("->List = Para listar los directorios desde ubicacion.");
        System.out.println("->changePath NuevoPath = Para cambiar el directorio remoto.");
        System.out.println("->down NombreFicheroRemoto = Para descargar fichero del servidor a local.");
        System.out.println("->up NombreFicheroLocal = Subir fichero de local al servidor. ");
        System.out.println("->back = para volver al directorio de inicio. ");
        System.out.println("->menu = Si quiere volver a ver el contenido de este menu. ");
        System.out.println("->Disconnect = Para desconectar del servidor.");
        System.out.println("-------------------------------");
    }

    public static void conectar(String servFTP, String usuario, String clave) {
        if (!cliente.isConnected()) {
            try {
                cliente.connect(servFTP);
                cliente.enterLocalPassiveMode();

                boolean login = cliente.login(usuario, clave);
                cliente.setFileType(FTP.BINARY_FILE_TYPE);
                if (login) {
                    System.out.println("Login correcto...");

                } else {
                    System.out.println("Login Incorrecto...");
                    cliente.disconnect();
                    //System.exit(1);
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }

        } else {
            System.out.println("cliente ya conectado.");
        }
    }

    public static void desconectar(String servFTP, String usuario, String clave) {
        if (cliente.isConnected()) {
            try {
                cliente.disconnect();
                System.out.println("Desconectado correctamente.");
                System.exit(0);
            } catch (IOException ex) {
                ex.printStackTrace();
            }

        } else {
            System.out.println("No estás conectado. Usa el comando 'Connect' primero.");
        }
    }

    public static void cambiarDirectorio(String nuevoPath) {
        try {
            if (cliente.isConnected()) {
                boolean change = cliente.changeWorkingDirectory(nuevoPath);
                if (change) {
                    System.out.println("Cambio correctamente. Nuevo directorio: " + nuevoPath);
                } else {
                    System.out.println("No se pudo cambiar al directorio: " + nuevoPath);
                }
            } else {
                System.out.println("No estás conectado. Usa el comando 'Connect' primero.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void descargar(String fichero) {
        if (cliente.isConnected()) {
            try {
                BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream("C:\\FTP\\" + fichero));
                if (cliente.retrieveFile(fichero, out)) {
                    System.out.println("Descargado exitosamente.");
                } else {
                    System.out.println("Descarga fallida.");
                }
                out.close();

            } catch (FileNotFoundException ex) {
                System.out.println("El fichero " + fichero + " no existe.");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            System.out.println("No estás conectado. Usa el comando 'Connect' primero.");
        }

    }

    public static void subir(String fichero) {
        String archivo = "C:\\FTP\\" + fichero;
        if (cliente.isConnected()) {
            try {
                BufferedInputStream in = new BufferedInputStream(new FileInputStream(archivo));

                if (cliente.storeFile(fichero, in)) {
                    System.out.println("Subido correctamente.");
                } else {
                    System.out.println("Subida fallida.");
                }
                in.close();
            } catch (FileNotFoundException ex) {
                System.out.println("El fichero " + fichero + " no existe.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("No estás conectado. Usa el comando 'Connect' primero.");
        }

    }
}
