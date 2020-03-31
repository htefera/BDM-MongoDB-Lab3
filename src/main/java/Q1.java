import com.mongodb.MongoClient;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.Arrays;
import java.util.List;

public class Q1 {

	/**
	 * This is the Java implementation of the following shell-based query
	 * 	db.person.aggregate({$lookup:{from:"company",localField:"worksIn",foreignField:"_id",as:"company"}})
	 *
	 * Check the full reference of available stages for the aggregation framework
	 * in https://docs.mongodb.com/manual/reference/operator/aggregation-pipeline/
	 */

	public static void execute() {
		// For each person, its name an its company name
		MongoClient client = new MongoClient("host");
		MongoDatabase database = client.getDatabase("bdm_lab3_training");
		MongoCollection<Document> personCollection = database.getCollection("person");

		AggregateIterable<Document> q1 = personCollection.aggregate(Arrays.asList(
				new Document("$lookup", new Document()
						.append("from","company")
						.append("localField","worksIn")
						.append("foreignField","_id")
						.append("as","company")),
				new Document("$unwind", "$company")
		));

		System.out.println("Q1: For each person, its name an its company name");
		for (Document d : q1 ) {
			Document company = (Document) d.get("company");
			System.out.println(d.get("firstName") + " " + d.get("lastName") + " works in " + company.get("_id"));
		}
		client.close();
	}

}
