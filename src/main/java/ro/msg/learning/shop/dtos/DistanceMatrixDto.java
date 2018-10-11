package ro.msg.learning.shop.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DistanceMatrixDto {
    private List<String> destination_addresses;
    private List<String> origin_addresses;
    private List<Row> rows;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Row {
        List<Element> elements;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Element {
        Distance distance;
        Duration duration;
        Status status;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Distance {

        String text;
        Integer value;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Duration {
        String text;
        Integer value;
    }

    enum Status {
        OK, NOT_FOUND, ZERO_RESULTS, MAX_ROUTE_LENGTH, EXCEEDED
    }
}

