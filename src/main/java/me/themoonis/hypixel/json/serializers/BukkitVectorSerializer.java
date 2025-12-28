package me.themoonis.hypixel.json.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.bukkit.util.Vector;

import java.io.IOException;

public class BukkitVectorSerializer extends StdSerializer<Vector> {

    public BukkitVectorSerializer(){
        this(null);
    }

    public BukkitVectorSerializer(Class<Vector> t) {
        super(t);
    }

    @Override
    public void serialize(Vector vector, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeNumberField("x", vector.getX());
        jsonGenerator.writeNumberField("y", vector.getY());
        jsonGenerator.writeNumberField("z", vector.getZ());
        jsonGenerator.writeEndObject();
    }
}
