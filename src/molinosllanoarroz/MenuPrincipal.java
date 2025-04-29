package molinosllanoarroz;

import javax.swing.*;
import java.awt.*;

public class MenuPrincipal extends JFrame {
    private JButton btnRegistro, btnReimprimir;

    public MenuPrincipal() {
        super("Molinos Llano Arroz: Menú Principal");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        // Hacemos la ventana más grande
        setSize(500, 200);
        setLocationRelativeTo(null);
        initComponents();
    }

    private void initComponents() {
        // Panel con FlowLayout para los botones
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 40));

        btnRegistro    = new JButton("Registro de Vehículos");
        btnReimprimir  = new JButton("Reimprimir Tiquete");

        panel.add(btnRegistro);
        panel.add(btnReimprimir);

        add(panel);

        // Acción para abrir el formulario de registro
        btnRegistro.addActionListener(e -> {
            dispose();
            new RegistroVehiculo().setVisible(true);
        });

        // Acción para reimprimir directamente
        btnReimprimir.addActionListener(e -> {
            dispose();
            // Abrimos RegistroVehiculoFrame para aprovechar su lógica
            RegistroVehiculo frame = new RegistroVehiculo();
            frame.setVisible(true);
            // Llamamos al método de reimpresión
            frame.reimprimirTiquete();
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MenuPrincipal().setVisible(true));
    }
}
