/**
 * ﻿Copyright ©2018 Sensus
 * All rights reserved
 */
package com.railinc.shipping.container.util;

/**
 * Created by Grace Gong on 1/7/19.
 */

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;


import java.util.ArrayList;
import java.util.List;

/*
 * Use the Jackson JSON utility
 */
public class JSONUtil {
    private static ObjectMapper mapper = new ObjectMapper();
    static {
        //mapper.getSerializationConfig().setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
        mapper.setDefaultPropertyInclusion(
                JsonInclude.Value.construct(JsonInclude.Include.ALWAYS, JsonInclude.Include.NON_NULL));
    }

    public static String toJSON(Object object) {
        try {
            return mapper.writer().withDefaultPrettyPrinter().writeValueAsString(object);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static Object fromJSON(String json, Class clazz) {
        try {
            return mapper.readValue(json, clazz);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static void main(String[] args) {

       Long epoch =System.currentTimeMillis();
       System.out.println("current time in millis " +epoch);

    }



}
