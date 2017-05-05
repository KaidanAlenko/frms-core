package hr.eestec_zg.frmscore.services;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import hr.eestec_zg.frmscore.utilities.LambdaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;


@Service
public class JacksonService {

    private final ObjectMapper objectMapper;

    @Autowired
    public JacksonService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @SuppressWarnings("unchecked")
    public <K> List<K> readListOfObjects(String content, Class<K> clazz) {
        JavaType type = objectMapper.getTypeFactory().constructCollectionType(List.class, clazz);
        return LambdaUtil.uncheckCall(() -> (List<K>) objectMapper.readValue(content, type));
    }

    public <K> K readJson(String content, Class<K> clazz) throws IOException {
        return objectMapper.readValue(content, clazz);
    }

    public String asJson(Object obj) {
        return LambdaUtil.uncheckCall(() -> objectMapper.writeValueAsString(obj));
    }

    public String asPrettyJson(Object obj) {
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        return LambdaUtil.uncheckCall(() -> objectMapper.writeValueAsString(obj));
    }

    public <K> boolean isValidJsonForListOfObjects(String content, Class<K> clazz) {
        JavaType type = objectMapper.getTypeFactory().constructCollectionType(List.class, clazz);
        try {
            objectMapper.readValue(content, type);
        } catch (IOException e) {
            return false;
        }
        return true;
    }

}
