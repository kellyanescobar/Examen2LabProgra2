/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package examen2labprogra2;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

/**
 *
 * @author laraj
 */
public class MainGUI extends JFrame {
    private PSNUsers psnUsers;

    public MainGUI(PSNUsers psnUsers) {
        super("PSN User Manager");
        this.psnUsers = psnUsers;

        setSize(420, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        UIManager.put("OptionPane.messageFont", new Font("Segoe UI", Font.PLAIN, 14));
        UIManager.put("OptionPane.buttonFont", new Font("Segoe UI", Font.PLAIN, 14));

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(new Color(245, 245, 245));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        JLabel title = new JLabel("Menu Principal");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        mainPanel.add(title);

        JButton addUserButton = createStyledButton("Agregar Usuario");
        JButton deactivateButton = createStyledButton("Desactivar Cuenta");
        JButton addTrophyButton = createStyledButton("Agregar Trofeo");
        JButton playerInfoButton = createStyledButton("Ver Informacion");
        JButton exitButton = createStyledButton("Salir");

        mainPanel.add(addUserButton);
        mainPanel.add(deactivateButton);
        mainPanel.add(addTrophyButton);
        mainPanel.add(playerInfoButton);
        mainPanel.add(exitButton);
        add(mainPanel);

        addUserButton.addActionListener(e -> {
            String username = JOptionPane.showInputDialog(this, "Ingrese nombre de usuario:");
            if (username != null && !username.isEmpty()) {
                try {
                    if (psnUsers.addUser(username)) {
                        JOptionPane.showMessageDialog(this, "Usuario agregado exitosamente!");
                    } else {
                        JOptionPane.showMessageDialog(this, "El nombre de usuario ya existe.");
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        deactivateButton.addActionListener(e -> {
            String username = JOptionPane.showInputDialog(this, "Ingrese el usuario que desea desactivar:");
            if (username != null && !username.isEmpty()) {
                try {
                    if (psnUsers.deactivateUser(username)) {
                        JOptionPane.showMessageDialog(this, "Usuario desactivado exitosamente.");
                    } else {
                        JOptionPane.showMessageDialog(this, "Usuario no encontrado.");
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        
        addTrophyButton.addActionListener(e -> {
        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.setPreferredSize(new Dimension(300, 150));
    
        JTextField userField = new JTextField();
        JTextField gameField = new JTextField();
        JTextField trophyNameField = new JTextField();
        JComboBox<Trophy> typeBox = new JComboBox<>(Trophy.values());

        panel.add(new JLabel("Usuario:"));
        panel.add(userField);
        panel.add(new JLabel("Juego:"));
        panel.add(gameField);
        panel.add(new JLabel("Nombre del Trofeo:"));
        panel.add(trophyNameField);
        panel.add(new JLabel("Tipo de Trofeo:"));
        panel.add(typeBox);

    int result = JOptionPane.showConfirmDialog(this, panel, "Agregar Trofeo", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

    if (result == JOptionPane.OK_OPTION) {
        String username = userField.getText().trim();
        String juego = gameField.getText().trim();
        String trofeo = trophyNameField.getText().trim();
        Trophy tipo = (Trophy) typeBox.getSelectedItem();

        if (username.isEmpty() || juego.isEmpty() || trofeo.isEmpty() || tipo == null) {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.");
            return;
        }

        try {
            if (psnUsers.addTrophieTo(username.toLowerCase(), juego, trofeo, tipo)) {
                JOptionPane.showMessageDialog(this, "Trofeo agregado exitosamente.");
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error al guardar el trofeo.");
            ex.printStackTrace();
        }
    }
});

        playerInfoButton.addActionListener(e -> {
            String username = JOptionPane.showInputDialog(this, "Ingrese el nombre de usuario:");
            if (username != null && !username.isEmpty()) {
                try {
                    String info = psnUsers.getPlayerInfo(username);
                    if (info != null) {
                        JTextArea textArea = new JTextArea(info);
                        textArea.setEditable(false);
                        textArea.setFont(new Font("Consolas", Font.PLAIN, 13));
                        JScrollPane scrollPane = new JScrollPane(textArea);
                        scrollPane.setPreferredSize(new Dimension(320, 200));
                        JOptionPane.showMessageDialog(this, scrollPane, "Informacion del Jugador", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(this, "Usuario no encontrado.");
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        exitButton.addActionListener(e -> System.exit(0));
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(250, 40));
        button.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        button.setFocusPainted(false);
        button.setBackground(new Color(220, 220, 220));
        button.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180)),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        return button;
    }

    public static void main(String[] args) {
        try {
            PSNUsers psnUsers = new PSNUsers("psn.txt", "trophies.txt");
            MainGUI mainMenu = new MainGUI(psnUsers);
            mainMenu.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
