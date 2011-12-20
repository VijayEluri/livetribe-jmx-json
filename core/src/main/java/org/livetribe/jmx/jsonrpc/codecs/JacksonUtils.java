/**
 * Copyright 2011 (C) The original author or authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.livetribe.jmx.jsonrpc.codecs;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializerProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.toolazydogs.jr4me.server.jackson.Deserializer;
import com.toolazydogs.jr4me.server.jackson.Serializer;


/**
 *
 */
public class JacksonUtils
{
    static final Logger LOG = LoggerFactory.getLogger(JacksonUtils.class);

    static Object deserialize(JsonParser parser, DeserializationContext context) throws IOException
    {
        ObjectMapper mapper = Deserializer.getMapper();
        if (mapper != null)
        {
            return mapper.readValue(parser, Object.class);
        }
        else
        {
            LOG.warn("Mapper not set by Deserializer");
            return null;
        }
    }

    static void serialize(Object object, JsonGenerator generator, SerializerProvider provider) throws IOException
    {
        ObjectMapper mapper = Serializer.getMapper();
        if (mapper != null)
        {
            mapper.writeValue(generator, object);
        }
        else
        {
            LOG.warn("Mapper not set by Serializer");
        }
    }

    private JacksonUtils() { }
}
