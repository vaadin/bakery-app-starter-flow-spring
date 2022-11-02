package com.vaadin.starter.bakery.serialization;

import uk.q3c.util.serial.tracer.SerializationOutcome;
import uk.q3c.util.serial.tracer.SerializationTracer;
import uk.q3c.util.serial.tracer.SerializationTracerKt;

public interface SerializationTester {

    String serialize(Object object);

    <T> T deserialize(String id);

    default <T> T test(T object) {
        //System.out.println(new SerializationTracer().trace(object)
        //                .results(SerializationTracerKt.getAnyFailure()));
        return deserialize(serialize(object));
    }

}
