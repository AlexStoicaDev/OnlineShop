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
import ro.msg.learning.shop.wrappers.StockLocationQauntityWrapper;
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


    public List<StockLocationQauntityWrapper> test(OrderDtoIn orderDtoIn) {

        final val listOfListsThatContainTheStocksForEveryProduct = orderDtoIn.getOrderDetails().parallelStream().map(orderDetailDto -> {
                Product product = new Product();
                product.setId(orderDetailDto.getProductId());
                return stockRepository.findAllByProductAndQuantityGreaterThan(product, 0);
            }
        ).collect(Collectors.toList());

        Node destinationNode = new Node();
        destinationNode.setLocationName(orderDtoIn.getAddress().getCity());

        List<Integer> quanties = new ArrayList<>();
        orderDtoIn.getOrderDetails().forEach(orderDetailDto -> quanties.add(orderDetailDto.getQuantity()));


        List<Node> nodes = createNodes(listOfListsThatContainTheStocksForEveryProduct, destinationNode);
        //throw exception if location is not found!

        List<Location> locations = getLocations(nodes, orderDtoIn);
        final val distancesMatrix = DistanceMatrixUtil.getDistancesMatrix(locations, apiKey, restTemplate);


        final val dijkstra = dijkstra(nodes, distancesMatrix, quanties);
        Collections.reverse(dijkstra);
        return dijkstra;
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

    private List<StockLocationQauntityWrapper> dijkstra(List<Node> nodes, int[][] distancesMatrix, List<Integer> quanties) {
        List<Node> vizitat = new ArrayList<>();
        int n = nodes.size();
        Node[] tata = new Node[n];
        int[] d = new int[n];

        for (int i = 0; i < n; i++) {
            d[i] = distancesMatrix[i][0];
            tata[i] = nodes.get(0);
        }
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


        return findShortestPath(tata, d, quanties, nodes);
    }

    List<StockLocationQauntityWrapper> tempPath = new ArrayList<>();
    List<StockLocationQauntityWrapper> solutionPath = new ArrayList<>();

    private int[] tempPathQuantities;


    private int tempPathDist;
    private int solutionPathDist = Integer.MAX_VALUE;

    private List<StockLocationQauntityWrapper> findShortestPath(Node[] tata, int[] d, List<Integer> quanties, List<Node> nodes) {


        final val stockLocationQauntityWrapperForDestination = new StockLocationQauntityWrapper();
        Node node = nodes.get(0);
        stockLocationQauntityWrapperForDestination.setLocationName(node.getLocationName());
        stockLocationQauntityWrapperForDestination.setLocationId(node.getLocationId());


        for (int i = 1; i < tata.length; i++) {
            if (tata[i] == tata[0]) {
                tempPath.clear();
                tempPathQuantities = new int[quanties.size()];
                for (int j = 0; j < quanties.size(); j++) {
                    tempPathQuantities[j] = 0;
                }
                tempPath.add(stockLocationQauntityWrapperForDestination);
                tempPathDist = 0;

                final val path = findPath(nodes, tata, d, quanties, nodes.get(i), 0);
                if (path && tempPathDist < solutionPathDist) {
                    solutionPath.clear();
                    solutionPathDist = tempPathDist;
                    solutionPath.addAll(tempPath);

                }
            }


        }

        return solutionPath;
    }

    private boolean findPath(List<Node> nodes, Node[] tata, int[] d, List<Integer> quanties, Node node, int ok) {

        StockLocationQauntityWrapper stockLocationQauntityWrapper = new StockLocationQauntityWrapper();
        stockLocationQauntityWrapper.setLocationName(node.getLocationName());
        stockLocationQauntityWrapper.setLocationId(node.getLocationId());
        final val stocksFromOrder = node.getStocksFromOrder();
        List<StockQuantityProductWrapper> stockQuantityProductWrappers = new ArrayList<>();

        for (int i = 0; i < stocksFromOrder.size(); i++) {

            final val stock = stocksFromOrder.get(i);
            int x = node.getStockNumber().get(i);

            if (tempPathQuantities[x] != quanties.get(x)) {


                if (quanties.get(x) - tempPathQuantities[x] <= stock.getQuantity()) {
                    ok++;
                    stockQuantityProductWrappers.add(new StockQuantityProductWrapper(stock, quanties.get(x) - tempPathQuantities[x], stock.getProduct().getId()));
                    tempPathQuantities[x] = quanties.get(x);

                } else {
                    tempPathQuantities[x] += stock.getQuantity();
                    stockQuantityProductWrappers.add(new StockQuantityProductWrapper(stock, stock.getQuantity(), stock.getProduct().getId()));
                }
            }

        }
        stockLocationQauntityWrapper.setStockQuantityProductWrappers(stockQuantityProductWrappers);
        tempPath.add(stockLocationQauntityWrapper);
        //aici poate o sa ai probleme!!!
        tempPathDist += d[nodes.indexOf(node)];
        if (ok == 3) {
            return true;
        } else {

            for (int i = 1; i < tata.length; i++) {
                if (tata[i].equals(node)) {
                    return findPath(nodes, tata, d, quanties, nodes.get(i), ok);
                }
            }

        }
        return false;
    }


    private List<Node> createNodes(List<List<Stock>> lists, Node node) {
        List<Node> nodes = new ArrayList<>();
        nodes.add(node);

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


