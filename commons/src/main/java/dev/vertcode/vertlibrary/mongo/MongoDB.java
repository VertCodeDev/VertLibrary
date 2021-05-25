/*
Mostly made by xCodiq#3662
 */
package dev.vertcode.vertlibrary.mongo;

import com.mongodb.*;
import lombok.Getter;
import lombok.SneakyThrows;
import org.jetbrains.annotations.Nullable;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.mapping.DefaultCreator;
import org.mongodb.morphia.query.Query;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Getter
public class MongoDB {

    private final MongoDBSettings mongoDBSettings;
    private final Morphia morphia;
    private final MongoClient mongoClient;
    private final Map<String, Datastore> datastoreMap = new HashMap<>();
    private final ExecutorService executorService;

    public MongoDB(MongoDBSettings mongoDBSettings, ClassLoader classLoader) {
        this.mongoDBSettings = mongoDBSettings;
        MongoClientURI mongoClientURI = new MongoClientURI(this.mongoDBSettings.getConnectionUri());

        this.morphia = new Morphia();
        loadMapperOptions(classLoader);
        this.mongoClient = new MongoClient(mongoClientURI);

        this.executorService = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread thread = new Thread(r);
            thread.setName("VertLibrary | MongoDB | " + this.mongoDBSettings.getDatabaseName());
            thread.setDaemon(true);
            return thread;
        });
    }

    /**
     * Converts a {@link DBObject} to the object you given
     *
     * @param object the object you want to convert
     * @param clazz  the class you want to convert it to
     * @param <T>    the type of the object
     * @return converted object
     */
    public <T> T fromDBObject(DBObject object, Class<T> clazz) {
        return morphia.fromDBObject(getDatastore(), clazz, object);
    }

    /**
     * Saves a object to the database
     *
     * @param obj the object you want to save
     * @param <T> the type of the object
     */
    public <T> void save(T obj) {
        this.executorService.execute(() -> ensureDatastore(obj).save(obj));
    }

    /**
     * Deletes a object from the database
     *
     * @param obj the object you want to delete
     * @param <T> the type of the object
     */
    public <T> void delete(T obj) {
        ensureDatastore(obj).delete(obj);
    }

    /**
     * Ensures a datastore for a certain "class"
     *
     * @param obj The object you want to ensure the datastore for
     * @param <T> the type of the datastore
     * @return the datastore
     */
    private <T> Datastore ensureDatastore(T obj) {
        if (!morphia.isMapped(obj.getClass())) createMapping(obj.getClass());
        return getDatastore();
    }

    /**
     * Returns the datastore stored in the datastoreMap, if not found it creates it.
     *
     * @return the datastore
     */
    public Datastore getDatastore() {
        return datastoreMap.computeIfAbsent(this.mongoDBSettings.getDatabaseName(), key -> {
            Datastore datastore = morphia.createDatastore(mongoClient, key);
            datastore.ensureIndexes();
            return datastore;
        });
    }

    /**
     * Returns a optional of the object you want to find by the key
     *
     * @param key   the key you want to search for
     * @param value the value that the key should have
     * @param clazz The class to query
     * @param <T>   the type of the query
     * @return the query
     */
    public <T> Optional<T> optional(String key, Object value, Class<T> clazz) {
        try {
            return Optional.ofNullable(query(clazz).get().filter(key, value).get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }


    /**
     * Find all instances by type
     *
     * @param clazz  the class to use for mapping the results
     * @param <T>    the type to query
     * @param filter the filter you want to sort the find with
     * @return the query
     */
    @Nullable
    @SneakyThrows
    public <T> DBCursor sort(Class<T> clazz, BasicDBObject filter) {
        return this.getCollection(clazz).get().find().sort(filter);
    }

    /**
     * Returns a new query bound to the class.
     *
     * @param clazz the class to query
     * @param <T>   the type of the query
     * @return the query
     */
    public <T> CompletableFuture<Query<T>> query(Class<T> clazz) {
        return CompletableFuture.supplyAsync(() -> getDatastore().createQuery(clazz));
    }

    /**
     * Find all instances by type
     *
     * @param clazz the class to use for mapping the results
     * @param <T>   the type to query
     * @return the query
     */
    public <T> CompletableFuture<Query<T>> find(Class<T> clazz) {
        return CompletableFuture.supplyAsync(() -> getDatastore().find(clazz));
    }

    /**
     * Gets a collection by type.
     *
     * @param clazz the class to use for mapping
     * @param <T>   the type of the object
     * @return the mapped collection for the collection
     */
    public <T> CompletableFuture<DBCollection> getCollection(Class<T> clazz) {
        return CompletableFuture.supplyAsync(() -> getDatastore().getCollection(clazz));
    }


    /**
     * Shuts down the connection to the database,
     * this will require you to recreate the class if you want to use it after this has been used
     */
    public void shutdown() {
        this.executorService.shutdown();
        this.mongoClient.close();
    }

    private <T> void createMapping(Class<T> clazz) {
        this.datastoreMap.values()
                .stream()
                .filter(datastore -> !morphia.getMapper().isMapped(clazz))
                .forEach(datastore -> morphia.getMapper().addMappedClass(clazz));
        morphia.map(clazz);
    }

    private void loadMapperOptions(ClassLoader classLoader) {
        this.morphia.getMapper().getOptions().setStoreEmpties(true);
        this.morphia.getMapper().getOptions().setStoreNulls(true);
        this.morphia.getMapper().getOptions().setObjectFactory(new DefaultCreator() {
            @Override
            protected ClassLoader getClassLoaderForClass() {
                return classLoader;
            }
        });
    }

}
