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
     * @param tClass
     * @param inputMessage
     * @return
     * @throws IOException
     */
    //in str
    public static <T> List<T> fromCsv(Class<T> tClass, InputStream inputStream) throws IOException {
        val mapper = new CsvMapper();
        val schema = CsvSchema.emptySchema().withHeader();

        MappingIterator<T> iterator = mapper.readerFor(tClass).with(schema).readValues(inputStream);


        return iterator.readAll();
    }

    /**
     * @param tClass
     * @param tList
     * @param outputStream
     * @throws IOException
     */
    public static <T> void toCsv(Class<T> tClass, List<T> tList, OutputStream outputStream) throws IOException {

        val mapper = new CsvMapper();
        val schema = mapper.schemaFor(tClass).withHeader().withColumnReordering(false);

        val writer = mapper.writerFor(tClass).with(schema);
        writer.writeValuesAsArray(outputStream).writeAll(tList);
        outputStream.close();

      /*  Field[] fields = tClass.getDeclaredFields();
        ArrayList<String> header = new ArrayList<>();
        for (Field field : fields) {
            header.add(field.getName());
        }
        beanWriter.writeHeader(header.toArray(new String[0]));

        for (Object object : tList) {
            beanWriter.write(object, header.toArray(new String[0]));
        }*/

    }

    @Override
    protected void writeInternal(List list, Type type, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {

      /*  outputMessage.getHeaders().add(HttpHeaders.CONTENT_TYPE, String.valueOf(new MediaType("text", "csv")));
        try (final Writer writer = new OutputStreamWriter(outputMessage.getBody())) {
            final CsvBeanWriter beanWriter = new CsvBeanWriter(writer, CsvPreference.STANDARD_PREFERENCE);

            ParameterizedType pType = (ParameterizedType) type;
            Class<T> clazz = (Class<T>) pType.getActualTypeArguments()[0];
            toCsv(clazz, list, beanWriter);
            beanWriter.flush();
        }*/

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
