package Repository;

import Domain.DTO;
import Domain.Database;
import com.mongodb.*;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import jdk.nashorn.internal.runtime.doubleconv.DtoaBuffer;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import javax.xml.bind.DatatypeConverter;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

public class MongoDbConfig {
    private MongoClient mongoClient;
    private MongoDatabase db;

    public MongoDbConfig(Database currentDatabase) {

        MongoClientURI uri = new MongoClientURI(
                "mongodb+srv://diana:balsoi@cluster0.emk7j.mongodb.net/" + currentDatabase + "?retryWrites=true&w=majority");

        this.mongoClient = new MongoClient(uri);
        this.db = mongoClient.getDatabase(currentDatabase.getDatabaseName());
    }

    public String insert(String tableName, DTO dto) {
        try {
            MongoCollection<Document> dbCollection = db.getCollection(tableName);
            Document document = new Document();
//        BasicDBObject document = new BasicDBObject();
            document.put("_id", dto.getKey());
            document.put("value", dto.getValue());
            dbCollection.insertOne(document);
        } catch (Exception error) {

            if(error.getMessage().contains("11000"))
            {
                return "Key already exists";
            }
        }
        return "Document successfully added!";
    }

    public String insertIndex(String fileName, DTO dto) {
        try {
            MongoCollection<Document> dbCollection = db.getCollection(fileName);
            Document document = new Document();
            document.put("_id", dto.getKey());
            document.put("value", dto.getValue());
            dbCollection.insertOne(document);
        } catch (Exception error) {
            if(error.getMessage().contains("11000"))
            {
                return "Key already exists";
            }
        }
        return "Index successfully added!";
    }

    public void delete(String tableName, DTO dto){
        MongoCollection<Document> dbCollection = db.getCollection(tableName);
        Document document = new Document();
        document.put("_id", dto.getKey());
        dbCollection.deleteOne(document);
    }

    public void setDatabase(Database db) {
        this.db = mongoClient.getDatabase(db.getDatabaseName());
    }

    public Document getValueByKey(String collectionName,String k) throws UnsupportedEncodingException {

        MongoCollection<Document> myCollection = db.getCollection(collectionName);
        return myCollection.find(eq("_id", k)).first();

    }

    public Document getDocumentByValue(String collectionName, String value) {
        MongoCollection<Document> myCollection = db.getCollection(collectionName);
        return myCollection.find(eq("value", value)).first();
    }

    public void deleteByDocument(String tableName, Document document){
        MongoCollection<Document> dbCollection = db.getCollection(tableName);
        dbCollection.deleteOne(document);
    }
}
