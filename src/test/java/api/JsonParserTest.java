package api;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.challenge.client.JsonParser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = JsonParser.class)
public class JsonParserTest {
    @Autowired
    JsonParser jsonParser;

    @Test
    public void parseFieldTest() {
   var field =     jsonParser.parseField("{\"id\":\"id1\",\"name\":\"Test\",\"color\":\"blue\"}","name");
   Assertions.assertEquals("Test",field);
    }
}
