package com.example;

import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.Scanner;

public class FeedbackSystem {

    // MongoDB connection URI (Local or Atlas connection string)
    private static final String URI = "mongodb://localhost:27017";  // Adjust for MongoDB Atlas if needed

    public static void main(String[] args) {
        // Scanner for reading user input
        Scanner scanner = new Scanner(System.in);

        // Connect to MongoDB using the new MongoClients API (MongoClient is deprecated in the latest versions)
        try (var client = MongoClients.create(URI)) {
            MongoDatabase database = client.getDatabase("feedback_db");
            MongoCollection<Document> collection = database.getCollection("feedback");

            // Prompt user for feedback and rating
            System.out.print("Enter your feedback: ");
            String feedbackText = scanner.nextLine();

            System.out.print("Enter your rating (1-5): ");
            String ratingInput = scanner.nextLine();

            // Check if rating is provided
            if (ratingInput.isEmpty()) {
                System.out.println("Error: Rating is required.");
                return;
            }

            int rating = Integer.parseInt(ratingInput);
            if (rating < 1 || rating > 5) {
                System.out.println("Error: Rating must be between 1 and 5.");
                return;
            }

            // Create the feedback document to insert into MongoDB
            Document feedback = new Document("feedbackText", feedbackText)
                    .append("rating", rating);

            // Insert the document into the collection
            collection.insertOne(feedback);
            System.out.println("Feedback submitted successfully!");

        } catch (Exception e) {
            // Exception handling for database connection and other errors
            e.printStackTrace();
            System.out.println("Error: Could not connect to the database or submit feedback.");
        } finally {
            // Close the scanner resource to prevent resource leaks
            scanner.close();
        }
    }
}
