package es.p2.ufv.front;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OptimizationService
{
    private final ProductService productService;

    public OptimizationService(ProductService productService)
    {
        this.productService = productService;
    }

    // Este método busca el supermercado más barato para una lista de productos.
    public String findCheapestSupermarket(List<Product> shoppingList, List<String> supermarkets)
    {
        String bestSupermarket = null;
        double minTotal = Double.MAX_VALUE;

        for (String supermarket : supermarkets)
        {
            double total = 0;
            boolean allAvailable = true;
            for (Product product : shoppingList)
            {
                // Suponemos que ProductService tiene un método para obtener un producto por nombre y supermercado
                Product p = productService.findProductByNameAndSupermarket(product.getName(), supermarket);
                if (p != null)
                {
                    total += p.getPrice() * product.getQuantity();
                }
                else
                {
                    allAvailable = false;
                    break;
                }
            }
            if (allAvailable && total < minTotal)
            {
                minTotal = total;
                bestSupermarket = supermarket;
            }
        }
        return bestSupermarket;
    }

    // Este método calcula el precio total optimizado para una lista de productos en varios supermercados.
    public double calculateOptimizedTotalPrice(List<Product> shoppingList, List<String> supermarkets)
    {
        double total = 0;
        for (Product product : shoppingList)
        {
            double bestPrice = Double.MAX_VALUE;
            for (String supermarket : supermarkets)
            {
                Product p = productService.findProductByNameAndSupermarket(product.getName(), supermarket);
                if (p != null && p.getPrice() < bestPrice)
                {
                    bestPrice = p.getPrice();
                }
            }
            if (bestPrice == Double.MAX_VALUE)
            {
                // Si algún producto no está disponible en ninguno, lo penalizamos
                total += 9999;
            }
            else
            {
                total += bestPrice * product.getQuantity();
            }
        }
        return total;
    }
}

