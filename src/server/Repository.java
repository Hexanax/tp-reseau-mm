package server;

import domain.Conversation;
import org.dizitart.no2.Document;
import org.dizitart.no2.Nitrite;
import org.dizitart.no2.NitriteCollection;


public class Repository {
    private final NitriteCollection conversationsCollection;
    private static final String CONVERSATIONS_COLLECTION = "conversations";

    public Repository() {
        // Create a Nitrite Collection
        Nitrite db = Nitrite.builder().compressed().filePath("data.db").openOrCreate();
        conversationsCollection = db.getCollection(CONVERSATIONS_COLLECTION);
    }

    public void createConversation(Conversation conversation){
        Document doc = conversationToDoc(conversation);
        conversationsCollection.insert(doc);
    }

    public void updateConversation(Conversation conversation){

    }

    private Document conversationToDoc(Conversation conversation){
        return Document.createDocument("firstName", "John")
                .put("index", conversation.getIndex())
                .put("conversationID", conversation.getConversationID())
                .put("members", conversation.getMembers())
                .put("messages", conversation.getMessages());
    }
}