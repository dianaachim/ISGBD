package Repository;

import Domain.Database;
import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;

public class MongoDbConfig {
    private MongoClient mongoClient;
    private MongoDatabase db;
    private Database currentDatabase;

    public MongoDbConfig(Database currentDatabase) {
        this.currentDatabase = currentDatabase;

        MongoClientURI uri = new MongoClientURI(
                "mongodb+srv://diana:balsoi@cluster0.emk7j.mongodb.net/" + this.currentDatabase + "?retryWrites=true&w=majority");

        this.mongoClient = new MongoClient(uri);
        this.db = mongoClient.getDatabase(this.currentDatabase.getDatabaseName());
    }
}
