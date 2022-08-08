package com.cargo.booking.nsi.util;


import com.cargo.booking.nsi.dto.XlsxHeader;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class XlsxUtil {

    public static List<XlsxHeader> modelObjectToXLSXHeader(Class cls) {
        List<XlsxHeader> headers = new ArrayList<>();
        for (int i = 0; i < cls.getDeclaredFields().length; i++) {
            Field field = cls.getDeclaredFields()[i];
            if (field.getAnnotation(XlsxField.class) != null) {
                XlsxField annotation = field.getAnnotation(XlsxField.class);
                headers.add(new XlsxHeader(field.getName(), i, annotation.pojoColumnType(),
                        annotation.required(), annotation.dictionaryLink()));
            }
        }
        return headers;
    }

    public static boolean isSetterMethod(String field, Method method) {
        return method.getName().equals("set" + field.substring(0, 1).toUpperCase() + field.substring(1));
    }

    public static boolean isGetterMethod(String field, Method method) {
        return method.getName().equals("get" + field.substring(0, 1).toUpperCase() + field.substring(1));
    }
}