package org.apache.servicecomb.samples.springmvc.provider;

public class Customer {
    int id;
    String name;
    int level;

    Customer(int id, String name, int level) {
        this.id=id;
        this.name = name;
        this.level = level;
    }
}
