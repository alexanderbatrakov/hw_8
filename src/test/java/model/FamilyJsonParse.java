package model;

import java.util.List;

public class FamilyJsonParse {
    public String family;
    public int id;
    public boolean status;
    public List<Composition> composition;

    public static class Composition {
        public String name;
        public int age;
    }
}
