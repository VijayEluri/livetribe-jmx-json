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

import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import java.io.IOException;

import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.deser.std.StdDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.toolazydogs.jr4me.server.model.Call;


/**
 *
 */
public class ObjectNameDeserializer extends StdDeserializer<ObjectName>
{
    static final Logger LOG = LoggerFactory.getLogger(ObjectNameDeserializer.class);

    public ObjectNameDeserializer()
    {
        super(ObjectName.class);
    }

    @Override
    public ObjectName deserialize(JsonParser parser, DeserializationContext context) throws IOException, JsonProcessingException
    {
        JsonToken token = parser.getCurrentToken();
        if (token == JsonToken.START_OBJECT)
        {
            token = parser.nextToken();
            if (token != JsonToken.FIELD_NAME) throw context.wrongTokenException(parser, token, "Expected field name for JSON RPC");
        }
        else
        {
            throw context.wrongTokenException(parser, token, "Expected start of JSON object");
        }

        LOG.trace("Starting parse of ObjectName");

        String objectName = null;
        while (token != JsonToken.END_OBJECT)
        {
            if (token == JsonToken.FIELD_NAME)
            {
                String name = parser.getText();

                if ("name".equals(name))
                {
                    token = parser.nextToken();
                    if (token != JsonToken.VALUE_STRING) throw context.wrongTokenException(parser, token, "Expected string value for ObjectName name");
                    objectName = parser.getText();

                    LOG.trace("ObjectName name {}", objectName);
                }
                else
                {
                    throw context.unknownFieldException(Call.class, name);
                }

                token = parser.nextToken();
            }
            else
            {
                throw context.wrongTokenException(parser, token, "Expected field name or end of JSON object");
            }
        }

        LOG.trace("Completed parse of ObjectName");

        if (objectName == null) throw context.instantiationException(Call.class, "ObjectName name was not set");

        try
        {
            return ObjectName.getInstance(objectName);
        }
        catch (MalformedObjectNameException mone)
        {
            throw new JsonMappingException("Malformed objectName", mone);
        }
    }
}
