import com.devskiller.jfairy.Fairy;
import com.devskiller.jfairy.producer.person.Person;
import com.mongodb.MongoClient;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class Q2 {

	/**
	 * This is the Java implementation of the following shell-based query
	 * 	db.company.aggregate([{$lookup:{from:"person",localField:"_id",foreignField:"worksIn",as:"employees"}},
	 * 		{$project:{_id:1,employees:{$size:"$employees"}}}])
	 *
	 * Check the full reference of available stages for the aggregation framework
	 * in https://docs.mongodb.com/manual/reference/operator/aggregation-pipeline/
	 */

	public static void execute() {
		// For each company, the name and the number of employees.
		MongoClient client = new MongoClient("host");
		MongoDatabase database = client.getDatabase("bdm_lab3_training");
		MongoCollection<Document> companyCollection = database.getCollection("company");

		AggregateIterable<Document> q2 = companyCollection.aggregate(Arrays.asList(
				new Document("$lookup", new Document()
						.append("from","person")
						.append("localField","_id")
						.append("foreignField","worksIn")
						.append("as","employees")
				),
				new Document("$project", new Document()
						.append("_id",1)
						.append("employees", new Document("$size","$employees"))
				),
				new Document("$sort", new Document()//sorting the result by the number of employees a company has
						.append("employees", 1))
		));

		System.out.println("Q2: For each company, the name and the number of employees");
		for (Document d : q2 ) {
			System.out.println(d.get("_id") + " has " + d.get("employees")
					+ (d.getInteger("employees") == 1 ? " employee." : " employees."));
		}
		client.close();

	}
}
