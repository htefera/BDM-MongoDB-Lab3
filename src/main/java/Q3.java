import com.mongodb.MongoClient;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import java.util.Arrays;
import java.util.List;

public class Q3 {
    public static void execute() {
        // For each company, the name and the number of employees.
        MongoClient client = new MongoClient("10.4.41.144");
        MongoDatabase database = client.getDatabase("test");
        MongoCollection<Document> companyCollection = database.getCollection("restaurants");

        AggregateIterable<Document> q2 = companyCollection.aggregate(Arrays.asList(
                new Document("$unwind", "$grades"),
                new Document("$group", new Document()
                        .append("_id", "$cuisine")
                        .append("number", new Document("$avg", "$grades.score"))
                )
        ));

        System.out.println("Q3: average score of each cuisine type");
        for (Document d : q2) {
            System.out.println(d.get("_id") + " has average score of " + d.get("number")
            );
        }
        client.close();

    }
}