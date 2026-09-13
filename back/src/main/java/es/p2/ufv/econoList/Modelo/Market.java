package es.p2.ufv.econoList.Modelo;

public enum Market
{
    CARREFOUR("Carrefour"),
    ALIMERKA("Alimerka"),
    MASYMAS("Masymas"),
    DIA("Dia"),
    HIPERCOR("Hipercor"),
    EROSKI("Eroski"),
    MERCADONA("Mercadona"),
    ALDI("Aldi"),
    GADIS("Gadis"),
    CONSUM("Consum"),
    ALCAMPO("Alcampo");

    private final String name;

    Market(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}