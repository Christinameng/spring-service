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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.HashMap;

@RestSchema(schemaId = "resource")
@RequestMapping(path = "/hwclouds/v1/resource", produces = MediaType.APPLICATION_JSON)
public class ResourceService {
    private ResourceDao resourceDao = new ResourceDao("test.db");
    private CustomerDao customerDao = new CustomerDao("test.db");

    ResourceService() {
        resourceDao.insert();
        resourceDao.insert();
        resourceDao.insert();
        resourceDao.insert();
        resourceDao.insert();
        resourceDao.insert();
        resourceDao.insert();
        resourceDao.insert();
    }

    @RequestMapping(path = "/order", method = RequestMethod.POST)
    public HashMap<String, Object> getResource(@RequestParam(value = "userId") Integer id) {
        int customerLevel = customerDao.getCustomer(id).level;
        int customerResources = resourceDao.getUsedResourcesByCustomerId(id).size();
        HashMap<String, Object> result = new HashMap<>();
        if ((customerLevel >= 0 && customerLevel <= 3 && customerResources < 2) ||
                (customerLevel == 4 && customerResources < 5) ||
                (customerLevel == 5 && customerResources < 10)) {
            ArrayList<Resource> availableResources = resourceDao.getAvailableResources();
            if (availableResources.size() > 0) {
                int resourceId = availableResources.get(0).id;
                resourceDao.acquireResource(resourceId, id);
                Resource resource = resourceDao.getResource(resourceId);
                HashMap<String, Integer> resourceInfo = new HashMap<>();
                resourceInfo.put("id", resource.id);
                resourceInfo.put("state", resource.state);
                resourceInfo.put("user_id", resource.customer_id);
                result.put("ret_code", 200);
                result.put("resource", resourceInfo);
            } else {
                result.put("ret_code", 400);
                result.put("ret_desc", "资源配额不足");
            }
        } else {
            result.put("ret_code", 400);
            result.put("ret_desc", "用户权限不足");
        }
        return result;
    }

    @RequestMapping(path = "/", method = RequestMethod.DELETE)
    public HashMap<String, Object> releaseResource(@RequestParam(value = "userId") Integer userId,
                                                   @RequestParam(value = "resourceId") Integer resourceId) {
        resourceDao.releaseResource(resourceId, userId);
        HashMap<String, Object> result = new HashMap<>();
        result.put("ret_code", 200);
        return result;
    }

    @RequestMapping(path = "/", method = RequestMethod.GET)
    public HashMap<String, Object> getUsedResourcesByUserId(@RequestParam(value = "userId") Integer userId) {
        ArrayList<Resource> resources = resourceDao.getUsedResourcesByCustomerId(userId);
        HashMap<String, Object> result = new HashMap<>();
        ArrayList<HashMap<String, Integer>> resourcesInfo = new ArrayList<>();
        for (Resource resource : resources) {
            HashMap<String, Integer> resourceInfo = new HashMap<>();
            resourceInfo.put("id", resource.id);
            resourceInfo.put("state", resource.state);
            resourceInfo.put("user_id", resource.customer_id);
            resourcesInfo.add(resourceInfo);
        }
        result.put("ret_code", 200);
        result.put("resources", resourcesInfo);
        return result;
    }
}
