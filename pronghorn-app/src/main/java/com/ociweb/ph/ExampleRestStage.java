package com.ociweb.ph;

import com.ociweb.json.encode.JSONRenderer;
import com.ociweb.pronghorn.network.OrderSupervisorStage;
import com.ociweb.pronghorn.network.config.HTTPContentType;
import com.ociweb.pronghorn.network.config.HTTPContentTypeDefaults;
import com.ociweb.pronghorn.network.config.HTTPHeader;
import com.ociweb.pronghorn.network.config.HTTPRevision;
import com.ociweb.pronghorn.network.config.HTTPSpecification;
import com.ociweb.pronghorn.network.config.HTTPVerb;
import com.ociweb.pronghorn.network.config.HTTPVerbDefaults;
import com.ociweb.pronghorn.network.http.HTTPUtil;
import com.ociweb.pronghorn.network.module.AbstractAppendablePayloadResponseStage;
import com.ociweb.pronghorn.network.schema.HTTPRequestSchema;
import com.ociweb.pronghorn.network.schema.ServerResponseSchema;
import com.ociweb.pronghorn.pipe.ChannelReader;
import com.ociweb.pronghorn.pipe.DataInputBlobReader;
import com.ociweb.pronghorn.pipe.Pipe;
import com.ociweb.pronghorn.pipe.StructuredReader;
import com.ociweb.pronghorn.stage.PronghornStage;
import com.ociweb.pronghorn.stage.scheduling.GraphManager;
import com.ociweb.pronghorn.util.AppendableBuilder;
import com.ociweb.pronghorn.util.AppendableByteWriter;

public class ExampleRestStage<      T extends Enum<T> & HTTPContentType,
									R extends Enum<R> & HTTPRevision,
									V extends Enum<V> & HTTPVerb,
									H extends Enum<H> & HTTPHeader> extends AbstractAppendablePayloadResponseStage<T,R,V,H> {

    private static final byte[] HEY = "Hey, ".getBytes();
	private static final byte[] BANG = "!".getBytes();
	
	private static final JSONRenderer<StructuredReader> jsonRenderer = new JSONRenderer<StructuredReader>()
            .beginObject()
            .string("message", (reader,target) ->  {target.write(HEY); reader.readText(Fields.name, target).write(BANG);} )
            .bool("happy", reader -> !reader.readBoolean(Fields.happy))
            .integer("age", reader -> reader.readInt(Fields.age) * 2)
            .endObject();
	
	public static ExampleRestStage newInstance(GraphManager graphManager, 
			Pipe<HTTPRequestSchema> inputPipes,
			Pipe<ServerResponseSchema> outputPipe, 
			HTTPSpecification httpSpec) {
		return new ExampleRestStage(graphManager, inputPipes, outputPipe, httpSpec);
	}
	
	public ExampleRestStage(GraphManager graphManager, 
			Pipe<HTTPRequestSchema> inputPipes,
			Pipe<ServerResponseSchema> outputPipes, 
			HTTPSpecification httpSpec) {
		
		super(graphManager, join(inputPipes), join(outputPipes), httpSpec, 1<<8);

	}

	@Override
	protected byte[] contentType() {
		return HTTPContentTypeDefaults.JSON.getBytes();
	}

	@Override
	protected boolean payload(AppendableByteWriter<?> payload, GraphManager gm, 
			                 ChannelReader params, HTTPVerbDefaults verb) {	
		//TODO: just hangs if we do not have a pipe long enugh?
		jsonRenderer.render(payload, params.structured());
		return true;
	}

}
