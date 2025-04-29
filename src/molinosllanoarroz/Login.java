package molinosllanoarroz;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class Login extends JFrame {
    private JTextField  txtUser;
    private JPasswordField txtPass;
    private JButton     btnLogin;

    public Login() {
        super("SGI – Molinos Llano Arroz: Login");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(300,180);
        setLocationRelativeTo(null);
        initComponents();
    }

    private void initComponents() {
        JPanel p = new JPanel(new GridLayout(3,2,5,5));
        p.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        p.add(new JLabel("Usuario:"));
        txtUser = new JTextField(); p.add(txtUser);
        p.add(new JLabel("Contraseña:"));
        txtPass = new JPasswordField(); p.add(txtPass);
        btnLogin = new JButton("Entrar"); p.add(new JLabel());
        p.add(btnLogin);
        add(p);

        btnLogin.addActionListener(e -> {
            try (Connection c = DBConnection.getConnection();
                 PreparedStatement ps = c.prepareStatement(
                   "SELECT * FROM usuario WHERE nombre = ? AND contrasena = ?"
                 )) {
                ps.setString(1, txtUser.getText());
                ps.setString(2, new String(txtPass.getPassword()));
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    dispose();
                    new MenuPrincipal().setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(this,"Credenciales inválidas");
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this,"Error BD: "+ex.getMessage());
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Login().setVisible(true));
    }
}
