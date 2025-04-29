package molinosllanoarroz;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.time.LocalDate;

public class RegistroVehiculo extends JFrame {
    private JTextField txtId;
    private JTextField txtConductor, txtCC, txtPlacas;
    private JComboBox<String> comboTipo;
    private JTextField txtTara, txtBruto;
    private JButton btnGuardar, btnReimprimir, btnVolver;

    public RegistroVehiculo() {
        super("Registro de Vehículos");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(400, 380);
        setLocationRelativeTo(null);
        initComponents();
    }

    private void initComponents() {
        JPanel p = new JPanel(new GridLayout(9, 2, 5, 5));
        p.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        p.add(new JLabel("ID Registro:"));
        txtId = new JTextField();
        txtId.setEnabled(false);
        p.add(txtId);

        p.add(new JLabel("Fecha:"));
        JTextField txtFecha = new JTextField(LocalDate.now().toString());
        txtFecha.setEnabled(false);
        p.add(txtFecha);

        p.add(new JLabel("Conductor:"));
        txtConductor = new JTextField();
        p.add(txtConductor);

        p.add(new JLabel("CC Conductor:"));
        txtCC = new JTextField();
        p.add(txtCC);

        p.add(new JLabel("Tipo Vehículo:"));
        comboTipo = new JComboBox<>(new String[]{"Turbo", "Mula"});
        p.add(comboTipo);

        p.add(new JLabel("Placas:"));
        txtPlacas = new JTextField();
        p.add(txtPlacas);

        p.add(new JLabel("Peso Tara (kg):"));
        txtTara = new JTextField();
        p.add(txtTara);

        p.add(new JLabel("Peso Bruto (kg):"));
        txtBruto = new JTextField();
        p.add(txtBruto);

        btnGuardar = new JButton("Guardar y Tiquete");
        btnReimprimir = new JButton("Reimprimir Tiquete");
        p.add(btnGuardar);
        p.add(btnReimprimir);

        add(p, BorderLayout.CENTER);

        JPanel south = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnVolver = new JButton("Volver");
        south.add(btnVolver);
        add(south, BorderLayout.SOUTH);

        btnGuardar.addActionListener(e -> guardarRegistro(txtFecha.getText()));
        btnReimprimir.addActionListener(e -> reimprimirTiquete());
        btnVolver.addActionListener(e -> {
            dispose();
            SwingUtilities.invokeLater(() -> new MenuPrincipal().setVisible(true));
        });
    }

    private void guardarRegistro(String fecha) {
        try (Connection c = DBConnection.getConnection()) {
            String sql = """
                INSERT INTO registro_vehiculo
                  (fecha,nombre_conductor,cc_conductor,tipo_vehiculo,placas,peso_tara,peso_bruto,peso_neto)
                VALUES(?,?,?,?,?,?,?,?)
            """;
            PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, fecha);
            ps.setString(2, txtConductor.getText());
            ps.setString(3, txtCC.getText());
            ps.setString(4, comboTipo.getSelectedItem().toString());
            ps.setString(5, txtPlacas.getText());
            double tara = Double.parseDouble(txtTara.getText());
            double bruto = Double.parseDouble(txtBruto.getText());
            ps.setDouble(6, tara);
            ps.setDouble(7, bruto);
            ps.setDouble(8, bruto - tara);
            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) txtId.setText(String.valueOf(keys.getInt(1)));

            JOptionPane.showMessageDialog(this,
                String.format("""
                    *** Tiquete ***
                    ID Registro: %s
                    Fecha: %s
                    Conductor: %s
                    CC: %s
                    Tipo: %s
                    Placas: %s
                    Tara: %.2f kg
                    Bruto: %.2f kg
                    Neto: %.2f kg
                    """,
                    txtId.getText(), fecha,
                    txtConductor.getText(), txtCC.getText(),
                    comboTipo.getSelectedItem(),
                    txtPlacas.getText(), tara, bruto, bruto - tara
                )
            );
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error BD: " + ex.getMessage(),
                                          "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void reimprimirTiquete() {
        
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
                JOptionPane.showMessageDialog(this,
                    String.format("""
                        *** Reimpresión Tiquete ***
                        ID Registro: %d
                        Fecha: %s
                        Conductor: %s
                        CC: %s
                        Tipo: %s
                        Placas: %s
                        Tara: %.2f kg
                        Bruto: %.2f kg
                        Neto: %.2f kg
                        """,
                        rs.getInt("id_registro"),
                        rs.getDate("fecha"),
                        rs.getString("nombre_conductor"),
                        rs.getString("cc_conductor"),
                        rs.getString("tipo_vehiculo"),
                        rs.getString("placas"),
                        tara, bruto, bruto - tara
                    )
                );
            } else {
                JOptionPane.showMessageDialog(this, "No se encontró registro.",
                                              "Atención", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error BD: " + ex.getMessage(),
                                          "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new RegistroVehiculo().setVisible(true));
    }
}
