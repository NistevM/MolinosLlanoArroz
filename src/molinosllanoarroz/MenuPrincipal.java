package molinosllanoarroz;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class MenuPrincipal extends JFrame {
    private JButton btnRegistro, btnReimprimir;

    public MenuPrincipal() {
        super("Molinos Llano Arroz: Menú Principal");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(500, 200); // Ventana más grande
        setLocationRelativeTo(null);
        initComponents();
    }

    private void initComponents() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 40));
        btnRegistro   = new JButton("Registro de Vehículos");
        btnReimprimir = new JButton("Reimprimir Tiquete");

        panel.add(btnRegistro);
        panel.add(btnReimprimir);

        add(panel);

        btnRegistro.addActionListener(e -> {
            dispose();
            new RegistroVehiculo().setVisible(true);
        });

        btnReimprimir.addActionListener(e -> {
            String criterio = JOptionPane.showInputDialog(this,
                "Ingrese ID registro, CC o placas:");
            if (criterio == null || criterio.isBlank()) return;

            String sql = """
                SELECT * FROM registro_vehiculo
                WHERE id_registro = ? OR cc_conductor = ? OR placas = ?
            """;

            try (Connection c = DBConnection.getConnection();
                 PreparedStatement ps = c.prepareStatement(sql)) {
                ps.setString(1, criterio);
                ps.setString(2, criterio);
                ps.setString(3, criterio);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    double tara = rs.getDouble("peso_tara");
                    double bruto = rs.getDouble("peso_bruto");

                    DatosTicket datos = new DatosTicket(
                        rs.getInt("id_registro"),
                        rs.getDate("fecha").toString(),
                        rs.getString("nombre_conductor"),
                        rs.getString("cc_conductor"),
                        rs.getString("tipo_vehiculo"),
                        rs.getString("placas"),
                        tara, bruto
                    );
                    new TicketPDF(datos).setVisible(true);

                } else {
                    JOptionPane.showMessageDialog(this, "No se encontró registro.",
                                                  "Atención", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this,
                    "Error BD: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MenuPrincipal().setVisible(true));
    }
}
