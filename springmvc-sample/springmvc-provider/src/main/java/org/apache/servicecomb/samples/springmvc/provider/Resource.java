package org.apache.servicecomb.samples.springmvc.provider;

public class Resource {
    int id;
    int state;
    int customer_id;

    Resource(int id, int state, int customer_id) {
        this.id = id;
        this.state = state;
        this.customer_id = customer_id;
    }
}
