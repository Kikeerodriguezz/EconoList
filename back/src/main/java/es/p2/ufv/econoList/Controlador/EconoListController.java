package es.p2.ufv.econoList.Controlador;

import es.p2.ufv.econoList.Modelo.Market;
import es.p2.ufv.econoList.Modelo.Product;
import es.p2.ufv.econoList.Servicio.Finders.Finder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/find")
public class EconoListController
{
    @Autowired
    private List<Finder> marketFinderList;
    @CrossOrigin(origins = "*", maxAge = 3600)
    @GetMapping
    public List<Product> findByTerm(@RequestParam(required = true) String term, Market[] markets)
    {
        final List<Product> productList = new ArrayList<Product>();

        for (Finder finder : marketFinderList)
        {
            if(markets == null || Arrays.stream(markets).anyMatch(finder.getMarket()::equals))
            {
                productList.addAll(finder.findProductsByTerm(term));
            }
        }
        List<Product> finalList = new ArrayList<Product>();
        term = term.trim();
        term = StringUtils.stripAccents(term);
        String[] termSplit = term.toLowerCase().split(" ");
        for (Product p : productList)
        {
            if (Arrays.stream(termSplit).allMatch(StringUtils.stripAccents(p.getName().toLowerCase())::contains))
            {
                finalList.add(p);
            }
        }
        Collections.sort(finalList);
        return finalList;
    }
}