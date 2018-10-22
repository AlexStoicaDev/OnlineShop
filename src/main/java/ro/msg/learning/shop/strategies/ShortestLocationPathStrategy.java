package ro.msg.learning.shop.strategies;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;
import ro.msg.learning.shop.dtos.orders.OrderDtoIn;
import ro.msg.learning.shop.entities.Location;
import ro.msg.learning.shop.entities.Product;
import ro.msg.learning.shop.entities.Stock;
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

    //for each vertex v≠s predecessors[v] is the penultimate vertex in the shortest path from start to v.
    private Node[] predecessors;
    //where for each vertex v we store the current length of the shortest path from s to v in length[v]
    private int[] length;

    private int[] tempPathQuantities;
    private int tempPathDist;
    private int solutionPathDist = Integer.MAX_VALUE;

    private List<StockLocationQuantityWrapper> tempPath = new ArrayList<>();
    private List<StockLocationQuantityWrapper> solutionPath = new ArrayList<>();

    @Override
    public List<StockQuantityProductWrapper> getStockQuantityProductWrapper(OrderDtoIn orderDtoIn) {
        List<StockQuantityProductWrapper> stockQuantityProductWrappers = new ArrayList<>();
        final val stockLocationQuantityWrapper1 = getStockLocationQuantityWrapper(orderDtoIn);
        stockLocationQuantityWrapper1.remove(stockLocationQuantityWrapper1.size() - 1);

        stockLocationQuantityWrapper1.forEach(stockLocationQuantityWrapper ->
            stockQuantityProductWrappers.addAll(stockLocationQuantityWrapper.getStockQuantityProductWrappers()));
        return stockQuantityProductWrappers;
    }


    // returns a list of objects that contain all the stocks, their location and the quantity that will be taken from the stock
    private List<StockLocationQuantityWrapper> getStockLocationQuantityWrapper(OrderDtoIn orderDtoIn) {

        final val listOfListsThatContainTheStocksForEveryProduct = getListsOfStocksForAllProducts(orderDtoIn);
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

    /*
    returns all the stocks that have the products in the order
     */
    private List<List<Stock>> getListsOfStocksForAllProducts(OrderDtoIn orderDtoIn) {
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


    private List<StockLocationQuantityWrapper> dijkstra(List<Node> nodes, int[][] distancesMatrix, List<Integer> quantitiesRequiredForEachProductInOrder) {
        //stores for each vertex v whether it's marked. Initially all vertices are unmarked
        List<Node> marked = new ArrayList<>();

        int n = nodes.size();

        createDistanceAndPredecessorsVectors(distancesMatrix, nodes);
        marked.add(nodes.get(0));

        int ok = 1;
        int k = -1;
        while (ok == 1) {
            int min = Integer.MAX_VALUE;
            for (int i = 0; i < n; i++) {
                if (!marked.contains(nodes.get(i)) && min > length[i]) {
                    min = length[i];
                    k = i;
                }
            }

            if (min != Integer.MAX_VALUE) {
                marked.add(nodes.get(k));
                for (int i = 0; i < n; i++) {
                    if (!marked.contains(nodes.get(i)) && length[i] > length[k] + distancesMatrix[k][i]) {
                        length[i] = length[k] + distancesMatrix[k][i];
                        predecessors
                            [i] = nodes.get(k);
                    }
                }
            } else ok = 0;

        }


        return findShortestPath(predecessors
            , length, quantitiesRequiredForEachProductInOrder, nodes);
    }


    private void createDistanceAndPredecessorsVectors(int[][] distancesMatrix, List<Node> nodes) {
        int n = nodes.size();
        predecessors
            = new Node[n];
        length = new int[n];

        for (int i = 0; i < n; i++) {
            length[i] = distancesMatrix[i][0];
            predecessors
                [i] = nodes.get(0);
        }

    }


    private List<StockLocationQuantityWrapper> findShortestPath(Node[] predecessors
        , int[] length, List<Integer> quantitiesRequiredForEachProductInOrder, List<Node> nodes) {


        final val stockLocationQuantityWrapperForDestination = new StockLocationQuantityWrapper();
        Node node = nodes.get(0);
        stockLocationQuantityWrapperForDestination.setLocationName(node.getLocationName());
        stockLocationQuantityWrapperForDestination.setLocationId(node.getLocationId());


        for (int i = 1; i < predecessors
            .length; i++) {
            if (predecessors[i] == predecessors[0]) {
                tempPath.clear();
                tempPathQuantities = new int[quantitiesRequiredForEachProductInOrder.size()];
                for (int j = 0; j < quantitiesRequiredForEachProductInOrder.size(); j++) {
                    tempPathQuantities[j] = 0;
                }
                tempPath.add(stockLocationQuantityWrapperForDestination);
                tempPathDist = 0;

                final val path = findPath(nodes, predecessors
                    , length, quantitiesRequiredForEachProductInOrder, nodes.get(i), 0);
                if (path && tempPathDist < solutionPathDist) {
                    solutionPath.clear();
                    solutionPathDist = tempPathDist;
                    solutionPath.addAll(tempPath);

                }
            }


        }

        return solutionPath;
    }

    private boolean findPath(List<Node> nodes, Node[] predecessors
        , int[] length, List<Integer> quantitiesRequiredForEachProductInOrder, Node node, int ok) {

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
        tempPathDist += length[nodes.indexOf(node)];
        if (ok == 3) {
            return true;
        } else {

            for (int i = 1; i < predecessors
                .length; i++) {
                if (predecessors
                    [i].equals(node)) {
                    return findPath(nodes, predecessors
                        , length, quantitiesRequiredForEachProductInOrder, nodes.get(i), ok);
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


