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
package org.apache.servicecomb.samples.springmvc.consumer;

import org.apache.servicecomb.foundation.common.utils.BeanUtils;
import org.apache.servicecomb.provider.springmvc.reference.RestTemplateBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;

@Component
public class SpringmvcConsumerMain {
    private static final Logger LOG = LoggerFactory.getLogger(SpringmvcConsumerMain.class);

    private static RestTemplate restTemplate = RestTemplateBuilder.create();

    @SuppressWarnings("unchecked")
    public static void main(String[] args) throws Exception {
        BeanUtils.init();
        // RestTemplate Consumer or POJO Consumer. You can choose whatever you like
        // RestTemplate Consumer
        HashMap<String, Object> result = new HashMap<>();
        HashMap<String, Object> resp =
                restTemplate.getForObject("cse://resource_control/hwclouds/v1/customer/1",
                        (Class<? extends HashMap<String, Object>>) result.getClass());

        HashMap<String, Object> resourceOrderResp =
                restTemplate.postForObject("cse://resource_control/hwclouds/v1/resource/order?userId=1", null,
                        (Class<? extends HashMap<String, Object>>) result.getClass());
        HashMap<String, Object> resourceOrderResp2 =
                restTemplate.postForObject("cse://resource_control/hwclouds/v1/resource/order?userId=1", null,
                        (Class<? extends HashMap<String, Object>>) result.getClass());

        restTemplate.delete("cse://resource_control/hwclouds/v1/resource/?userId=1&resourceId=1");

        HashMap<String, Object> resourcesResp =
                restTemplate.getForObject("cse://resource_control/hwclouds/v1/resource/?userId=1",
                        (Class<? extends HashMap<String, Object>>) result.getClass());

        System.out.println("RestTemplate consumer customer services: " + resp);
        System.out.println("RestTemplate consumer resource order services: " + resourceOrderResp);
        System.out.println("RestTemplate consumer resource order services: " + resourceOrderResp2);
        System.out.println("RestTemplate consumer resources services: " + resourcesResp);
    }
}
