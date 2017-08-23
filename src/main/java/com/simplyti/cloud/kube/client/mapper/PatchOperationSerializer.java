package com.simplyti.cloud.kube.client.mapper;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.simplyti.cloud.kube.client.JsonPatch.PatchOperation;

public class PatchOperationSerializer extends StdSerializer<PatchOperation> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3928974824119336154L;

	public PatchOperationSerializer() {
        this(null);
    }

	public PatchOperationSerializer(Class<PatchOperation> t) {
        super(t);
    }

	@Override
	public void serialize(PatchOperation value, JsonGenerator jgen, SerializerProvider provider)
			throws IOException, JsonProcessingException {
		jgen.writeString(value.name().toLowerCase());
	}
}
