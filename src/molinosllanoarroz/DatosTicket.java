package molinosllanoarroz;

import java.util.List;
import java.util.Arrays;

public class DatosTicket {
    public final int    id;
    public final String fecha, conductor, cc, tipo, placas;
    public final double tara, bruto, neto;

    public DatosTicket(int id, String fecha, String conductor, String cc,
                      String tipo, String placas, double tara, double bruto) {
        this.id        = id;
        this.fecha     = fecha;
        this.conductor = conductor;
        this.cc        = cc;
        this.tipo      = tipo;
        this.placas    = placas;
        this.tara      = tara;
        this.bruto     = bruto;
        this.neto      = bruto - tara;
    }

    /** Devuelve una lista de líneas para iterar al generar el PDF. */
    public List<String> formatLines() {
        return Arrays.asList(
            String.format("ID Registro: %d", id),
            "Fecha: "     + fecha,
            "Conductor: " + conductor,
            "CC: "        + cc,
            "Tipo: "      + tipo,
            "Placas: "    + placas,
            String.format("Peso Tara: %.2f kg", tara),
            String.format("Peso Bruto: %.2f kg", bruto),
            String.format("Peso Neto: %.2f kg", neto)
        );
    }

    /** Cadena para mostrar en el JTextArea. */
    public String format() {
        StringBuilder sb = new StringBuilder();
        for (var line : formatLines()) {
            sb.append(line).append("\n");
        }
        return sb.toString();
    }
}
