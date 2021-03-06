package controllers;

import scala.Option;
import testResources.BaseTest;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.fail;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static play.test.Helpers.contentAsString;
import static play.test.Helpers.fakeRequest;
import static play.test.Helpers.route;

/**
 * @author Simon Olofsson
 */
public class InterestsControllerTest extends BaseTest {

    Option<Integer> count = Option.apply(10);
    Option<Integer> offset = Option.apply(0);
    Option<Long> tenantId = Option.empty();
    Option<Long> accommodationId = Option.apply(1L);
    Option<Boolean> mutual = Option.apply(false);

    /*
     * Utility methods.
     */

    /*private Result makeAuthenticatedRequest(Option<Integer> count, Option<Integer> offset, Option<Long> tenantId, Option<Long> accommodationId, Option<Boolean> active) {

        //String authToken = renter1.createToken();
        String authToken = usersService.getToken(renter1);

        Http.RequestBuilder fakeRequest = fakeRequest(controllers.routes.EdgesController.get(count, offset, tenantId, accommodationId, active));
        fakeRequest.header(SecurityController.AUTH_TOKEN_HEADER, authToken);

        return route(fakeRequest);

    }

    private Accommodation createRenterAndAccommodation() {

        Renter renter2 = new Renter("eva@example.com", "password", "Eva Hellman", "Tja! Här vare' boende.", 52);
        renter2.save();

        Address address = new Address("Hägerstensvägen", 108, "Mälarhöjden", 100, 100);
        address.save();

        Accommodation renter2Accommodation = renter2.createAccommodation(5500, 25, 1, 6000,
                false, false, true, true, "Schyrrebyrre", address);

        return renter2Accommodation;

    }

    private Result makePostRequest(String authToken, long tenantId, long accommodationId) {

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode objectNode = mapper.createObjectNode();

        objectNode.put("tenantId", tenantId);
        objectNode.put("accommodationId", accommodationId);

        Http.RequestBuilder fakeRequest = fakeRequest(controllers.routes.EdgesController.create());
        fakeRequest.bodyJson(objectNode);
        fakeRequest.header(SecurityController.AUTH_TOKEN_HEADER, authToken);

        return route(fakeRequest);

    }

    private Result createRenterAndAccommodationAndMakePostRequest() {

        Accommodation renter2Accommodation = createRenterAndAccommodation();

        //String authToken = tenant1.createToken();
        String authToken = usersService.getToken(tenant1);

        return makePostRequest(authToken, tenant1.id, renter2Accommodation.id);

    }*/

    /*
     * GET
     */

    /*@Test
    public void returns401OnUnauthorizedGetRequest() {

        Result result = route(fakeRequest(controllers.routes.EdgesController.get(count, offset, tenantId, accommodationId, active)));
        assertEquals(UNAUTHORIZED, result.status());

    }

    @Test
    public void returns200OnAuthorizedGetRequest() {

        Result result = makeAuthenticatedRequest(count, offset, tenantId, accommodationId, active);
        assertEquals(OK, result.status());

    }

    @Test
    public void returnsAllInterestsForRenter() {

        Result result = makeAuthenticatedRequest(count, offset, tenantId, accommodationId, active);
        JsonNode responseBody = Json.parse(contentAsString(result));

        assertTrue(responseBody.size() > 0);

        for (JsonNode interest : responseBody) {

            assertNotNull(interest.findValue("tenantId"));

            try {

                int tenantId = Integer.parseInt(interest.findValue("tenantId").asText());

            } catch (NumberFormatException e) {
                fail("NumberFormatException when parsing tenantId");
            }

            assertNotNull(interest.findValue("accommodationId"));

            try {

                int accommodationId = Integer.parseInt(interest.findValue("accommodationId").asText());

            } catch (NumberFormatException e) {
                fail("NumberFormatException when parsing accommodationId");
            }

            assertNotNull(interest.findValue("active"));
            assertTrue(interest.findValue("active").asText().equals("true") || interest.findValue("active").asText().equals("false"));

        }
    }

    @Test
    public void returnsOnlySpecifiedRangeOnGet() {

        Accommodation renter2Accommodation = createRenterAndAccommodation();

        tenant1.addInterest(renter2Accommodation);

        Option<Integer> count = Option.apply(1);
        Option<Integer> offset = Option.apply(0);
        Option<Long> tenantId = Option.apply(tenant1.id);
        Option<Long> accommodationId = Option.empty();
        Option<Boolean> active = Option.empty();

        //String authToken = tenant1.createToken();
        String authToken = usersService.getToken(tenant1);

        Http.RequestBuilder fakeRequest = fakeRequest(controllers.routes.EdgesController.get(count, offset, tenantId, accommodationId, active));
        fakeRequest.header(SecurityController.AUTH_TOKEN_HEADER, authToken);

        Result result = route(fakeRequest);
        JsonNode responseBody = Json.parse(contentAsString(result));

        assertEquals(1, responseBody.size());

    }

    @Test
    public void canHandleTooOffsetTooGreat() {

        Option<Integer> count = Option.apply(1);
        Option<Integer> offset = Option.apply(5);
        Option<Long> tenantId = Option.apply(tenant1.id);
        Option<Long> accommodationId = Option.empty();
        Option<Boolean> active = Option.empty();

        //String authToken = tenant1.createToken();
        String authToken = usersService.getToken(tenant1);

        Http.RequestBuilder fakeRequest = fakeRequest(controllers.routes.EdgesController.get(count, offset, tenantId, accommodationId, active));
        fakeRequest.header(SecurityController.AUTH_TOKEN_HEADER, authToken);

        Result result = route(fakeRequest);
        JsonNode responseBody = Json.parse(contentAsString(result));

        assertEquals(BAD_REQUEST, result.status());
        assertEquals(ResponseBuilder.OUT_OF_RANGE, responseBody.findValue("type").asText());

    }

    @Test
    public void cannotViewInterestsUserIsNotAuthorizedToView() {
        // Implement this
    }*/

    /*
     * POST
     */

    /*@Test
    public void authorizedPostReturnsNOCONTENT() {

        Result result = createRenterAndAccommodationAndMakePostRequest();
        assertEquals(NO_CONTENT, result.status());

    }

    @Test
    public void authorizedPostSavesInterest() {

        Tenant tenant2 = new Tenant("jonte@example.com", "password", "Jonte Jontesson",
                "Jag heter Jonte", 25, 1, 5000, 18000, "Jonte!", 8000);

        String authToken = usersService.getToken(tenant2);

        Result result = makePostRequest(authToken, tenant2.id, renter1Accommodation.id);
        assertNotNull(Edge.findByTenantAndAccommodation(tenant2.id, renter1Accommodation.id));

    }

    @Test
    public void noSuchEntityErrorTypeOnNonTenantPost() {

        String authToken = usersService.getToken(renter1);

        Result result = makePostRequest(authToken, tenant1.id, renter1Accommodation.id);
        JsonNode responseJson = Json.parse(contentAsString(result));
        assertEquals(ResponseBuilder.NO_SUCH_ENTITY, responseJson.findValue("type").asText());

    }

    @Test
    public void canHandleNonExistingAccommodation() {
        // Implement this
    }*/

    /*
     * PUT
     */

    /*@Test
    public void authorizedPutReturnsUpdatedInterest() {

        //String authToken = renter1.createToken();
        String authToken = usersService.getToken(renter1);

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode bodyJson = mapper.createObjectNode();

        bodyJson.put("active", "true");

        Http.RequestBuilder fakeRequest = fakeRequest(controllers.routes.EdgesController.setMutual(interest1.tenant.id, interest1.accommodation.id));
        fakeRequest.header(SecurityController.AUTH_TOKEN_HEADER, authToken);
        fakeRequest.bodyJson(bodyJson);

        Result result = route(fakeRequest);
        assertEquals(OK, result.status());

        JsonNode responseJson = Json.parse(contentAsString(result));

        assertEquals("true", responseJson.findValue("active").asText());
        assertEquals(5L, responseJson.findValue("tenantId").asLong());
        assertEquals(1L, responseJson.findValue("accommodationId").asLong());

    }

    @Test
    public void authorizedPutReturnsBadRequestOnNoRequestBody() {
        // Implement this
    }

    @Test
    public void authorizedPutReturnsBadRequestOnNonBooleanBodyValue() {
        // Implement this
    }*/

    /*
     * DELETE
     */

    /*private Renter createSampleRenter() {

        Renter renter = new Renter("renter@example.com", "password", "Renter Rentersson",
                "I'm a renter", 35);

        renter.save();

        return renter;

    }

    private Address createSampleAddress() {
        Address address = new Address("The Street", 1, 'A', "The Area", 10, 10);
        address.save();
        return address;
    }

    private Accommodation createSampleAccommodation(Renter renter, Address address, int rent, int size, int deposit, String accommodation) {
        return renter.createAccommodation(rent, size, 1, deposit, false, false, true, true, accommodation, address);
    }

    private Tenant createSampleTenant() {
        Tenant tenant = new Tenant("tenant@texample.com", "password", "Tenant Tenantsson",
                "I'm a tenant", 35, 1, 5000, 18000, "Full-time tenant.", 8000);
        tenant.save();
        return tenant;
    }

    @Test
    public void deleteInterestSavesResult() {

        Renter renter = createSampleRenter();
        Address address = createSampleAddress();

        Accommodation accommodation = createSampleAccommodation(renter, address, 4000, 20, 8000, "Accommodation");

        Tenant tenant = createSampleTenant();
        tenant.addInterest(accommodation);

        //String authToken = tenant.createToken();
        String authToken = usersService.getToken(tenant);

        Http.RequestBuilder fakeRequest = fakeRequest(controllers.routes.EdgesController.withdrawInterest(tenant.id, accommodation.id));
        fakeRequest.header(SecurityController.AUTH_TOKEN_HEADER, authToken);

        Result result = route(fakeRequest);

        assertEquals(NO_CONTENT, result.status());
        assertNull(Edge.findByTenantAndAccommodation(tenant.id, accommodation.id));

    }

    @Test
    public void deleteInterestRemovesInterestFromTenant() {

        Renter renter = createSampleRenter();
        Address address = createSampleAddress();

        Accommodation accommodation = createSampleAccommodation(renter, address, 4000, 20, 8000, "Accommodation");

        Tenant tenant = createSampleTenant();
        tenant.addInterest(accommodation);

        //String authToken = tenant.createToken();
        String authToken = usersService.getToken(tenant);

        Http.RequestBuilder fakeRequest = fakeRequest(controllers.routes.EdgesController.withdrawInterest(tenant.id, accommodation.id));
        fakeRequest.header(SecurityController.AUTH_TOKEN_HEADER, authToken);

        route(fakeRequest);

        Logger.debug(tenant.interests.toString());
        //addInterest(accommodation, tenant);

    }*/

}
