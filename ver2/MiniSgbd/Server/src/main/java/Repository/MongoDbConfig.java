package Repository;

import Domain.DTO;
import Domain.Database;
import com.mongodb.*;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import java.io.UnsupportedEncodingException;
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

    public String getValueByKey2(String collectionName,String k) {
        MongoCollection<Document> myCollection = db.getCollection(collectionName);
        Document doc = myCollection.find(eq("_id", k)).first();
        if (doc == null) {
            return "No find";
        }
        return doc.getString("value");
    }

    public Document getDocumentByValue(String collectionName, String value) {
        MongoCollection<Document> myCollection = db.getCollection(collectionName);
        return myCollection.find(eq("value", value)).first();
    }

    public List<DTO> getDto(String tbName)
    {
        List<DTO> list=new ArrayList<>();
        MongoCollection<Document> collection = db.getCollection(tbName);
        FindIterable<Document> dbCursor = collection.find();
        for (Document doc: dbCursor) {
            String key = (String) doc.get("_id");
            String value= (String) doc.get("value");
            DTO dto=new DTO(key,value);
            list.add(dto);
        }
        return list;

    }

//    public List<Document> getDocs(String collectionName) {
//        List<DTO> dtoList = new ArrayList<>();
//        MongoCollection<Document> dbCollection = db.getCollection(collectionName);
//        FindIterable<Document> listDocument = dbCollection.find();
//        for (Document doc: listDocument) {
//
//        }
//    }

//    public List<DTO> getDTOIndex(String collectionName, String index) {
//        MongoCollection<Document> myCollection = db.getCollection(collectionName);
//        FindIterable<Document> iterable = myCollection.find();
//
//    }

    public void deleteByDocument(String tableName, Document document){
        MongoCollection<Document> dbCollection = db.getCollection(tableName);
        dbCollection.deleteOne(document);
    }

    public void update(String collectioName, DTO dto)
    {

        MongoCollection<Document> collection = db.getCollection(collectioName);

        BasicDBObject updateFields = new BasicDBObject();
        updateFields.append("value", dto.getValue());

        BasicDBObject setQuery = new BasicDBObject();
        setQuery.append("$set", updateFields);

        BasicDBObject searchQuery = new BasicDBObject("_id", dto.getKey());

        collection.updateOne(searchQuery, setQuery);
    }
}
