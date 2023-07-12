package co.yixiang.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * @author ：taozi
 * @date ：Created in 2020-10-21 14:12
 * @description：
 * @modified By：
 * @version:
 */
public class BigDecimalSerializer extends JsonSerializer<BigDecimal> {
    @Override
    public void serialize(BigDecimal value, JsonGenerator gen, SerializerProvider serializerProvider) throws IOException {
        if (value != null && !"".equals(value)) {
            DecimalFormat df2 =new DecimalFormat("0.00");
            gen.writeString(df2.format(value));
        } else {
            gen.writeString(value + "");
        }
    }
}
