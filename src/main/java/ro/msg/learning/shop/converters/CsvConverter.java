package ro.msg.learning.shop.converters;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractGenericHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class CsvConverter<T> extends AbstractGenericHttpMessageConverter<List> {
    public CsvConverter() {
        super(new MediaType("text", "csv"));
    }

    @Override
    protected List readInternal(Class<? extends List> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
        return null;
    }

    private void toCvs(Class<T> tClass, List<T> tList, CsvBeanWriter beanWriter) throws IOException {


        Field[] fields = tClass.getDeclaredFields();
        ArrayList<String> header = new ArrayList<>();
        for (Field field : fields) {
            header.add(field.getName());
        }
        beanWriter.writeHeader(header.toArray(new String[0]));

        for (Object object : tList) {
            beanWriter.write(object, header.toArray(new String[0]));
        }

    }

    @Override
    protected void writeInternal(List list, Type type, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {

        outputMessage.getHeaders().add(HttpHeaders.CONTENT_TYPE, String.valueOf(new MediaType("text", "csv")));
        try (final Writer writer = new OutputStreamWriter(outputMessage.getBody())) {
            final CsvBeanWriter beanWriter = new CsvBeanWriter(writer, CsvPreference.STANDARD_PREFERENCE);

            ParameterizedType pType = (ParameterizedType) type;
            Class<T> clazz = (Class<T>) pType.getActualTypeArguments()[0];
            toCvs(clazz, list, beanWriter);
            beanWriter.flush();
        }
    }

    @Override
    public List read(Type type, Class<?> contextClass, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
        return null;
    }


}
