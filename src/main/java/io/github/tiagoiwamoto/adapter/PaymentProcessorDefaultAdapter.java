package io.github.tiagoiwamoto.adapter;

import io.github.tiagoiwamoto.entrypoint.dto.PaymentRequest;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@Path("/payments")
@RegisterRestClient
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface PaymentProcessorDefaultAdapter {

    @GET
    @Path("/service-health")
    Response checkServiceHealth();

    @POST
    Response processPayment(PaymentRequest payload);

}
