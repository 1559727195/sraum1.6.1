package com.Util;


import android.util.JsonReader;
import android.util.JsonToken;
import android.util.JsonWriter;

import java.io.IOException;

/**
 * Created by masskywcy on 2017-03-28.
 */

//public class StringNullAdapter extends TypeAdapter<String> {
//    @Override
//    public String read(JsonReader reader) throws IOException {
//        // TODO Auto-generated method stub
//        if (reader.peek() == JsonToken.NULL) {
//            reader.nextNull();
//            return "";
//        }
//        return reader.nextString();
//    }
//
//    @Override
//    public void write(JsonWriter writer, String value) throws IOException {
//        // TODO Auto-generated method stub
//        if (value == null) {
//            writer.nullValue();
//            return;
//        }
//        writer.value(value);
//    }
//}
