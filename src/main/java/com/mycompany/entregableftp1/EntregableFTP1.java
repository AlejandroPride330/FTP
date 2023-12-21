/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.mycompany.entregableftp1;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
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

    public static void main(String[] args) {
        FTPClient cliente = new FTPClient();
        Scanner sc = new Scanner(System.in);

        String servFTP = "";
        String usuario = "";
        String clave = "";

        menuComandos();
        String respuesta;
        String[] trozos;
        do {
            System.out.print("-> ");
            respuesta = sc.nextLine();
            trozos = respuesta.split(" ");
            switch (trozos[0]) {
                case "Connect":
                    servFTP = trozos[1];
                    String[] partes = trozos[2].split("/");
                    usuario = partes[0];
                    clave = partes[1];
//                    conectar(cliente, servFTP, usuario, clave);
                    System.out.println(conectar(cliente, servFTP, usuario, clave));
                    break;
                case "List":
                    if (cliente.isConnected()) {
                        System.out.println(listar(cliente));
//                        listar(cliente);
                    } else {
//                        System.out.println("No estás conectado. Usa el comando 'Connect' primero.");
                    }
                    break;
                case "changePath":
                    String nuevoPath = trozos[1];
//                    cambiarDirectorio(cliente, nuevoPath);
                    System.out.println(cambiarDirectorio(cliente, nuevoPath));
                    break;
                case "down":
//                    descargar(cliente, trozos[1]);
                    System.out.println(descargar(cliente, trozos[1]));
                    break;
                case "up":
//                    subir(cliente, trozos[1]);
                    System.out.println(subir(cliente, trozos[1]));
                    break;
                case "back":
//                    cambiarDirectorio(cliente, "/");
                    System.out.println(cambiarDirectorio(cliente, "/"));
                    break;
                case "menu":
                    menuComandos();
                    break;
                default:
                    System.out.println("comando incorrecto.");
                    break;
            }

        } while (!respuesta.equalsIgnoreCase("Disconnect"));
//        desconectar(cliente, servFTP, usuario, clave);
        System.out.println(desconectar(cliente, servFTP, usuario, clave));
    }

    public static String listar(FTPClient cliente) {
        String resp = "";
        try {
            if (cliente.isConnected()) {
//                System.out.println("Directorio actual: "
//                        + cliente.printWorkingDirectory());
                String dir = "Directorio actual: "
                        + cliente.printWorkingDirectory();
                String arch = "";

                FTPFile[] files = cliente.listFiles();
//                System.out.println("Ficheros en el directorio actual:"
//                        + files.length);

                String tipos[] = {"Fichero", "Directorio", "Enlace simb."};

                for (int i = 0; i < files.length; i++) {
//                    System.out.println("\t" + files[i].getName() + " => "
//                            + tipos[files[i].getType()]);
                    arch += "\t" + files[i].getName() + " => "
                            + tipos[files[i].getType()] + "\n";
                }
                resp = dir + "\n" + arch;
            } else {
//                System.out.println("No estás conectado. Usa el comando 'Connect' primero.");
                resp = "No estás conectado. Usa el comando 'Connect' primero.";
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return resp;
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

    public static String conectar(FTPClient cliente, String servFTP, String usuario, String clave) {
        String r = "";
        if (!cliente.isConnected()) {
            try {
                cliente.connect(servFTP);
                cliente.enterLocalPassiveMode();

                boolean login = cliente.login(usuario, clave);
                cliente.setFileType(FTP.BINARY_FILE_TYPE);
                if (login) {
//                    System.out.println("Login correcto...");
                    r = "Login correcto...";
                } else {
//                    System.out.println("Login Incorrecto...");
                    r = "Login Incorrecto...";
                    cliente.disconnect();
                    //System.exit(1);
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }

        } else {
//            System.out.println("cliente ya conectado.");
            r = "cliente ya conectado.";
        }
        return r;
    }

    public static String desconectar(FTPClient cliente, String servFTP, String usuario, String clave) {
        String r = "";
        if (cliente.isConnected()) {

            try {
                cliente.disconnect();
//                System.out.println("Desconectado correctamente.");
                r = "Desconectado correctamente.";
                System.exit(0);
            } catch (IOException ex) {
                ex.printStackTrace();
            }

        } else {
//            System.out.println("No estás conectado. Usa el comando 'Connect' primero.");
            r = "No estás conectado. Usa el comando 'Connect' primero.";
        }
        return r;
    }

    public static String cambiarDirectorio(FTPClient cliente, String nuevoPath) {
        String r = "";
        try {
            if (cliente.isConnected()) {
                boolean change = cliente.changeWorkingDirectory(nuevoPath);
                if (change) {
//                    System.out.println("Cambio correctamente. Nuevo directorio: " + nuevoPath);
                    r = "Cambio correctamente. Nuevo directorio: " + nuevoPath;
                } else {
//                    System.out.println("No se pudo cambiar al directorio: " + nuevoPath);
                    r = "No se pudo cambiar al directorio: " + nuevoPath;
                }
            } else {
//                System.out.println("No estás conectado. Usa el comando 'Connect' primero.");
                r = "No estás conectado. Usa el comando 'Connect' primero.";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return r;
    }

    public static String descargar(FTPClient cliente, String fichero) {
        String r = "";
        String archivo1 = fichero;
        String[] trozos = archivo1.split(("\\\\"));

        if (trozos.length > 1) {
            boolean existeFile = false;
            int indicador = trozos.length - 1;
            String fichero1 = trozos[indicador];
            if (cliente.isConnected()) {

                try {
                    FileOutputStream fileOutStream = new FileOutputStream(archivo1);
                    BufferedOutputStream out = new BufferedOutputStream(fileOutStream);
                    FTPFile[] files = cliente.listFiles();
                    for (FTPFile file : files) {
                        System.out.println(file.getName());
                        if (file.getName().equals(fichero1)) {
                            existeFile = true;
                           // System.out.println(fichero1);
                        }
                    }

                    if (existeFile) {
                        if (cliente.retrieveFile(fichero1, out)) {
//                        System.out.println("Descargado correctamente.");
                            r = "Descargado correctamente.";
                        } else {
//                        System.out.println("Descargado fallida.");
                            r = "Descargado fallida.";
                        }
                        out.close();
                    } else {
                        r = "El fichero " + fichero1 + " no existe.";
                        return r;
                    }
                } catch (FileNotFoundException ex) {
//                    System.out.println("El fichero " + fichero + " no existe.");
                    r = "El fichero " + fichero + " no existe.";
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
//                System.out.println("No estás conectado. Usa el comando 'Connect' primero.");
                r = "No estás conectado. Usa el comando 'Connect' primero.";
            }

        } else {
            if (cliente.isConnected()) {
                try {
                    boolean existeFile = false;
                    File file = new File("C:\\FTP\\" + fichero);
                    BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(file));
                    FTPFile[] files = cliente.listFiles();
                    for (FTPFile file1 : files) {
                        if (file1.getName().equals(fichero)) {
                            existeFile = true;
                           // System.out.println(fichero);
                        }else{
                            existeFile=false;
                        }
                    }

                    if (existeFile) {
                        if (cliente.retrieveFile(fichero, out)) {
//                        System.out.println("Descargado exitosamente.");
                            r = "Descargado exitosamente.";
                        } else {
//                        System.out.println("Descarga fallida.");
                            r = "Descarga fallida.";
                        }
                        out.close();
                    } else {
                        r = "El fichero " + fichero + " no existe.";
                        return r;
                    }
                } catch (FileNotFoundException ex) {
//                    System.out.println("El fichero " + fichero + " no existe.");
                    r = "El fichero " + fichero + " no existe.";

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } else {
//                System.out.println("No estás conectado. Usa el comando 'Connect' primero.");
                r = "No estás conectado. Usa el comando 'Connect' primero.";
            }
        }
        return r;
    }

    public static String subir(FTPClient cliente, String fichero) {
        String r = "";
        String archivo1 = fichero;
        String[] trozos = archivo1.split(("\\\\"));

        if (trozos.length > 1) {
            int indicador = trozos.length - 1;
            String fichero1 = trozos[indicador];
            if (cliente.isConnected()) {

                File file = new File(archivo1);
                if (!file.exists()) {
//                    System.out.println("El fichero " + fichero + " no existe.");
                    r = "El fichero " + fichero + " no existe.";
                    return r;
                }
                try {
                    FileInputStream fileInputStream = new FileInputStream(archivo1);
                    BufferedInputStream in = new BufferedInputStream(fileInputStream);

                    if (cliente.storeFile(fichero1, in)) {
//                        System.out.println("Subido correctamente.");
                        r = "Subido correctamente.";
                    } else {
//                        System.out.println("Subida fallida.");
                        r = "Subida fallida.";
                    }
                    in.close();
                } catch (FileNotFoundException ex) {
//                    System.out.println("El fichero " + fichero + " no existe.");
                    r = "El fichero " + fichero + " no existe.";
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
//                System.out.println("No estás conectado. Usa el comando 'Connect' primero.");
                r = "No estás conectado. Usa el comando 'Connect' primero.";
            }

        } else {
            String archivo = "C:\\FTP\\" + fichero;
            if (cliente.isConnected()) {

                File file = new File(archivo);
                if (!file.exists()) {
//                    System.out.println("El fichero " + fichero + " no existe.");
                    r = "El fichero " + fichero + " no existe.";
                    return r;
                }
                try {
                    FileInputStream fileInputStream = new FileInputStream(archivo);
                    BufferedInputStream in = new BufferedInputStream(fileInputStream);

                    if (cliente.storeFile(fichero, in)) {
//                        System.out.println("Subido correctamente.");
                        r = "Subido correctamente.";
                    } else {
//                        System.out.println("Subida fallida.");
                        r = "Subida fallida.";
                    }
                    in.close();
                } catch (FileNotFoundException ex) {
//                    System.out.println("El fichero " + fichero + " no existe.");
                    r = "El fichero " + fichero + " no existe.";
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
//                System.out.println("No estás conectado. Usa el comando 'Connect' primero.");
                r = "No estás conectado. Usa el comando 'Connect' primero.";
            }
        }
        return r;
    }
}
