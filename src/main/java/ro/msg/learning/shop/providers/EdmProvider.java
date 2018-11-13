package ro.msg.learning.shop.providers;

import org.apache.olingo.commons.api.edm.EdmPrimitiveTypeKind;
import org.apache.olingo.commons.api.edm.FullQualifiedName;
import org.apache.olingo.commons.api.edm.provider.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Note: edm is the abbreviation for Entity Data Model.
 * Accordingly, we understand that the CsdlEdmProvider is supposed to provide static descriptive information.
 */
public class EdmProvider extends CsdlAbstractEdmProvider {

    // Service Namespace
    private static final String NAMESPACE = "OData.Demo";

    // EDM Container
    private static final String CONTAINER_NAME = "Container";
    private static final FullQualifiedName CONTAINER = new FullQualifiedName(NAMESPACE, CONTAINER_NAME);

    // Entity Types Names
    public static final String ET_PRODUCT_NAME = "Product";
    private static final FullQualifiedName ET_PRODUCT_FQN = new FullQualifiedName(NAMESPACE, ET_PRODUCT_NAME);

    public static final String ET_ORDER_NAME = "Order";
    private static final FullQualifiedName ET_ORDER_FQN = new FullQualifiedName(NAMESPACE, ET_PRODUCT_NAME);


    // Entity Set Names
    public static final String ES_PRODUCTS_NAME = "Products";


    /**
     * getEntityType() Here we declare the EntityType “Product” and a few of its properties
     */
    @Override
    public CsdlEntityType getEntityType(FullQualifiedName entityTypeName) {
        // this method is called for one of the EntityTypes that are configured in the Schema
        if (entityTypeName.equals(ET_PRODUCT_FQN)) {

            //create EntityType properties
            CsdlProperty id = new CsdlProperty().setName("ID").setType(EdmPrimitiveTypeKind.Int32.getFullQualifiedName());
            CsdlProperty name = new CsdlProperty().setName("Name").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
            CsdlProperty description = new CsdlProperty().setName("Description").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());


            // create CsdlPropertyRef for Key element
            CsdlPropertyRef propertyRef = new CsdlPropertyRef();
            propertyRef.setName("ID");

            // configure EntityType
            CsdlEntityType entityType = new CsdlEntityType();
            entityType.setName(ET_PRODUCT_NAME);
            entityType.setProperties(Arrays.asList(id, name, description));
            entityType.setKey(Collections.singletonList(propertyRef));

            return entityType;
        }
        if (entityTypeName.equals(ET_ORDER_FQN)) {
            CsdlProperty id = new CsdlProperty().setName("ID").setType(EdmPrimitiveTypeKind.Int32.getFullQualifiedName());
            CsdlProperty name = new CsdlProperty().setName("Name").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());


            // create CsdlPropertyRef for Key element
            CsdlPropertyRef propertyRef = new CsdlPropertyRef();
            propertyRef.setName("ID");

            // configure EntityType
            CsdlEntityType entityType = new CsdlEntityType();
            entityType.setName(ET_ORDER_NAME);
            entityType.setProperties(Arrays.asList(id, name));
            entityType.setKey(Collections.singletonList(propertyRef));

            return entityType;

        }


        return null;
    }


    /**
     * getEntitySet() Here we state that the list of products can be called via the EntitySet “Products”
     */
    @Override
    public CsdlEntitySet getEntitySet(FullQualifiedName entityContainer, String entitySetName) {

        if (entityContainer.equals(CONTAINER) && entitySetName.equals(ES_PRODUCTS_NAME)) {

            CsdlEntitySet entitySet = new CsdlEntitySet();
            entitySet.setName(ES_PRODUCTS_NAME);
            entitySet.setType(ET_PRODUCT_FQN);

            return entitySet;
        }
        return null;
    }


    /**
     * getEntityContainer() Here we provide a Container element that is necessary to host the EntitySet.
     */
    @Override
    public CsdlEntityContainerInfo getEntityContainerInfo(FullQualifiedName entityContainerName) {
        // This method is invoked when displaying the Service Document at e.g. http://localhost:8080/DemoService/DemoService.svc
        if (entityContainerName == null || entityContainerName.equals(CONTAINER)) {
            CsdlEntityContainerInfo entityContainerInfo = new CsdlEntityContainerInfo();
            entityContainerInfo.setContainerName(CONTAINER);
            return entityContainerInfo;
        }

        return null;
    }


    /**
     * getSchemas() The Schema is the root element to carry the elements.
     */
    @Override
    public List<CsdlSchema> getSchemas() {
        // create Schema
        CsdlSchema schema = new CsdlSchema();
        schema.setNamespace(NAMESPACE);

        // add EntityTypes
        List<CsdlEntityType> entityTypes = new ArrayList<>();
        entityTypes.add(getEntityType(ET_PRODUCT_FQN));
        schema.setEntityTypes(entityTypes);

        // add EntityContainer
        schema.setEntityContainer(getEntityContainer());

        // finally
        List<CsdlSchema> schemas = new ArrayList<>();
        schemas.add(schema);

        return schemas;
    }

    /**
     * getEntityContainerInfo() Information about the EntityContainer to be displayed in the Service Document
     */
    @Override
    public CsdlEntityContainer getEntityContainer() {
        // create EntitySets
        List<CsdlEntitySet> entitySets = new ArrayList<>();
        entitySets.add(getEntitySet(CONTAINER, ES_PRODUCTS_NAME));

        // create EntityContainer
        CsdlEntityContainer entityContainer = new CsdlEntityContainer();
        entityContainer.setName(CONTAINER_NAME);
        entityContainer.setEntitySets(entitySets);

        return entityContainer;
    }


}
