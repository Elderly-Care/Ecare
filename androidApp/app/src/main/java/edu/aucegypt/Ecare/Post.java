package edu.aucegypt.Ecare;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

// [START post_class]
@IgnoreExtraProperties
public class Post {
    public String kitchen;
    public String bathroom;

    public Post() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    public Post(String bathroom, String kitchen) {
        this.bathroom = bathroom;
        this.kitchen = kitchen;
    }

    // [START post_to_map]
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("Kitchen", kitchen);
        result.put("Bathroom", bathroom);

        return result;
    }
    // [END post_to_map]
}
// [END post_class]
