package co.edu.upn.arena;

public class Partido {
    private int id;
    private int equipoLocalId;
    private int equipoVisitanteId;
    private int puntosLocal;
    private int puntosVisitante;
    private String fecha;

    public Partido(int id, int equipoLocalId, int equipoVisitanteId, int puntosLocal, int puntosVisitante, String fecha) {
        this.id = id;
        this.equipoLocalId = equipoLocalId;
        this.equipoVisitanteId = equipoVisitanteId;
        this.puntosLocal = puntosLocal;
        this.puntosVisitante = puntosVisitante;
        this.fecha = fecha;
    }

    public int getId() { return id; }
    public int getEquipoLocalId() { return equipoLocalId; }
    public int getEquipoVisitanteId() { return equipoVisitanteId; }
    public int getPuntosLocal() { return puntosLocal; }
    public int getPuntosVisitante() { return puntosVisitante; }
    public String getFecha() { return fecha; }

    public void setPuntosLocal(int puntosLocal) { this.puntosLocal = puntosLocal; }
    public void setPuntosVisitante(int puntosVisitante) { this.puntosVisitante = puntosVisitante; }
    public void setFecha(String fecha) { this.fecha = fecha; }
}