import com.devskiller.jfairy.Fairy;
import com.devskiller.jfairy.producer.company.Company;
import com.devskiller.jfairy.producer.payment.IBAN;
import com.devskiller.jfairy.producer.person.Address;
import com.devskiller.jfairy.producer.person.Person;
import com.mongodb.MongoClient;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class PopulateTest {

    public static void populate(int N) {
        Fairy fairy = Fairy.create();

        MongoClient client = new MongoClient("10.4.41.144");
        MongoDatabase database = client.getDatabase("bdm_lab3");
        MongoCollection<Document> personCollection = database.getCollection("person");
        MongoCollection<Document> companyCollection = database.getCollection("company");

        for (int i = 0; i < N; ++i) {
            Person person = fairy.person();
            Address personAddress = person.getAddress();
            Company personsCompany = person.getCompany();
            /**
             * First, check that the person does not already exist
             * The primary key for Person is passport number
             */
            Document personDocument = new Document();
            personDocument.put("_id",person.getPassportNumber());
            /**
             * If the count of document with this passport number is not zero then the person
             * already exists and we can ignore it
             */
            if (personCollection.countDocuments(personDocument) == 0) {
                personDocument.put("age", person.getAge());
                personDocument.put("companyEmail",person.getCompanyEmail());
                personDocument.put("dateOfBirth",person.getDateOfBirth().toString());
                personDocument.put("firstName",person.getFirstName());
                personDocument.put("lastName",person.getLastName());

                //This is the reference to the Company collection
                personDocument.put("worksIn",personsCompany.getName());
                //This is to embed the Address in the Person
                personDocument.put("address", new Document()
                        .append("street",personAddress.getStreet())
                        .append("streetNumber", personAddress.getStreetNumber())
                        .append("apartmentNumber", personAddress.getApartmentNumber())
                );
                personDocument.put("city",personAddress.getCity());
                personDocument.put("country",person.getNationality().getCode());

                //Insert the document to the collection
                personCollection.insertOne(personDocument);
            }
            /**
             * Same idea as before for companies
             * The primary key for Company is its VAT id number
             */
            Document companyDocument = new Document();
            companyDocument.put("_id",personsCompany.getName());
            if (companyCollection.countDocuments(companyDocument) == 0) {
                companyDocument.put("domain",personsCompany.getDomain());
                companyDocument.put("email",personsCompany.getEmail());
                companyDocument.put("url",personsCompany.getUrl());

                //Insert the document to the collection
                companyCollection.insertOne(companyDocument);
            }
        }
        client.close();
    }

}