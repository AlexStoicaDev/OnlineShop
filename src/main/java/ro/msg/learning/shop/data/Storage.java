package ro.msg.learning.shop.data;

import org.apache.olingo.commons.api.data.Entity;
import org.apache.olingo.commons.api.data.EntityCollection;
import org.apache.olingo.commons.api.data.Property;
import org.apache.olingo.commons.api.data.ValueType;
import org.apache.olingo.commons.api.edm.EdmEntitySet;
import org.apache.olingo.commons.api.edm.EdmEntityType;
import org.apache.olingo.commons.api.ex.ODataRuntimeException;
import org.apache.olingo.commons.api.http.HttpStatusCode;
import org.apache.olingo.server.api.ODataApplicationException;
import org.apache.olingo.server.api.uri.UriParameter;
import org.springframework.stereotype.Component;
import ro.msg.learning.shop.providers.EdmProvider;
import ro.msg.learning.shop.repositories.ProductRepository;
import ro.msg.learning.shop.utils.EDMutil;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Component
public class Storage {

    private List<Entity> productList;
    private final ProductRepository productRepository;

    public Storage(ProductRepository productRepository) {
        this.productRepository = productRepository;
        productList = new ArrayList<>();

        initSampleData();
    }

    /* PUBLIC FACADE */
    public EntityCollection readEntitySetData(EdmEntitySet edmEntitySet) {
        // actually, this is only required if we have more than one Entity Sets
        if (edmEntitySet.getName().equals(EdmProvider.ES_PRODUCTS_NAME)) {
            return getProducts();
        }
        return null;
    }

    public Entity readEntityData(EdmEntitySet edmEntitySet, List<UriParameter> keyParams) throws ODataApplicationException {
        EdmEntityType edmEntityType = edmEntitySet.getEntityType();

        // actually, this is only required if we have more than one Entity Type
        if (edmEntityType.getName().equals(EdmProvider.ET_PRODUCT_NAME)) {
            return getProduct(edmEntityType, keyParams);
        }

        return null;
    }

    /*  INTERNAL */

    private EntityCollection getProducts() {
        EntityCollection retEntitySet = new EntityCollection();

        for (Entity productEntity : this.productList) {
            retEntitySet.getEntities().add(productEntity);
        }

        return retEntitySet;
    }

    private Entity getProduct(EdmEntityType edmEntityType, List<UriParameter> keyParams) throws ODataApplicationException {

        // the list of entities at runtime
        EntityCollection entitySet = getProducts();

        /*  generic approach  to find the requested entity */
        Entity requestedEntity = EDMutil.findEntity(edmEntityType, entitySet, keyParams);

        if (requestedEntity == null) {
            // this variable is null if our data doesn't contain an entity for the requested key
            // Throw suitable exception
            throw new ODataApplicationException("Entity for requested key doesn't exist",
                HttpStatusCode.NOT_FOUND.getStatusCode(), Locale.ENGLISH);
        }

        return requestedEntity;
    }

    /* HELPER */
    private void initSampleData() {

        productList = productRepository.findAll().parallelStream().map(product -> {
            final Entity e1 = new Entity()
                .addProperty(new Property(null, "ID", ValueType.PRIMITIVE, product.getId()))
                .addProperty(new Property(null, "Name", ValueType.PRIMITIVE, product.getName()))
                .addProperty(new Property(null, "Description", ValueType.PRIMITIVE, product.getDescription())
                );
            e1.setId(createId("Products", product.getId()));
            return e1;
        }).collect(Collectors.toList());


    }

    private URI createId(String entitySetName, Object id) {
        try {
            return new URI(entitySetName + "(" + String.valueOf(id) + ")");
        } catch (URISyntaxException e) {
            throw new ODataRuntimeException("Unable to create id for entity: " + entitySetName, e);
        }
    }

}
