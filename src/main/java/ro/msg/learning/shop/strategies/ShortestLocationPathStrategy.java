package ro.msg.learning.shop.strategies;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;
import ro.msg.learning.shop.dtos.DistanceMatrixDto;
import ro.msg.learning.shop.dtos.OrderDetailDto;
import ro.msg.learning.shop.entities.Product;
import ro.msg.learning.shop.entities.Stock;
import ro.msg.learning.shop.entities.embeddables.Address;
import ro.msg.learning.shop.repositories.StockRepository;
import ro.msg.learning.shop.utils.DistanceMatrixUtil;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@RequiredArgsConstructor
public class ShortestLocationPathStrategy implements LocationStrategy {

    private final StockRepository stockRepository;
    private final RestTemplate restTemplate;

    @Value("${online-shop.api-key}")
    private final String apiKey;


    public DistanceMatrixDto distanceMatrix(OrderDetailDto orderDetailDto, Address address) {

        Product product = new Product();
        product.setId(orderDetailDto.getProductId());
        return DistanceMatrixUtil.getDistanceMatrixDto(stockRepository.findAllByProductAndQuantityGreaterThan(product, 0),
            address, apiKey, restTemplate);
    }

    @Override
    public Stock getStockForProduct(OrderDetailDto orderDetailDto, Address address) {
        return null;
    }


    private List<Stock> utilizat = new ArrayList<>();
    private List<Stock> vizitat = new ArrayList<>();
    private List<Stock> solutionPath = new ArrayList<>();
    private List<Stock> tempPath = new ArrayList<>();
    private int[][] distances;
    private int solutionPathTotalDistance = Integer.MAX_VALUE;

    private int finalQuantity;
    private List<Stock> stocks;

    public List<Stock> test(OrderDetailDto orderDetailDto, Address address) {
        Product product = new Product();
        product.setId(orderDetailDto.getProductId());

        utilizat.clear();
        vizitat.clear();
        tempPath.clear();
        solutionPath.clear();

        //all stocks that have the product
        stocks = stockRepository.findAllByProductAndQuantityGreaterThan(product, 0);
        //distance matrix between al locations that have the product and destination
        distances = DistanceMatrixUtil.getDistancesMatrix(stocks, address, apiKey, restTemplate);
        finalQuantity = orderDetailDto.getQuantity();

        stocks.forEach(stock -> {

            vizitat.add(stock);
            utilizat.add(stock);
            tempPath.add(stock);
            back(stock.getQuantity());

            vizitat.clear();
            tempPath.clear();

        });

        return solutionPath;
    }


    private void back(int currentQuantity) {
        if (currentQuantity >= finalQuantity) {
            if (solutionPath.isEmpty() || isBetterPath()) {
                copyTempPathToSolution();

            }
        } else {
            if (vizitat.size() != stocks.size()) {
                for (Stock stock1 : stocks) {
                    if (!utilizat.contains(stock1) && !vizitat.contains(stock1)) {
                        vizitat.add(stock1);
                        tempPath.add(stock1);
                        back(currentQuantity + stock1.getQuantity());
                        vizitat.remove(stock1);
                        tempPath.remove(stock1);
                    }
                }
            }
        }
    }

    private void copyTempPathToSolution() {
        solutionPathTotalDistance = calculateTotalDistanceForTempPath();
        solutionPath.clear();
        solutionPath.addAll(tempPath);
    }

    private void sortTempPath() {
        Comparator<Stock> comparator = Comparator.comparingInt(stock -> distances[stocks.indexOf(stock)][stocks.size()]);
        tempPath.sort(comparator);
    }

    private boolean isBetterPath() {

        sortTempPath();
        return solutionPathTotalDistance > calculateTotalDistanceForTempPath();

    }

    private int calculateTotalDistanceForTempPath() {
        int totalDistance = distances[stocks.indexOf(tempPath.get(0))][stocks.size()];
        int i;
        for (i = 1; i < tempPath.size(); i++) {

            totalDistance += distances[stocks.indexOf(tempPath.get(i - 1))][stocks.indexOf(tempPath.get(i))];
        }
        return totalDistance;
    }


}


