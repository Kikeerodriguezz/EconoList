package es.p2.ufv.econoList;

import es.p2.ufv.econoList.Modelo.Market;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MarketTest
{
    @Test
    void testGetName() {
        assertEquals("Carrefour", Market.CARREFOUR.getName());
        assertEquals("Alimerka", Market.ALIMERKA.getName());
        assertEquals("Masymas", Market.MASYMAS.getName());
        assertEquals("Dia", Market.DIA.getName());
        assertEquals("Hipercor", Market.HIPERCOR.getName());
        assertEquals("Eroski", Market.EROSKI.getName());
        assertEquals("Mercadona", Market.MERCADONA.getName());
        assertEquals("Aldi", Market.ALDI.getName());
        assertEquals("Gadis", Market.GADIS.getName());
        assertEquals("Consum", Market.CONSUM.getName());
        assertEquals("Alcampo", Market.ALCAMPO.getName());
    }
}
