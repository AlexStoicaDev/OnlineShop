package ro.msg.learning.shop.strategies;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;
import ro.msg.learning.shop.dtos.DistanceMatrixDto;
import ro.msg.learning.shop.dtos.OrderDetailDto;
import ro.msg.learning.shop.dtos.orders.OrderDtoIn;
import ro.msg.learning.shop.entities.Location;
import ro.msg.learning.shop.entities.Product;
import ro.msg.learning.shop.entities.Stock;
import ro.msg.learning.shop.entities.embeddables.Address;
import ro.msg.learning.shop.repositories.LocationRepository;
import ro.msg.learning.shop.repositories.StockRepository;
import ro.msg.learning.shop.utils.DistanceMatrixUtil;
import ro.msg.learning.shop.wrappers.StockLocationQuantityWrapper;
import ro.msg.learning.shop.wrappers.StockQuantityProductWrapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class ShortestLocationPathStrategy implements LocationStrategy {
    private final StockRepository stockRepository;
    private final RestTemplate restTemplate;
    private final LocationRepository locationRepository;

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

    //------------

    public List<StockLocationQuantityWrapper> test(OrderDtoIn orderDtoIn) {

        final val listOfListsThatContainTheStocksForEveryProduct = getListsOfStocksForAllProduts(orderDtoIn);
        List<Node> locationsAsNodesList = createNodes(listOfListsThatContainTheStocksForEveryProduct, orderDtoIn);
        List<Integer> quantitiesRequiredForEachProductInOrder = getQuantitiesForEachProduct(orderDtoIn);
        List<Location> locations = getLocations(locationsAsNodesList, orderDtoIn);
        final val distancesBetweenEachLocationMatrix = DistanceMatrixUtil.getDistancesMatrix(locations, apiKey, restTemplate);

        final val solution = dijkstra(locationsAsNodesList, distancesBetweenEachLocationMatrix, quantitiesRequiredForEachProductInOrder);
        Collections.reverse(solution);

        return solution;
    }

    private List<Integer> getQuantitiesForEachProduct(OrderDtoIn orderDtoIn) {
        List<Integer> quantities = new ArrayList<>();
        orderDtoIn.getOrderDetails().forEach(orderDetailDto -> quantities.add(orderDetailDto.getQuantity()));
        return quantities;
    }

    private List<List<Stock>> getListsOfStocksForAllProduts(OrderDtoIn orderDtoIn) {
        return orderDtoIn.getOrderDetails().parallelStream().map(orderDetailDto -> {
                Product product = new Product();
                product.setId(orderDetailDto.getProductId());
                return stockRepository.findAllByProductAndQuantityGreaterThan(product, 0);
            }
        ).collect(Collectors.toList());
    }


    private List<Location> getLocations(List<Node> nodes, OrderDtoIn orderDtoIn) {
        List<Location> locations = new ArrayList<>();
        Location location = new Location();
        location.setAddress(orderDtoIn.getAddress());
        locations.add(location);

        nodes.stream().skip(1).
            forEach(node -> locations.add(locationRepository.findById(node.getLocationId()).get()));
        return locations;
    }


    private Node[] tata;
    private int[] d;

    private List<StockLocationQuantityWrapper> dijkstra(List<Node> nodes, int[][] distancesMatrix, List<Integer> quantitiesRequiredForEachProductInOrder) {
        List<Node> vizitat = new ArrayList<>();
        int n = nodes.size();
        createDistanceAndTataVectors(distancesMatrix, nodes);
        vizitat.add(nodes.get(0));

        int ok = 1;
        int k = -1;
        while (ok == 1) {
            int min = Integer.MAX_VALUE;
            for (int i = 0; i < n; i++) {
                if (!vizitat.contains(nodes.get(i)) && min > d[i]) {
                    min = d[i];
                    k = i;
                }
            }

            if (min != Integer.MAX_VALUE) {
                vizitat.add(nodes.get(k));
                for (int i = 0; i < n; i++) {
                    if (!vizitat.contains(nodes.get(i)) && d[i] > d[k] + distancesMatrix[k][i]) {
                        d[i] = d[k] + distancesMatrix[k][i];
                        tata[i] = nodes.get(k);
                    }
                }
            } else ok = 0;

        }


        return findShortestPath(tata, d, quantitiesRequiredForEachProductInOrder, nodes);
    }

    
    private void createDistanceAndTataVectors(int[][] distancesMatrix, List<Node> nodes) {
        int n = nodes.size();
        tata = new Node[n];
        d = new int[n];

        for (int i = 0; i < n; i++) {
            d[i] = distancesMatrix[i][0];
            tata[i] = nodes.get(0);
        }

    }

    private List<StockLocationQuantityWrapper> tempPath = new ArrayList<>();
    private List<StockLocationQuantityWrapper> solutionPath = new ArrayList<>();

    private int[] tempPathQuantities;


    private int tempPathDist;
    private int solutionPathDist = Integer.MAX_VALUE;

    private List<StockLocationQuantityWrapper> findShortestPath(Node[] tata, int[] d, List<Integer> quantitiesRequiredForEachProductInOrder, List<Node> nodes) {


        final val stockLocationQuantityWrapperForDestination = new StockLocationQuantityWrapper();
        Node node = nodes.get(0);
        stockLocationQuantityWrapperForDestination.setLocationName(node.getLocationName());
        stockLocationQuantityWrapperForDestination.setLocationId(node.getLocationId());


        for (int i = 1; i < tata.length; i++) {
            if (tata[i] == tata[0]) {
                tempPath.clear();
                tempPathQuantities = new int[quantitiesRequiredForEachProductInOrder.size()];
                for (int j = 0; j < quantitiesRequiredForEachProductInOrder.size(); j++) {
                    tempPathQuantities[j] = 0;
                }
                tempPath.add(stockLocationQuantityWrapperForDestination);
                tempPathDist = 0;

                final val path = findPath(nodes, tata, d, quantitiesRequiredForEachProductInOrder, nodes.get(i), 0);
                if (path && tempPathDist < solutionPathDist) {
                    solutionPath.clear();
                    solutionPathDist = tempPathDist;
                    solutionPath.addAll(tempPath);

                }
            }


        }

        return solutionPath;
    }

    private boolean findPath(List<Node> nodes, Node[] tata, int[] d, List<Integer> quantitiesRequiredForEachProductInOrder, Node node, int ok) {

        StockLocationQuantityWrapper stockLocationQuantityWrapper = new StockLocationQuantityWrapper();
        stockLocationQuantityWrapper.setLocationName(node.getLocationName());
        stockLocationQuantityWrapper.setLocationId(node.getLocationId());
        final val stocksFromOrder = node.getStocksFromOrder();
        List<StockQuantityProductWrapper> stockQuantityProductWrappers = new ArrayList<>();

        for (int i = 0; i < stocksFromOrder.size(); i++) {

            final val stock = stocksFromOrder.get(i);
            int x = node.getStockNumber().get(i);

            if (tempPathQuantities[x] != quantitiesRequiredForEachProductInOrder.get(x)) {


                if (quantitiesRequiredForEachProductInOrder.get(x) - tempPathQuantities[x] <= stock.getQuantity()) {
                    ok++;
                    stockQuantityProductWrappers.add(new StockQuantityProductWrapper(stock, quantitiesRequiredForEachProductInOrder.get(x) - tempPathQuantities[x], stock.getProduct().getId()));
                    tempPathQuantities[x] = quantitiesRequiredForEachProductInOrder.get(x);

                } else {
                    tempPathQuantities[x] += stock.getQuantity();
                    stockQuantityProductWrappers.add(new StockQuantityProductWrapper(stock, stock.getQuantity(), stock.getProduct().getId()));
                }
            }

        }
        stockLocationQuantityWrapper.setStockQuantityProductWrappers(stockQuantityProductWrappers);
        tempPath.add(stockLocationQuantityWrapper);
        tempPathDist += d[nodes.indexOf(node)];
        if (ok == 3) {
            return true;
        } else {

            for (int i = 1; i < tata.length; i++) {
                if (tata[i].equals(node)) {
                    return findPath(nodes, tata, d, quantitiesRequiredForEachProductInOrder, nodes.get(i), ok);
                }
            }

        }
        return false;
    }


    private List<Node> createNodes(List<List<Stock>> lists, OrderDtoIn orderDtoIn) {
        List<Node> nodes = new ArrayList<>();
        Node destinationNode = new Node();
        destinationNode.setLocationName(orderDtoIn.getAddress().getCity());
        nodes.add(destinationNode);

        int stockNumber = 0;

        for (List<Stock> stocks : lists) {
            for (Stock stock : stocks) {
                addNode(stock, stockNumber, nodes);
            }
            stockNumber++;
        }

        return nodes;
    }

    private void addNode(Stock stock, int stockNumber, List<Node> nodes) {

        boolean nodeIsPresent = false;
        for (Node node : nodes) {
            if (node.getLocationId() == stock.getLocation().getId()) {
                node.getStocksFromOrder().add(stock);
                node.getStockNumber().add(stockNumber);
                nodeIsPresent = true;
                break;
            }
        }
        if (!nodeIsPresent) {
            List<Stock> stocks = new ArrayList<>();
            stocks.add(stock);
            List<Integer> stockNumbers = new ArrayList<>();
            stockNumbers.add(stockNumber);
            nodes.add(new Node(stock.getLocation().getId(), stock.getLocation().getAddress().getCity(), stocks, stockNumbers));
        }

    }

}


