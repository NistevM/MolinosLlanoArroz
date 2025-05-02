package molinosllanoarroz;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;


public class TicketPDF extends JFrame {
    public TicketPDF(DatosTicket data) {
        super("Registro de vehículo: Tiquete " + data.id);
        setSize(400, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // Área de texto con la información del tiquete
        JTextArea area = new JTextArea(data.format());
        area.setEditable(false);
        area.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scroll = new JScrollPane(area);

        // Botón Generar PDF
        JButton btnPdf = new JButton("Generar PDF");
        btnPdf.addActionListener(e -> generarPdf(data));

        // Layout
        add(scroll, BorderLayout.CENTER);
        JPanel south = new JPanel(new FlowLayout(FlowLayout.CENTER));
        south.add(btnPdf);
        add(south, BorderLayout.SOUTH);
    }

    private void generarPdf(DatosTicket data) {
        JFileChooser fc = new JFileChooser();
        fc.setFileFilter(new FileNameExtensionFilter("Archivos PDF", "pdf"));
        if (fc.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) return;

        File file = fc.getSelectedFile();
        if (!file.getName().toLowerCase().endsWith(".pdf")) {
            file = new File(file.getParentFile(), file.getName() + ".pdf");
        }

        try (PDDocument doc = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.LETTER);
            doc.addPage(page);

            try (PDPageContentStream cs = new PDPageContentStream(doc, page)) {
                cs.beginText();
                // Título
                cs.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 18);
                cs.newLineAtOffset(50, 750);
                cs.showText("Molinos Llano Arroz");
                // Subtítulo
                cs.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 14);
                cs.newLineAtOffset(0, -24);
                cs.showText("Registro de Vehículo");
                // Espacio antes de datos
                cs.newLineAtOffset(0, -30);
                // Cuerpo
                cs.setFont(new PDType1Font(Standard14Fonts.FontName.COURIER), 12);
                for (String line : data.formatLines()) {
                    cs.showText(line);
                    cs.newLineAtOffset(0, -16);
                }
                cs.endText();
            }

            doc.save(file);
            JOptionPane.showMessageDialog(this,
                "PDF generado correctamente:\n" + file.getAbsolutePath(),
                "Éxito", JOptionPane.INFORMATION_MESSAGE);

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this,
                "Error generando PDF:\n" + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
