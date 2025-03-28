/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package examen2labprogra2;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JOptionPane;
/**
 *
 * @author laraj
 */
public class PSNUsers {
    private RandomAccessFile raf;
    private RandomAccessFile trophiesFile;
    private HashTable users;

    public PSNUsers(String usersFilename, String trophiesFilename) throws IOException {
        this.raf = new RandomAccessFile(usersFilename, "rw");
        this.trophiesFile = new RandomAccessFile(trophiesFilename, "rw");
        this.users = new HashTable();
        reloadHashTable();
    }

    private void reloadHashTable() throws IOException {
        raf.seek(0);
        while (raf.getFilePointer() < raf.length()) {
            long pos = raf.getFilePointer();
            String username = raf.readUTF();
            raf.readInt();
            raf.readInt();
            boolean isActive = raf.readBoolean();
            if (isActive) {
                users.add(username, pos);
            }
        }
    }

    public boolean addUser(String username) throws IOException {
        if (users.search(username) != -1L) {
            return false;
        }

        raf.seek(raf.length());
        long pos = raf.getFilePointer();
        raf.writeUTF(username);
        raf.writeInt(0);
        raf.writeInt(0);
        raf.writeBoolean(true);
        users.add(username, pos);
        return true;
    }

    public boolean deactivateUser(String username) throws IOException {
        long pos = users.search(username);
        if (pos == -1L) {
            return false;
        }

        raf.seek(pos);
        raf.readUTF();
        raf.readInt();
        raf.readInt();
        raf.writeBoolean(false);
        return true;
    }

    public boolean addTrophieTo(String username, String trophyGame, String trophyName, Trophy type) throws IOException {
        username = username.toLowerCase();  
        long userPos = users.search(username); 
        if (userPos == -1L) {
            JOptionPane.showMessageDialog(null, "Usuario no existe.");
            return false;
        }

        raf.seek(userPos);
        raf.readUTF();  
        int currentPoints = raf.readInt();
        int currentTrophies = raf.readInt();
        boolean isActive = raf.readBoolean(); 

        if (!isActive) {
            JOptionPane.showMessageDialog(null, "Error: Usuario inactivo. No se pueden agregar trofeos.");
            return false;
        }

        if (trophyGame == null || trophyName == null || type == null) {
            return true;  
        }

        currentPoints += type.points;
        currentTrophies += 1;
        raf.seek(userPos + username.length() + 2); 
        raf.writeInt(currentPoints);
        raf.writeInt(currentTrophies);
        trophiesFile.seek(trophiesFile.length());  
        trophiesFile.writeUTF(username);  
        trophiesFile.writeUTF(type.name());  
        trophiesFile.writeUTF(trophyGame); 
        trophiesFile.writeUTF(trophyName);  
        String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date()); 
        trophiesFile.writeUTF(currentDate);  
        return true;
    }

    public String getPlayerInfo(String username) throws IOException {
        long userPos = users.search(username);
        if (userPos == -1L) {
            return null;
        }

        raf.seek(userPos);
        String user = raf.readUTF();
        int points = raf.readInt();
        int trophies = raf.readInt();
        boolean isActive = raf.readBoolean();
        
        if (!isActive) {
             return "El usuario esta desactivado.";
        }

        StringBuilder info = new StringBuilder();
        info.append("Usuario: ").append(user).append("\n")
                .append("Puntos de trofeos: ").append(points).append("\n")
                .append("Cantidad de trofeos: ").append(trophies).append("\n")
                .append("Estado: ").append(isActive ? "Activo" : "Inactivo").append("\n\n")
                .append("Trofeos ganados:\n");

        trophiesFile.seek(0);
        while (trophiesFile.getFilePointer() < trophiesFile.length()) {
            String trofeoUser = trophiesFile.readUTF();
            String tipo = trophiesFile.readUTF();
            String juego = trophiesFile.readUTF();
            String descripcion = trophiesFile.readUTF();
            String fecha = trophiesFile.readUTF();

            if (trofeoUser.equals(username)) {
                info.append(fecha).append(" - ").append(tipo).append(" - ").append(juego).append(" - ").append(descripcion).append("\n");
            }
        }

        return info.toString();
    }
}
