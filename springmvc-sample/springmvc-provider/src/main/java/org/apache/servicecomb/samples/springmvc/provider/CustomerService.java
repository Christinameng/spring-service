/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.servicecomb.samples.springmvc.provider;


import org.apache.servicecomb.provider.rest.common.RestSchema;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.ws.rs.core.MediaType;
import java.util.HashMap;

@RestSchema(schemaId = "customer")
@RequestMapping(path = "/hwclouds/v1/customer", produces = MediaType.APPLICATION_JSON)
public class CustomerService {
    private CustomerDao dao = new CustomerDao("test.db");
    CustomerService() {
        dao.insert("test0", 0);
        dao.insert("test1", 1);
        dao.insert("test2", 2);
        dao.insert("test3", 3);
        dao.insert("test4", 4);
        dao.insert("test5", 5);
    }

    @RequestMapping(path = "/{userId}", method = RequestMethod.GET)
    public HashMap<String, Object> getUser(@PathVariable(value = "userId") Integer id) {
        Customer customer = dao.getCustomer(id);
        HashMap<String, Object> result = new HashMap<>();
        if (customer != null) {
            HashMap<String, Object> info= new HashMap<>();
            info.put("id", customer.id);
            info.put("name", customer.name);
            info.put("level", customer.level);
            result.put("info", info);
            result.put("ret_code", 200);
        } else {
            result.put("ret_code", 404);
        }

        return result;
    }
}
