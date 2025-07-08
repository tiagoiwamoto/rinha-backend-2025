package io.github.tiagoiwamoto.entrypoint;

import io.github.tiagoiwamoto.entrypoint.dto.PaymentRequest;
import io.github.tiagoiwamoto.entrypoint.dto.PaymentSummary;
import io.github.tiagoiwamoto.entrypoint.dto.SummaryDetails;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Path("/payments")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PaymentEntrypoint {

    @POST
    public Response processPayment(PaymentRequest paymentRequest) {
        if (paymentRequest.correlationId() == null || paymentRequest.amount() == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid request").build();
        }

        // Simulação de processamento do pagamento
        return Response.status(Response.Status.ACCEPTED).entity("Payment processed").build();
    }

    @GET
    @Path("/payments-summary")
    public Response getPaymentsSummary(@QueryParam("from") Instant from, @QueryParam("to") Instant to) {
        PaymentSummary summary = PaymentSummary.builder()
                .defaultSummary(SummaryDetails.builder().totalRequests(43236).totalAmount(BigDecimal.valueOf(415542345.98)).build())
                .fallbackSummary(SummaryDetails.builder().totalRequests(423545).totalAmount(BigDecimal.valueOf(329347.34)).build())
                .build();

        return Response.ok(summary).build();
    }
}
