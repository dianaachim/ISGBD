package Repository;

import Domain.DTO;
import Domain.Database;
import com.mongodb.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import jdk.nashorn.internal.runtime.doubleconv.DtoaBuffer;
import org.bson.Document;

public class MongoDbConfig {
    private MongoClient mongoClient;
    private MongoDatabase db;

    public MongoDbConfig(Database currentDatabase) {

        MongoClientURI uri = new MongoClientURI(
                "mongodb+srv://diana:balsoi@cluster0.emk7j.mongodb.net/" + currentDatabase + "?retryWrites=true&w=majority");

        this.mongoClient = new MongoClient(uri);
        this.db = mongoClient.getDatabase(currentDatabase.getDatabaseName());
    }

    public void insert(String tableName, DTO dto) {
        MongoCollection<Document> dbCollection = db.getCollection(tableName);
        Document document = new Document();
//        BasicDBObject document = new BasicDBObject();
        document.put("_id", dto.getKey());
        document.put("value", dto.getValue());
        dbCollection.insertOne(document);
    }

    public void setDatabase(Database db) {
        this.db = mongoClient.getDatabase(db.getDatabaseName());
    }
}
