package es.p2.ufv.econoList.Servicio.Finders;

import java.util.List;
import es.p2.ufv.econoList.Modelo.Product;
import es.p2.ufv.econoList.Modelo.Market;

public interface Finder
{
    public List<Product> findProductsByTerm(String term);
    public Market getMarket();
}
