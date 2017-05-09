package unit.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Test;
import org.mockito.Mock;
import services.AccommodationService;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static play.test.Helpers.route;

/**
 * @author Simon Olofsson
 */
public class AccommodationControllerTest {

    @Mock
    private AccommodationService accommodationService;

    @Test
    public void testReturnsOKOnCorrectRequest() throws JsonProcessingException {

    }
}
