package com.vaadin.starter.bakery.serialization;


import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.invoke.SerializedLambda;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.serializers.ClosureSerializer;
import com.esotericsoftware.kryo.util.DefaultInstantiatorStrategy;
import com.hazelcast.map.IMap;
import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.StreamSerializer;
import org.jetbrains.annotations.NotNull;
import org.objenesis.instantiator.ObjectInstantiator;
import org.objenesis.strategy.InstantiatorStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.vaadin.azure.starter.sessiontracker.CurrentKey;
import com.vaadin.azure.starter.sessiontracker.SessionSerializer;
import com.vaadin.azure.starter.sessiontracker.backend.BackendConnector;
import com.vaadin.azure.starter.sessiontracker.backend.SessionInfo;
import com.vaadin.starter.bakery.backend.data.entity.AbstractEntity;

@Configuration
public class SerializationConfig {

    @Autowired
    ApplicationContext applicationContext;

    @Bean
    SerializationTester vaadinSerTester(SessionSerializer sessionSerializer, BackendConnector backendConnector) {
        return new AzuerKitSerializationTester(sessionSerializer, backendConnector);
    }

    //@Bean
    SerializationTester kryoTester() {
        return new KryoSerializationTester();
    }

    private static class KryoSpringInstantiator<T> implements ObjectInstantiator<T> {
        private final Class<T> type;
        private final ApplicationContext appCtx;

        public KryoSpringInstantiator(Class<T> type, ApplicationContext appCtx) {
            this.type = type;
            this.appCtx = appCtx;
        }

        @Override
        public T newInstance() {
            return appCtx.getBean(type);
        }
    }

    private Kryo kryo() {
        Kryo kryo = new Kryo();
        kryo.setRegistrationRequired(false);
        kryo.setInstantiatorStrategy(new DefaultInstantiatorStrategy(new InstantiatorStrategy() {
            @Override
            public <T> ObjectInstantiator<T> newInstantiatorOf(Class<T> type) {
                return new KryoSpringInstantiator<>(type, applicationContext);
            }
        }));
        kryo.register(Object[].class);
        kryo.register(Class.class);
        kryo.register(SerializedLambda.class);
        kryo.register(ClosureSerializer.Closure.class, new ClosureSerializer());
        return kryo;
    }

    private StreamSerializer<AbstractEntity> kryoEntitySerializer() {
        Kryo kryo = kryo();
        return new StreamSerializer<AbstractEntity>() {
            @Override
            public void write(@NotNull ObjectDataOutput out, @NotNull AbstractEntity object) throws IOException {
                try (Output output = new Output((OutputStream) out)) {
                    kryo.writeClassAndObject(output, object);
                }
            }

            @NotNull
            @Override
            public AbstractEntity read(@NotNull ObjectDataInput data) throws IOException {
                try (Input input = new Input((InputStream) data)) {
                    return (AbstractEntity) kryo.readClassAndObject(input);
                }
            }

            @Override
            public int getTypeId() {
                return 999;
            }
        };
    }

    private static class KryoSerializationTester implements SerializationTester {

        private final Kryo kryo;
        private final Map<String, Output> outputMap;

        public KryoSerializationTester() {
            outputMap = new HashMap<>();
            kryo = new Kryo();
            kryo.setRegistrationRequired(false);
        }

        @Override
        public String serialize(Object object) {
            String id = UUID.randomUUID().toString();
            Output output = new Output(1024, -1);
            kryo.writeClassAndObject(output, object);
            outputMap.put(id, output);
            return id;
        }

        @Override
        public <T> T deserialize(String id) {
            Output output = outputMap.get(id);
            return (T) kryo.readClassAndObject(new Input(output.getBuffer()));
        }
    }

    private static class HazelcastSerializationTester implements SerializationTester {
        private final IMap<Object, Object> map;

        public HazelcastSerializationTester(IMap<Object, Object> map) {
            this.map = map;
        }

        @Override
        public String serialize(Object object) {
            String id = UUID.randomUUID().toString();
            map.put(id, object);
            return id;
        }

        @Override
        public <T> T deserialize(String id) {
            return (T) map.get(id);
        }
    }

    private static class AzuerKitSerializationTester implements SerializationTester {
        final SessionSerializer sessionSerializer;
        final BackendConnector connector;
        final Map<String, SessionInfo> sessions = new ConcurrentHashMap<>();


        public AzuerKitSerializationTester(SessionSerializer sessionSerializer, BackendConnector backendConnector) {
            this.sessionSerializer = sessionSerializer;
            connector = backendConnector;
        }

        @Override
        public String serialize(Object object) {
            String sid = UUID.randomUUID().toString();
            HttpSession session = new MockSession(sid);
            session.setAttribute(CurrentKey.COOKIE_NAME, sid);
            session.setAttribute("OBJ", object);
            sessionSerializer.serialize(session);
            // wait for session being written
            SessionInfo info;
            int attempts = 0;
            while ((info = connector.getSession(sid)) == null && ++attempts < 100) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            sessions.put(sid, info);
            return sid;
        }

        @Override
        public <T> T deserialize(String sid) {
            MockSession session = new MockSession(sid);
            try {
                sessionSerializer.deserialize(sessions.get(sid), session);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return (T) session.getAttribute("OBJ");
        }

        private static class MockSession implements HttpSession {

            final String sid;
            final Map<String, Object> delegate = new HashMap<>();

            public MockSession(String sid) {
                this.sid = sid;
            }

            @Override
            public long getCreationTime() {
                return 0;
            }

            @Override
            public String getId() {
                return sid;
            }

            @Override
            public long getLastAccessedTime() {
                return 0;
            }

            @Override
            public ServletContext getServletContext() {
                return null;
            }

            @Override
            public void setMaxInactiveInterval(int interval) {

            }

            @Override
            public int getMaxInactiveInterval() {
                return 0;
            }

            @Override
            public HttpSessionContext getSessionContext() {
                return null;
            }

            @Override
            public Object getAttribute(String name) {
                return delegate.get(name);
            }

            @Override
            public Object getValue(String name) {
                return null;
            }

            @Override
            public Enumeration<String> getAttributeNames() {
                return Collections.enumeration(delegate.keySet());
            }

            @Override
            public String[] getValueNames() {
                return new String[0];
            }

            @Override
            public void setAttribute(String name, Object value) {
                delegate.put(name, value);
            }

            @Override
            public void putValue(String name, Object value) {

            }

            @Override
            public void removeAttribute(String name) {

            }

            @Override
            public void removeValue(String name) {

            }

            @Override
            public void invalidate() {

            }

            @Override
            public boolean isNew() {
                return false;
            }
        }
    }
}
