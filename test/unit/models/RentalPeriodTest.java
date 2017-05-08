package unit.models;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.RentalPeriod;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.Test;
import play.libs.Json;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static org.junit.Assert.assertEquals;

/**
 * @author Simon Olofsson
 */
public class RentalPeriodTest {

    @Test
    public void testDeserializesProperly() throws JsonProcessingException, ParseException {

        ObjectNode rentalPeriodJson = Json.newObject();
        rentalPeriodJson.put("start", "2017-05-01");
        rentalPeriodJson.put("end", "2018-05-01");

        ObjectMapper mapper = new ObjectMapper();
        RentalPeriod rentalPeriod = mapper.treeToValue(rentalPeriodJson, RentalPeriod.class);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        assertEquals(DateUtils.truncate(rentalPeriod.start, Calendar.DAY_OF_MONTH),
                DateUtils.truncate(dateFormat.parse("2017-05-01"), Calendar.DAY_OF_MONTH));

        assertEquals(DateUtils.truncate(rentalPeriod.end, Calendar.DAY_OF_MONTH),
                DateUtils.truncate(dateFormat.parse("2018-05-01"), Calendar.DAY_OF_MONTH));

    }
}
