package ro.msg.learning.shop.controllers;

import lombok.RequiredArgsConstructor;
import org.apache.olingo.server.api.OData;
import org.apache.olingo.server.api.ODataHttpHandler;
import org.apache.olingo.server.api.ServiceMetadata;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ro.msg.learning.shop.data.Storage;
import ro.msg.learning.shop.processors.MyEntityCollectionProcessor;
import ro.msg.learning.shop.processors.MyEntityProcessor;
import ro.msg.learning.shop.processors.MyPrimitiveProcessor;
import ro.msg.learning.shop.providers.EdmProvider;
import ro.msg.learning.shop.repositories.ProductRepository;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;

@RestController
@RequiredArgsConstructor
@RequestMapping(EDMController.URI)
public class EDMController {

    private final ProductRepository productRepository;
    static final String URI = "/odata";

    //$metadata
    //Products
    @RequestMapping(value = "**")
    public void odata(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException {
        try {

            HttpSession session = req.getSession(true);
            Storage storage = (Storage) session.getAttribute(Storage.class.getName());
            if (storage == null) {
                storage = new Storage(productRepository);
                session.setAttribute(Storage.class.getName(), storage);
            }

            // create odata handler and configure it with EdmProvider and Processor
            OData odata = OData.newInstance();
            ServiceMetadata edm = odata.createServiceMetadata(new EdmProvider(), new ArrayList<>());
            ODataHttpHandler handler = odata.createHandler(edm);
            handler.register(new MyEntityCollectionProcessor(storage));
            handler.register(new MyEntityProcessor(storage));
            handler.register(new MyPrimitiveProcessor(storage));

            handler.process(new HttpServletRequestWrapper(req) {
                // odata//
                // Spring MVC matches the whole path as the servlet path
                // Olingo wants just the prefix, ie upto /odata, so that it
                // can parse the rest of it as an OData path. So we need to override
                // getServletPath()
                @Override
                public String getServletPath() {
                    return EDMController.URI;
                }
            }, resp);

        } catch (RuntimeException e) {
            throw new ServletException(e);
        }
    }


}
