package me.themoonis.hypixel.json.deserializers;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.bukkit.util.Vector;

import java.io.IOException;

public class BukkitVectorDeserializer extends StdDeserializer<Vector> {

    public BukkitVectorDeserializer(){
        this(null);
    }


    public BukkitVectorDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Vector deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);

        double x = node.get("x").asDouble();
        double y = node.get("y").asDouble();
        double z = node.get("z").asDouble();

        return new Vector(x,y,z);
    }
}
