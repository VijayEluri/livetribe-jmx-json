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

import javax.management.ObjectInstance;
import java.io.IOException;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.ser.std.SerializerBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 *
 */
public class ObjectInstanceSerializer extends SerializerBase<ObjectInstance>
{
    static final Logger LOG = LoggerFactory.getLogger(ObjectNameSerializer.class);

    public ObjectInstanceSerializer()
    {
        super(ObjectInstance.class);
    }

    @Override
    public void serialize(ObjectInstance instance, JsonGenerator generator, SerializerProvider provider) throws IOException, JsonProcessingException
    {
        LOG.trace("Serializing object instance {}", instance);

        generator.writeStartObject();
        generator.writeObjectField("name", instance.getObjectName());
        generator.writeStringField("class", instance.getClassName());
        generator.writeEndObject();
    }
}
