package ro.msg.learning.shop.converters;


import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import lombok.SneakyThrows;
import lombok.val;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractGenericHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.lang.Nullable;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;


/**
 * Converts POJOs to CSV and from CSV
 * message converters for CSV handling
 * @param <T> generic type parameter <T> = the type of the POJOs stored in the CSV
 */

public class CsvConverter<T> extends AbstractGenericHttpMessageConverter<List> {

    public CsvConverter() {
        super(new MediaType("text", "csv"));
    }


    @Override
    @SneakyThrows
    public boolean canRead(Type type, @Nullable Class<?> contextClass, @Nullable MediaType mediaType) {

        return mediaType != null && mediaType.getType().equalsIgnoreCase("text") && mediaType.getSubtype().equalsIgnoreCase("csv")
            && Class.forName(((ParameterizedTypeImpl) type).getRawType().getName()).equals(List.class);
    }


    @Override
    @SneakyThrows
    public boolean canWrite(@Nullable Type type, Class<?> clazz, @Nullable MediaType mediaType) {

        return mediaType != null && mediaType.getType().equalsIgnoreCase("text") && mediaType.getSubtype().equalsIgnoreCase("csv")
            && Class.forName(((ParameterizedTypeImpl) type).getRawType().getName()).equals(List.class);
    }


    @Override
    public List<T> readInternal(Class<? extends List> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {

        return null;
    }

    /**
     * Has a generic type parameter, <T> = the type of the POJOs stored in the CSV.
     * @param inputStream Has an input stream parameter.
     * @return a List<T>
     * @throws IOException
     */
    public static <T> List<T> fromCsv(Class<T> tClass, InputStream inputStream) throws IOException {
        CsvMapper mapper = new CsvMapper();
        CsvSchema schema = CsvSchema.emptySchema().withHeader();

        MappingIterator<T> iterator = mapper.readerFor(tClass).with(schema).readValues(inputStream);


        return iterator.readAll();
    }

    /**
     * Has a generic type parameter <T> = the type of the POJOs stored in the CSV
     * @param tClass
     * @param tList the list of POJOs to be written in the CSV
     * @throws IOException
     */
    public static <T> void toCsv(Class<T> tClass, List<T> tList, OutputStream outputStream) throws IOException {

        val mapper = new CsvMapper();
        val schema = mapper.schemaFor(tClass).withHeader().withColumnReordering(false);

        val writer = mapper.writerFor(tClass).with(schema);
        writer.writeValuesAsArray(outputStream).writeAll(tList);
        outputStream.close();
    }


    @Override
    protected void writeInternal(List list, Type type, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {

        ParameterizedType pType = (ParameterizedType) type;
        Class<T> clazz = (Class<T>) pType.getActualTypeArguments()[0];
        toCsv(clazz, list, outputMessage.getBody());
    }


    @Override
    public List read(Type type, Class<?> contextClass, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
        ParameterizedType pType = (ParameterizedType) type;
        Class<T> clazz = (Class<T>) pType.getActualTypeArguments()[0];
        return fromCsv(clazz, inputMessage.getBody());
    }


}
