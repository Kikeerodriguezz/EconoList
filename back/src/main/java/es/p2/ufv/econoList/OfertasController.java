package es.p2.ufv.econoList;

import es.p2.ufv.econoList.Modelo.Product;
import es.p2.ufv.econoList.Servicio.Finders.Finder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/offers")
@CrossOrigin(origins = "*")
public class OfertasController // Cambia la ruta base a /api/offers
{
    @Autowired
    private List<Finder> finders; // Spring inyecta todos los @Service que implementan Finder

    @GetMapping("/all")
    public List<Product> getAllOffers()
    {
        List<Product> allProducts = new ArrayList<>();
        for (Finder finder : finders)
        {
            try
            {
                allProducts.addAll(finder.findProductsByTerm("")); // Este método viene de Abstracto
            }
            catch (Exception e)
            {
                System.err.println("Error al consultar ofertas de " + finder.getMarket());
            }
        }
        return allProducts;
    }
}