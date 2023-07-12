package visang.dataportal.test.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@Getter @Setter
public class Node {

    private String name;
    private String color;
    private String id;
    private List<Node> children;
    private int loc;

    public Node(String name, String color, String id) {
        this.name = name;
        this.color = color;
        this.id = id;
        this.children = new ArrayList<>();
        this.loc = -1;
    }

    public Node(String name, String color, String id, int loc) {
        this(name, color, id);
        this.loc = loc;
    }

    public void addChild(Node child) {
        children.add(child);
    }

    public Node findOrCreateChild(String name, String color, String id) {
        return children.stream()
                .filter(child -> child.name.equals(name) && child.id.equals(id))
                .findFirst()
                .orElseGet(() -> {
                    Node child = new Node(name, color, id);
                    children.add(child);
                    return child;
                });
    }

    public Node findOrCreateChild(String name, String color, String id, int loc) {
        return children.stream()
                .filter(child -> child.name.equals(name) && child.id.equals(id) && child.loc == loc)
                .findFirst()
                .orElseGet(() -> {
                    Node child = new Node(name, color, id, loc);
                    children.add(child);
                    return child;
                });
    }
}
