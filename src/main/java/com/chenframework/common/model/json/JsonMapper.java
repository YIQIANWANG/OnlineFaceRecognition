package com.chenframework.common.model.json;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.TimeZone;

@Slf4j
public class JsonMapper extends ObjectMapper {
    private static final long serialVersionUID = 1L;

    public JsonMapper() {
        this(Include.ALWAYS);
    }

    public JsonMapper(Include include) {
        if (include != null) {
            this.setSerializationInclusion(include);
        }

        this.enableSimple();
        this.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        this.getSerializerProvider().setNullValueSerializer(new JsonSerializer<Object>() {
            public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
                jgen.writeString("");
                // jgen.writeNull();
            }
        });

        this.setTimeZone(TimeZone.getDefault());

        this.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

        this.enableEnumUseToString();

        this.registerModule(new Hibernate5Module());

        SimpleModule simpleModule = new SimpleModule();
        this.registerModule(simpleModule);
    }

    public JsonMapper enableEnumUseToString() {
        this.enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);
        this.enable(DeserializationFeature.READ_ENUMS_USING_TO_STRING);
        return this;
    }

    public JsonMapper enableSimple() {
        this.configure(Feature.ALLOW_SINGLE_QUOTES, true);
        this.configure(Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        return this;
    }

    public <T> T fromJson(String jsonString, Class<T> clazz) {
        if (!StringUtils.isEmpty(jsonString)) {
            try {
                return this.readValue(jsonString, clazz);
            } catch (IOException e) {
                log.error("Parse json string error:{}", jsonString, e);
            }
        }
        return null;

    }

    @SuppressWarnings("unchecked")
    public <T> T fromJson(String jsonString, JavaType javaType) {
        if (!StringUtils.isEmpty(jsonString)) {
            try {
                return (T) this.readValue(jsonString, javaType);
            } catch (IOException e) {
                log.error("Parse json string error:{}", jsonString, e);
            }
        }
        return null;
    }

    public ObjectMapper getMapper() {
        return this;
    }

    public String toJson(Object object) {
        try {
            return this.writeValueAsString(object);
        } catch (IOException e) {
            log.error("Write to json string error:{}" + object, e);
        }
        return null;
    }

    public static <T> List<T> fromJson2List(String jsonString, Class<T> clazz) {
        JsonMapper mapper = JsonMapper.getInstance();
        JavaType javaType = mapper.getTypeFactory().constructParametricType(List.class, clazz);
        return mapper.fromJson(jsonString, javaType);
    }

    public static Object fromJsonString(String jsonString, Class<?> clazz) {
        return JsonMapper.getInstance().fromJson(jsonString, clazz);
    }

    public static JsonMapper getInstance() {
        return new JsonMapper();
    }

    public static JsonMapper getInstanceNonDefault() {
        return new JsonMapper(Include.NON_DEFAULT);
    }

    public static String toJsonString(Object object) {
        return JsonMapper.getInstance().toJson(object);
    }

}
