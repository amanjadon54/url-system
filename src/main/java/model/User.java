package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

@Data
@AllArgsConstructor
public class User {

    private String userId;
    private String name;
    private String identity;
    private boolean isActive;
    private List<String> urls;

}
